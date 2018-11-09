package game;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Factory {

	// use getShape method to get object of type shape
	public FalllingShape getShape(String shapeType)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		File classesDir = new File("C:\\");

		ClassLoader parentLoader = FalllingShape.class.getClassLoader();

		URLClassLoader loader = new URLClassLoader(
				new URL[] { classesDir.toURL() }, parentLoader);
		Class cls = null;

		if (shapeType == null) {
			return null;
		}
		if (shapeType.equalsIgnoreCase("YellowPlate")) {
			cls = loader.loadClass("game.YellowPlate");
		} else if (shapeType.equalsIgnoreCase("BluePlate")) {
			cls = loader.loadClass("game.BluePlate");
		} else if (shapeType.equalsIgnoreCase("RedPlate")) {
			cls = loader.loadClass("game.RedPlate");
		} else if (shapeType.equalsIgnoreCase("RedEgg")) {
			cls = loader.loadClass("game.RedEgg");
		} else if (shapeType.equalsIgnoreCase("BlueEgg")) {
			cls = loader.loadClass("game.BlueEgg");
		} else if (shapeType.equalsIgnoreCase("YellowEgg")) {
			cls = loader.loadClass("game.YellowEgg");
		}
		return (FalllingShape) cls.newInstance();
	}
}