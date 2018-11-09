package dbms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Validation implements Validator {
	private Query query;
	private File workSpace = new File(FileSystemView.getFileSystemView().getHomeDirectory()+ "\\DBMS Workspace");

	public Validation(Query query) {
		this.query = query;
	}

	public Validation() {
	}

	@Override
	public boolean isDBExist() {
		String DBname = query.getDbName();
		File file = new File(workSpace.getAbsolutePath() + "\\" + DBname);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isTableExist() {
		String DBname = query.getDbName();
		String TableName = query.getTableName() + ".xml";
		File file = new File(workSpace.getAbsolutePath() + "\\" + DBname + "\\"+ TableName);
		System.out.println(file.getAbsolutePath());
		if (file.exists())	return true;
		return false;
	}

	@Override
	public boolean areAttributesExist(HashMap<String, ArrayList<String>> table) {
		// table has been chosen from database
		ArrayList<String> columnsNames = null;
		columnsNames = parseFileName(table);
		// get the names of the query attributes
		ArrayList<String> QueryAttributes = this.query.getAttribute()
				.getColumns();
		if (QueryAttributes.get(0).equals("*")) {
			return true;
		}
		// i didn't know what you did in this case but if you fulfill the
		// columns arraylist , you can delete these three line of code
		for (String n : QueryAttributes) {// looping over all query attributes
			String str = n.trim();
			if (!(columnsNames.contains(str))) {
				return false;
			}
		}

		return true;
	}

	public ArrayList<String> parseFileName(
			HashMap<String, ArrayList<String>> table) {
		ArrayList<String> columnsNames = new ArrayList<String>();
		Set<String> names = table.keySet();
		for (String str : names) {
			columnsNames.add(str);
		}
		return columnsNames;
	}

	public ArrayList<String> parseFileType(
			HashMap<String, ArrayList<String>> table) {
		ArrayList<String> columnsTypes = new ArrayList<String>();
		Set<String> names = table.keySet();
		HashMap<String, String> columnsProperties = new HashMap<String, String>();
		columnsProperties = getCol(table, names);
		for (String str : names) {
			columnsTypes.add(columnsProperties.get(str));
		}
		return columnsTypes;
	}

	@Override
	public boolean isAttributesTypeGotValue(
			HashMap<String, ArrayList<String>> table) {
		// table has been chosen from database
		ArrayList<String> columnsNames = null;
		columnsNames = parseFileName(table);
		// all the columns names of the tables
		ArrayList<String> columnsTypes = null;
		columnsTypes = parseFileType(table);
		// all the columns types of the table
		ArrayList<String> QueryNames = this.query.getAttribute().getColumns();
		// get the columns names of the query
		ArrayList<String> QueryValues = this.query.getAttribute().getValues();
		// get the columns values of the query
		for (int i = 0; i < QueryNames.size(); i++) {
			String name = QueryNames.get(i).trim();// query column name
			String value = QueryValues.get(i).trim();// query column value
			for (int j = 0; j < columnsNames.size(); j++) {
				if (name.equalsIgnoreCase(columnsNames.get(j).trim())) {
					String type = columnsTypes.get(j).trim();
					if (!checkValue(value, type))
						return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isCondition(HashMap<String, ArrayList<String>> table) {
		Set<String> ColNames = table.keySet();
		HashMap<String, String> columns = getCol(table, ColNames);
		Conditions con = this.query.getCondition();
		ArrayList<String> conditionNames = con.getColumnName();
		ArrayList<String> conditionValues = con.getValue();
		ArrayList<String> condtionOperator = con.getOperator();
		for (int i = 0; i < conditionNames.size(); i++) {
			String name = conditionNames.get(i);
			String value = conditionValues.get(i);
			String Op = condtionOperator.get(i);
			if (!ColNames.contains(name))
				return false;
			String type = columns.get(name);
			if (!checkValue(value, type))
				return false;
			if (type.equalsIgnoreCase("boolean")) {
				if (!Op.equals("="))
					return false;
			} else {
				if (!(Op.equals(">")) && !(Op.equals("<")) && !(Op.equals("="))
						&& !(Op.equals(">=")) && !(Op.equals("<="))
						&& !(Op.equals("!="))) {
					return false;
				}
			}
		}
		return true;
	}

	public HashMap<String, String> getCol(
			HashMap<String, ArrayList<String>> table, Set<String> names) {
		HashMap<String, String> columns = new HashMap<String, String>();
		for (String name : names) {
			String type = table.get(name).get(0);
			columns.put(name, type);
		}
		return columns;
	}

	public boolean checkValue(String value, String type) {
		if (value.equals("empty")) {
			return true;
		} else if (type.equalsIgnoreCase("int")) {
			try {
				int val = Integer.parseInt(value);
			} catch (Exception e) {
				return false;
			}
		} else if (type.equalsIgnoreCase("double")) {
			try {
				double val = Double.parseDouble(value);
			} catch (Exception e) {
				return false;
			}
		} else if (type.equalsIgnoreCase("Boolean")) {
			if (!(value.equalsIgnoreCase("true"))
					&& !(value.equalsIgnoreCase("false"))) {
				return false;
			}
		} else if (type.equalsIgnoreCase("date")) {
			value = value.trim();
			try {
				Date.valueOf(value);
			} catch (Exception e) {
				return false;
			}
		} else {
			try {
				int val = Integer.parseInt(value);
				return false;
			} catch (Exception e) {
				if ((value.equalsIgnoreCase("true"))
						|| (value.equalsIgnoreCase("false"))) {
					return false;
				}
				return true;
			}

		}
		return true; // string
	}

	@Override
	public boolean isSupportedType(String type) {
		if (type.equalsIgnoreCase("int")) {
			return true;
		}
		if (type.equalsIgnoreCase("string")) {
			return true;
		}
		if (type.equalsIgnoreCase("boolean")) {
			return true;
		}
		if (type.equalsIgnoreCase("date")) {
			return true;
		}
		if (type.equalsIgnoreCase("double")) {
			return true;
		}
		return false;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

}
