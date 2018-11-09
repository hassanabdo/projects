package game;

public class Ball {
	private int X;
	private int Y;
	private int type;
	private boolean falling;
	private boolean processing;
	private boolean taken;

	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	public boolean isProcessing() {
		return processing;
	}

	public void setProcessing(boolean processing) {
		this.processing = processing;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
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
