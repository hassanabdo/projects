package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class BluePlate extends FalllingShape {
	public BluePlate() {
		loadImage();
		this.color = 1;
	}

	private void loadImage() {
		img = new ImageIcon("bluePlate.gif").getImage().getScaledInstance(50,
				30, Image.SCALE_SMOOTH);
	}

}
