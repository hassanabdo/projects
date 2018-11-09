package com.example.rest.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.ujmp.core.Matrix;

import com.example.rest.Main;
import com.example.rest.model.AddFanResponse;
import com.example.rest.model.City;
import com.example.rest.model.Country;
import com.example.rest.model.Persona;
import com.example.rest.model.PersonaName;
import com.example.rest.model.PersonaName2;
import com.example.rest.model.Region;
import com.example.rest.model.UpdateFanResponse;
import com.example.rest.persistence.DatabaseHandler;
import com.example.rest.persistence.FileHandler;
import com.example.rest.utils.GlobalVariables;
import com.example.rest.utils.RequestType;

public class Controller {

	public static ArrayList<Persona> getPersona(int n, RequestType type) {
		ArrayList<Persona> personas_arrayList = new ArrayList<Persona>();
		if (!GlobalVariables.isPrePared){
			return personas_arrayList;
		}

		int numOfPersonas = 1;
		if (type == RequestType.SVD) {
			Matrix originalMatrix = Matrix.Factory.zeros(GlobalVariables.allSocialMatrix.length,
					GlobalVariables.SOCIAL_METRICS.length);

			for (int i = 0; i < GlobalVariables.allSocialMatrix.length; i++) {
				for (int j = 0; j < GlobalVariables.allSocialMatrix[i].length; j++) {
					originalMatrix.setAsInt(GlobalVariables.allSocialMatrix[i][j], i, j);
				}
			}
			Matrix[] svd = null;
			try {
				svd = originalMatrix.svd();
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<Double> list = new ArrayList<>();
			// the diagonal matrix is sorted in decreasing order
			for (int r = 0; r < svd[1].getRowCount(); r++) {
				// if (svd[1].getAsDouble(r, r) > 1) {
				list.add(svd[1].getAsDouble(r, r));
				// }
			}
			double[] personaPowersRaw = new double[list.size()];
			int i = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				personaPowersRaw[i++] = (double) iterator.next();
			}
			double[] normVec = normalizeVectorMinMax(personaPowersRaw);
			for (int j = 0; j < normVec.length; j++) {
				if (normVec[j] > GlobalVariables.STRENGTH)
					numOfPersonas++;
			}
		} else {
			numOfPersonas = n;
		}

		/*
		 * calling r function with n.
		 */
		double[][] wMatrix = null;
		double[][] hMatrix = null;

		runRFunction(numOfPersonas);
		wMatrix = new double[GlobalVariables.allSocialMatrix.length][numOfPersonas];
		hMatrix = new double[numOfPersonas][GlobalVariables.SOCIAL_METRICS.length];

		FileHandler.readMatrix(wMatrix, 0);
		FileHandler.readMatrix(hMatrix, 1);

		ArrayList<String> persona_demographics = new ArrayList<String>();
		int[] personaSizesTmp = new int[numOfPersonas];
		calculatePersonaDemographics(persona_demographics, wMatrix, personaSizesTmp);

		ArrayList<String> persona_least_topics = new ArrayList<String>();
		calculatePersonaLeastOrMostImportantTopic(persona_least_topics, hMatrix, type, true);

		ArrayList<String> persona_most_topics = new ArrayList<String>();
		calculatePersonaLeastOrMostImportantTopic(persona_most_topics, hMatrix, type, false);

		double[] persona_fb_pos = calculatePersonaImpression(hMatrix, 0, persona_demographics);
		double[] persona_fb_neg = calculatePersonaImpression(hMatrix, 1, persona_demographics);
		double[] persona_tw_pos = calculatePersonaImpression(hMatrix, 2, persona_demographics);
		double[] persona_tw_neg = calculatePersonaImpression(hMatrix, 3, persona_demographics);

		ArrayList<String> persona_names = new ArrayList<String>();
		getPersonaNames(persona_names, persona_demographics);


		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < persona_demographics.size(); i++) {
			Persona persona = new Persona();
			persona.setId(i);
			if (persona_demographics.get(i).equals(GlobalVariables.REDUNDANT)) {
				persona.setName(GlobalVariables.REDUNDANT);
			} else {
				String[] arr = persona_demographics.get(i).split(",");
				int gender_code = Integer.parseInt(arr[2]);
				persona.setGender(GlobalVariables.genderMap.get(gender_code).getGender_name());
				int age_code = Integer.parseInt(arr[1]);
				persona.setAge(GlobalVariables.agesMap.get(age_code).getAge_range());
				int country_code = 0;
				if (GlobalVariables.isCityKey) {
					int city_code = Integer.parseInt(arr[0]);
					City city = GlobalVariables.cityMap.get(city_code);
					country_code = city.getCountry_id();
					persona.setCity(city.getCity_name());
					persona.setCountry(GlobalVariables.countryMap.get(city.getCountry_id()).getName());
				} else {
					country_code = Integer.parseInt(arr[0]);
					Country country = GlobalVariables.countryMap.get(country_code);
					persona.setCountry(country.getName());
				}

				String srcPath = getPhoto(GlobalVariables.countryMap.get(country_code).getRegion_id(), gender_code,
						age_code, set);
				persona.setPhoto(srcPath);

				persona.setName(persona_names.get(i));

				DecimalFormat df2 = new DecimalFormat(".##");

				persona.setFbPos(GlobalVariables.DegreeToString((int) persona_fb_pos[i]));
				persona.setFbNeg(GlobalVariables.DegreeToString((int) persona_fb_neg[i]));
				persona.setTwPos(GlobalVariables.DegreeToString((int) persona_tw_pos[i]));
				persona.setTwNeg(GlobalVariables.DegreeToString((int) persona_tw_neg[i]));

				persona.setMostTopic(persona_most_topics.get(i));
				persona.setLeastTopic(persona_least_topics.get(i));

				persona.setPersonaSize(personaSizesTmp[i]);
				persona.setPersonaSizePercent(
						df2.format(((double) personaSizesTmp[i] / GlobalVariables.AudienceSize) * 100) + " %");

			}
			personas_arrayList.add(persona);
		}

