package dbms;

import java.io.File;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class EntryPoint {
	public static void main(String[] args) {
		// try
		// {
		// System.out.println(EntryPoint.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		// }
		// catch (URISyntaxException e) {e.printStackTrace();}

		// get desktop path
		FileSystemView filesys = FileSystemView.getFileSystemView();
		String desktopPath = filesys.getHomeDirectory() + "";

		// check work space
		File workSpace = new File(filesys.getHomeDirectory()
				+ "\\DBMS Workspace");
		boolean success = workSpace.mkdirs();
		if (success) {
			System.out.println("Workspace Created.");
		} else {
			System.out.println("Workspace successfully found.");
		}

		String query;
		Parser parser = new Parser();
		boolean databaseIsSet = false;
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Enter a query: ");
			query = in.nextLine();
			if (query.equalsIgnoreCase("test")) {

				Result result = JUnitCore.runClasses(TestUnit.class);
				for (Failure failure : result.getFailures()) {
					System.out.println(failure.toString());
				}
				System.out.println(result.wasSuccessful());
			} else if (query.startsWith("USE ") || !databaseIsSet) {
				if (query.equalsIgnoreCase("Exit"))
					break;

				parser.excuteQuery(query);
				databaseIsSet = parser.isDBSet();
			}

			else {
				if (query.equalsIgnoreCase("Exit"))
					break;
				parser.excuteQuery(query);
			}
		}
	}
}