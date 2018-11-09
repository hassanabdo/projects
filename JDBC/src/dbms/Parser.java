package dbms;

import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

public class Parser {
	private String inputQuery;
	private TableOperator tableOperator;
	private Validation validator;
	private Query query;
	public static String workSpacePath = FileSystemView.getFileSystemView().getHomeDirectory()+"";
	
	private int charIndex;
	
	private String temp;
	private boolean b;
	private ArrayList<String> types;
	private ArrayList<String> names;
		
	
	public Parser() {
		  this.workSpacePath = workSpacePath;
		  query = new Query();
	}

	public boolean isDBSet() { return b; }	
	
	public Query excuteQuery(String inputQuery) { 
		
		inputQuery = inputQuery.trim();
		inputQuery = inputQuery.replaceAll("\\s+", " ");
		this.inputQuery = inputQuery;
		names = new ArrayList<String>();
		types = new ArrayList<String>();
		
		query.setCondition(new Conditions());
		query.setAttribute(new Attributes());
		//try{
			if(!inputQuery.contains(";") || inputQuery.charAt(inputQuery.length()-1)!=';' ||inputQuery.indexOf(';')!=inputQuery.length()-1)
				System.out.println("Not a Valid Query Statement!"); 
			else
				validateAndDetect();
		//}catch(Exception e){
		//	System.out.println("ERROR!");
		//} 
		return query;
	}

	private boolean validateAndDetect() {
		if(inputQuery.length()>4 && inputQuery.substring(0,4).equalsIgnoreCase("USE "))
			dealwithUseDB();
		else if(inputQuery.length()>16 && inputQuery.substring(0,16).equalsIgnoreCase("CREATE DATABASE "))
			dealWithCreateDB(); //1
		else if(inputQuery.length()>13 && inputQuery.substring(0,13).equalsIgnoreCase("CREATE TABLE "))
			dealWithCreateTable(); //2 
		else if(inputQuery.length()>12 && inputQuery.substring(0,12).equalsIgnoreCase("INSERT INTO "))
			dealWithInsertInto(); //3 
		else if(inputQuery.length()>12 && inputQuery.substring(0,12).equalsIgnoreCase("DELETE FROM "))
			dealWithDelete(); //4 
		else if(inputQuery.length()>7 && inputQuery.substring(0,7).equalsIgnoreCase("SELECT "))
			dealWithSelect(); //5
		else if(inputQuery.length()>7 && inputQuery.substring(0,7).equalsIgnoreCase("UPDATE "))
			dealWithUpdate(); //6
		else System.out.println("Not a Valid Query Statement!");
		return true;
	}


	private boolean checkSpaces(int begin, int end) {
		for(int i=begin+1;i<end-1;i++){
			if(inputQuery.charAt(i)==' ')
				return false;
		}
		return true;
	}
	
	private boolean containsIgnoreCase(String s, String part) {
		  String s1 = s.toLowerCase();
		  String part1 = part.toLowerCase();
		  return s1.contains(part1);
	 }

