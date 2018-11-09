import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.StringSelection;

import javax.swing.JFrame;

public class Circle extends Ellipse {
	private int radius;

	public Circle() {
		// TODO Auto-generated constructor stub
		majorYaxis = 0;
		majorXaxis = 0;
		center = new Point();
		isTriangle=false;
		isSpecialCase=true;
	}

	public void setSecondPoint(Point secondPoint) {
		majorXaxis = majorYaxis = (int) Math.sqrt(Math.pow(center.x
				- secondPoint.x, 2)
				+ Math.pow(center.y - secondPoint.y, 2));
		this.secondPoint = new Point(center.x + majorXaxis, center.y
				+ majorXaxis);
		firstPoint = new Point(center.x - majorXaxis, center.y - majorXaxis);
		makeReq();
	}

	
	public void setFirstPoint(Point firstPoint) {
		this.firstPoint = firstPoint;
		this.center = firstPoint;
	}

}
