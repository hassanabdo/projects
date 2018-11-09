import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

public class Rectangler extends shape {

	public Rectangler() {
		// TODO Auto-generated constructor stub
		points = new Rectangle[4];
		centers = new Shape[5];
		isTriangle = false;
		isSpecialCase = false;
	}

	public void setSecondPoint(Point secondPoint) {
		this.secondPoint = secondPoint;
		makeReq();
	}

	public void moveShape(Point q) {
		// TODO Auto-generated method stub
		int x = 0, y = 0;
		switch (move) {
		case 0:
			x = q.x - firstPoint.x;
			y = q.y
					- (Math.max(secondPoint.y, firstPoint.y) - Math
							.abs(secondPoint.y - firstPoint.y) / 2);
			break;
		case 1:
			x = q.x - secondPoint.x;
			y = q.y
					- (Math.max(secondPoint.y, firstPoint.y) - Math
							.abs(secondPoint.y - firstPoint.y) / 2);
			break;
		case 2:
			x = q.x
					- (Math.max(secondPoint.x, firstPoint.x) - Math
							.abs(secondPoint.x - firstPoint.x) / 2);
			y = q.y - firstPoint.y;
			break;
		case 3:
			x = q.x
					- (Math.max(firstPoint.x, secondPoint.x) - Math
							.abs(firstPoint.x - secondPoint.x) / 2);
			y = q.y - secondPoint.y;
			break;
		case 4:
			x = q.x
					- (Math.max(firstPoint.x, secondPoint.x) - Math
							.abs(firstPoint.x - secondPoint.x) / 2);
			y = q.y
					- (Math.max(secondPoint.y, firstPoint.y) - Math
							.abs(secondPoint.y - firstPoint.y) / 2);
			break;
		default:
			break;
		}
		firstPoint.x += x;
		firstPoint.y += y;
		secondPoint.x += x;
		secondPoint.y += y;
		makeReq();
	}

	public void resizeShape(Point q) {
		// TODO Auto-generated method stub
		if (this.isSpecialCase) {
			switch (resize) {
			case 0:
				firstPoint.x = q.x;
				if (firstPoint.y > secondPoint.y) {
					firstPoint.y = secondPoint.y
							+ Math.abs(q.x - secondPoint.x);
				} else {
					firstPoint.y = secondPoint.y
							- Math.abs(q.x - secondPoint.x);
				}
				break;
			case 1:
				secondPoint.x = q.x;
				if (firstPoint.y > secondPoint.y) {
					firstPoint.y = secondPoint.y + Math.abs(q.x - firstPoint.x);
				} else {
					firstPoint.y = secondPoint.y - Math.abs(q.x - firstPoint.x);
				}
				break;
			case 2:
				secondPoint.x = q.x;
				if (secondPoint.y > firstPoint.y) {
					secondPoint.y = firstPoint.y + Math.abs(q.x - firstPoint.x);
				} else {
					secondPoint.y = firstPoint.y - Math.abs(q.x - firstPoint.x);
				}
				break;
			case 3:
				firstPoint.x = q.x;
				if (secondPoint.y > firstPoint.y) {
					secondPoint.y = firstPoint.y
							+ Math.abs(q.x - secondPoint.x);
				} else {
					secondPoint.y = firstPoint.y
							- Math.abs(q.x - secondPoint.x);
				}
				break;

			default:
				break;
			}
		} else {
			switch (resize) {
			case 0:
				firstPoint = q;
				break;
			case 1:
				firstPoint.y = q.y;
				secondPoint.x = q.x;
				break;
			case 2:
				secondPoint = q;
				break;
			case 3:
				firstPoint.x = q.x;
				secondPoint.y = q.y;
				break;

			default:
				break;
			}
		}
		makeReq();
	}

	public void makeReq() {
		points[0] = new Rectangle(firstPoint.x - 6, firstPoint.y - 6, 12, 12);
		points[1] = new Rectangle(secondPoint.x - 6, firstPoint.y - 6, 12, 12);
		points[2] = new Rectangle(secondPoint.x - 6, secondPoint.y - 6, 12, 12);
		points[3] = new Rectangle(firstPoint.x - 6, secondPoint.y - 6, 12, 12);
		centers[0] = new Ellipse2D.Double(firstPoint.x - 6, (Math.max(
				secondPoint.y, firstPoint.y) - Math.abs(secondPoint.y
				- firstPoint.y) / 2) - 6, 12, 12);
		centers[1] = new Ellipse2D.Double(secondPoint.x - 6, (Math.max(
				secondPoint.y, firstPoint.y) - Math.abs(secondPoint.y
				- firstPoint.y) / 2) - 6, 12, 12);
		centers[2] = new Ellipse2D.Double(
				(Math.max(secondPoint.x, firstPoint.x) - Math.abs(secondPoint.x
						- firstPoint.x) / 2) - 6, firstPoint.y - 6, 12, 12);
		centers[3] = new Ellipse2D.Double(
				(Math.max(firstPoint.x, secondPoint.x) - Math.abs(firstPoint.x
						- secondPoint.x) / 2) - 6, secondPoint.y - 6, 12, 12);
		centers[4] = new Ellipse2D.Double(
				(Math.max(firstPoint.x, secondPoint.x) - Math.abs(firstPoint.x
						- secondPoint.x) / 2) - 6,
				(Math.max(secondPoint.y, firstPoint.y) - Math.abs(secondPoint.y
						- firstPoint.y) / 2) - 6, 12, 12);

	}