	 private int indexOfIgnoreCase(String s, String part) {
		  String s1 = s.toLowerCase();
		  String part1 = part.toLowerCase();

		  return s1.indexOf(part1);
	 }
		
	
	private boolean createInfix(String s){
		System.out.println("Infix");
		s=s.trim();
		if(s.equals(""))
			return false;
		ArrayList <String> a = new ArrayList<String>();
		String s1;
		int counter=0,i;
		char c;
		while(s.length()!=0){
			s1="";
			c = ' ';
			if(s.length()>3 && s.substring(0,3).equalsIgnoreCase("not")){
				query.addConditionElement("NOT");
				s=s.substring(3).trim();
			}
			while(s.charAt(0)=='('){
				query.addConditionElement("(");
				s=s.substring(1).trim();
				++counter;
				if(s.length()>3 && s.substring(0,3).equalsIgnoreCase("not")){
					query.addConditionElement("NOT");
					s=s.substring(3).trim();
				}
			}
			
			if(s.charAt(0)==')')
				return false;
			for(i=0;i<s.length();i++){
				if(s.charAt(i)=='!' || s.charAt(i)=='=' || s.charAt(i)=='>' || s.charAt(i)=='<'){
					c = s.charAt(i);
					break;
				}
				else{
					s1+=s.charAt(i);
				}
			}
			s1=s1.trim();
			if(s1.substring(0, 1).equals("'") && s1.substring(s1.length()-1,s1.length()).equals("'")){
				query.addConditionElement(s1.substring(1,s1.length()-1));
				query.getCondition().addColumnByIndex(s1.substring(1,s1.length()-1));
			}
			else if(!s1.contains(" ")){
				query.addConditionElement(s1.trim());
				query.getCondition().addColumnByIndex(s1.trim());
			}
			else
				return false;
			System.out.println(s1);
			System.out.println(s.substring(s.indexOf('!')+1,s.indexOf('!')+3)+"teet");
			if(c=='!' && s.length()>3 && (s.charAt(s.indexOf('!')+1)=='='
					|| s.substring(s.indexOf('!')+1,s.indexOf('!')+3).equals(" ="))){
				query.addConditionElement("!=");
				query.getCondition().addOperatorByIndex("!=");
			}
			else if(c=='>' && s.length()>3 && (s.charAt(s.indexOf('>')+1)=='='
					|| s.substring(s.indexOf('>')+1,s.indexOf('>')+3).equals(" ="))){
				query.addConditionElement(">=");
				query.getCondition().addOperatorByIndex(">=");
			}
			else if(c=='<' && s.length()>3 && (s.charAt(s.indexOf('<')+1)=='='
					|| s.substring(s.indexOf('<')+1,s.indexOf('<')+3).equals(" ="))){
				query.addConditionElement("<=");
				query.getCondition().addOperatorByIndex("<=");
			}
			else if(c==' ')
				return false;
			else {
				query.addConditionElement(c+"");
				query.getCondition().addOperatorByIndex(c+"");
			}
			s=s.substring(i).trim();
			System.out.println(s);
			if(s.length()<3)
				return false;
		//	if (s.charAt(0)=='!' && s.charAt(1)==' ')
			//	s=s.substring(2);
			if(s.charAt(0)=='!' || s.charAt(0)=='>' || s.charAt(0)=='<')
				s=s.substring(1).trim();
			s=s.substring(1).trim();
			System.out.println(s);
			s1="";
			if(s.length()==0 )
				return false;
			if(s.substring(0,1).equals("'")){
				for(i=1;i<s.length();i++){
					if(!s.substring(i,i+1).equals("'"))
						s1+=s.charAt(i);
					else
						break;
				}
			}else{
				System.out.println(s+"To be integer");
				i=0;
				while(i<s.length() && s.charAt(i)!=' ')
					s1+=s.charAt(i++);
				try{
					System.out.println(s1+"To be added");
					int check = Integer.parseInt(s1);
					System.out.println(check);
				}catch(Exception e){
					return false;
				}
			}
			System.out.println(s1);
			if(s1.length()==0 || s.length()==i ) //||!s1.substring(s1.length()-1,s1.length()).equals("'")
				return false;
			query.addConditionElement(s1.substring(0,s1.length()).trim());
			query.getCondition().addValueByIndex(s1.substring(0,s1.length()).trim());
			s=s.substring(i+1).trim();
			System.out.println(s);
			while(s.length()>0 && s.charAt(0)==')'){
				query.addConditionElement(")");
				s=s.substring(1).trim();
				--counter;
			}
			if(s.length()>0 && s.charAt(0)=='(')
				return false;
			if(s.length()>3 && s.substring(0,3).equalsIgnoreCase("And")){
				query.addConditionElement("AND");
				s=s.substring(3).trim();
			}
			else if(s.length()>2 && s.substring(0,2).equalsIgnoreCase("or")){
				query.addConditionElement("OR");
				s=s.substring(2).trim();
			}
			else if(s.length()>3 && s.substring(0,3).equalsIgnoreCase("not")){
				query.addConditionElement("NOT");
				s=s.substring(3).trim();
			}
			else if(s.length()!=0)
				return false;
			System.out.println(s);
		//	query.addConditionElement(s1.trim());
		}
		a = query.getInfixCondition();
		for(i=0;i<a.size();i++)
			System.out.print(a.get(i)+" ");
		System.out.println(a.size());
		if(counter!=0)
			return false;
//		query.getCondition().infixToPostfix(query.getInfixCondition());
	//	ArrayList <String> postfix = query.getCondition().getPostfix();
	//	for(i=0;i<postfix.size();i++)
	//		System.out.print(postfix.get(i)+" ");
		System.out.println(a.size());
		return true;
	}
	

