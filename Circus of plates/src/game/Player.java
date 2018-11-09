package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Player implements Serializable {
	protected int score;
	private int xForBox;
	private int HeightForBox;

	public int getxForBox() {
		return xForBox;
	}

	public void setxForBox(int xForBox) {
		this.xForBox = xForBox;
	}

	public int getHeightForBox() {
		return HeightForBox;
	}

	public void setHeightForBox(int heightForBox) {
		HeightForBox = heightForBox;
	}

	protected transient Image img;

	public Image getImg() {
		return img;
	}

	public int getScore() {
		return score;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void display(Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, x, y, null);
	};

	private void writeObject(ObjectOutputStream outStr) throws IOException {
		outStr.defaultWriteObject();

		int[] pixels = img != null ? new int[100 * 180] : null;

		if (img != null) {
			try {
				PixelGrabber pg = new PixelGrabber(img, 0, 0, 100, 180, pixels,
						0, 100);
				pg.grabPixels();
				if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
					throw new IOException("failed to load image contents");
				}
			} catch (InterruptedException e) {
				throw new IOException("image load interrupted");
			}
		}
		outStr.writeInt(100);
		outStr.writeInt(180);
		outStr.writeObject(pixels);
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException,
			IOException {
		s.defaultReadObject();

		int w = s.readInt();
		int h = s.readInt();
		int[] pixels = (int[]) (s.readObject());

		if (pixels != null) {
			Toolkit tk = Toolkit.getDefaultToolkit();
			ColorModel cm = ColorModel.getRGBdefault();
			img = tk.createImage(new MemoryImageSource(w, h, cm, pixels, 0, w));
		}
	}
}
