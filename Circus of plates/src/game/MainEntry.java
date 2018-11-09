package game;

import java.net.MalformedURLException;

public class MainEntry {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		Controller cont = Controller.getInstance();
		cont.startGame();

	}

}
