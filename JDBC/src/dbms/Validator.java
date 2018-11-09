package dbms;

import java.util.ArrayList;
import java.util.HashMap;


public interface Validator {
	public boolean isDBExist(); // use db_name from query
	public boolean isTableExist(); // use tableName from query
	public boolean areAttributesExist(HashMap<String,ArrayList<String>> table);// use tableName and attribute from query
	public boolean isAttributesTypeGotValue(HashMap<String,ArrayList<String>> table); // use tableName and attribute from query
	public boolean isCondition(HashMap<String,ArrayList<String>> table);// use tableName and condition from query
	public boolean isSupportedType(String type);// check the type of the attributes
}