		return personas_arrayList;

	}

	// matrix normalization is needed
	private static double[] calculatePersonaImpression(double[][] hMatrix, int colNum,
			ArrayList<String> persona_demographics) {
		double[] finalArray = new double[hMatrix.length];
		int numOfReal = 0;
		for (int i = 0; i < hMatrix.length; i++) {
			if (persona_demographics.get(i).equals(GlobalVariables.REDUNDANT))
				finalArray[i] = GlobalVariables.REDUNDANT_IMPRESSION;
			else
				numOfReal++;
		}
		double[] array = new double[numOfReal];
		int c = 0;
		for (int i = 0; i < hMatrix.length; i++) {
			if (!persona_demographics.get(i).equals(GlobalVariables.REDUNDANT)) {
				array[c] = hMatrix[i][colNum];
				c++;
			}
		}
		double[] normalizedArray = calculatePersonaZNew(array);
		c = 0;
		for (int i = 0; i < finalArray.length; i++) {
			if (finalArray[i] != GlobalVariables.REDUNDANT_IMPRESSION) {
				finalArray[i] = normalizedArray[c];
				c++;
			}
		}
		return finalArray;
	}

	private static double[] calculatePersonaZNew(double[] array) {
		double[] arr_codes = new double[array.length];
		double[] normVec = normalizeVector(array);
		for (int i = 0; i < normVec.length; i++) {
			if (normVec[i] <= GlobalVariables.LOWER_LIMIT) {
				arr_codes[i] = GlobalVariables.LOW;
			} else if (normVec[i] > GlobalVariables.LOWER_LIMIT && normVec[i] < GlobalVariables.UPPER_LIMIT) {
				arr_codes[i] = GlobalVariables.MEDIUM;
			} else if (normVec[i] >= GlobalVariables.UPPER_LIMIT) {
				arr_codes[i] = GlobalVariables.HIGH;
			} else {
				System.out.println("wierd !!!!!!!!!!!");
			}
		}
		return arr_codes;
	}

	private static double[] calculateStandardProb(double[] array) {
		double[] normVec = new double[array.length];
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		for (int i = 0; i < normVec.length; i++) {
			normVec[i] = array[i] / sum;
		}
		return normVec;
	}

	private static double[] calculateSoftMaxFunc(double[] array) {
		double[] z_exp = new double[array.length];
		double[] normVec = normalizeVector(array);
		for (int i = 0; i < normVec.length; i++) {
			z_exp[i] = Math.exp(normVec[i]);
		}
		double sum_z_exp = 0;
		for (int i = 0; i < z_exp.length; i++) {
			sum_z_exp += z_exp[i];
		}
		double[] softmax = new double[array.length];
		for (int i = 0; i < softmax.length; i++) {
			softmax[i] = z_exp[i] / sum_z_exp;
		}
		return softmax;
	}

	private static double[] normalizeVector(double[] vec) {
		double[] normVec = new double[vec.length];

		double mean = 0;
		for (int i = 0; i < vec.length; i++) {
			mean += vec[i];
		}
		mean /= vec.length;
		// System.out.println("mean " + mean);
		double stdDev = 0;
		for (int i = 0; i < vec.length; i++) {
			stdDev += Math.pow(vec[i] - mean, 2);
		}
		double segma = Math.sqrt(stdDev / vec.length);
		// System.out.println("segma " + segma);
		for (int i = 0; i < vec.length; i++) {
			normVec[i] = (vec[i] - mean) / segma;
			// System.out.println(normVec[i]);
		}
		return normVec;
	}

	private static double[] normalizeVectorMinMax(double[] vec) {
		double[] normVec = new double[vec.length];
		double min = vec[0];
		for (int i = 1; i < vec.length; i++) {
			if (vec[i] < min)
				min = vec[i];
		}
		double max = vec[0];
		for (int i = 1; i < vec.length; i++) {
			if (vec[i] > max)
				max = vec[i];
		}
		for (int i = 0; i < normVec.length; i++) {
			normVec[i] = (vec[i] - min) / (max - min);

		}
		return normVec;
	}

	private static void getPersonaNames(ArrayList<String> persona_names, ArrayList<String> persona_demographics) {

		for (Iterator<String> iterator = persona_demographics.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			if (string.equals(GlobalVariables.REDUNDANT)) {
				persona_names.add(GlobalVariables.REDUNDANT);
			} else {
				String[] arr = string.split(",");
				int country_id = Integer.parseInt(arr[0]);
				if (GlobalVariables.isCityKey) {
					int city_id = Integer.parseInt(arr[0]);
					country_id = GlobalVariables.cityMap.get(city_id).getCountry_id();
				}
//				PersonaName2 name = GlobalVariables.namesMap.get(country_id);
				Country country = GlobalVariables.countryMap.get(country_id);
				if (arr[2].equalsIgnoreCase("1"))// male
					persona_names.add(country.getMale());
				else
					persona_names.add(country.getFemale());
			}
		}

	}

	private static void calculatePersonaLeastOrMostImportantTopic(ArrayList<String> persona_topics, double[][] hMatrix,
			RequestType type, boolean isLeast) {
		for (int i = 0; i < hMatrix.length; i++) {
			if (isLeast) {
				String leastTopic = "";
				leastTopic += GlobalVariables.SOCIAL_METRICS[2] + " = " + hMatrix[i][2];
				leastTopic += " / ";
				leastTopic += GlobalVariables.SOCIAL_METRICS[3] + " = " + hMatrix[i][3];
				persona_topics.add(leastTopic);
			} else {
				String mostTopic = "";
				mostTopic += GlobalVariables.SOCIAL_METRICS[0] + " = " + hMatrix[i][0];
				mostTopic += " / ";
				mostTopic += GlobalVariables.SOCIAL_METRICS[1] + " = " + hMatrix[i][1];
				persona_topics.add(mostTopic);
			}

		}
	}

	private static void calculatePersonaDemographics(ArrayList<String> persona_demographics, double[][] wMatrix,
			int[] personaSizesTmp) {
		Set<Integer> personasSet = new HashSet<>();
		int numOfRedundant = 0;
		for (int col = 0; col < wMatrix[0].length; col++) {

			double[] valArray = new double[wMatrix.length];
			int[] indexArray = new int[wMatrix.length];

			int numOfZeros = 0;
			for (int row = 0; row < indexArray.length; row++) {
				valArray[row] = wMatrix[row][col];
				indexArray[row] = row;
				if (valArray[row] == 0)
					numOfZeros++;
			}

			if (numOfZeros == indexArray.length)
				numOfRedundant++;

			bubbleSort(valArray, indexArray);

			if (numOfZeros == indexArray.length) {
				persona_demographics.add(GlobalVariables.REDUNDANT);
			} else {
				// for (int i = 0; i < indexArray.length; i++) {
				// if (!personasSet.contains(indexArray[i])) {
				persona_demographics.add(GlobalVariables.allSocialdemographicsList.get(indexArray[0]));
				// personasSet.add(indexArray[i]);
				// break;
				// }
				// }
			}

			double size = 0;
			for (int i = 0; i < valArray.length; i++) {
				size += valArray[i] * GlobalVariables.personaSizes.get(i);
			}
			// System.out.println("size of persona " + col + " = " + size);
			personaSizesTmp[col] = (int) size;
		}

		System.out.println("numOfRedundant >>>>>>>>>>>>>> " + numOfRedundant + " <<<<<<<<<<<<<< ");

	}

	private static void bubbleSort(double[] arr, int[] indexes) {
		int n = arr.length;
		double temp = 0;
		int tmpIndex = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {
				if (arr[j - 1] < arr[j]) {
					// swap elements
					temp = arr[j - 1];
					arr[j - 1] = arr[j];
					arr[j] = temp;

					tmpIndex = indexes[j - 1];
					indexes[j - 1] = indexes[j];
					indexes[j] = tmpIndex;
				}
			}
		}
	}

	private static String getPhoto(int regionIndex, int genderIndex, int ageIndex, HashSet<String> set) {

		String key = calculateIndex(genderIndex, ageIndex, regionIndex);

		if (!set.contains(key)) {
			set.add(key);
			return GlobalVariables.regions2Map.get(regionIndex).getRegion_name() + "/"
					+ GlobalVariables.genderMap.get(genderIndex).getGender_name() + "/"
					+ GlobalVariables.agesMap.get(ageIndex).getAge_range();
		}
		// see the two surrounding ages
		if (ageIndex > 0) { // see the earlier
			key = calculateIndex(genderIndex, ageIndex - 1, regionIndex);
			if (!set.contains(key)) {
				set.add(key);
				return GlobalVariables.regions2Map.get(regionIndex).getRegion_name() + "/"
						+ GlobalVariables.genderMap.get(genderIndex).getGender_name() + "/"
						+ GlobalVariables.agesMap.get(ageIndex - 1).getAge_range();
			}
		}
		if (ageIndex < GlobalVariables.agesMap.size() - 1) {
			key = calculateIndex(genderIndex, ageIndex + 1, regionIndex);
			if (!set.contains(key)) {
				set.add(key);
				return GlobalVariables.regions2Map.get(regionIndex).getRegion_name() + "/"
						+ GlobalVariables.genderMap.get(genderIndex).getGender_name() + "/"
						+ GlobalVariables.agesMap.get(ageIndex + 1).getAge_range();
			}
		}
		ArrayList<Integer> neighbours = GlobalVariables.regions2Map.get(regionIndex).getNeighbours();
		for (Iterator iterator = neighbours.iterator(); iterator.hasNext();) {
			Integer altRegionIndex = (Integer) iterator.next();
			key = calculateIndex(genderIndex, ageIndex, altRegionIndex);
			if (!set.contains(key)) {
				set.add(key);
				return GlobalVariables.regions2Map.get(altRegionIndex).getRegion_name() + "/"
						+ GlobalVariables.genderMap.get(genderIndex).getGender_name() + "/"
						+ GlobalVariables.agesMap.get(ageIndex).getAge_range();
			}
		}
		return "";
	}

	private static String calculateIndex(int gender, int age, int region) {
		return String.valueOf(gender) + "-" + String.valueOf(age) + "-" + String.valueOf(region);
	}

	public static void readFromFile(ArrayList<String> arrayList, String fileName) {
		JSONParser parser = new JSONParser();
		try {
			File file = new File(Main.projectPath + fileName + ".json");
			FileReader reader = new FileReader(file);
			JSONArray a = (JSONArray) parser.parse(reader);
			for (Object o : a) {
				String str;
				try {
					str = (String) o;
				} catch (Exception e) {
					// TODO: handle exception
					str = (o) + "";
				}

				arrayList.add(str);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void runRFunction(int n) {
		int typeNum = 3;
		System.out.println("number of personas " + n);
		String command = null;
		// System.out.println(command);
		try {
			RConnection connection = new RConnection();// make a new local connection on default port (6311)
			command = "setwd('" + Main.projectPath + "')";
			REXP rResponseObject = null;
			try {
				rResponseObject = connection.parseAndEval("try(eval(" + command + "),silent=TRUE)");
			} catch (REXPMismatchException e) {
				e.printStackTrace();
			}
			if (rResponseObject.inherits("try-error")) {
				System.out.println("R Serve Eval Exception : " + rResponseObject.asString());
			} else {
				System.out.println(connection.eval("getwd()").asString());

				try {
					rResponseObject = connection
							.parseAndEval("try(eval(" + "source('NMFCreation_new.r')" + "),silent=TRUE)");
				} catch (REXPMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (rResponseObject.inherits("try-error")) {
					System.out.println("R Serve Eval Exception : " + rResponseObject.asString());
				} else {
					rResponseObject = connection.parseAndEval(
							"try(eval(" + "persona_function(" + n + "," + typeNum + ")" + "),silent=TRUE)");
					if (rResponseObject.inherits("try-error")) {
						System.out.println("R Serve Eval Exception : " + rResponseObject.asString());
					} else {
						System.out.println("done successfully");
					}
				}
			}

		} catch (REngineException e) {
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static AddFanResponse addFan(String facebook, String twitter, long country, long city, long gender,
			long age) {
		AddFanResponse response = new AddFanResponse();
		if (!GlobalVariables.countryMap.containsKey((int) country)) {
			response.setError_message(GlobalVariables.NO_SUCH_COUNTRY);
			return response;
		}
		if (!GlobalVariables.cityMap.containsKey((int) city)) {
			response.setError_message(GlobalVariables.NO_SUCH_CITY);
			return response;
		}
		if (!GlobalVariables.genderMap.containsKey((int) gender)) {
			response.setError_message(GlobalVariables.NO_SUCH_GENDER);
			return response;
		}
		if (!GlobalVariables.agesMap.containsKey((int) age)) {
			response.setError_message(GlobalVariables.NO_SUCH_AGE);
			return response;
		}
		if (facebook == null && twitter == null) {
			response.setError_message(GlobalVariables.NO_SOCIAL_ACCOUNT);
			return response;
		}
		int id = DatabaseHandler.addFan(facebook, twitter, country, city, gender, age);
		response.setUser_id(id);
		return response;
	}

	public static UpdateFanResponse updateFan(String facebook, String twitter, long pos, long neg) {
		return DatabaseHandler.updateFan(facebook, twitter, (int)pos, (int)neg);
	}

}
