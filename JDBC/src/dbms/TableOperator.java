package dbms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class TableOperator implements DBMS
{
	private Query query; // this should contain attributes + values + types + dbName + tableName + condition
	private Validation validator;
	static String resultOfOperation;
	Logger log = Logger.getLogger(TableOperator.class);
	
	public String getResultOfOperation() {
		return resultOfOperation;
	}
	
	public TableOperator() 
	{
		validator = new Validation();
	}

	public TableOperator(Query query) 
	{
		this.query = query;
		validator = new Validation(query);
	}
	
	public void setQueryAndValidator(Query query)
	{
		this.query = query;
		validator.setQuery(query);
	}
	

	
	@Override
	public int createDB()
	{
		System.out.println(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName());
		File directory = new File(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName());
    	// create directory
    	boolean success = directory.mkdirs();
    	if (!success) 
    	{
    		this.resultOfOperation = "Failed to create the database! May be its already exist.";	
//    		System.out.println("Failed to create the database! May be its already exist.");
    		log.info("Failed to create the database! May be its already exist.");
    		return -1;
    	}
    	else
    	{
    		this.resultOfOperation = "Database "+query.getDbName()+" Created Successfully.";
//    		System.out.println("Database "+query.getDbName()+" Created Successfully.");
    		log.info("Database "+query.getDbName()+" Created Successfully.");
    		return 0;
    	}
	}

	@Override
	public int createTable() 
	{
		if(!validator.isTableExist())
		{
			Element table = new Element(query.getTableName());
	
			int valid = 0; // 0 valid , -1 invalid
			
			Element attributes = new Element("attributes");
			for(int i = 0; i < query.getAttribute().getNumberOfColumns(); i++)
			{
				Element column = new Element("column");
				Element name = new Element("name").setText(query.getAttribute().getColumn(i));
                column.addContent(name);
				if(validator.isSupportedType(query.getAttribute().getType(i)))
				{
				    Element type = new Element("type").setText(query.getAttribute().getType(i));
				    boolean[] columnProperties = query.getAttribute().getProperties(i);
				    // auto>>0 , not nullable>>1 , readonly >> 2 !(writable), searchable >> 3 
				    Element auto = new Element("auto").setText(columnProperties[0]+"");
				    Element notNullable = new Element("notNullable").setText(columnProperties[1]+"");
				    Element readOnly = new Element("readOnly").setText(columnProperties[2]+"");
				    Element writable = new Element("writable").setText(!columnProperties[2]+"");
				    Element NotSearchable = new Element("NotSearchable").setText(columnProperties[3]+"");
				    column.addContent(type);
				    column.addContent(auto);
				    column.addContent(notNullable);
				    column.addContent(readOnly);
				    column.addContent(writable);
				    column.addContent(NotSearchable);
				}
				else
				{
					this.resultOfOperation ="Can't create the table. Unsupported type.";
//				    System.out.println("Can't create the table. Unsupported type.");
					log.info("Can't create the table. Unsupported type.");
				    valid = -1;
				}
				if(valid == 0)   attributes.addContent(column);
			   	else return valid;
			}
			table.addContent(attributes);
			
			Element records = new Element("records");
			table.addContent(records);
            
			Document doc = new Document();
			doc.setRootElement(table);
			
			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();
		 
			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			
			//create file to save in
			try 
			{
//				System.out.println(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");
				
				FileOutputStream stream = new FileOutputStream(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");
			}catch (FileNotFoundException e1) {e1.printStackTrace();}
			try 
			{
				xmlOutput.output(doc, new FileWriter(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml"));
			} catch (IOException e) { e.printStackTrace();}
			this.resultOfOperation = "Table Created Successfully.";
//			System.out.println("Table Created Successfully.");
			log.info("Table Created Successfully.");
			
		}
		else
		{ 
//		    System.out.println("Can't create the table. It's already exist.");
			log.info("Can't create the table. It's already exist.");
		    return -1;
		}
		return 0;
	}

	@Override
	public ArrayList<ArrayList<String> > select() 
	{
		boolean getColumnNames = true;
	    ArrayList<ArrayList<String> > returnTable = new ArrayList();
	    ArrayList<String> columnNamesRow = new ArrayList<String>();
	    returnTable.add(columnNamesRow);
	    
	    HashMap<String,ArrayList<String>> tableWithProperties = getColumnsProperties();
	    
	    if(validator.isTableExist())
        {
            SAXBuilder builder = new SAXBuilder();
		
            File xmlFile = new File(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");
		    
		    // converted file to document object
			Document document = null;
			try 
			{
				document = builder.build(xmlFile);
				// get root node from xml  
				Element table = document.getRootElement();
				Element records = table.getChild("records");
        		List<Element> recordList = (List<Element>) records.getChildren(); 
            	Element attributes = table.getChild("attributes"); 
                List<Element> columns = (List<Element>) attributes.getChildren(); 
        		
        		boolean hasCondition = (query.getCondition()!=null);
        		
        		if(query.getAttribute()==null) // astrek
        		{
        		    if(hasCondition && !validator.isCondition(tableWithProperties)) // condtion should be handled here
        		    {
//        		        System.out.println("Invalid Query. Please check the condition.");
        		    	log.info("Invalid Query. Please check the condition.");
        		        return null;
        		    }
        		    int index = 0,entered = 0;
        		    String columnName="";
    		   		for(Element record: recordList)
    	   	        {   
    		   			ArrayList<String> row = new ArrayList<String>();
    		   			returnTable.add(row);
    		   				
    			        for(Element column: columns)
    			        {
    			        	columnName = column.getChildText("name");
    			            if ( (!hasCondition || applyCondition(record)) && !isNotSearchable(tableWithProperties,columnName))
    			            {
    			            	if(getColumnNames) 
    			            	{
    			            		returnTable.get(index).add(columnName); // to get the column names
    			            		returnTable.get(index).add(column.getChildText("type"));
    			            		entered++;
    			            	}
    			            	returnTable.get(index+1).add(record.getChildText(columnName));
    			            }
    			        }
    			        if(entered!=0) getColumnNames = false;
    			        index++;
    	    	     }
    		   		this.resultOfOperation = "Successful Selection";
        		}
			    else // without astrek
			    {
			    	if(validator.areAttributesExist(tableWithProperties))
			    	{
				       if(hasCondition && !validator.isCondition(tableWithProperties))
	        		    {
//	        		        System.out.println("Invalid Query. Please check the condition.");
				    	   log.info("Invalid Query. Please check the condition.");
	        		        return null;
	        		    }
	        		    int index = 0,entered =0;
	        		    String columnName="",type="";
	    		   		for(Element record: recordList)
	    	   	        {   
	    		   			ArrayList<String> row = new ArrayList<String>();
	    		   			returnTable.add(row);
	    		   			
	    		   			for(Element column: columns)
    			            {
    			                 columnName = column.getChildText("name");
    			                 type = column.getChildText("type");
    			                 if(query.getAttribute().getColumns().contains(columnName) && !isNotSearchable(tableWithProperties,columnName))
    			                 {
        			                 if (!hasCondition || applyCondition(record))// with or without condition
    				                 {
    				                	if(getColumnNames)
    				                	{
    				                		returnTable.get(index).add(columnName); // to get the column names
    				                		returnTable.get(index).add(type);
    				                		entered++;
    				                	}
    				                	returnTable.get(index+1).add(record.getChildText(columnName));    
    				                 }
    			                 }
    			            }
				            if(entered!=0) getColumnNames = false;
				            index++;
				        }
				     }
			    	else
			    	{
//			    		System.out.println("Invalid Query.Columns doesn't exist.");
			    		log.info("Invalid Query.Columns doesn't exist.");
			    		return null;
			    	}
			    }
			}
			catch (JDOMException | IOException e) {e.printStackTrace();}
        }
        else
        {
//            System.out.println("Invalid Query. Table doesn't exist.");
        	log.info("Invalid Query. Table doesn't exist.");
		    return null;
        }
	    this.resultOfOperation="Successful Selection";
//	    System.out.println("Successful Selection");
	    log.info("Successful Selection");
		return returnTable;
	}
	
	public boolean applyCondition(Element record)
	{
		return Postfix.evaluatePostFix(query.getCondition().getPostfix(), record);
	}

	@Override
	public int update() 
	{
		int updatedRecords = 0;
		HashMap<String,ArrayList<String>> tableWithProperties = getColumnsProperties();
		// converted file to document object
		Document document = null;		    
	    if(validator.isTableExist() && validator.areAttributesExist(tableWithProperties) && validator.isAttributesTypeGotValue(tableWithProperties))
        {
            SAXBuilder builder = new SAXBuilder();
		    File xmlFile = new File(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");
			try 
			{
				document = builder.build(xmlFile);
				// get root node from xml  
				Element table = document.getRootElement();
				
				Element records = table.getChild("records");
        		List<Element> recordList = (List<Element>) records.getChildren(); 
        		
            	Element attributes = table.getChild("attributes");
                List<Element> columns = (List<Element>) attributes.getChildren();

        		boolean hasCondition = (query.getCondition()!=null);

        		if(hasCondition) // there is a condition
        		{
        		    if(validator.isCondition(tableWithProperties))
        		    {
        		        for(Element record: recordList)
    	   	            {
    	   	                if(applyCondition(record))
                            {
            	   	             for(int i = 0 ; i < query.getAttribute().getNumberOfColumns() ; i++)
            			         {
            	   	            	 String columnName = query.getAttribute().getColumn(i);
            	   	            	 if(!isAutoIncremented(tableWithProperties,columnName) && !isReadOnly(tableWithProperties, columnName))
            			             {
            	   	            		 record.getChild(columnName).setText(query.getAttribute().getValue(i));
            	   	            		 updatedRecords++;
            			             }
            			         }
                            }
    	   	            }    			              
        		    }
        		    else {
        		    	this.resultOfOperation = "Invalid Query. Please check the condition.";
//                        System.out.println("Invalid Query. Please check the condition.");
        		    	log.info("Invalid Query. Please check the condition.");
        		        return -1;
        		    }
        		}
        		else // no condition
        		{
        		    for(Element record: recordList)
    	   	        {
                         for(int i = 0 ; i < query.getAttribute().getNumberOfColumns() ; i++)
    			         {
                        	 String columnName = query.getAttribute().getColumn(i);
    	   	            	 if(!isAutoIncremented(tableWithProperties,columnName) && !isReadOnly(tableWithProperties, columnName))
    			             {
    	   	            		 record.getChild(columnName).setText(query.getAttribute().getValue(i));
    	   	            		 updatedRecords++;
    			             }
                         }
    	   	        } 
        		}
			}
			catch (JDOMException | IOException e) {e.printStackTrace();}
        }
        else
        {
        	this.resultOfOperation = "Invalid Query.";   
//            System.out.println("Invalid Query.");
        	log.info("Invalid Query.");
		    return -1;
        }
        
        XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try 
		{
			xmlOutput.output(document, new FileWriter(Parser.workSpacePath+"DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml"));
		} catch (IOException e) {e.printStackTrace();}
		this.resultOfOperation="Table updated successfully.";
//        System.out.println("Table updated successfully.");   
		log.info("Table updated successfully.");
		return updatedRecords;
	}

	@Override
	public int delete() 
	{  	
		// converted file to document object
		Document document = null;
		int deletedRecords = 0;
	    if(validator.isTableExist())
        {
            SAXBuilder builder = new SAXBuilder();
		    File xmlFile = new File(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");
			try 
			{
				document = builder.build(xmlFile);
				// get root node from xml  
				Element table = document.getRootElement();
				
				Element records = table.getChild("records");
        		List<Element> recordList = (List<Element>) records.getChildren(); 
        		
            	Element attributes = table.getChild("attributes"); // when implementing the condition
                List<Element> columns = (List<Element>) attributes.getChildren(); 
        		
        		boolean hasCondition = (query.getCondition()!=null);
                
        		if(hasCondition) // there is a condition
        		{
        		    if(validator.isCondition(getColumnsProperties()))
        		    {
            	    	List<Element> elements = new ArrayList<Element>();
            	    	Iterator iterator =recordList.iterator(); 
            	    	while(iterator.hasNext())
            	    	{
            	    	   Element subchild = (Element) iterator.next();
            	    	   if (applyCondition(subchild))
            	    	   {
            	    	       deletedRecords++;
            	    	       elements.add(subchild);
            	    	   }

            	    	}
            	    	  
            	    	for (Element element : elements)    element.getParent().removeContent(element);
            	    	recordList =  elements;
        		    }
        		    else 
        		    {
//                        System.out.println("Invalid Query. Please check the condition.");
        		    	log.info("Invalid Query. Please check the condition.");
        		        return -1;
        		    }
        		}
        		else // no condition
        		{
        			deletedRecords = recordList.size();
        		    records.removeContent();
        		}
			}
			catch (JDOMException | IOException e) {e.printStackTrace();}
        }
        else
        {
//            System.out.println("Invalid Query. Table doesn't exist.");
        	log.info("Invalid Query. Table doesn't exist.");
		    return -1;
        }
        
        XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try 
		{
			xmlOutput.output(document, new FileWriter(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml"));
		} catch (IOException e) {e.printStackTrace();}
		
        if(deletedRecords!=0 && deletedRecords!=-1){
        	this.resultOfOperation = "Record deleted successfully.";
//        	System.out.println("Record deleted successfully.");
        	log.info("Record deleted successfully.");
        }   
//        else System.out.println("No records found.");
        log.info("No records found.");
		return deletedRecords;
		
	}
	
	public void addAllColumns(List<Element> columns)
	{
        for(Element column: columns)
    		{
        		boolean exist = false;
        		for(int i = 0 ; i < query.getAttribute().getNumberOfColumns() ; i++)
        		{
        			if(column.getChildText("name").equalsIgnoreCase(query.getAttribute().getColumn(i)))	exist = true;
        		}
        		if(!exist) query.getAttribute().addCol(column.getChildText("name"));
    		}
        for(int i = query.getAttribute().getNumberOfValues() ; i < query.getAttribute().getNumberOfColumns()  ; i++)
        	query.getAttribute().addValue("empty");
	}

	@Override
	public int insertInto() 
	{
		int insertedColumns = 0;
	    // converted file to document object
		Document document = null;   
		HashMap<String,ArrayList<String>> tableWithProperties = getColumnsProperties();
	    if(validator.isTableExist())
        {
            SAXBuilder builder = new SAXBuilder();
		    File xmlFile = new File(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");

			try 
			{
				document = builder.build(xmlFile);
				// get root node from xml  
				Element table = document.getRootElement();
				
				Element records = table.getChild("records");
        		List<Element> recordList = (List<Element>) records.getChildren(); 
        		
            	Element attributes = table.getChild("attributes"); // when implementing the condition
                List<Element> columns = (List<Element>) attributes.getChildren(); 
                
                // a not nullable column is not inserted
                if(!isNullableInserted(tableWithProperties)) return -1;
    			
        		if(query.getAttribute().getNumberOfColumns() != columns.size()) // no columns
                {	addAllColumns(columns);	}


		        if(validator.isAttributesTypeGotValue(tableWithProperties))
		        {
    				Element record  = new Element("record");
    				
    				// do auto-incremented for columns
    				incrementAutoIncrementedColumns(record, tableWithProperties);
    				
                	for(int i = 0 ; i < query.getAttribute().getNumberOfValues() ; i++)
                	{
                		if(!isAutoIncremented(tableWithProperties, query.getAttribute().getColumn(i)))
                	    {
                			Element column = new Element(query.getAttribute().getColumn(i)).setText(query.getAttribute().getValue(i));
                			record.addContent(column);
                			insertedColumns++;
                	    }
                	}
            		records.addContent(record);
		        }
		        else
		        {
		        	this.resultOfOperation = "Invalid Values.";   
//		            System.out.println("Invalid Values.");
		        	log.info("Invalid Values.");
		            return -1;
		        }
			}
			catch (JDOMException | IOException e) {e.printStackTrace();}
        }
        else
        {
        	this.resultOfOperation = "Invalid Query. Table doesn't exist.";
//            System.out.println("Invalid Query. Table doesn't exist.");
        	log.info("Invalid Query. Table doesn't exist.");
		    return -1;
        }
        
        XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try 
		{
			xmlOutput.output(document, new FileWriter(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml"));
		} catch (IOException e) {e.printStackTrace();}
		
		this.resultOfOperation = "Record inserted successfully.";   
//        System.out.println("Record inserted successfully.");
		log.info("Record inserted successfully.");
		return insertedColumns;
	}

	
	private void incrementAutoIncrementedColumns(Element record,HashMap<String, ArrayList<String>> tableWithProperties) 
	{
		ArrayList<String> autoIncrementedColumns = new ArrayList<String>();
		Iterator it = tableWithProperties.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
	        ArrayList<String> value = (ArrayList<String>) pairs.getValue();
	        if(value.get(1).equals("true"))	autoIncrementedColumns.add((String)pairs.getKey());
	    }
	    
	    ArrayList<Integer> nextValue = getMaxValues(autoIncrementedColumns);
	    
	    for(int i =0 ; i < autoIncrementedColumns.size() ; i++)
	    {
	    	Element column = new Element(autoIncrementedColumns.get(i)).setText((nextValue.get(i)+1)+"");
	    	record.addContent(column);
	    }
	}
	
	private ArrayList<Integer> getMaxValues(ArrayList<String> autoIncrementedColumns)
	{
		ArrayList<Integer> maxValues = new ArrayList<Integer>();
		SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");
		try 
		{
			Document document = builder.build(xmlFile);
			Element table = document.getRootElement();
        	Element records = table.getChild("records"); 
            List<Element> recordList = (List<Element>) records.getChildren();
            boolean firstTime = true;
            int temp;
            for (Element record : recordList) 
            {
            	for(int i = 0 ; i < autoIncrementedColumns.size() ; i++)
            	{
            		temp = Integer.parseInt(record.getChildText(autoIncrementedColumns.get(i)));
            		if(firstTime)	maxValues.add(temp);
            		else if(temp > maxValues.get(i))	maxValues.set(i, temp);
            	}
            	firstTime = false;
			}
		}
		catch (JDOMException | IOException e) {e.printStackTrace();}
		return maxValues;
	}

	private boolean isNullableInserted(HashMap<String, ArrayList<String>> tableWithProperties) 
	{
		Iterator it = tableWithProperties.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
	        ArrayList<String> value = (ArrayList<String>) pairs.getValue();
	        if(value.get(2).equals("true"))
	        {
	        	if(!query.getAttribute().getColumns().contains(pairs.getKey())) 
	        	{
	        		if(!isAutoIncremented(tableWithProperties, (String) pairs.getKey()))	return false;
	        	}
	        }
	    }
		return true;
	}

	@Override
	public int detectIntegerCommand(int command) 
	{
		switch(command)
		{
			case 0:		return -1;
			case 1:		return createDB();
			case 2:		return createTable();
			case 3:		return insertInto();
			case 4:		return delete();
			case 6:		return update();
		}
		return -2; // command = -1 or command = 5
	}
	
	private HashMap<String,ArrayList<String>> getColumnsProperties()
	{
		HashMap<String,ArrayList<String>> columnWithProperties = new HashMap<String,ArrayList<String>>();
		SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");
		try 
		{
			Document document = builder.build(xmlFile);
			Element table = document.getRootElement();
        	Element attributes = table.getChild("attributes"); 
            List<Element> columns = (List<Element>) attributes.getChildren();
            for (Element column : columns) 
            {
            	ArrayList<String> properties = new ArrayList<String>();
            	// type>>0 , auto>>1 , not nullable>>2 , readOnly>>3 , writable>>4 , searchable>>5
            	properties.add(column.getChildText("type"));
            	properties.add(column.getChildText("auto"));
            	properties.add(column.getChildText("notNullable"));
            	properties.add(column.getChildText("readOnly"));
            	properties.add(column.getChildText("writable"));
            	properties.add(column.getChildText("NotSearchable"));
            	
            	columnWithProperties.put(column.getChildText("name"),properties);
			}
		}
		catch (JDOMException | IOException e) {e.printStackTrace();}
		return columnWithProperties;
	}
	
	private boolean isNotSearchable(HashMap<String,ArrayList<String>> tableWithProperties,String columnName)
	{
		String isNotSearchable = tableWithProperties.get(columnName).get(5);
		return (isNotSearchable.equals("true"));
	}
	
	private boolean isNotNullable(HashMap<String,ArrayList<String>> tableWithProperties,String columnName)
	{
		String isNotNullable = tableWithProperties.get(columnName).get(2);
		return (isNotNullable.equals("true"));
	}

	private boolean isAutoIncremented(HashMap<String,ArrayList<String>> tableWithProperties,String columnName)
	{
		String isAutoIncremented = tableWithProperties.get(columnName).get(1);
		return (isAutoIncremented.equals("true"));
	}
	
	private boolean isReadOnly(HashMap<String,ArrayList<String>> tableWithProperties,String columnName)
	{
		String isReadOnly = tableWithProperties.get(columnName).get(3);
		return (isReadOnly.equals("true"));
	}
	
	public int[][] propArray()
	{
		SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(Parser.workSpacePath+"\\DBMS Workspace\\"+query.getDbName()+"\\"+query.getTableName()+".xml");
        int[][] props = null;
		try 
		{
			Document document = builder.build(xmlFile);
			Element table = document.getRootElement();
        	Element attributes = table.getChild("attributes"); 
            List<Element> columns = (List<Element>) attributes.getChildren();
            props = new int[columns.size()][4];
            int i = 0;
            for (Element column : columns) 
            {
            	props[i][0] = (column.getChildText("auto").equalsIgnoreCase("true"))? 1 : 0;
            	props[i][1] = (column.getChildText("readOnly").equalsIgnoreCase("true"))? 1 : 0;
            	props[i][2] = (column.getChildText("notNullable").equalsIgnoreCase("false"))? 1 : 0;
            	props[i][3] = (column.getChildText("NotSearchable").equalsIgnoreCase("false"))? 1 : 0;
            	i++;
			}
		}
		catch (JDOMException | IOException e) {e.printStackTrace();}
	
		return props;
	}
	
}


