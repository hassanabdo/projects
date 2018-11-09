package game;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Timer;

public class Source implements Serializable {
	private ArrayList<FalllingShape> operatingShapes;
	private Player p1;
	private Player p2;
	private int  interval;

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public Source(int numberOfTypes, int StartOfLeftRows,
			int HeightOfUpperRows, int StartOfRightRows, int HeightOfLowerRows)
			throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		interval =120;
		p1 = new FirstPlayer();
		p2 = new SecondPlayer();
		operatingShapes = new ArrayList<FalllingShape>();
		Factory factory = new Factory();
		int NumberOfBallsOfEachType = 25;
		String shapeType = "";
		String[] Types = { "YellowPlate", "BluePlate", "RedPlate", "BlueEgg",
				"redEgg", "yellowEgg" };
		for (int i = 0; i <= numberOfTypes; i++) {
			for (int j = 0; j < NumberOfBallsOfEachType; j++) {
				shapeType = Types[(int) (Math.random() * 6)];
				FalllingShape b = factory.getShape(shapeType);
				b.setType(i);
				b.setState(new NotProcessing());
				if (i == 0) {
					b.setX(StartOfLeftRows);
					b.setY(HeightOfUpperRows);
				} else if (i == 1) {
					b.setX(StartOfRightRows - 70);
					b.setY(HeightOfUpperRows);
				} else if (i == 2) {
					b.setX(StartOfLeftRows);
					b.setY(HeightOfLowerRows);
				} else {
					b.setX(StartOfRightRows - 70);
					b.setY(HeightOfLowerRows);
				}
				operatingShapes.add(b);
			}
		}

	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public void setP2(Player p2) {
		this.p2 = p2;
	}

	public Player getP1() {
		return p1;
	}

	public Player getP2() {
		return p2;
	}

	public ArrayList<FalllingShape> getOperatingShapes() {
		return operatingShapes;
	}

	public void setOperatingBalls(ArrayList<FalllingShape> operatingBalls) {
		this.operatingShapes = operatingBalls;
	}

}