package game;

public class ProcessingFalling implements State{

	@Override
	public void doAction(FalllingShape b) {
		// TODO Auto-generated method stub
		Controller c = Controller.getInstance();
		int x = b.getX();
		int y = b.getY();
		int type = b.getType();
		if (!c.LiesInBoxes(x, y)) {
			if (y + 60 >= 600) {
				c.resetShape(b);
			} else {
				b.setY(y + 10);
				if (type == 0 || type == 2)
					b.setX(x + 5);
				else
					b.setX(x - 5);
			}
		} else {
//			b.setTaken(true);
			b.setState(new Taken());
//			b.setProcessing(false);
			if (x + 50 >= c.scr.getP1().getxForBox()
					&& x < c.scr.getP1().getxForBox() + 100) {
				c.Box1Shapes.add(b);
				b.setX(c.scr.getP1().getxForBox() + 40);
				b.setY(c.scr.getP1().getHeightForBox() - 10);
			} else if (x + 50 >= c.scr.getP2().getxForBox()
					&& x < c.scr.getP2().getxForBox() + 100) {
				c.Box2Shapes.add(b);
				b.setX(c.scr.getP2().getxForBox() + 40);
				b.setY(c.scr.getP2().getHeightForBox() - 10);
			}
		}
	}

}
