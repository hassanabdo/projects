package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class FirstPlayer extends Player {
	public FirstPlayer() {
		loadImage();
	}

	private void loadImage() {
		img = new ImageIcon("firstPlayer.gif").getImage().getScaledInstance(
				100, 180, Image.SCALE_SMOOTH);

	}
}
