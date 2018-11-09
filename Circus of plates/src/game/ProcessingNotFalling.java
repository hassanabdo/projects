package game;

public class ProcessingNotFalling implements State{

	@Override
	public void doAction(FalllingShape b) {
		// TODO Auto-generated method stub
		int type = b.getType();
		int x = b.getX();
		int y = b.getY();
		Controller c = Controller.getInstance();
		if (type == 0) {
			if (x < c.StartOfLeftRows + c.LengthOfUpperRows) {
				b.setX(x + 10);
			} else {
				b.setState(new ProcessingFalling());
				b.setY(y + 10);
				b.setX(x + 10);
			}
		} else if (type == 1) {
			if (x > c.StartOfRightRows - c.LengthOfUpperRows - 70) {
				b.setX(x - 10);
			} else {
				b.setState(new ProcessingFalling());
				b.setY(y + 10);
				b.setX(x - 10);
			}
		} else if (type == 2) {
			if (x < c.StartOfLeftRows + c.LengthOfLowerRows)
				b.setX(x + 10);
			else {
				b.setState(new ProcessingFalling());
				b.setY(y + 10);
				b.setX(x + 10);
			}
		} else {
			if (x > c.StartOfRightRows - c.LengthOfLowerRows - 70) {
				b.setX(x - 10);
			} else {
				b.setState(new ProcessingFalling());
				b.setY(y + 10);
				b.setX(x - 10);
			}

		}
	}

	

}
