package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class RedPlate extends FalllingShape {
	public RedPlate() {
		loadImage();
		color = 2;
	}

	private void loadImage() {
		img = new ImageIcon("redPlate.gif").getImage().getScaledInstance(50,
				30, Image.SCALE_SMOOTH);
	}

}