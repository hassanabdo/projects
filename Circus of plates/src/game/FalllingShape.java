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
import java.util.Random;

public abstract class FalllingShape implements Serializable {
	protected transient Image img;
	protected int color;
	private int X;
	private int Y;
	private int type;
	private State state;

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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getColor() {
		return color;
	}

	public Image getImg() {
		return img;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		this.X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		this.Y = y;
	}

}
