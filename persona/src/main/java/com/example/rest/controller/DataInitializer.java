package com.example.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.example.rest.model.Age;
import com.example.rest.model.City;
import com.example.rest.model.Country;
import com.example.rest.model.Gender;
import com.example.rest.model.GroupInteraction;
import com.example.rest.persistence.FileHandler;
import com.example.rest.spark.SparkController;
import com.example.rest.utils.GlobalVariables;
import com.example.rest.utils.HelperMethods;

public class DataInitializer {

	public static boolean collectData(boolean isCityKey) {

		try {
			GlobalVariables.isCityKey = isCityKey;
			GlobalVariables.init();

			int counter = 0;
			if (isCityKey) {
				for (Map.Entry<Integer, City> entry : GlobalVariables.cityMap.entrySet()) {
					for (Map.Entry<Integer, Age> entry_age : GlobalVariables.agesMap.entrySet()) {
						for (Map.Entry<Integer, Gender> entry_gender : GlobalVariables.genderMap.entrySet()) {
							StringBuilder builder = new StringBuilder();
							builder.append(entry.getKey()).append(",").append(entry_age.getKey()).append(",")
									.append(entry_gender.getKey());
							GlobalVariables.demographicsMap.put(builder.toString(), counter);
							GlobalVariables.demographicsList.add(builder.toString());
							GlobalVariables.personaSizes.add(0);
							counter++;
						}
					}
				}
			} else {
				for (Map.Entry<Integer, Country> entry : GlobalVariables.countryMap.entrySet()) {
					for (Map.Entry<Integer, Age> entry_age : GlobalVariables.agesMap.entrySet()) {
						for (Map.Entry<Integer, Gender> entry_gender : GlobalVariables.genderMap.entrySet()) {
							StringBuilder builder = new StringBuilder();
							builder.append(entry.getKey()).append(",").append(entry_age.getKey()).append(",")
									.append(entry_gender.getKey());
							GlobalVariables.demographicsMap.put(builder.toString(), counter);
							GlobalVariables.demographicsList.add(builder.toString());
							GlobalVariables.personaSizes.add(0);
							counter++;
						}
					}
				}
			}

			GlobalVariables.allSocialMatrix = new int[GlobalVariables.demographicsList
					.size()][GlobalVariables.SOCIAL_METRICS.length];

			createMatrices();

			FileHandler.saveMatrices();
		} catch (Exception e) {
			return false;
		}
		GlobalVariables.isPrePared = true;
		return true;
	}

	public static void createMatrices() {
		GroupInteraction[] Matrix = SparkController.getRecords();
		int[] sumArray_all_social = new int[GlobalVariables.demographicsList.size()];
		GlobalVariables.AudienceSize = 1000; // twp
		for (int i = 0; i < Matrix.length; i++) {
			int fb_pos = Matrix[i].getFb_pos();
			int fb_neg = Matrix[i].getFb_neg();
			int tw_pos = Matrix[i].getTw_pos();
			int tw_neg = Matrix[i].getTw_neg();

			GlobalVariables.allSocialMatrix[i][0] += fb_pos;
			GlobalVariables.allSocialMatrix[i][1] += fb_neg;
			GlobalVariables.allSocialMatrix[i][2] += tw_pos;
			GlobalVariables.allSocialMatrix[i][3] += tw_neg;
			sumArray_all_social[i] = fb_pos + fb_neg + tw_pos + tw_neg;
		}

		// SparkController.stop();
		FileHandler.write2DArray(GlobalVariables.allSocialMatrix, "OLD_ALL_SOCIAL_MATRIX.txt");
		FileHandler.writeStringArray(GlobalVariables.demographicsList, GlobalVariables.personaSizes,
				"OLD_ALL_SOCIAL_DEMOGRAPHICS_LIST.txt");
		filter(sumArray_all_social);

	}

	private static void filter(int[] sumArray_all_social) {
		int threshold = getThreshold();
		System.out.println(">>>>> Threshold = " + threshold);
		int newAllSocialRecordNum = getNumOfRecords(sumArray_all_social, threshold);
		System.out.println(">>>>>>>>>>> newAllSocialRecordNum " + newAllSocialRecordNum);

		ArrayList<String> demographicsList_allSocial = new ArrayList<>();
		ArrayList<Integer> personaSizes_allSocial = new ArrayList<>();
		int[][] matrix_allSocial = new int[newAllSocialRecordNum][GlobalVariables.SOCIAL_METRICS.length];
		Map<String, Integer> demographicsMap_allSocial = new HashMap<>();

		filterMatrices(sumArray_all_social, GlobalVariables.allSocialMatrix, GlobalVariables.demographicsList,
				demographicsList_allSocial, matrix_allSocial, demographicsMap_allSocial, threshold,
				GlobalVariables.personaSizes, personaSizes_allSocial);
		GlobalVariables.allSocialMatrix = matrix_allSocial;
		GlobalVariables.allSocialdemographicsList = demographicsList_allSocial;
		GlobalVariables.allSocialDemographicsMap = demographicsMap_allSocial;
		GlobalVariables.personaSizes = personaSizes_allSocial;

	}

	private static int getThreshold() {
		int[][] array = GlobalVariables.allSocialMatrix;
		long sum = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				sum += array[i][j];
			}
		}
		return (int) ((sum / array.length) * 0.7);
	}

	private static int getNumOfRecords(int[] sumArray, int threshold) {
		int numOfNewRows = 0;
		for (int i = 0; i < sumArray.length; i++) {
			if (sumArray[i] > threshold)
				numOfNewRows++;
		}
		return numOfNewRows;
	}

	private static void filterMatrices(int[] sumArray, int[][] matrix, ArrayList<String> demographicsList,
			ArrayList<String> demographicsListNew, int[][] matrixNew, Map<String, Integer> demographicsMapNew,
			int threshold, ArrayList<Integer> personaSizes_allSocial, ArrayList<Integer> personaSizes_allSocialNew) {

		// System.out.println("///////////////////////////////////////////////////////");
		int k = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (sumArray[i] > threshold) {
				for (int j = 0; j < matrixNew[k].length; j++) {
					matrixNew[k][j] = matrix[i][j];
				}
				demographicsMapNew.put(demographicsList.get(i), k);
				demographicsListNew.add(demographicsList.get(i));
				personaSizes_allSocialNew.add(personaSizes_allSocial.get(i));

				// System.out.println("k = "+k+", key = "+demographicsListNew.get(k)+", old
				// index = "+i);

				k++;
			}
		}

	}

}