	public boolean contain(Point c) {
		// TODO Auto-generated method stub
		if (((c.getY() >= this.firstPoint.y && c.getY() <= this.secondPoint.y) || (c
				.getY() < this.firstPoint.y && c.getY() > this.secondPoint.y))
				&& ((c.getX() >= this.firstPoint.x && c.getX() <= this.secondPoint.x) || (c
						.getX() < this.firstPoint.x && c.getX() > this.secondPoint.x))) {
			return true;
		}
		return false;
	}

	public boolean pointSelected(Point c) {
		if (Math.abs(c.getX() - this.firstPoint.x) < 4
				|| Math.abs(c.getX() - this.secondPoint.x) < 4) {
			if ((c.getY() >= this.firstPoint.y && c.getY() <= this.secondPoint.y)
					|| (c.getY() < this.firstPoint.y && c.getY() > this.secondPoint.y)) {
				return true;
			}
		}
		if (Math.abs(c.getY() - this.firstPoint.y) < 4
				|| Math.abs(c.getY() - this.secondPoint.y) < 4) {
			if ((c.getX() >= this.firstPoint.x && c.getX() <= this.secondPoint.x)
					|| (c.getX() < this.firstPoint.x && c.getX() > this.secondPoint.x)) {
				return true;
			}
		}
		return false;
	}

	public void paint(Graphics g) {
		if (this.firstPoint == null || this.secondPoint == null)
			return;
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (this.isSelected()) {
			g2.setStroke(new BasicStroke(StrockThickness + 1));
			g2.setColor(Color.ORANGE);
			for (Shape i : points)
				g2.draw(i);
			g2.setColor(Color.green);
			for (Shape i : centers)
				g2.draw(i);
			g2.drawLine((Math.max(firstPoint.x, secondPoint.x) - Math
					.abs(firstPoint.x - secondPoint.x) / 2), (Math.max(
					secondPoint.y, firstPoint.y) - Math.abs(secondPoint.y
					- firstPoint.y) / 2), (Math
					.max(firstPoint.x, secondPoint.x) - Math.abs(firstPoint.x
					- secondPoint.x) / 2), (Math.max(secondPoint.y,
					firstPoint.y) - Math.abs(secondPoint.y - firstPoint.y) / 2));
			g2.setColor(Color.YELLOW);
		} else {
			g2.setColor(this.getColor());
		}
		

		if (dotted) {
			float dash1[] = { 10.0f };
			BasicStroke dashed = new BasicStroke(StrockThickness,
					BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
					0.0f);
			g2.setStroke(dashed);
		} else {
			g2.setStroke(new BasicStroke(StrockThickness));
		}
		int x1 = this.firstPoint.x;
		int y1 = this.firstPoint.y;
		int x2 = this.secondPoint.x;
		int y2 = this.secondPoint.y;

		if (x2 > x1) {
			if (y2 > y1) {
				g2.drawRect(x1, y1, x2 - x1, Math.abs(y2 - y1));
				if (this.isFill()) {
					g2.setColor(getFillin());

					g2.fillRect(x1 + 1, y1 + 1, x2 - x1 - 1,
							Math.abs(y2 - y1) - 1);
				}
			} else {
				g2.drawRect(x1, y2, x2 - x1, Math.abs(y2 - y1));
				if (this.isFill()) {
					g2.setColor(getFillin());

					g2.fillRect(x1 + 1, y2 + 1, x2 - x1 - 1,
							Math.abs(y2 - y1) - 1);
				}
			}
		} else {
			if (y2 > y1) {

				g2.drawRect(x2, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
				if (this.isFill()) {
					g2.setColor(getFillin());

					g2.fillRect(x2 + 1, y1 + 1, Math.abs(x2 - x1) - 1,
							Math.abs(y2 - y1) - 1);
				}
			} else {
				g2.drawRect(x2, y2, Math.abs(x2 - x1), Math.abs(y2 - y1));
				if (this.isFill()) {
					g2.setColor(getFillin());

					g2.fillRect(x2 + 1, y2 + 1, Math.abs(x2 - x1) - 1,
							Math.abs(y2 - y1) - 1);
				}
			}
		}
		g2.setColor(Color.BLACK);
		String s = "";
		String s1 = "";
		if (this instanceof Square) {
			s = "Square properties :";
			s1 = "Dimention = " + String.valueOf(Math.abs(x2 - x1));
			;
		} else {
			s = "Rectangler properties :";
			s1 = "Width = " + String.valueOf(Math.abs(x2 - x1))
					+ ",    Height = " + String.valueOf(Math.abs(y2 - y1));
		}

		String s2 = "Area  = "
				+ String.valueOf(Math.abs((y2 * y1) * (x2 - x1)));
		s = s + "           " + s1 + "           " + s2;
		this.setProperties(s);

	}

}
