import java.awt.*;

public class Square extends Rectangler {

	public Square() {
		// TODO Auto-generated constructor stub
		points = new Rectangle[4];
		isTriangle=false;
		isSpecialCase=true;
	}

	public void setSecondPoint(Point secondPoint) {
		int x1 = this.firstPoint.x;
		int y1 = this.firstPoint.y;
		int x2 = secondPoint.x;
		int y2 = secondPoint.y;
		int dimention = Math.abs(x2 - x1);

		if (x2 > x1) {
			if (y2 > y1) {
				this.secondPoint = (new Point(x1 + dimention, y1 + dimention));
			} else {
				this.secondPoint = (new Point(x1 + dimention, y1 - dimention));
			}
		} else {
			if (y2 > y1) {
				this.secondPoint = (new Point(x1 - dimention, y1 + dimention));
			} else {
				this.secondPoint = (new Point(x1 - dimention, y1 - dimention));
			}
		}
		this.makeReq();

	}

}
