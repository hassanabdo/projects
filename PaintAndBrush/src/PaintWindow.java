import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FileChooserUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PaintWindow extends JFrame {

	private ArrayList<JButton> buttons;
	private ArrayList<shape> draw;
	private shape currentObject;
	private String currentObjectName;
	private boolean motion, fill, dotted;
	private int triThirdPointFlag;
	private Stack<shape> operation;
	private Map<String, Class> loadedClasses;
	private ArrayList<String> nameOfLoadedClasses;
	protected JPanel p1, c, t, p2;
	private GridBagConstraints n;
	private Color defaultColor, fillColor;
	protected JLabel colorviewer, strings;
	protected JButton b;
	private String[] q = { "Line", "Ellipse", "Circle", "Triangle",
			"Rectangler", "Square", "UnDo", "ReDo", "Fill", "Load" };
	private String[] upperButtonsName = { "New", "Save", "Loader", "Delete" };
	private ArrayList<JButton> upperButtons;
	protected JColorChooser chooser;
	private int defaultStroke;
	private JPopupMenu menu;
	Container cp;

	public PaintWindow() {
		super("Paint");
		setVisible(true);
		currentObjectName = "Dash";
		// setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		upperButtons = new ArrayList<JButton>();
		setLayout(new FlowLayout());
		p1 = new JPanel(new GridBagLayout());
		p2 = new JPanel(new GridBagLayout());
		cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		buttons = new ArrayList<JButton>();
		draw = new ArrayList<shape>();
		loadedClasses = new TreeMap<String, Class>();
		nameOfLoadedClasses = new ArrayList<String>();
		strings = new JLabel(
				"This Paint is made by : Ayman Geneidy & Hassan Abd el Mo3ty");

		operation = new Stack<shape>();
		boolean[] exist = new boolean[q.length];
		Arrays.fill(exist, true);
		if (!CheckClassExist(getClass().getResource("/" + "shape" + ".class"),
				"shape"))
			System.exit(0);
//		for (int i = 0; i < 6; i++) {
//			if (exist[i])
//				exist[i] = CheckClassExist(
//						getClass().getResource("/" + q[i] + ".class"), q[i]);
//			if ((i == 1 || i == 4) && !exist[i])
//				exist[i + 1] = false;
//		}
		for (int i = 0; i < q.length; i++) {
			if (exist[i]) {
				addButton(i, q[i]);
			}
		}
		n = new GridBagConstraints();
		n.gridy = 0;
		for (JButton k : buttons) {
			n.gridy++;
			n.gridx = 0;
			p1.add(k, n);
		}
		n.gridy = 0;
		for (int i = 0; i < upperButtonsName.length; i++) {
			n.gridx++;
			n.gridy = 0;
			p2.add(getButton(upperButtonsName[i]), n);
		}
		JPanel p5 = new JPanel(new BorderLayout());
		chooser = new JColorChooser();
		defaultStroke = 2;
		defaultColor = chooser.getColor();
		AbstractColorChooserPanel[] oldPanels = chooser.getChooserPanels();
		for (int i = 0; i < oldPanels.length; i++) {
			String clsName = oldPanels[i].getClass().getName();
			if (!(clsName
					.equals("javax.swing.colorchooser.DefaultSwatchChooserPanel"))) {
				chooser.removeChooserPanel(oldPanels[i]);
			}
		}
		colorviewer = new JLabel("           ");
		fill = false;
		defaultColor = Color.BLACK;
		colorviewer.setOpaque(true);
		colorviewer.setSize(100, 100);
		colorviewer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		colorviewer.setBackground(defaultColor);
		chooser.getSelectionModel().addChangeListener(new colormodifier());
		AbstractColorChooserPanel colorPanel = chooser.getChooserPanels()[0];
		JPanel a1 = (JPanel) colorPanel.getComponent(0);
		a1.remove(2);
		a1.remove(1);
		JButton u;
		String s = "lol";
		u = getButton(s);
		b = getButton("Pop Up");
		b.addActionListener(new buttonListner());
		menu = new JPopupMenu("Menu");
		JButton[] o = new JButton[5];
		o[0] = getButton("2");
		o[1] = getButton("3");
		o[2] = getButton("4");
		o[3] = getButton("6");
		o[4] = getButton("9");
		for (JButton a : o) {
			a.addActionListener(new buttonListner());
			menu.add(a);
		}
		c = new JPanel();
		t = new JPanel();
		formNewPanel();

		dotted = true;
		c.add(t);
		c.add(u);

		c.add(a1);
		c.add(colorviewer);
		c.add(b);
		cp.add(p2, BorderLayout.NORTH);
		cp.add(c, BorderLayout.SOUTH);
		cp.add(p1, BorderLayout.WEST);
		PanelPaint p = new PanelPaint();
		p5.setBackground(Color.WHITE);
		motion = false;
		p5.add(p);
		p5.add(strings, BorderLayout.SOUTH);
		cp.add(p5);

	}

	private void formNewPanel() {
		if (currentObject == null) {

			t.removeAll();
			try {
				Image img = ImageIO.read(getClass().getResource(
						"/" + "Painter" + ".PNG"));
				// Image newimg = img.getScaledInstance(50, 50,
				// java.awt.Image.SCALE_SMOOTH);
				JLabel b = (new JLabel(new ImageIcon(img)));
				t.add(b);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		} else {
			t.removeAll();
			try {
				Image img = ImageIO.read(getClass().getResource(
						"/" + currentObjectName + "Dash.PNG"));
				Image newimg = img.getScaledInstance(50, 50,
						java.awt.Image.SCALE_SMOOTH);
				JButton b = new JButton(new ImageIcon(img));
				b.setActionCommand("Dash");
				b.addActionListener(new buttonListner());
				b.setBorder(LineBorder.createGrayLineBorder());
				t.add(b);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				JButton b = new JButton("Dashed");
				b.setActionCommand("Dash");
				b.addActionListener(new buttonListner());
				t.add(b);
			}
			try {
				Image img = ImageIO.read(getClass().getResource(
						"/" + currentObjectName + "Solid.PNG"));
				Image newimg = img.getScaledInstance(50, 50,
						java.awt.Image.SCALE_SMOOTH);
				JButton b = new JButton(new ImageIcon(img));
				b.setActionCommand("Solid");
				b.addActionListener(new buttonListner());
				b.setBorder(LineBorder.createGrayLineBorder());
				t.add(b);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				JButton b = new JButton("Solid");
				b.setActionCommand("Solid");
				b.addActionListener(new buttonListner());
				t.add(b);
			}
		}
		t.revalidate();
		cp.validate();

	}

	private JButton getButton(String s) {
		try {
			Image img = ImageIO.read(getClass().getResource("/" + s + ".PNG"));
			Image newimg = img.getScaledInstance(50, 50,
					java.awt.Image.SCALE_SMOOTH);
			JButton b = (new JButton(new ImageIcon(newimg)));
			b.addActionListener(new buttonListner());
			b.setActionCommand(s);
			System.out.println(s);
			b.setBorder(LineBorder.createGrayLineBorder());
			return b;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JButton b = (new JButton(s));
			b.addActionListener(new buttonListner());
			b.setActionCommand(s);
			return (b);
		}
	}

	public void colorChanged() {
		// TODO Auto-generated method stub
		for (shape p : draw)
			if (p.isSelected()) {
				p.setColor(defaultColor);
				p.setUnSelected();
			}
		colorviewer.setBackground(defaultColor);
		repaint();
	}

	protected void addButton(int i, String s) {
		buttons.add(getButton(s));
		setButtons(s, i);

	}

	private void setButtons(String s, int i) {
		// TODO Auto-generated method stub
		buttons.get(i).setActionCommand(s);
		switch (s) {
		case "UnDo":
			buttons.get(i).setToolTipText("To " + s + " Click here");
			break;
		case "ReDo":
			buttons.get(i).setToolTipText("To " + s + " Click here");
			break;
		case "Fill":
			fill = true;
			for (shape p : draw)
				p.setUnSelected();
			// setCursor (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			break;
		case "Load":
			buttons.get(i).setToolTipText("To " + s + " a class Click here");
			break;
		case "Triangle":
			buttons.get(i)
					.setToolTipText(
							"To Draw a "
									+ s
									+ " Click here     Hint: you have to draw the Triangle base first");
			break;
		default:
			buttons.get(i).setToolTipText("To Draw a " + s + " Click here");
			break;
		}
	}

	public void setPoints(Point A) {
		if (currentObject == null) {
			return;
		}
		if (currentObject.getFirstPoint() == null) {
			currentObject.setFirstPoint(A);
			return;
		}
		if (currentObject instanceof Triangle) {
			Triangle op = (Triangle) currentObject;
			if (triThirdPointFlag == 0)
				op.setSecondPoint(A);
			if (triThirdPointFlag == 1)
				op.setThirdPoint(A);
			strings.setText(currentObject.getProperties());
			if (!motion)
				triThirdPointFlag++;
			if (!motion && triThirdPointFlag == 2) {
				currentObject = null;
				formNewPanel();
			}
			repaint();
		} else {
			currentObject.setSecondPoint(A);
			repaint();
			strings.setText(currentObject.getProperties());
			if (!motion) {
				currentObject = null;
				formNewPanel();
			}
		}

	}

	public void fillShapes(Point q) {
		// TODO Auto-generated method stub
		boolean b = true;
		for (shape k : draw) {
			if (k.contain(q)) {
				b = false;
				if (k.isFill()) {
					k.setFill(false);
				} else {
					k.setFill(true);
					k.setFillin(defaultColor);
				}
			}
		}
		if (b) {
			fill = false;
		}
		repaint();
	}

	public void checkPoint(Point q) {
		boolean b = true;
		strings.setText("This Paint is made by : Ayman Geneidy & Hassan Abd el Mo3ty");
		if (fill) {
			fillShapes(q);
			return;
		}
		
		for (shape w : draw) {
			if (w.selected & b) {
				if (w.checkResize(q) || w.checkMove(q)) {
					b = false;
					repaint();
				}
			}
		}
		for (shape w : draw) {
			if (w.pointSelected(q) & b) {
				b = false;
				strings.setText(w.getProperties());
				w.setSelected();
				repaint();
			}
		}
		if (b) {
			for (shape w : draw) {
				w.setUnSelected();
			}
		}
		int w = 0;
		for (shape t : draw) {
			if (t.isSelected())
				w++;
		}
		if (w > 1)
			strings.setText("This Paint is made by : Ayman Geneidy & Hassan Abd el Mo3ty");
		repaint();
	}

	public void redo() {
		// TODO Auto-generated method stub
		if (operation.isEmpty())
			return;
		draw.add(operation.pop());
		repaint();
	}

	public void undo() {
		// TODO Auto-generated method stub
		if (draw.size() < 1)
			return;
		operation.push(draw.get(draw.size() - 1));
		draw.remove(draw.size() - 1);
		repaint();
	}

	public class mouseMotion implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			if (fill)
				return;
			if (motion) {
				setPoints(e.getPoint());
			} else {
				checkPoint(e.getPoint());
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			if (fill)
				return;
			if (motion)
				setPoints(e.getPoint());
		}
	}

	public class mouselistener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			if (currentObject != null) {
				motion = true;
				setPoints(e.getPoint());
			} else {
				checkPoint(e.getPoint());
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			if (fill)
				return;
			if (currentObject != null) {
				motion = false;
				setPoints(e.getPoint());
			} else {
				checkPoint(e.getPoint());
			}
			released();

		}

	}

	public class buttonListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			fill = false;
			switch (e.getActionCommand()) {
			case "UnDo":
				undo();
				return;
			case "ReDo":
				redo();
				return;
			case "Delete":
				Delete();
				return;
			case "Save":
				saveUsed();
				return;
			case "Load":
				loadChosenClass();
				return;
			case "Loader":
				LoadSaved();
				return;
			case "Fill":
				fill = true;
				for (shape p : draw)
					p.setUnSelected();
				return;
			case "Pop Up":
				menu.show(b, b.getWidth() / 2, b.getHeight() / 2);
				return;
			case "2":
				defaultStroke = 2;
				strocksetted();
				return;
			case "3":
				defaultStroke = 3;
				strocksetted();
				return;
			case "4":
				defaultStroke = 4;
				strocksetted();
				return;
			case "6":
				defaultStroke = 6;
				strocksetted();
				return;
			case "9":
				defaultStroke = 9;
				strocksetted();
				return;
			case "Dash":
				currentObject.setDotted(true);
				return;
			case "Solid":
				currentObject.setDotted(false);
				return;

			case "lol":
				Color clr = JColorChooser.showDialog(null, "Color Chooser",
						defaultColor);
				if (clr != null) {
					defaultColor = clr;
					colorChanged();
				}
				return;
			}
			for (shape i : draw)
				i.setUnSelected();
			if ((draw.size() != 0)
					&& (draw.get(draw.size() - 1).getFirstPoint() == null))
				return;
			switch (e.getActionCommand()) {
			case "Line":
				currentObjectName = e.getActionCommand();
				currentObject = new Line();
				formNewPanel();

				break;
			case "Circle":
				currentObject = new Circle();
				currentObjectName = e.getActionCommand();
				formNewPanel();
				break;
			case "Ellipse":
				currentObject = new Ellipse();
				currentObjectName = e.getActionCommand();
				formNewPanel();
				break;
			case "Triangle":
				triThirdPointFlag = 0;
				currentObject = new Triangle();
				currentObjectName = e.getActionCommand();
				formNewPanel();
				break;
			case "Rectangler":
				currentObject = new Rectangler();
				currentObjectName = e.getActionCommand();
				formNewPanel();
				break;
//			case "Square":
//				currentObject = new Square();
//				currentObjectName = e.getActionCommand();
//				formNewPanel();
//				break;
			default:
				try {
					currentObject = (shape) loadedClasses.get(
							e.getActionCommand()).newInstance();
				} catch (InstantiationException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					return;
				}
				break;
			}
			operation = new Stack<shape>();
			currentObject.setColor(defaultColor);
			currentObject.setFillin(fillColor);
			currentObject.setDotted(dotted);
			currentObject.setStroke(defaultStroke);
			draw.add(currentObject);
			strings.setText("This Paint is made by : Ayman Geneidy & Hassan Abd el Mo3ty");
			for (shape j : draw)
				j.setUnSelected();
			repaint();

		}

		protected void Delete() {
			// TODO Auto-generated method stub
			for (int i = 0; i < draw.size(); i++) {
				if (draw.get(i).isSelected()) {
					draw.remove(i);
				}
			}
			repaint();
		}
	}

	public class PanelPaint extends JPanel {

		public PanelPaint() {
			addMouseListener(new mouselistener());
			addMouseMotionListener(new mouseMotion());

		}

		@Override
		public void paint(Graphics g) {
			for (shape t : draw) {
				t.paint(g);
			}
		}
	}

	public void released() {
		// TODO Auto-generated method stub
		for (shape p : draw) {
			p.resize = -1;
			p.move = -1;
		}
	}

	public void LoadSaved() {
		// TODO Auto-generated method stub
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnVal = chooser.showOpenDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				URL p = file.toURL();
				if(p.toString().contains(".xml")){
					LoadXML(file);
				}else if(p.toString().contains(".JSON")){
					LoadJSon(file);
				}else{
					JOptionPane.showConfirmDialog(null, "Invalid Extention", "Error", JOptionPane.WARNING_MESSAGE);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showConfirmDialog(null, "Invalid Path", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		
		
	}

	private void LoadJSon(File file) {
		// TODO Auto-generated method stub
		ArrayList<shape> loadedClasses = new ArrayList<shape>();
		String path = file.getAbsolutePath();
		System.out.println(path);
		JsonFactory jfactory = new JsonFactory();
		try {
			JsonParser jParser = jfactory.createJsonParser(new File(path));
			while(jParser.nextToken() == JsonToken.START_OBJECT){
				shape newObject = new shape();
				while(jParser.nextToken() != JsonToken.END_OBJECT){
					String fieldName = jParser.getCurrentName();
//					System.out.println(fieldName);
					if(fieldName.equals("type")){
						jParser.nextToken();
						String typeOfLoadedClass = jParser.getText();
						switch(typeOfLoadedClass){
						case "Line":
							newObject = new Line();
							break;
						case "Circle":
							newObject = new Circle();
							break;
						case "Ellipse":
							newObject = new Ellipse();
							break;
//						case "Square":
//							newObject = new Square();
//							break;
						case "Rectangler":
							newObject = new Rectangler();
							break;
						case "Triangle":
							newObject = new Triangle();
							break;
						default:
							newObject = new shape();
							break;
						}
						loadedClasses.add(newObject);
					}
					
					if(fieldName.equals("firstPoint")){
						jParser.nextToken();
						String[] coordinates = jParser.getText().split(" ");
						System.out.println(coordinates[0]);
						int x1 = Integer.parseInt(coordinates[0]);
						int y1 = Integer.parseInt(coordinates[1]);
						newObject.setFirstPoint(new Point(x1,y1));
					}
					
					if(fieldName.equals("secondPoint")){
						jParser.nextToken();
						String[] coordinates = jParser.getText().split(" ");
						int x2 = Integer.parseInt(coordinates[0]);
						int y2 = Integer.parseInt(coordinates[1]);
						newObject.setSecondPoint(new Point(x2,y2));
					}
					
					if(fieldName.equals("centerPoint")){
						jParser.nextToken();
						String[] coordinates = jParser.getText().split(" ");
						int x = Integer.parseInt(coordinates[0]);
						int y = Integer.parseInt(coordinates[1]);
//						((Circle)newObject).center = new Point(x,y);
					}
					
					if(fieldName.equals("Axes")){
						jParser.nextToken();
						String[] axes = jParser.getText().split(" ");
						int xAxis = Integer.parseInt(axes[0]);
						int yAxis = Integer.parseInt(axes[1]);
						((Circle)newObject).majorXaxis=xAxis;
						((Circle)newObject).majorYaxis=yAxis;
						int x = ((Circle)newObject).center.x;
						int y = ((Circle)newObject).center.y;
						((Circle)newObject).firstPoint = new Point(x-xAxis,y-yAxis);
						((Circle)newObject).secondPoint= new Point(x+xAxis,y+yAxis);
					}
					
					if(fieldName.equals("thirdPoint")){
						if(newObject instanceof Triangle){
							jParser.nextToken();
							String[] coordinates = jParser.getText().split(" ");
							int x3 = Integer.parseInt(coordinates[0]);
							int y3 = Integer.parseInt(coordinates[1]);
							((Triangle) newObject).setThirdPoint(new Point(x3,y3));
						}
					}
					
					if(fieldName.equals("fontColor")){
						jParser.nextToken();
						String[] RGB = jParser.getText().split(" ");
						int r = Integer.parseInt(RGB[0]);
						int g = Integer.parseInt(RGB[1]);
						int b = Integer.parseInt(RGB[2]);
						newObject.setColor(new Color(r,g,b));
					}
					
					if(fieldName.equals("Fill")){
						jParser.nextToken();
						String f = jParser.getText();
						if(f.equals("True")){
						newObject.setFill(true);
						}
					}
					
					if(fieldName.equals("fillColor")){
						jParser.nextToken();
						String[] RGB = jParser.getText().split(" ");
						int r = Integer.parseInt(RGB[0]);
						int g = Integer.parseInt(RGB[1]);
						int b = Integer.parseInt(RGB[2]);
//						newObject.setFill(true);
						newObject.setFillin(new Color(r,g,b));
					}
					
					if(fieldName.equals("dotted")){
						jParser.nextToken();
						String str = jParser.getText();
						if(str.equals("True")){
							newObject.setDotted(true);
						}else{
							newObject.setDotted(false);
						}
					}
					
					if(fieldName.equals("StrockThickness")){
						jParser.nextToken();
						int st = Integer.parseInt(jParser.getText());
						newObject.setStroke(st);
					}
					
				}
				
				
				
			}
			draw = loadedClasses;
			repaint();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("JSON");
	}

	private void LoadXML(File file) {
		// TODO Auto-generated method stub
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(file);
			ArrayList<shape> loadedShapes = new ArrayList<shape>();
			if (doc.hasChildNodes()) {
				NodeList root = doc.getChildNodes();
				NodeList nodes = root.item(0).getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					shape object;
					Node currentNode = (Node) nodes.item(i);
					String str = currentNode.getNodeName();
					System.out.println(str);
					switch(str){
					case "Line":
						object = new Line();
						break;
					case "Circle":
						object = new Circle();
						break;
					case "Ellipse":
						object = new Ellipse();
						break;
//					case "Square":
//						object = new Square();
//						break;
					case "Rectangler":
						object = new Rectangler();
						break;
					case "Triangle":
						object = new Triangle();
						break;
					default:
						object = new shape();
						break;
					}
					NodeList currentNodeList = currentNode.getChildNodes();
					if(object instanceof Circle){
						Node centerPoint = (Node) currentNodeList.item(0);
						NodeList centerNodeList = centerPoint.getChildNodes();
						int x = Integer.parseInt(centerNodeList.item(0).getTextContent());
//						System.out.println(x);
						int y = Integer.parseInt(centerNodeList.item(1).getTextContent());
//						System.out.println(y);
						((Circle) object).center.x = x;
						((Circle) object).center.y = y;
//	
						Node Axes = (Node) currentNodeList.item(1);
						NodeList AxesList = Axes.getChildNodes();
						int xAxis = Integer.parseInt(AxesList.item(0).getTextContent());
						System.out.println(xAxis);
						int yAxis = Integer.parseInt(AxesList.item(1).getTextContent());
						((Circle) object).firstPoint = new Point(x-xAxis,y-yAxis);
						((Circle) object).secondPoint = new Point(x+xAxis,y+yAxis);
						((Circle) object).majorXaxis = xAxis;
						((Circle) object).majorYaxis = yAxis;
						
					}else{
					Node firstPointNode = (Node) currentNodeList.item(0);
//					System.out.println(firstPointNode.getNodeName());
					NodeList firstNodeList = firstPointNode.getChildNodes();
					int x1 = Integer.parseInt(firstNodeList.item(0).getTextContent());
//					System.out.println(firstNodeList.item(0).getTextContent());
					int y1 = Integer.parseInt(firstNodeList.item(1).getTextContent());
//					System.out.println(firstNodeList.item(1).getTextContent());
					object.setFirstPoint(new Point(x1,y1));
					Node secondPointNode = (Node) currentNodeList.item(1);
//					System.out.println(secondPointNode.getNodeName());
					NodeList secondNodeList = secondPointNode.getChildNodes();
					int x2 = Integer.parseInt(secondNodeList.item(0).getTextContent());
//					System.out.println(secondNodeList.item(0).getTextContent());
					int y2 = Integer.parseInt(secondNodeList.item(1).getTextContent());
//					System.out.println(secondNodeList.item(1).getTextContent());
					object.setSecondPoint(new Point(x2,y2));
					}
					int count=2;
					if(object instanceof Triangle){
						Node thirdPointNode = (Node) currentNodeList.item(count);
						NodeList thirdPointList = thirdPointNode.getChildNodes();
						int x3 = Integer.parseInt(thirdPointList.item(0).getTextContent());
						int y3 = Integer.parseInt(thirdPointList.item(1).getTextContent());
						((Triangle) object).setThirdPoint(new Point(x3,y3));
						count++;
					}
					Node fontColor = (Node) currentNodeList.item(count);
					count++;
					String[] RGB = fontColor.getTextContent().split(" ");
					int r = Integer.parseInt(RGB[0]);
					int g = Integer.parseInt(RGB[1]);
					int b = Integer.parseInt(RGB[2]);
					object.setColor(new Color(r,g,b));
					Node isFill = (Node) currentNodeList.item(count);
					count++;
					System.out.println(isFill.getTextContent());
					String s = isFill.getTextContent();
					if( s.equals("True") ){
						System.out.println("i am filling with red");
						Node fillColor = (Node) currentNodeList.item(count);
						count++;
						String[] RGB2 = fillColor.getTextContent().split(" ");
						int r2 = Integer.parseInt(RGB2[0]);
						int g2 = Integer.parseInt(RGB2[1]);
						int b2 = Integer.parseInt(RGB2[2]);
						object.setFill(true);
						object.setFillin(new Color(r2,g2,b2));
						
					}
					Node dotted = (Node)currentNodeList.item(count);
					count++;
					String d = dotted.getTextContent();
					if(d.equals("True")){
						object.setDotted(true);
					}else{
						object.setDotted(false);
					}
					Node strock = (Node) currentNodeList.item(count);
					count++;
					int strocknum = Integer.parseInt(strock.getTextContent());
					object.setStroke(strocknum);
					System.out.println("here1");
					loadedShapes.add(object);
				}
				System.out.println("here2");
				draw = loadedShapes;
				repaint();
			}
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("XML");
	}

	public void strocksetted() {
		// TODO Auto-generated method stub
		for (shape p : draw)
			if (p.isSelected()) {
				// p.setUnSelected();
				p.setStroke(defaultStroke);
			}
		repaint();
	}

	public void loadChosenClass() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnVal = chooser.showOpenDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			URL myUrl;
			try {
				String nameOfClass = "";
				myUrl = file.toURL();
				String str = myUrl + "";// full path
				int i = 0;
				for (i = str.length() - 1; i >= 0; i--) {
					if (str.charAt(i) == '/') {
						break;
					}
				}
				nameOfClass = str.substring(++i, str.length() - 6);
				URL name;

				name = new URL(str.substring(0, i));

				loadClass(name, nameOfClass);
			} catch (MalformedURLException e1) {

			}

		}
	}

	private void loadClass(URL name, String nameOfClass) {

		URL[] my = { name };
		URLClassLoader classloader = new URLClassLoader(my);
		for (String s : q)
			if (nameOfClass.equals(s)) {
				return;
			}

		try {
			Class myClass = classloader.loadClass(nameOfClass);
			JButton b = new JButton(nameOfClass);
			if (nameOfLoadedClasses.contains(nameOfClass)) {
				System.out.println("u already loaded such a class");
				return;
			}
			nameOfLoadedClasses.add(nameOfClass);
			buttons.add(b);
			addButton(buttons.size() - 1, nameOfClass);
			p1.add(b);
			p1.revalidate();
			// p1.repaint();
			b.addActionListener(new buttonListner());
			loadedClasses.put(nameOfClass, myClass);
			System.out.println(nameOfClass);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("Cant load from this path " + name);
			// JOptionPane.showMessageDialog("u cannot load class " + str,wa);
		}

	}

	private boolean CheckClassExist(URL name, String nameOfClass) {
		URL[] my = { name };
		URLClassLoader classloader = new URLClassLoader(my);
		try {
			Class myClass = classloader.loadClass(nameOfClass);
			nameOfLoadedClasses.add(nameOfClass);
			return true;
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("Cant load from this path " + name);
			// JOptionPane.showMessageDialog("u cannot load class " + str,wa);
			return false;
		}

	}

	public class colormodifier implements ChangeListener {

		public colormodifier() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			defaultColor = chooser.getColor();
			colorChanged();
		}

	}

	public void saveUsed() {
		int selectedOption = JOptionPane
				.showConfirmDialog(
						null,
						"Wanna Save the Object using XML press yes , using Jarson press no , else press cancel",
						"Choose", JOptionPane.YES_NO_CANCEL_OPTION);
		if (selectedOption == JOptionPane.YES_OPTION) {
			saveXML();
		} else if (selectedOption == JOptionPane.NO_OPTION) {
			SaveJason();
		} else {
			return;
		}
	}

	private void SaveJason() {
		// TODO Auto-generated method stub
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showSaveDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			String dir = file.getAbsolutePath()
					+ ".JSON";
			System.out.println(dir.toString());
			JsonFactory jfactory = new JsonFactory();
			 
			/*** write to file ***/
			try {
				JsonGenerator jGenerator = jfactory.createJsonGenerator(new File(
						dir), JsonEncoding.UTF8);
				jGenerator.useDefaultPrettyPrinter();
				for(shape ob : draw){
					String type ="";
					if(ob instanceof Line){
						type = "Line";
					}
					else 
						if(ob instanceof Circle){
						type = "Circle";
					}
						else if(ob instanceof Ellipse){
						type = "Ellipse";
					}
//						else if(ob instanceof Square){
//						type = "Square";
//					}
						else if(ob instanceof Rectangler){
						type = "Rectangler";
					}else {
						type = "Triangle";
					}
					jGenerator.writeStartObject();
					jGenerator.writeStringField("type", type);
					if(type.equals("Circle")){
						Point center = ((Circle)ob).center;
						String x = center.x+"";
						String y = center.y+"";
						jGenerator.writeStringField("centerPoint", x+" "+y);
						String xAxis = ((Circle)ob).majorXaxis+"";
						String yAxis = ((Circle)ob).majorYaxis+"";
						jGenerator.writeStringField("Axes", xAxis+" "+yAxis);
					}else{ 
					Point first = ob.getFirstPoint();
					String x1 = first.x +"";
					String y1 = first.y +"";
					jGenerator.writeStringField("firstPoint", x1+" "+y1);
					Point second = ob.getSecondPoint();
					String x2 = second.x+"";
					String y2 = second.y+"";
					jGenerator.writeStringField("secondPoint", x2+" "+y2);
					}
					if(ob instanceof Triangle){
						Point third=((Triangle) ob).thirdPoint;
						String x3 = third.x+"";
						String y3 = third.y+"";
						jGenerator.writeStringField("thirdPoint", x3+" "+y3);
					}
					Color clr = ob.getColor();
					String r = clr.getRed()+"";
					String g = clr.getGreen()+"";
					String b = clr.getBlue()+"";
					jGenerator.writeStringField("fontColor", r+" "+g+" "+b);
					boolean fillbool = ob.isFill();
					if(fillbool){
						jGenerator.writeStringField("Fill", "True");
						Color fillColor = ob.getFillin();
						 r = fillColor.getRed()+"";
						 g = fillColor.getGreen()+"";
						 b = fillColor.getBlue()+"";
						jGenerator.writeStringField("fillColor", r+" "+g+" "+b);
					}else{
						jGenerator.writeStringField("Fill", "False");
					}
					boolean d = ob.isDotted();
					if(d){
						jGenerator.writeStringField("dotted", "True");
					}else{
						jGenerator.writeStringField("dotted", "False");
					}
					int strock = ob.getStroke();
					jGenerator.writeStringField("StrockThickness", strock+"");
					jGenerator.writeEndObject();
				}
				jGenerator.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void saveXML() {
		// TODO Auto-generated method stub
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory
					.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("shapes");
			doc.appendChild(rootElement);
			for (shape ob : draw) {
				String type = "";
				if (ob instanceof Line) {
					type = "Line";
				} 
				else 
					if (ob instanceof Circle) {
					type = "Circle";
				} 
					else if (ob instanceof Ellipse) {
					type = "Ellipse";
				} 
//					else if (ob instanceof Square) {
//					type = "Square";
//				} 
					else if (ob instanceof Rectangler) {
					type = "Rectangler";
				} else {
					type = "Triangle";
				}
				Element object = doc.createElement(type);
				rootElement.appendChild(object);
				
				if(type.equals("Circle")){
					Point center = ((Circle)ob).center;
					Element centerPoint = doc.createElement("center");
					object.appendChild(centerPoint);
					Element x = doc.createElement("x");
					x.appendChild(doc.createTextNode(center.x+""));
					Element y = doc.createElement("y");
					y.appendChild(doc.createTextNode(center.y+""));
					centerPoint.appendChild(x);
					centerPoint.appendChild(y);
					int xAxis = ((Circle)ob).majorXaxis;
					int yAxis = ((Circle)ob).majorYaxis;
					Element axes = doc.createElement("Axes");
					object.appendChild(axes);
					Element majorX = doc.createElement("majorX");
					majorX.appendChild(doc.createTextNode(xAxis+""));
					Element majorY = doc.createElement("majorY");
					majorY.appendChild(doc.createTextNode(yAxis+""));
					axes.appendChild(majorX);
					axes.appendChild(majorY);
				}
				else{
				// add the first point of the shape
				Point first = ob.getFirstPoint();
				Element firstPoint = doc.createElement("firstPoint");
				object.appendChild(firstPoint);
				Element x1 = doc.createElement("x1");
				x1.appendChild(doc.createTextNode(first.x + ""));
				Element y1 = doc.createElement("y1");
				y1.appendChild(doc.createTextNode(first.y + ""));
				firstPoint.appendChild(x1);
				firstPoint.appendChild(y1);
				// add the second point of the shape
				Point second = ob.getSecondPoint();
				Element secondPoint = doc.createElement("secondPoint");
				object.appendChild(secondPoint);
				Element x2 = doc.createElement("x2");
				x2.appendChild(doc.createTextNode(second.x + ""));
				Element y2 = doc.createElement("y2");
				y2.appendChild(doc.createTextNode(second.y + ""));
				secondPoint.appendChild(x2);
				secondPoint.appendChild(y2);
				}
				
				if (ob instanceof Triangle) {
					Triangle newObject = (Triangle) ob;
					Point third = newObject.thirdPoint;
					Element thirdPoint = doc
							.createElement("thirdPoint");
					object.appendChild(thirdPoint);
					Element x3 = doc.createElement("x3");
					x3.appendChild(doc.createTextNode(third.x + ""));
					Element y3 = doc.createElement("y3");
					y3.appendChild(doc.createTextNode(third.y + ""));
					thirdPoint.appendChild(x3);
					thirdPoint.appendChild(y3);
				}

				// add font color
				Element fontColor = doc.createElement("fontColor");
				object.appendChild(fontColor);
				int r = ob.clr.getRed();
				int g = ob.clr.getGreen();
				int b = ob.clr.getBlue();
				fontColor.appendChild(doc.createTextNode(r + " " + g
						+ " " + b));

				// add fill color
				if (ob.isFill()) {
					Element fill = doc.createElement("fill");
					object.appendChild(fill);
					fill.appendChild(doc.createTextNode("True"));
					Element fillColor = doc.createElement("fillColor");
					object.appendChild(fillColor);
					int r2 = ob.fillin.getRed();
					int g2 = ob.fillin.getGreen();
					int b2 = ob.fillin.getBlue();
					fillColor.appendChild(doc.createTextNode(r2 + " "
							+ g2 + " " + b2));
				} else {
					Element fill = doc.createElement("fill");
					object.appendChild(fill);
					fill.appendChild(doc.createTextNode("false"));
				}
				
				//add dotted boolean
				Element dotted = doc.createElement("dotted");
				object.appendChild(dotted);
				boolean d = ob.isDotted();
				if(d){
					dotted.appendChild(doc.createTextNode("True"));
				}else{
					dotted.appendChild(doc.createTextNode("False"));
				}
				
				Element strock = doc.createElement("StrockThickness");
				object.appendChild(strock);
				int s = ob.getStroke();
				strock.appendChild(doc.createTextNode(s+""));

			}
			// System.out.println("save done");
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showSaveDialog(getParent());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				String dir = file.getAbsolutePath()
						+ ".xml";
				System.out.println(dir.toString());
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				try {
					Transformer transformer = transformerFactory
							.newTransformer();
					DOMSource source = new DOMSource(doc);
					
					StreamResult result = new StreamResult(
							new File(dir));
					transformer.transform(source, result);
					// System.out.println("File saved!");
				} catch (TransformerConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TransformerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
