package jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;


public class Driver implements java.sql.Driver{
	
	static Logger log =Logger.getLogger(Driver.class);
	private static boolean isRead = false;
	private static String DB_URL;
	private static String Driver_Name;
	private static String User_Name;
	private static String PassWord;
	public static boolean testConnect = false;
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		// TODO Auto-generated method stub
		if(!isRead){
			readConfig();
		}
		if(url.equals(DB_URL))
		{
			log.info("Accepted URL");
			return true;
		}else{
			log.info("Wrong URL");
		return false;
		}		
	}
	
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		if(!isRead){
			readConfig();
		}
		Connection con = null;
		String usr = info.getProperty("User");
		String pass = info.getProperty("PassWord");
		if(usr.equals(User_Name) && pass.equals(PassWord)){
			if(acceptsURL(url)){
				con = new Connection();
				testConnect = true;
				log.info("Successful Connection");
			}else if(!acceptsURL(url)){
//				System.out.println("wrong url");
				log.info("wrong url");
			}else{
				throw new SQLException();
			}
		}else{
//			System.out.println("wrong info");
			log.info("wrong info");
		}
		return con;
	}
	
	public void readConfig (){
		Properties prop = new Properties();
		File workSpace = new File(FileSystemView.getFileSystemView().getHomeDirectory()+"");
		try {
			prop.load(new FileInputStream(workSpace.getAbsolutePath()+"\\info.properties"));
			DB_URL = prop.getProperty("DataBaseURL");
			Driver_Name = prop.getProperty("DriverName");
			User_Name = prop.getProperty("User");
			PassWord = prop.getProperty("PassWord");
			isRead = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		}
	}
	
	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public java.util.logging.Logger getParentLogger()
			throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		// TODO Auto-generated method stub
		if(!isRead){
			readConfig();
		}
		DriverPropertyInfo[] DrProbInfo = null;
		if(!acceptsURL(url)) return DrProbInfo;
		Enumeration e =  info.propertyNames();	
		ArrayList<DriverPropertyInfo> arr = getArrayList(e);
	    DrProbInfo = new DriverPropertyInfo[arr.size()];
		int i=0;
		for(DriverPropertyInfo dr : arr){
			DrProbInfo[i] = new DriverPropertyInfo(dr.name,dr.value);
			i++;
		}
		log.info("");
		return DrProbInfo;
	}
	
	public String getDriverName(){
		if(!isRead){
			readConfig();
		}
		return Driver_Name;
	}
	
	public ArrayList<DriverPropertyInfo> getArrayList(Enumeration e){
		ArrayList<DriverPropertyInfo> arr = new ArrayList<DriverPropertyInfo>();
	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      String value=null;
	      if(key.equalsIgnoreCase("DataBaseURL")){
	    	  value = DB_URL;
	      }else if (key.equalsIgnoreCase("DriverName")){
	    	  value = Driver_Name;
	      }else if (key.equalsIgnoreCase("User")){
	    	  value = User_Name;
	      }else if (key.equalsIgnoreCase("PassWord")){
	    	  value = PassWord;
	      }
	      DriverPropertyInfo prob = new DriverPropertyInfo(key, value);
	      arr.add(prob);
	    }
	    return arr;
	}
	
	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

}
