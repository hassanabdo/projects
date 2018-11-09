import java.awt.*;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class shape {
	protected Point firstPoint;
	protected Point secondPoint;
	protected boolean selected, fill,isTriangle,isSpecialCase,dotted;
	public boolean isDotted() {
		return dotted;
	}

	public void setDotted(boolean dotted) {
		this.dotted = dotted;
	}

	protected Color clr, fillin;
	protected String properties;
	protected Shape[] points;
	protected Shape[] centers;
	protected int StrockThickness;
	public int getStroke() {
		return StrockThickness;
	}

	public void setStroke(int stroke) {
		StrockThickness = stroke;
	}

	protected int resize = -1, move = -1;

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
	public Color getFillin() {
		return fillin;
	}

	public void setFillin(Color fillin) {
		this.fillin = fillin;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public Color getColor() {
		return clr;
	}

	public void setColor(Color Clr) {
		this.clr = Clr;
	}

	public Point getSecondPoint() {
		return secondPoint;
	}

	public void setSecondPoint(Point secondPoint) {
		this.secondPoint = secondPoint;

	}

	public boolean pointSelected(Point c) {
		return true;
	}

	public Point getFirstPoint() {
		return firstPoint;
	}

	public void setFirstPoint(Point firstPoint) {
		this.firstPoint = firstPoint;
	}

	public void setSelected() {
		selected = true;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setUnSelected() {
		selected = false;
	}

	public shape() {
		// TODO Auto-generated constructor stub
		selected = false;
		clr = Color.BLACK;
		fill = false;
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub

	}

	public boolean checkResize(Point q) {
		// TODO Auto-generated method stub
		int i = 0;
		for (Shape w : points) {

			if (w.contains(q)) {
				resize = i;
			}
			i++;
		}
		if (resize > -1) {
			resizeShape(q);
			return true;
		}
		return false;
	}

	public boolean checkMove(Point q) {
		// TODO Auto-generated method stub
		int i = 0;
		for (Shape w : centers) {

			if (w.contains(q)) {
				move = i;
			}
			i++;
		}
		if (move > -1) {
			moveShape(q);
			return true;
		}
		return false;
	}

	public void moveShape(Point q) {
		// TODO Auto-generated method stub
		
	}

	public void resizeShape(Point q) {
		// TODO Auto-generated method stub

	}

	public boolean contain(Point q) {
		// TODO Auto-generated method stub
		return false;
	}

}
