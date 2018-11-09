package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class SecondPlayer extends Player {
	public SecondPlayer() {
		loadImage();
	}

	private void loadImage() {
		img = new ImageIcon("secondPlayer.gif").getImage().getScaledInstance(
				100, 180, Image.SCALE_SMOOTH);

	}
}
