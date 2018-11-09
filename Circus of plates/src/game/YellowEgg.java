package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class YellowEgg extends FalllingShape {
	public YellowEgg() {
		loadImage();
		color = 3;
	}

	private void loadImage() {
		img = new ImageIcon("yellowEgg.gif").getImage().getScaledInstance(50,
				30, Image.SCALE_SMOOTH);
	}

}