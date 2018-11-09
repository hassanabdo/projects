package jdbc;

import java.io.File;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

public class Main 
{

	public static void main(String[] args) 
	{
		createWorkSpace();
		String url = "JDBC:jdbc:dbms";
		Scanner in = new Scanner(System.in);
		try {	Class.forName("jdbc.Driver");	}
		catch (ClassNotFoundException e) {	e.printStackTrace();	}
		
		Connection con = null;
		Properties prob = new Properties();
		prob.setProperty("User", "lol");
		prob.setProperty("PassWord", "12345");
		Driver dr = new Driver();
		try 
		{
			con = dr.connect(url, prob);
			Statement stmt =  (Statement) con.createStatement();
			while(true) {
			System.out.println("Insert Query: ");
			String query = in.nextLine();
			
//			System.out.println(stmt.execute(query));
			System.out.println(stmt.executeUpdate(query));
//			System.out.println(stmt.executeQuery(query));
			}
			
		} catch (SQLException e) {e.printStackTrace();}

	}
	
	public static void createWorkSpace()
	{
		FileSystemView filesys = FileSystemView.getFileSystemView();		
		// check work space
		File workSpace = new File(filesys.getHomeDirectory() + "\\DBMS Workspace");
		boolean success = workSpace.mkdirs();
		if (success) 	System.out.println("Workspace Created.");
		else 	System.out.println("Workspace successfully found.");
	}

}
