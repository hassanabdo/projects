package com.example.rest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.rest.model.Age;
import com.example.rest.model.City;
import com.example.rest.model.Country;
import com.example.rest.model.Gender;
import com.example.rest.model.PersonaName;
import com.example.rest.model.PersonaName2;
import com.example.rest.model.Region2;
import com.example.rest.persistence.DatabaseHandler;
import com.example.rest.persistence.FileHandler;

public class GlobalVariables {


	public static boolean isCityKey = false;
	public static boolean isPrePared = false;

	public static Map<String, Integer> demographicsMap = null;

	public static final String[] ALL_SOCIAL_METRICS = { "FACEBOOK_INTERACTIONS", "TWITTER_INTERACTIONS",
			"FACEBOOK_POSITIVE", "FACEBOOK_NEGATIVE", "TWITTER_POSITIVE", "TWITTER_NEGATIVE" };

	public static final String[] SOCIAL_METRICS = { "FACEBOOK_POSITIVE", "FACEBOOK_NEGATIVE", "TWITTER_POSITIVE",
			"TWITTER_NEGATIVE" };

	public static final String[] REGIONS = { "African", "Caribbean", "Central Asia", "East Asia",
			"Europe & US & North Asia", "Gulf", "Latin", "Middle Eastern", "Pacific Ocean", "South Asia",
			"SouthEast Asia" };

	public static final double STRENGTH = 0.3;

	public static final double LOW = 0;

	public static final double MEDIUM = 1;

	public static final double HIGH = 2;

	public static final double UPPER_LIMIT = 0.5;

	public static final double LOWER_LIMIT = -0.5;

	public static final String REDUNDANT = "REDUNDANT";

	public static final double REDUNDANT_IMPRESSION = -1;

	public static final String NO_SUCH_COUNTRY = "THERE IS NO SUCH COUNTRY IN THE DATABASE";

	public static final String NO_SUCH_CITY = "THERE IS NO SUCH CITY IN THE DATABASE";

	public static final String NO_SUCH_GENDER = "THERE IS NO SUCH GENDER IN THE DATABASE";

	public static final String NO_SUCH_AGE = "THERE IS NO SUCH AGE IN THE DATABASE";

	public static final String NO_SOCIAL_ACCOUNT = "A FACEBOOK OR TWITTER ACCOUNT MUST BE PROVIDED";

	public static final String NO_CONNECTION = "CANNOT OPEN DATABASE CONNECTION";

	public static final String NO_USER_FACEBOOK = "NO FACEBOOK USER FOR THAT ID";

	public static final String MORE_THAN_ONE_FACEBOOK_USER = "THERE ARE MORE THAN ONE FACEBOOK USER FOR THAT ID";

	public static final String NO_USER_TWITTER = "NO TWITTER USER FOR THAT ID";

	public static final String MORE_THAN_ONE_TWITTER_USER = "THERE ARE MORE THAN ONE TWITTER USER FOR THAT ID";

	public static final String FACEBOOK_TWITTER_NULL = "FACEBOOK AND TWITTER USERS IDS ARE NULL";

	public static ArrayList<String> demographicsList = null;

	public static ArrayList<Integer> personaSizes = null;

	public static int[][] allSocialMatrix = null;

	public static ArrayList<String> allSocialdemographicsList;

	public static Map<String, Integer> allSocialDemographicsMap;

	public static Map<Integer, City> cityMap;

	public static Map<Integer, Country> countryMap;

//	public static Map<Integer, PersonaName2> namesMap;

	public static Map<Integer, Region2> regions2Map;

	public static Map<Integer, Age> agesMap;

	public static Map<Integer, Gender> genderMap;

	public static int AudienceSize;

	public static void init() {

		demographicsList = new ArrayList<>();
		personaSizes = new ArrayList<>();
		demographicsMap = new HashMap<>();

		if(regions2Map == null) {
			regions2Map = DatabaseHandler.getRegions();
			genderMap = DatabaseHandler.getGenders();
			countryMap = DatabaseHandler.getCountries();
			cityMap = DatabaseHandler.getCities();
//			namesMap = DatabaseHandler.getPersonaNames();
			agesMap = DatabaseHandler.getAges();
		}

	}

	public static String DegreeToString(int degree) {
		if (degree == LOW)
			return "LOW";
		if (degree == MEDIUM)
			return "MEDIUM";
		return "HIGH";
	}

}
