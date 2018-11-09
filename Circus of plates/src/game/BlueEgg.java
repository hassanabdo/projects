package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class BlueEgg extends FalllingShape {
	
	public BlueEgg() {
		loadImage();
		this.color = 1;
	}

	private void loadImage() {
		img = new ImageIcon("blueEgg.gif").getImage().getScaledInstance(50,
				30, Image.SCALE_SMOOTH);
	}

}