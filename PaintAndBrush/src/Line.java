import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Line extends shape {

	public Line() {
		points = new Shape[2];
		centers = new Ellipse2D[1];
		isTriangle=false;
		isSpecialCase=false;
	}

	public void setSecondPoint(Point secondPoint) {
		this.secondPoint = secondPoint;
		makeReq();
	}

	public void resizeShape(Point q) {
		// TODO Auto-generated method stub
		// double slope = Math.pow(firstPoint.x-secondPoint.x,
		// 2)+Math.pow(secondPoint.y-firstPoint.y, 2);
		// double b1 = Math.pow(q.x-secondPoint.x,
		// 2)+Math.pow(q.y-secondPoint.y, 2);
		// double b2 = Math.pow(firstPoint.x-q.x, 2)+Math.pow(secondPoint.y-q.y,
		// 2);
		switch (resize) {
		case 0:
			firstPoint = q;
			break;
		case 1:
			secondPoint = q;
			break;

		}
		this.makeReq();
	}

	public boolean pointSelected(Point c) {
		int x1 = this.getFirstPoint().x;
		int y1 = this.getFirstPoint().y;
		int x2 = this.getSecondPoint().x;
		int y2 = this.getSecondPoint().y;
		double b = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
		double b1 = Math.sqrt(Math.pow((c.getX() - x1), 2)
				+ Math.pow((c.getY() - y1), 2));
		double b2 = Math.sqrt(Math.pow((x2 - c.getX()), 2)
				+ Math.pow((y2 - c.getY()), 2));
		if (Math.abs(b - b1 - b2) < 1)
			return true;
		return false;
	}

	public void makeReq() {
		points[0] = new Rectangle(firstPoint.x - 6, firstPoint.y - 6, 12, 12);
		points[1] = new Rectangle(secondPoint.x - 6, secondPoint.y - 6, 12, 12);
		centers[0] = new Ellipse2D.Double(
				(firstPoint.x + secondPoint.x) / 2 - 6,
				(firstPoint.y + secondPoint.y) / 2 - 6, 12, 12);
	}

	public void moveShape(Point q) {
		// TODO Auto-generated method stub
		int x = q.x - (firstPoint.x + secondPoint.x) / 2;
		int y = q.y - (firstPoint.y + secondPoint.y) / 2;

		switch (move) {
		case 0:
			firstPoint.x += x;
			firstPoint.y += y;
			secondPoint.x += x;
			secondPoint.y += y;
			break;

		}
		this.makeReq();
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (this.getFirstPoint() == null || this.getSecondPoint() == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (dotted) {
			float dash1[] = { 10.0f };
			BasicStroke dashed = new BasicStroke(StrockThickness,
					BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
					0.0f);
			g2.setStroke(dashed);
		} else {
			g2.setStroke(new BasicStroke(StrockThickness));
		}
		if (this.isSelected()) {
			g2.setColor(Color.YELLOW);
		} else {
			g2.setColor(this.getColor());
		}
		int x1 = this.getFirstPoint().x;
		int y1 = this.getFirstPoint().y;
		int x2 = this.getSecondPoint().x;
		int y2 = this.getSecondPoint().y;
		g2.drawLine(x1, y1, x2, y2);
		if (this.isSelected()) {
			g2.setStroke(new BasicStroke(StrockThickness+1));
			g2.setColor(Color.ORANGE);
			for (Shape i : points)
				g2.draw(i);
			g2.setColor(Color.GREEN);
			for (Shape i : centers)
				g2.draw(i);
		}
		String s = "Line properties :";
		String s1 = "FirstPoint : x = " + String.valueOf(x1) + "  , y = "
				+ String.valueOf(y1) + "  \t SecondPoint : x = "
				+ String.valueOf(x2) + "  , y = " + String.valueOf(y2);
		;
		String s2 = "Length "
				+ String.valueOf((int) Math.sqrt(Math.pow((x2 - x1), 2)
						+ Math.pow((y2 - y1), 2)));

		s = s + "           " + s1 + "           " + s2;
		this.setProperties(s);

	}

}
