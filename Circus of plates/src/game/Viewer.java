package game;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Viewer extends JFrame {
	private panel drawingArea;
	private int xOfBall = 0;
	private int yOfBall = 0;
	private int yForBoxes = 480;
	private Source scr;
	private int LengthOfUpperRows;
	private int LengthOfLowerRows;
	private JLabel text1;
	private JLabel text2;
	private JLabel text3;
	private JLabel label;

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public Viewer(int LengthOfUpperRows, int LengthOfLowerRows) {
		super();
		drawingArea = new panel();
		Image img = new ImageIcon("background.jpg").getImage()
				.getScaledInstance(1378, 707, Image.SCALE_SMOOTH);
		ImageIcon im = new ImageIcon(img);
		setContentPane(new JLabel(im));
		// setVisible(true);
		this.setLayout(new BorderLayout());
		text1 = new JLabel("Score1");
		text2 = new JLabel("Score2");
		text3 = new JLabel("the winner");

		label = new JLabel("timer");
		label.setFont(label.getFont().deriveFont(30.0f));
		label.setForeground(Color.green);

		text1.setFont(label.getFont().deriveFont(15.0f));
		text1.setForeground(Color.GRAY);

		text2.setFont(label.getFont().deriveFont(15.0f));
		text2.setForeground(Color.GRAY);

		text3.setFont(label.getFont().deriveFont(15.0f));
		text3.setForeground(Color.GRAY);

		this.add(drawingArea, BorderLayout.CENTER);
		JPanel p1 = new JPanel(new GridLayout(1, 4));
		p1.add(text2);
		p1.add(text1);
		p1.add(text3);
		p1.add(label);
		this.add(p1, BorderLayout.SOUTH);
		// System.out.println(this.getHeight());
		// System.out.println(this.getWidth());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.LengthOfUpperRows = LengthOfUpperRows;
		this.LengthOfLowerRows = LengthOfLowerRows;
		this.setFocusable(true);
		this.requestFocus();
	}

	public void draw(Source scr) {
		this.scr = scr;
		repaint();
	};

	private class panel extends JPanel {

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(5));
			g2.setColor(Color.black);
			g2.draw(new Line2D.Float(0, 30, LengthOfUpperRows, 30));
			g2.draw(new Line2D.Float(this.getWidth(), 30,
					this.getWidth() - 350, 30));
			g2.draw(new Line2D.Float(0, 60, LengthOfLowerRows, 60));
			g2.draw(new Line2D.Float(this.getWidth(), 60,
					this.getWidth() - 250, 60));
			if (scr != null) {
				for (Iterator<FalllingShape> iter = scr.getOperatingShapes()
						.iterator(); iter.hasNext();) {
					FalllingShape b = iter.next();
					// if (b.isProcessing() == true || b.isTaken() == true) {
					if (!(b.getState() instanceof NotProcessing)) {
						xOfBall = b.getX();
						yOfBall = b.getY();
						b.display(g, xOfBall, yOfBall);
					}
				}
				scr.getP1().display(g, scr.getP1().getxForBox(), yForBoxes);
				scr.getP2().display(g, scr.getP2().getxForBox(), yForBoxes);
				text1.setText("||           Hassan's Points:            "
						+ scr.getP1().score + "");
				text2.setText("Diaa's Points:            " + scr.getP2().score
						+ "");
				if (scr.getP1().score > scr.getP2().score) {
					text3.setText("The Winner at this momnet is : Hassan");
				} else if (scr.getP1().score > scr.getP2().score) {
					text3.setText("The Winner at this momnet is : Diaa");
				} else {
					text3.setText("Draw");
				}
			}
		}
	}

	public int getWidthOfFrame() {
		return this.getWidth();
	}

}
