package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class RedEgg extends FalllingShape {
	public RedEgg() {
		loadImage();
		color = 2;
	}

	private void loadImage() {
		img = new ImageIcon("redEgg.gif").getImage().getScaledInstance(50,
				30, Image.SCALE_SMOOTH);
	}

}