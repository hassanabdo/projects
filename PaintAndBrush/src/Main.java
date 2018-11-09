import java.io.ObjectInputStream.GetField;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) throws MalformedURLException {
		PaintWindow i= new PaintWindow();
		i.pack();
		i.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		i.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		i.setSize(850, 800);
	}

}
