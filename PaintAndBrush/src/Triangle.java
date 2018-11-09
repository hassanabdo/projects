import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Triangle extends shape {
	protected Point thirdPoint;

	public void setThirdPoint(Point thirdPoint) {
		this.thirdPoint = thirdPoint;
		isTriangle=true;
		isSpecialCase=false;
		makeReq();
	}

	public boolean pointSelected(Point c) {

		int x1 = firstPoint.x;
		int y1 = firstPoint.y;
		int x2 = secondPoint.x;
		int y2 = secondPoint.y;
		int x3 = thirdPoint.x;
		int y3 = thirdPoint.y;

		// distance from point 1
		double b1 = Math.sqrt(Math.pow((c.getX() - x1), 2)
				+ Math.pow((c.getY() - y1), 2));
		// distance from point 2;
		double b2 = Math.sqrt(Math.pow((x2 - c.getX()), 2)
				+ Math.pow((y2 - c.getY()), 2));
		// distance from point 3
		double b3 = Math.sqrt(Math.pow((c.getX() - x3), 2)
				+ Math.pow((c.getY() - y3), 2));
		// first line length
		double b = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
		if ((b - b1 - b2) < 1 && (b - b1 - b2) > -1)
			return true;

		// second line length
		b = Math.sqrt(Math.pow((x2 - x3), 2) + Math.pow((y2 - y3), 2));

		if ((b - b3 - b2) < 1 && (b - b3 - b2) > -1)
			return true;
		b = Math.sqrt(Math.pow((x3 - x1), 2) + Math.pow((y3 - y1), 2));

		if ((b - b3 - b1) < 1 && (b - b3 - b1) > -1)
			return true;
		return false;
	}

	public void resizeShape(Point q) {
		// TODO Auto-generated method stub

		switch (resize) {
		case 0:
			firstPoint = q;
			break;
		case 1:
			secondPoint = q;
			break;
		case 2:
			thirdPoint = q;
			break;

		}
		this.makeReq();
	}

	public void moveShape(Point q) {
		// TODO Auto-generated method stub
		int x = 0, y = 0;
		switch (move) {
		case 0:
			x = q.x - (firstPoint.x + secondPoint.x) / 2;
			y = q.y - (firstPoint.y + secondPoint.y) / 2;
			break;
		case 1:
			x = q.x - (thirdPoint.x + secondPoint.x) / 2;
			y = q.y - (thirdPoint.y + secondPoint.y) / 2;
			break;
		case 2:
			x = q.x - (firstPoint.x + thirdPoint.x) / 2;
			y = q.y - (firstPoint.y + thirdPoint.y) / 2;
			break;
		case 3:
			x = q.x - (firstPoint.x + secondPoint.x + thirdPoint.x) / 3;
			y = q.y - (firstPoint.y + secondPoint.y + thirdPoint.y) / 3;
			break;
		}
		firstPoint.x += x;
		secondPoint.x += x;
		thirdPoint.x += x;
		firstPoint.y += y;
		secondPoint.y += y;
		thirdPoint.y += y;
		makeReq();
	}
	public boolean contain(Point c) {
		// TODO Auto-generated method stub
		int x1 = firstPoint.x;
		int y1 = firstPoint.y;
		int x2 = secondPoint.x;
		int y2 = secondPoint.y;
		int x3 = thirdPoint.x;
		int y3 = thirdPoint.y;
		int[] q = { x1, x2, x3 };
		int[] q1 = { y1, y2, y3 };
		Polygon t = new Polygon(q, q1, 3);
		if(t.contains(c))return true;
		return false;
	}
	private void makeReq() {
		points[0] = new Rectangle(firstPoint.x - 6, firstPoint.y - 6, 12, 12);
		points[1] = new Rectangle(secondPoint.x - 6, secondPoint.y - 6, 12, 12);
		points[2] = new Rectangle(thirdPoint.x - 6, thirdPoint.y - 6, 12, 12);
		centers[0] = new Ellipse2D.Double(
				(firstPoint.x + secondPoint.x) / 2 - 6,
				(firstPoint.y + secondPoint.y) / 2 - 6, 12, 12);
		centers[1] = new Ellipse2D.Double(
				(thirdPoint.x + secondPoint.x) / 2 - 6,
				(thirdPoint.y + secondPoint.y) / 2 - 6, 12, 12);
		centers[2] = new Ellipse2D.Double(
				(firstPoint.x + thirdPoint.x) / 2 - 6,
				(firstPoint.y + thirdPoint.y) / 2 - 6, 12, 12);
		centers[3] = new Ellipse2D.Double(
				(firstPoint.x + thirdPoint.x + secondPoint.x) / 3 - 6,
				(firstPoint.y + thirdPoint.y + secondPoint.y) / 3 - 6, 12, 12);
	}

	public Triangle() {
		// TODO Auto-generated constructor stub
		points = new Shape[3];
		centers = new Shape[4];
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (this.getFirstPoint() == null || this.getSecondPoint() == null)
			return;
		if (dotted) {
			float dash1[] = { 10.0f };
			BasicStroke dashed = new BasicStroke(StrockThickness,
					BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
					0.0f);
			g2.setStroke(dashed);
		} else {
			g2.setStroke(new BasicStroke(StrockThickness));
		}
		// TODO Auto-generated method stub
		int x1 = this.getFirstPoint().x;
		int y1 = this.getFirstPoint().y;
		int x2 = this.getSecondPoint().x;
		int y2 = this.getSecondPoint().y;

		if (this.isSelected()) {
			g2.setColor(Color.YELLOW);
		} else {
			g2.setColor(this.getColor());
		}
		if (this.getSecondPoint() != null) {
			g2.drawLine(x1, y1, x2, y2);
		}
		String s = " Triangle properties :";
		String s1 = "FirstPoint : x = " + String.valueOf(x1) + "  , y = "
				+ String.valueOf(y1) + "  \t SecondPoint : x = "
				+ String.valueOf(x2) + "  , y = " + String.valueOf(y2);
		;

		if (thirdPoint != null) {
			int x3 = thirdPoint.x;
			int y3 = thirdPoint.y;
			int[] q = { x1, x2, x3 };
			int[] q1 = { y1, y2, y3 };
			if (this.isFill()) {

				g2.setColor(getFillin());
				g2.fillPolygon(q, q1, 3);
			}

			if (this.isSelected()) {
				g2.setColor(Color.YELLOW);
			} else {
				g2.setColor(this.getColor());
			}
			g2.drawLine(x1, y1, x2, y2);
			g2.drawLine(x1, y1, x3, y3);
			g2.drawLine(x2, y2, x3, y3);

			String s2 = "Third Point : x = " + String.valueOf(x3) + "  , y = "
					+ String.valueOf(y3);
			String s3 = "Area  = "
					+ String.valueOf((int) Math.abs((x1 * (y2 - y3) + x2
							* (y3 - y1) + x3 * (y1 - y2)) / 2));
			s = s + "           " + s1 + "        " + s2 + "      " + s3;
			this.setProperties(s);
			if (this.isSelected()) {
				g2.setStroke(new BasicStroke(StrockThickness+1));
				g2.setColor(Color.ORANGE);
				for (Shape I : points)
					g2.draw(I);
				g2.setColor(Color.GREEN);
				for (Shape I : centers)
					g2.draw(I);

				g2.drawLine((x1 + x2 + x3) / 3, (y1 + y2 + y3) / 3,
						(x1 + x2 + x3) / 3, (y1 + y2 + y3) / 3);
			}

		} else {
			s = s + "           " + s1;
			this.setProperties(s);
		}

	}

}