	private boolean checkCols() {
		for(int i=0;i<query.getAttribute().getNumberOfColumns()-1;i++){
			for(int j=1+i;j<query.getAttribute().getNumberOfColumns();j++){
				if(query.getAttribute().getColumn(i).equals(query.getAttribute().getColumn(j))){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean getSavedTableName(int begin) {
		temp =inputQuery.charAt(begin++)+"";
	//	++begin;
	//	System.out.println(inputQuery.charAt(begin));
		if(temp.length()>2 && temp.substring(0,1).equals("'")){
			while(begin < inputQuery.length()-1 && !inputQuery.substring(begin, begin+1).equals("'") &&	inputQuery.charAt(begin)!=';'){
				temp+=inputQuery.charAt(begin++);
				//++begin;
			}
			temp+=inputQuery.charAt(begin++);
		//	++begin;
		}else{
			while(begin < inputQuery.length()-1 &&	inputQuery.charAt(begin)!=' ' && inputQuery.charAt(begin)!='('	&&	inputQuery.charAt(begin)!=';'){
				temp+=inputQuery.charAt(begin++);
			//	++begin;
			}
		}
		System.out.println(temp);
		if(temp.length() > 1 && temp.substring(1,temp.length()-1).trim().length()!=0 && temp.startsWith("'") && temp.endsWith("'"))
			query.setTableName(temp.substring(1,temp.length()-1).trim());
		else if(temp.trim().length()!=0 && !temp.trim().contains(" ") && temp.charAt(0)!=';')
			query.setTableName(temp.trim());
		else 
			return false;
		if(charIndex<inputQuery.length()-1 && inputQuery.charAt(charIndex)==' ' || inputQuery.charAt(begin)==' ')
			charIndex=begin+1;
		else
			charIndex=begin;
		return true;
	}

	private void dealwithUseDB() {
			String temp=inputQuery.substring(4,inputQuery.length()-1).trim();
			System.out.println(temp);
			if(temp.startsWith("'") && temp.endsWith("'")){
				if(temp.substring(1,temp.length()-1).trim().length()!=0){
					query.setDbName(temp.substring(1,temp.length()-1).trim());
				}else
					System.out.println("Not a Valid Query Statement!");
			}else if(temp.length()!=0 && !temp.contains(" ")){
				query.setDbName(temp.trim());
			}
		/*	if(inputQuery.length()-1>6 && inputQuery.substring(inputQuery.length()-2).equals("';")
					&& inputQuery.substring(4,5).equals("'")){
				query.setDbName(inputQuery.substring(5, inputQuery.length()-2));
				// method maatrix ely hanadeeha
		//		validator = new Validation(query);
		//		b = validator.isDBExist();
			}
			else if(inputQuery.length()-1>6 && inputQuery.substring(inputQuery.length()-3).equals("' ;")
					&& inputQuery.substring(4,5).equals("'")){
				query.setDbName(inputQuery.substring(5, inputQuery.length()-3));
//				validator = new Validation(query);
				//		b = validator.isDBExist();
			}else if(inputQuery.length()-1>4 && !inputQuery.substring(5, inputQuery.length()-2).contains("'") 
					&& !inputQuery.substring(5, inputQuery.length()-3).contains(" ")){
				query.setDbName(inputQuery.substring(4, inputQuery.length()-2).trim());
//				validator = new Validation(query);
				//		b = validator.isDBExist();
			}*/
				
			else
				System.out.println("Not a Valid Query Statement!");
			
	}
	
	private void dealWithCreateDB() {
			System.out.println(":3");
			String temp=inputQuery.substring(16,inputQuery.length()-1).trim();
			System.out.println(temp);
			if(temp.startsWith("'") && temp.endsWith("'")){
				if(temp.substring(1,temp.length()-1).trim().length()!=0){
					query.setDbName(temp.substring(1,temp.length()-1).trim());
					query.setType(1);
				}else
					System.out.println("Not a Valid Query Statement!");
			}else if(temp.length()!=0 && !temp.contains(" ")){
				query.setDbName(temp.trim());
				query.setType(1);
			}
		/*	if(inputQuery.length()-1>18 && inputQuery.substring(inputQuery.length()-2).equals("';")
					&& inputQuery.substring(16,17).equals("'")){
				query.setDbName(inputQuery.substring(17, inputQuery.length()-2));
				// method maatrix ely hanadeeha
//				tableOperator = new TableOperator(query);
//				tableOperator.createDB();
				query.setType(1);
			}else if(inputQuery.length()-1>18 && inputQuery.substring(inputQuery.length()-3).equals("' ;")
					&& inputQuery.substring(16,17).equals("'")){
				query.setDbName(inputQuery.substring(17, inputQuery.length()-3));
				// method maatrix ely hanadeeha
//				tableOperator = new TableOperator(query);
//				tableOperator.createDB();
				query.setType(1);
			}else if(inputQuery.length()-1>17 && !inputQuery.substring(17, inputQuery.length()-2).contains("'") 
					&& !inputQuery.substring(17, inputQuery.length()-3).contains(" ")){
				query.setDbName(inputQuery.substring(16, inputQuery.length()-2).trim());
//				validator = new Validation(query);
				//		b = validator.isDBExist();
				query.setType(1);
			}*/
			else
				System.out.println("Not a Valid Query Statement!");
		//	System.out.println(query.getDbName());
	}
	
	private void dealWithCreateTable() {
		//	temp = "CREATE TABLE ";
		//	b = true;
		//	for(int i=0;i<temp.length();i++){
		//		if(inputQuery.charAt(i)!=temp.charAt(i))
		//			b = false;
		//	}
		//	if(b)
		//		getTableName();
		//	else
		//		System.out.println("Not a Valid Query Statement!");
		if(getSavedTableName(13))
			getTableAttributes(inputQuery.substring(charIndex,inputQuery.length()-1).trim());
		else
			System.out.println("Not a Valid Query Statement!");
		}

	
	private void getTableAttributes(String s) {
		boolean b = true;
	//	System.out.println(s+"teet");
		if(s.length()>2 &&  s.startsWith("(") && s.endsWith(")") && s.substring(1,s.length()-1).trim().length()>4){
			s=s.substring(1,s.length()-1).trim();
			String a[]=s.split(",");
			System.out.println(a.length);
			for(int i=0;i<a.length;i++){
			//	System.out.println(a[i]+"wa2");
				if(!getAttribute(a[i].trim()) || !checkCols()){
					b = false;
					break;
				}	
			}	
			if(!b || checkComas(s)+1!=query.getAttribute().getNumberOfColumns())
				System.out.println("Not a Valid Query Statement!");
			else{
	//		tableOperator = new TableOperator(query);
//			tableOperator.createTable();
			query.setType(2);
			}
		}else
			System.out.println("Not a Valid Query Statement!");
	}

	private int checkComas(String s) {
		int counter=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)==',')
				++counter;
		}
		return counter;
	}

	private boolean getAttribute(String s) {
	//	boolean b[] = new boolean[4];
		boolean b = true;
		System.out.println(s);
		String temp="";
		int curPos=0;
		if(s.length()==0)
			return false;
		if(s.length()>2 && s.startsWith("'")){
			++curPos;
			while(curPos<s.length() && !s.substring(curPos, curPos+1).equalsIgnoreCase("'"))
				temp+=s.charAt(curPos++);
			if(curPos<s.length())
				temp+=s.charAt(curPos++);
			System.out.println(temp);
			if(temp.endsWith("'")){
				if(temp.substring(0,temp.length()-1).trim().length()==0)
					return false;
				query.getAttribute().addCol(temp.substring(0,temp.length()-1).trim());
				if(!getTableValue(s.substring(curPos).trim()))
					return false;
			}else return false;
		}else{
			while(curPos<s.length() && s.charAt(curPos)!=' ')
				temp+=s.charAt(curPos++);
			System.out.println(temp);
			if(temp.contains(" "))
				return false;
			else 
				query.getAttribute().addCol(temp.trim());
		//	curPos++;
			if(!getTableValue(s.substring(++curPos).trim()))
				return false;
		}
		return true;
	}

	private boolean getTableValue(String s) {
	//	String temp="";
	//	int curPos=0;
	//	while(curPos<s.length() && s.charAt(curPos)!=' ')
	//		temp+=s.charAt(curPos++);
		boolean b[] = new boolean[4];
		String a[]=s.split(" ");
		if(getType(a[0].trim())){
			for(int i=1;i<a.length;i++){
				if(getProp(a[i].trim())==4)
					return false;
				else if(getProp(a[i].trim())==0){
					b[0]=true;
					b[1]=true;
					b[2]=true;
				}else
					b[getProp(a[i].trim())]=true;
			}
		}else{
			return false;
		}
	//	System.out.println("ya rab");
	//	for(int i=0;i<b.length;i++)
	//		System.out.println(b[i]);
		query.getAttribute().addProperties(b);
		return true;
	}

	private int getProp(String s) {
		if(s.equalsIgnoreCase("AUTOINCREMENT")){
			if(query.getAttribute().getType(query.getAttribute().getNumberOfTypes()-1).equalsIgnoreCase("int"))
				return 0;
			else return 4;
		}else if(s.equalsIgnoreCase("NOTNULL"))
			return 1;
		else if(s.equalsIgnoreCase("READONLY"))
			return 2;
		else if(s.equalsIgnoreCase("NOTSEARCHABLE"))
			return 3;
		else 
			return 4;
	}

	private boolean getType(String s) {
		if(s.equalsIgnoreCase("INT"))
			query.getAttribute().addType("int");
		else if(s.equalsIgnoreCase("DOUBLE"))
			query.getAttribute().addType("double");
		else if(s.equalsIgnoreCase("BOOLEAN"))
			query.getAttribute().addType("boolean");
		else if(s.equalsIgnoreCase("STRING"))
			query.getAttribute().addType("string");
		else if(s.equalsIgnoreCase("DATE"))
			query.getAttribute().addType("date");
		else
			return false;
		return true;
	}

	private void getTableName1() {
		String s="";
			//int i = temp.length();
		int i = 13;
		while(i < inputQuery.length() && inputQuery.charAt(i)!='('){
			s+=inputQuery.charAt(i);
			++i;
		}
		//	s=s.trim();
		//	System.out.println(s);
		if(inputQuery.charAt(13+s.length())=='(' 
				&& (inputQuery.charAt(inputQuery.length()-2)==')' ||
				(inputQuery.charAt(inputQuery.length()-3)==')'
				&& inputQuery.charAt(inputQuery.length()-2)==' ') )
				&& checkSpaces(13,13+s.length())){
				query.setTableName(s.trim());
				checkAttributesFormat(13+s.length()+1);
		}
		else
			System.out.println("Not a Valid Query Statement!");
	}

	private void checkAttributesFormat(int begin) {
		int counter1 = 0,counter2 = 0;
		String s="";
		//	System.out.println(inputQuery.charAt(begin));
		//	if(inputQuery.charAt(begin)=='(')
		//		++begin;
		for(int i=begin;i<inputQuery.length()-2;i++){
			if(inputQuery.charAt(i)!=' ' && inputQuery.charAt(i)!=','){
				s+=inputQuery.charAt(i);
			}else if(inputQuery.charAt(i)==' ' && !(inputQuery.charAt(i-1)==',' 
					|| inputQuery.charAt(i+1)==',' || i==begin || i==inputQuery.length()-3)){
				++counter1;
				names.add(s);
				s="";
			}else if(inputQuery.charAt(i)==','){
				++counter2;
				types.add(s);
				s="";
			}
		}
		types.add(s);
		if(counter1==counter2+1)
			dealWithTableAttributes();
		else
			System.out.println("Not a Valid Query Statement!");	
	}

	private void dealWithTableAttributes() {
		b = true;
		for(int i=0;i<types.size();i++){
			if(!(types.get(i).equals("String") || types.get(i).equals("int") || types.get(i).equals("float")
					|| types.get(i).equals("boolean") || types.get(i).equals("double")))
				b = false;
		}
		if(b){
			for(int i=0;i<types.size();i++)	
				query.getAttribute().addColumnWithType(names.get(i), types.get(i));
			checkCols();
			if(b){
//				tableOperator = new TableOperator(query);
//				tableOperator.createTable();
				query.setType(2);
			}
			else
				System.out.println("Not a Valid Query Statement!");
		}
		else
			System.out.println("Not a Valid Query Statement!");
	}
	
	private void dealWithInsertInto() {
		charIndex = 12;
		if( getSavedTableName(charIndex) && charIndex < inputQuery.length()-1 && charIndex+6<inputQuery.length() &&(inputQuery.substring(charIndex,charIndex+1).equalsIgnoreCase("V")
				 || inputQuery.substring(charIndex,charIndex+2).equalsIgnoreCase(" V"))){
			//query.getAttribute().addCol(null);
			checkWordValue();
		}
		else if(getSavedTableName(charIndex=12) && charIndex < inputQuery.length()-1 && (inputQuery.charAt(charIndex)=='('
				|| inputQuery.substring(charIndex,charIndex+2).equalsIgnoreCase(" (")))
			addCol(inputQuery.substring(charIndex, inputQuery.length()-1).trim());
		else
			System.out.println("Not a Valid Query Statement!");
	}

		

	private void addCol(String s) {
		String temp="";
		b = true;
		System.out.println(s);
		if(inputQuery.charAt(charIndex)==' ')
			++charIndex;
		for(int i=0;i<s.length();i++){
			temp+=s.charAt(i);
			if(s.charAt(i)==')')
				break;
		}
		charIndex+=temp.length();
		if(temp.length()>2 && temp.startsWith("(") && temp.endsWith(")")){
			String a []= temp.substring(1,temp.length()-1).split(",");
		//	for(int i=0;i<a.length;i++)
		//		System.out.print(a[i]+" ");
		//	System.out.println();
			for(int i=0;i<a.length;i++){
				a[i]=a[i].trim();
				System.out.print(a[i].contains(" ")+" ");
				if(!a[i].equals("") && a[i].startsWith("'") && a[i].endsWith("'"))
					query.getAttribute().addCol(a[i].substring(1,a[i].length()-1).trim());
				else if(!a[i].equals("") && !a[i].contains(" "))
					query.getAttribute().addCol(a[i].trim());
				else{
					System.out.println(i);
					b=false;
					break;
				}
			}
			names.add("-1");
			System.out.println();
			if(b)
				checkWordValue();
			else
				System.out.println("Not a Valid Query Statement!");
		}else
			System.out.println("Not a Valid Query Statement!");
	}

	private void addCol1() {
		String s="";
		b = true;
		++charIndex;
		while(charIndex < inputQuery.length() && inputQuery.charAt(charIndex)!=')'){
			if(inputQuery.charAt(charIndex)==','){
				if(checkSpaces(charIndex-s.length(),charIndex))
					query.getAttribute().addCol(s.trim());
				else
					b=false;
				s="";
		}
		else
			s+=inputQuery.charAt(charIndex);
			++charIndex;
		}
		if(s!="")
			query.getAttribute().addCol(s.trim());
		names.add("-1");
		checkCols();
		if(charIndex < inputQuery.length() && b && inputQuery.charAt(++charIndex)==' '){
			++charIndex;
			checkWordValue();
		}
		else
			System.out.println("Not a Valid Query Statement!");	
	}
	
	private void checkWordValue() {
		temp = "";
		if(inputQuery.charAt(charIndex)==' ')
			++charIndex;
		b = true;
	//	for(int i=0;i<6 && charIndex < inputQuery.length();i++){
		//	if(inputQuery.charAt(charIndex)!=temp.charAt(i))
		//		b = false;
	//		temp+=inputQuery.charAt(charIndex);
	//		++charIndex;
	//	}
		if(charIndex+6>inputQuery.length()-1 || !inputQuery.substring(charIndex, charIndex+6).equalsIgnoreCase("VALUES"))
			b = false;
		charIndex+=6;
		if(charIndex < inputQuery.length() && b)
			addValues(inputQuery.substring(charIndex, inputQuery.length()-1).trim());
		else
			System.out.println("Not a Valid Query Statement!");
	}

	private void addValues(String s) {
		b=true;
		System.out.println(s);
		if(s.length()>2 && s.startsWith("(") && s.endsWith(")")){
			String a []= s.substring(1,s.length()-1).trim().split(",");
			for(int i=0;i<a.length;i++)
				System.out.print(a[i]+" ");
			System.out.println();
			
			for(int i=0;i<a.length;i++){
				a[i]=a[i].trim();
				if(!a[i].equals("") && a[i].startsWith("'") && a[i].endsWith("'"))
					query.getAttribute().addValue(a[i].substring(1,a[i].length()-1).trim());
				else{
					try{
						int check = Integer.parseInt(a[i]);
						query.getAttribute().addValue(a[i]);
				//		System.out.println(check +"number to be checked");
					}catch(Exception e){
						b=false;
						break;
					}
				}
			}
			if(names.size() == 1 && query.getAttribute().getNumberOfColumns()!=query.getAttribute().getNumberOfValues())
				b = false;
			if(b && checkCols()){
				//method 3abdou
//				tableOperator = new TableOperator(query);
//				tableOperator.insertInto();
				query.setType(3);
				System.out.println("leh");
			}else
				System.out.println("Not a Valid Query Statement!");
		}else
			System.out.println("Not a Valid Query Statement!");
	}

	
	
	private void dealWithDelete() {
		charIndex = 12;
		if(getSavedTableName(charIndex) && (query.getTableName().contains("=") || query.getTableName().contains(">")
			|| query.getTableName().contains("<")))
			System.out.println("Not a Valid Query Statement!");
		else if(getSavedTableName(charIndex=12) && (inputQuery.substring(charIndex).equals(" ;") || inputQuery.charAt(charIndex)==';')){
			query.setCondition(null);
//			tableOperator = new TableOperator(query);
//			tableOperator.delete();
			System.out.println("Delete all");
			query.setType(4);
		}
		else if(getSavedTableName(charIndex=12) && charIndex+6<inputQuery.length()-1 && inputQuery.substring(charIndex, charIndex+5).equalsIgnoreCase("WHERE")){
			charIndex+=5;
			System.out.println("hna");
		//	if(charIndex < inputQuery.length()-1 && b){
				if(!createInfix(inputQuery.substring(charIndex, inputQuery.length()-1).trim()))
					System.out.println("Not a Valid Query Statement! ya rab");
		//	}
		}
	//		checkWordWhere();
		else
			System.out.println("Not a Valid Query Statement :3 !");
		System.out.println(charIndex+6<inputQuery.length()-1);
	}	

	private void checkWordWhere() {
		temp = "WHERE ";
		b = true;
		if(charIndex+6>inputQuery.length()-1 || !inputQuery.substring(charIndex, charIndex+6).equalsIgnoreCase("VALUES"))
			b = false;
		charIndex+=5;
		if(charIndex < inputQuery.length() && b){
			if(!createInfix(inputQuery.substring(charIndex, inputQuery.length()-1).trim()))
				System.out.println("Not a Valid Query Statement! ya rab");
		}
		else
			System.out.println("Not a Valid Query Statement! heya di");
	/*	for(int i=0;i<temp.length() && charIndex < inputQuery.length();i++){
			if(inputQuery.charAt(charIndex)!=temp.charAt(i))
				b = false;
			++charIndex;
		}
		if(charIndex < inputQuery.length() && b)
			addCol2();
		else
			System.out.println("Not a Valid Query Statement!");*/
	}

/*	private void addCol2() {
		String s="";
		b = true;
		while(charIndex < inputQuery.length()-1){
			if(inputQuery.charAt(charIndex)=='=' || inputQuery.charAt(charIndex)=='>'
					|| inputQuery.charAt(charIndex)=='<'){
				query.getCondition().setColumnName(s.trim());
				break;
			}
			else
				s+=inputQuery.charAt(charIndex);
			++charIndex;
		}
		if(s.contains("=") || s.contains(">") || s.contains("<"))
			b =false;
		s=""+inputQuery.charAt(charIndex);
	//	if(inputQuery.lastIndexOf('=')==charIndex || inputQuery.lastIndexOf('<')==charIndex
	//			|| inputQuery.lastIndexOf('>')==charIndex)
			query.getCondition().setoperator(s);
	//	else
	//		b = false;
		if(b && (inputQuery.charAt(charIndex)=='=' || inputQuery.charAt(charIndex)=='>'
				|| inputQuery.charAt(charIndex)=='<') && checkSpaces(charIndex-s.length(),charIndex)){
			//method 3abdou
			addValue2();
		}
		else
			System.out.println("Not a Valid Query Statement!");
	}*/

/*	private void addValue2() {
		String s="";
		b = true;
		++charIndex;
		while(charIndex < inputQuery.length()-1){
			s+=inputQuery.charAt(charIndex);
			++charIndex;
		}
		if(checkSpaces(charIndex-s.length(),charIndex) && s!="" && !s.contains("=")
				&& !s.contains(">") && !s.contains("<")){
			query.getCondition().setValue(s.trim());
			//method3abodu
//			tableOperator = new TableOperator(query);
//			tableOperator.delete();
			query.setType(4);
		}
		else
			System.out.println("Not a Valid Query Statement!");
	}*/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean checkElement(String element) {
		element = element.trim();
		if (element.startsWith("'")) {
			if (element.endsWith("'"))
				return true;
			else
				return false;
		} else {
			if (element.contains(" "))
				return false;
		}
		return true;
	}

	private String getElement(String element) {
		element = element.trim();
		if (element.startsWith("'")) {
			if (element.endsWith("'"))
				element = element.substring(1, element.length() - 1);
		}
		return element;
	}

	private boolean setColumns(int from, int to) {
		boolean oneAtr = false, repeated = false;
		String[] columns = inputQuery.substring(from, to).split(",");
		if (columns[0].trim().equals("*") && columns[0].trim().length() == 1
				&& columns.length == 1) {
			query.setAttribute(null);
			return true;
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].trim().contains("*"))
				return false;
			if (checkElement(columns[i])) {
				for (int j = i - 1; j >= 0 && !repeated; j--)
					if (getElement(columns[i]).equals(getElement(columns[j])))
						repeated = true;
				if (!repeated)
					query.getAttribute().addCol(getElement(columns[i]));
			} else
				return false;
			oneAtr = true;
			repeated = false;
		}
		return oneAtr;
	}

	private int checkSet(String set) {
		// 0>>invalid //1>>int //2>>double //3>>boolean //4>>string
		set = set.trim();
		if (set.indexOf("=") > 0 && set.indexOf("=") < set.length() - 1
				&& !set.contains(">") && !set.contains("<")
				&& set.indexOf("=", set.indexOf("=") + 1) < 0) {
			String value = set.substring(set.indexOf("=") + 1, set.length());
			value = value.trim();
			if (value.startsWith("'") && value.endsWith("'"))
				return 4;
			else if (!value.contains("'") && !value.contains(" ")) {
				if (value.equalsIgnoreCase("true")
						|| value.equalsIgnoreCase("false"))
					return 3;
				for (int i = 0; i < value.length(); i++) {
					if ((value.charAt(i) < 48 || value.charAt(i) > 57)
							&& value.charAt(i) != 46)
						return 0;
				}
				if (!value.contains("."))
					return 1;
				else {
					if (value.split(".").length > 2)
						return 0;
				}
				return 2;
			} else
				return 0;
		} else
			return 0;
	}

	private boolean setSets(int from, int to) {
		String column = "", type = "", value = "";
		boolean oneAtr = false;
		String[] sets = inputQuery.substring(from, to).split(",");
		for (int i = 0; i < sets.length; i++) {
			sets[i] = sets[i].trim();
			if (checkSet(sets[i]) > 0)
				column = sets[i].substring(0, sets[i].indexOf("=")).trim();
			if (checkSet(sets[i]) == 1) { /* INT */
				type = "int";
				value = sets[i].substring(sets[i].indexOf("=") + 1,
						sets[i].length()).trim();
			} else if (checkSet(sets[i]) == 2) { /* DOUBLE */
				type = "double";
				value = sets[i].substring(sets[i].indexOf("=") + 1,
						sets[i].length()).trim();
			} else if (checkSet(sets[i]) == 3) { /* BOOLEAN */
				type = "boolean";
				value = sets[i].substring(sets[i].indexOf("=") + 1,
						sets[i].length()).trim();
			} else if (checkSet(sets[i]) == 4) { /* STRING */
				type = "string";
				value = sets[i].substring(sets[i].indexOf("="),
						sets[i].length()).trim();
				value = value.substring(value.indexOf("'") + 1,
						value.indexOf("'", value.indexOf("'") + 1));
			} else
				return false;
			if (!column.equals("") && !type.equals("") && !value.equals("")) {
				query.getAttribute()
						.addColWithTyptAndValue(column, type, value);
				oneAtr = true;
			}
		}
		return oneAtr;
	}

	private boolean setTableName(int from, int to) {
		if (checkElement(inputQuery.substring(from, to)))
			query.setTableName(getElement(inputQuery.substring(from, to)));
		else
			return false;
		return true;
	}

	

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// SELECTION
	// ///////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////
	private boolean checkSelect() {
		if (containsIgnoreCase(inputQuery, " FROM ")) {
			if (containsIgnoreCase(inputQuery, " WHERE ")) {
				if (indexOfIgnoreCase(inputQuery, " FROM ") > indexOfIgnoreCase(
						inputQuery, " WHERE "))
					return false;
			}
		} else
			return false;
		return true;
	}

	private void dealWithSelect() {
		boolean valid = true;
		System.out.println("select");

		int endTableName = inputQuery.length() - 1;
		if (containsIgnoreCase(inputQuery, " WHERE "))
			endTableName = indexOfIgnoreCase(inputQuery, " WHERE ");
		if (!checkSelect()){
			valid = false;
			System.out.println("Not a Valid Query Statement! 1");
		}
		if (valid && !setColumns(7, indexOfIgnoreCase(inputQuery, " FROM "))){
			valid = false;
			System.out.println("Not a Valid Query Statement! 2");
		}
		if (valid &&!setTableName(indexOfIgnoreCase(inputQuery, "FROM") + 4, endTableName)){
			valid = false;
			System.out.println("Not a Valid Query Statement! 3");
		}
		if (valid && containsIgnoreCase(inputQuery, " WHERE ")){
			if(!createInfix(inputQuery.substring( indexOfIgnoreCase(inputQuery, "WHERE") + 5,inputQuery.length() - 1))){
				valid = false;
				System.out.println("Not a Valid Query Statement! 4");
			}
			
		}
		if(valid) {
			query.setType(5);
		}
		charIndex = 0;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// UPDATE
	// ///////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////

	private boolean checkUpdate() {
		if (containsIgnoreCase(inputQuery, " SET ")) {
			if (containsIgnoreCase(inputQuery, " WHERE ")) {
				if (indexOfIgnoreCase(inputQuery, " SET ") > indexOfIgnoreCase(
						inputQuery, " WHERE "))
					return false;
			}
		} else
			return false;
		return true;
	}

	private void dealWithUpdate() {
		int endSet = inputQuery.length() - 1;
		if (containsIgnoreCase(inputQuery, " WHERE "))
			endSet = indexOfIgnoreCase(inputQuery, " WHERE ");


		if (!checkUpdate())
			System.out.println("Not a Valid Query Statement! 1");
		else if (!setTableName(7, indexOfIgnoreCase(inputQuery, " SET ")))
			System.out.println("Not a Valid Query Statement! 2");
		else if (!setSets(indexOfIgnoreCase(inputQuery, "SET") + 3, endSet))
			System.out.println("Not a Valid Query Statement 3!");
		else if (containsIgnoreCase(inputQuery, " WHERE ")&&!createInfix(inputQuery.substring(
				indexOfIgnoreCase(inputQuery, "WHERE") + 5,
				inputQuery.length() - 1)))
			System.out.println("Not a Valid Query Statement 4!");
		else {
			// TableOperator tableOperator = new TableOperator(query);
			// tableOperator.update();
			query.setType(6);
		}
		charIndex = 0;
	}
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////	

	public TableOperator getTableOperator() {
		return tableOperator;
	}
	
}
