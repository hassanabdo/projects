package com.example.rest.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.rest.model.Age;
import com.example.rest.model.City;
import com.example.rest.model.Country;
import com.example.rest.model.Gender;
import com.example.rest.model.PersonaName2;
import com.example.rest.model.Region2;
import com.example.rest.model.UpdateFanResponse;
import com.example.rest.utils.GlobalVariables;

public class DatabaseHandler {

	// docker
	private static final String DB_URL = "jdbc:mysql://db-mysql:3306/socialHub_accountManagement?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	// non docker
	 // private static final String DB_URL = "jdbc:mysql://localhost:3307/socialHub_accountManagement?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PASS = "12345";
	private static final String TABLE_NAME = "customers";

	private static Connection getDBConection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return null;
		}
		System.out.println("MySQL JDBC Driver Registered!");
		// Connection connection = null;

		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");
			return conn;
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
	}

	public static Map<Integer, Country> getCountries() {
		Map<Integer, Country> countryHills = new HashMap<>();
		Connection conn = getDBConection();
		if (conn != null) {
			Statement stmt = null;
			System.out.println("Creating statement... for countries");
			try {
				stmt = conn.createStatement();
				String sql = "select id, country_name, region_id, male, female from countries;";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					int id = rs.getInt("id");
					int region_id = rs.getInt("region_id");
					String country_name = rs.getString("country_name");
					Country country = new Country();
					country.setId(id);
					country.setName(country_name);
					country.setRegion_id(region_id);
					country.setMale(rs.getString("male"));
					country.setFemale(rs.getString("female"));
					countryHills.put(id, country);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
		}
		return countryHills;
	}

	public static Map<Integer, City> getCities() {
		Map<Integer, City> cityMap = new HashMap<Integer, City>();
		Connection conn = getDBConection();
		if (conn != null) {
			Statement stmt = null;
			System.out.println("Creating statement... for cities");
			try {
				stmt = conn.createStatement();
				String sql = "select id, country_id, city_name from cities;";
				ResultSet rs = stmt.executeQuery(sql);
				// STEP 5: Extract data from result set
				while (rs.next()) {
					int id = rs.getInt("id");
					int country_id = rs.getInt("country_id");
					String city_name = rs.getString("city_name");
					City city = new City();
					city.setId(id);
					city.setCountry_id(country_id);
					city.setCity_name(city_name);
					cityMap.put(id, city);
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
		}

		return cityMap;
	}

	public static Map<Integer, PersonaName2> getPersonaNames() {
		Map<Integer, PersonaName2> names = new HashMap<>();
		Connection conn = getDBConection();
		if (conn != null) {
			Statement stmt = null;
			System.out.println("Creating statement... for names");
			try {
				stmt = conn.createStatement();
				String sql = "select country_id, male, female from persona_names;";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					PersonaName2 name2 = new PersonaName2();
					name2.setCountry_id(rs.getInt("country_id"));
					name2.setMale(rs.getString("male"));
					name2.setFemale(rs.getString("female"));
					names.put(rs.getInt("country_id"), name2);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
		}
		return names;
	}

	public static Map<Integer, Region2> getRegions() {
		Map<Integer, Region2> regionsMap = new HashMap<>();
		Connection conn = getDBConection();
		if (conn != null) {
			Statement stmt = null;
			System.out.println("Creating statement... for regions");
			try {
				stmt = conn.createStatement();
				String sql = "select id, region_name from regions;";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					int id = rs.getInt("id");
					String region_name = rs.getString("region_name");
					Statement stmt2 = conn.createStatement();
					sql = "select d_id from regions_relations where s_id = " + id + ";";
					ResultSet rs2 = stmt2.executeQuery(sql);
					ArrayList<Integer> neigh = new ArrayList<>();
					while (rs2.next()) {
						neigh.add(rs2.getInt("d_id"));
					}
					Region2 region2 = new Region2();
					region2.setId(id);
					region2.setRegion_name(region_name);
					region2.setNeighbours(neigh);
					regionsMap.put(id, region2);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
		}
		return regionsMap;
	}

	public static Map<Integer, Age> getAges() {
		Map<Integer, Age> agesMap = new HashMap<Integer, Age>();
		Connection conn = getDBConection();
		if (conn != null) {
			Statement stmt = null;
			System.out.println("Creating statement... for ages");
			try {
				stmt = conn.createStatement();
				String sql = "select id, age_range from age_ranges;";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					int id = rs.getInt("id");
					String age_range = rs.getString("age_range");
					Age age = new Age();
					age.setId(id);
					age.setAge_range(age_range);
					agesMap.put(id, age);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
		}
		return agesMap;
	}

	public static Map<Integer, Gender> getGenders() {
		Map<Integer, Gender> genderMap = new HashMap<>();
		Connection conn = getDBConection();
		if (conn != null) {
			Statement stmt = null;
			System.out.println("Creating statement... for genders");
			try {
				stmt = conn.createStatement();
				String sql = "select id, gender from genders;";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					int id = rs.getInt("id");
					String gender_name = rs.getString("gender");
					Gender gender = new Gender();
					gender.setId(id);
					gender.setGender_name(gender_name);
					genderMap.put(id, gender);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
		}
		return genderMap;
	}

	public static int addFan(String facebook, String twitter, long country, long city, long gender, long age) {
		int id = 0;
		Connection conn = getDBConection();
		if (conn != null) {
			PreparedStatement prest = null;
			try {
				conn.setAutoCommit(false);
				String sql = "insert into socials(country_id, city_id, age_range_id, gender_id) values(?,?,?,?);";
				prest = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				prest.setInt(1, (int) country);
				prest.setInt(2, (int) city);
				prest.setInt(3, (int) gender);
				prest.setInt(4, (int) age);
				prest.executeUpdate();
				ResultSet rs = prest.getGeneratedKeys();
				if (rs.next()) {
					id = rs.getInt(1);
					System.out.println(id);
				}
				if (facebook != null) {
					sql = "insert into facebook_interactions(facebook_user_id, social_id,pos,neg) values (?,?,0,0);";
					PreparedStatement facebookStmt = conn.prepareStatement(sql);
					facebookStmt.setString(1, facebook);
					facebookStmt.setInt(2, id);
					facebookStmt.executeUpdate();
				}
				if (twitter != null) {
					sql = "insert into twitter_interactions(twitter_user_id, social_id,pos,neg) values (?,?,0,0);";
					PreparedStatement twitterStmt = conn.prepareStatement(sql);
					twitterStmt.setString(1, twitter);
					twitterStmt.setInt(2, id);
					twitterStmt.executeUpdate();
				}
				conn.commit();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (prest != null)
						prest.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					conn.setAutoCommit(true);
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
		}
		return id;
	}

	public static UpdateFanResponse updateFan(String facebook, String twitter, int pos, int neg) {
		UpdateFanResponse response = new UpdateFanResponse();
		if(facebook == null && twitter == null) {
			response.setError_message(GlobalVariables.FACEBOOK_TWITTER_NULL);
			return response;
		}
		response.setError_message("");

		Connection conn = getDBConection();
		if (conn != null) {
			System.out.println("updating fan base");
			PreparedStatement stmt = null;
			try {
				conn.setAutoCommit(false);
				int result1 = -1, result2 = -1;
				if (facebook != null) {
					String sql = "update facebook_interactions " + "set pos = ?, neg = ?"
							+ " where facebook_user_id = ?;";
					stmt = conn.prepareStatement(sql);
					stmt.setInt(1, pos);
					stmt.setInt(2, neg);
					stmt.setString(3, facebook);
					result1 = stmt.executeUpdate();
					if (result1 == 0) {
						response.setError_message(GlobalVariables.NO_USER_FACEBOOK);
						response.setDoneFacebook(false);
					}
					if (result1 > 1) {
						response.setError_message(GlobalVariables.MORE_THAN_ONE_FACEBOOK_USER);
						response.setDoneFacebook(false);
					}
				}
				PreparedStatement stmt2 = null;
				if (twitter != null) {
					String sql = "update twitter_interactions " + "set pos = ?, neg = ?"
							+ " where twitter_user_id = ?;";
					stmt2 = conn.prepareStatement(sql);
					stmt2.setInt(1, pos);
					stmt2.setInt(2, neg);
					stmt2.setString(3, twitter);
					result2 = stmt2.executeUpdate();
					if (result2 == 0) {
						response.setError_message(
								response.getError_message() + " / " + GlobalVariables.NO_USER_TWITTER);
						response.setDoneTwitter(false);
					}
					if (result2 > 1) {
						response.setError_message(
								response.getError_message() + " / " + GlobalVariables.MORE_THAN_ONE_TWITTER_USER);
						response.setDoneTwitter(false);
					}
				}
				if ((facebook != null && result1 == 1 && twitter == null)
						|| (facebook == null && result2 == 1 && twitter != null)
						|| (facebook != null && twitter != null && result1 == 1 && result2 == 1)) {
					conn.commit();
					response.setDoneForBoth(true);
				} else
					response.setDoneForBoth(false);
				return response;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// finally block used to close resources
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			}
		}
		response.setError_message(GlobalVariables.NO_CONNECTION);
		return response;
	}

}
