package com.example.rest.spark;

import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.rdd.JdbcRDD;

import com.example.rest.model.GroupInteraction;
import com.example.rest.utils.GlobalVariables;

import scala.Tuple2;
import scala.reflect.ClassManifestFactory$;

public class SparkController {

	private static final JavaSparkContext sc = new JavaSparkContext(
			new SparkConf().setAppName("SparkJdbcDs").setMaster("local[4]"));

	// docker
	 private static final String DB_URL = "jdbc:mysql://db-mysql:3306/socialHub_accountManagement?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	// non docker
	 // private static final String DB_URL = "jdbc:mysql://localhost:3307/socialHub_accountManagement?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	private static final String USER = "root";
	private static final String PASS = "12345";
	private static final String TABLE_NAME = "customers";
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	private static DBConnection getDBConection() {
		DBConnection dbConnection = new DBConnection(MYSQL_DRIVER, DB_URL, USER, PASS);
		return dbConnection;
	}

//	public static ArrayList<String> getDistinctCities() {
//		ArrayList<String> cities = null;
//		JdbcRDD<String> jdbcRDD = new JdbcRDD<>(sc.sc(), getDBConection(),
//				"select distinct city from " + TABLE_NAME + " where ? = ?", 1, 1, 1, new MapCityToString(),
//				ClassManifestFactory$.MODULE$.fromClass(String.class));
//		JavaRDD<String> javaRDD = JavaRDD.fromRDD(jdbcRDD, ClassManifestFactory$.MODULE$.fromClass(String.class));
//		List<String> ci = javaRDD.collect();
//		cities = new ArrayList<>(ci);
//		// sc.stop();
//		return cities;
//	}

	public static GroupInteraction[] getRecords() {
//		sc.sc().cancelAllJobs();
		Broadcast<Map<String, Integer> > broadcastVar = sc.broadcast(GlobalVariables.demographicsMap);
		Broadcast<Boolean> broadcastBoolean = sc.broadcast(GlobalVariables.isCityKey);

		String sql = "select xxx.id, xxx.age, xxx.country, xxx.city, xxx.gender, xxx.fb_pos, xxx.fb_neg, xxx.tw_pos, xxx.tw_neg\n" +
				"from(\n" +
				"select s.id as id, s.age_range_id as age, s.country_id as country, s.city_id as city, s.gender_id as gender, fb.pos as fb_pos, fb.neg as fb_neg, tw.neg as tw_pos, tw.pos as tw_neg\n" +
				"from socials s, facebook_interactions fb, twitter_interactions tw\n" +
				"where fb.social_id = tw.social_id and s.id = fb.social_id\n" +
				"union\n" +
				"select s.id, s.age_range_id as age, s.country_id as country, s.city_id as city , s.gender_id as gender, fb2.pos as fb_pos, fb2.neg as fb_neg, 0 as tw_pos, 0 as tw_neg\n" +
				"from socials s, facebook_interactions fb2\n" +
				"where s.id = fb2.social_id and s.id not in (select tw2.social_id from twitter_interactions tw2)\n" +
				"union\n" +
				"select s.id, s.age_range_id as age, s.country_id as country, s.city_id as city , s.gender_id as gender, 0 as fb_pos, 0 as fb_neg, tw3.neg as tw_pos, tw3.pos as tw_neg\n" +
				"from socials s, twitter_interactions tw3\n" +
				"where s.id = tw3.social_id and s.id not in (select fb3.social_id from facebook_interactions fb3)\n" +
				")xxx where id >= ? and id <= ? and age <> 0 and country <> 0 and city <> 0 and gender <> 0\n" +
				";\n";
		JdbcRDD<String> jdbcRDD = new JdbcRDD<>(sc.sc(), getDBConection(),
				sql, 0, 100000000, 10, new MapCustomerToGroup(),
				ClassManifestFactory$.MODULE$.fromClass(String.class));

		JavaRDD<String> javaRDD = JavaRDD.fromRDD(jdbcRDD, ClassManifestFactory$.MODULE$.fromClass(String.class));

		JavaPairRDD<Integer, GroupInteraction> pairs = javaRDD.mapToPair(new Mapper(broadcastVar.value(), broadcastBoolean.value()));
		JavaPairRDD<Integer, GroupInteraction> counts = pairs.reduceByKey(new Reducer());
		List<Tuple2<Integer, GroupInteraction>> groups = counts.collect();
		GroupInteraction[] groupsArray = new GroupInteraction[GlobalVariables.demographicsList.size()];

		for (Tuple2<Integer, GroupInteraction> tuple2 : groups) {
			System.out.println(tuple2._1().toString());
			groupsArray[tuple2._1()] = tuple2._2();
		}
		return groupsArray;

	}

	public static void stop() {
		sc.sc().stop();
	}

}
