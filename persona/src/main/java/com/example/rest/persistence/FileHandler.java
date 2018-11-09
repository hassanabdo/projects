package com.example.rest.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.rest.Main;
import com.example.rest.model.PersonaName;
import com.example.rest.model.Region;
import com.example.rest.utils.GlobalVariables;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileHandler {

	public static void saveMatrices() {
		write2DArray(GlobalVariables.allSocialMatrix, "All_Social_Matrix.txt");
		writeStringArray(GlobalVariables.allSocialdemographicsList,GlobalVariables.personaSizes, "ALL_SOCIAL_DEMOGRAPHICS_LIST.txt");
	}

	public static void writeStringArray(ArrayList<String> list,ArrayList<Integer> sizeList, String fileName) {
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = null;
		Iterator<Integer> iterator_size = null;
		for (iterator = list.iterator(), iterator_size = sizeList.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			Integer integer = iterator_size.next();
			builder.append(string).append(" ").append(integer).append("\n");
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(Main.projectPath + "/" + fileName));
			writer.write(builder.toString());// save the string representation of the board
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void write2DArray(int[][] matrix, String filename) {
		int[][] board = matrix;

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < board.length; i++)// for each row
		{

			for (int j = 0; j < board[i].length; j++)// for each column
			{
				builder.append(board[i][j] + "");// append to the output string
				if (j < board[i].length - 1)// if this is not the last row element
					builder.append(" ");// then add comma (if you don't like commas you can use spaces)
			}

			builder.append("\n");
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(Main.projectPath + "/" + filename));
			writer.write(builder.toString());// save the string representation of the board
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void write2DArray(double[][] matrix, String filename) {
		double[][] board = matrix;

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < board.length; i++)// for each row
		{

			for (int j = 0; j < board[i].length; j++)// for each column
			{
				builder.append(board[i][j] + "");// append to the output string
				if (j < board[i].length - 1)// if this is not the last row element
					builder.append(" ");// then add comma (if you don't like commas you can use spaces)
			}

			builder.append("\n");
		}
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(Main.projectPath + "/" + filename));
			writer.write(builder.toString());// save the string representation of the board
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readMatrix(double[][] matrix, int type) {
		String fileName = Main.projectPath + "/";
		if (type == 0) { // read wMatrix
			fileName += "w_matrix.json";
		} else { // read hMatrix
			fileName += "h_matrix.json";
		}
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String sCurrentLine;
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] row = sCurrentLine.split(" ");
				for (int j = 0; j < row.length; j++) {
					matrix[i][j] = Double.parseDouble(row[j]);
				}
				i++;
				// System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<PersonaName> readPersonaNames() {
		ArrayList<PersonaName> personaNames = null;
		JSONParser parser = new JSONParser();
		try {
			JSONArray personaNamesArray = (JSONArray) parser
					.parse(new FileReader(Main.projectPath + "/persona_names.json"));
			if (personaNamesArray.size() > 0) {
				personaNames = new ArrayList<PersonaName>();
				for (Object obj : personaNamesArray) {
					JSONObject personaName = (JSONObject) obj;
					PersonaName name = new PersonaName();
					name.setCountry_name((String) personaName.get("country_name"));
					JSONArray arr = (JSONArray) personaName.get("names_list");
					String[] names = new String[arr.size()];
					int i = 0;
					for (Iterator iterator = arr.iterator(); iterator.hasNext();) {
						Object object = iterator.next();
						names[i++] = (String) object;
					}
					name.setNames_list(names);
					personaNames.add(name);
				}

			} else {
				System.err.println("Failed to read persona names json file");
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return personaNames;
	}

	public static ArrayList<Region> readRegionsJSON() {
		ArrayList<Region> regions = new ArrayList<Region>();
		ObjectMapper mapper = new ObjectMapper();
		JSONParser parser = new JSONParser();
		try {
			File file = new File(Main.projectPath + "peoples.json");
			FileReader reader = new FileReader(file);
			JSONArray a = (JSONArray) parser.parse(reader);
			for (Object o : a) {
				JSONObject object = (JSONObject) o;
				Region region = mapper.readValue(object.toJSONString(), Region.class);
				regions.add(region);
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
		return regions;
	}

}
