import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ellipse extends shape {
	protected int majorXaxis;// major x axis
	protected int majorYaxis; // major y axis
	protected Point center; // center cord

	public Ellipse() {
		// TODO Auto-generated constructor stub
		majorYaxis = 0;
		points = new Shape[4];
		centers = new Shape[1];
		center = new Point();
		majorXaxis = 0;
		isTriangle=false;
		isSpecialCase=false;
	}

	public void resizeShape(Point q) {
		// TODO Auto-generated method stub
		if (this.isSpecialCase) {
			if (resize == 0 || resize == 1)
				majorYaxis = majorXaxis = Math.abs(q.x - center.x);
			if (resize == 2 || resize == 3)
				majorXaxis = majorYaxis = Math.abs(q.y - center.y);
		} else {
			if (resize == 0 || resize == 1)
				majorXaxis = Math.abs(q.x - center.x);
			if (resize == 2 || resize == 3)
				majorYaxis = Math.abs(q.y - center.y);
		}
		makeReq();
	}
	public void moveShape(Point q) {
		// TODO Auto-generated method stub
		center=q;
		this.makeReq();
	}

	public void setSecondPoint(Point secondPoint) {
		this.secondPoint = secondPoint;
		int x1 = this.firstPoint.x;
		int y1 = this.firstPoint.y;
		int x2 = secondPoint.x;
		int y2 = secondPoint.y;
		if (x2 > x1) {
			if (y2 > y1) {
				majorXaxis = Math.abs(x1 - x2) / 2;
				majorYaxis = Math.abs(y1 - y2) / 2;
				center.y = y1 + majorYaxis;
			} else {
				majorXaxis = Math.abs(x1 - x2) / 2;
				majorYaxis = Math.abs(y1 - y2) / 2;
				center.y = y2 + majorYaxis;
			}
			center.x = x1 + majorXaxis;
		} else {
			if (y2 > y1) {
				majorXaxis = Math.abs(x1 - x2) / 2;
				majorYaxis = Math.abs(y1 - y2) / 2;
				center.y = y1 + majorYaxis;

			} else {

				majorXaxis = Math.abs(x1 - x2) / 2;
				majorYaxis = Math.abs(y1 - y2) / 2;
				center.y = y2 + majorYaxis;
			}
			center.x = x2 + majorXaxis;
		}
		makeReq();
	}
	public boolean contain(Point c) {
		int i = Math.max(majorXaxis, 5);
		int t = Math.max(majorYaxis, 5);
		double q = Math.pow((c.getX() - center.x), 2) / Math.pow(i, 2);
		q += Math.pow((c.getY() - center.y), 2) / Math.pow(t, 2);
		if (q <1) {
			return true;
		} else {
			return false;
		}
	}
	public boolean pointSelected(Point c) {
		int i = Math.max(majorXaxis, 5);
		int t = Math.max(majorYaxis, 5);
		double q = Math.pow((c.getX() - center.x), 2) / Math.pow(i, 2);
		q += Math.pow((c.getY() - center.y), 2) / Math.pow(t, 2);
		if (q > .7 && q < 1.3) {
			return true;
		} else {
			return false;
		}
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
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
		g2.setColor(getFillin());
		if (this.getFirstPoint() == null || this.getSecondPoint() == null)
			return;

		if (this.isFill()) {
			g2.fillOval(center.x - majorXaxis, center.y - majorYaxis,
					2 * majorXaxis, 2 * majorYaxis);
		}
		if (this.isSelected()) {
			g2.setColor(Color.YELLOW);
		} else {
			g2.setColor(this.getColor());
		}
		g2.drawOval(center.x - majorXaxis, center.y - majorYaxis,
				2 * majorXaxis, 2 * majorYaxis);
		if (this.isSelected()) {
			g2.setStroke(new BasicStroke(StrockThickness+1));
			g2.setColor(Color.ORANGE);
			for (Shape i : points) {
				g2.draw(i);
			}
			g2.setColor(Color.GREEN);
			g2.drawLine(center.x, center.y, center.x, center.y);
			for (Shape i : centers) {
				g2.draw(i);
			}
		}

		String s = "Ellipse properties :";
		String s1 = "Center : x = " + String.valueOf(center.x) + "  , y = "
				+ String.valueOf(center.y);
		String s2 = "Height = " + String.valueOf(majorYaxis) + " , Width = "
				+ String.valueOf(majorXaxis);
		s = s + "           " + s1 + "           " + s2;
		this.setProperties(s);

	}

	public void makeReq() {
		// TODO Auto-generated method stub
		points[0] = new Rectangle(center.x - majorXaxis - 6, center.y - 6, 12,
				12);
		points[1] = new Rectangle(center.x + majorXaxis - 6, center.y - 6, 12,
				12);
		points[2] = new Rectangle(center.x - 6, center.y - majorYaxis - 6, 12,
				12);
		points[3] = new Rectangle(center.x - 6, center.y + majorYaxis - 6, 12,
				12);
		centers[0] = new Ellipse2D.Double(center.x - 6, center.y - 6, 12, 12);
	}
}
