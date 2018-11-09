package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller implements MouseListener, MouseMotionListener,
		KeyListener {

	private static Controller Control = new Controller();
	private static double delta = .1;
	private boolean runFlag = false;
	private Viewer view;
	protected Source scr;
	private int numberOfTypes = 3;
	private int HeightOfUpperRows = 0;
	private int HeightOfLowerRows = 30;
	protected int StartOfRightRows = 0;
	protected int StartOfLeftRows = 0;
	protected int LengthOfUpperRows = 350;
	protected int LengthOfLowerRows = 250;
	private int counter = 0;
	protected Queue<FalllingShape> Box1Shapes;
	protected Queue<FalllingShape> Box2Shapes;
	private JButton restart;
	private JButton start;
	private JButton load;
	private JButton load2;
	private JButton save;
	private JButton exit;
	private JButton exit2;
	private JButton resume;
	private EntranceViewer entryView;
	private MenuViewer MenuView;
	private Thread loop;
	private Timer timer;
	private boolean running;

	private Controller() {
		timer = new Timer();

		view = new Viewer(LengthOfUpperRows, LengthOfLowerRows);
		try {
			scr = new Source(numberOfTypes, StartOfLeftRows, HeightOfUpperRows,
					StartOfRightRows, HeightOfLowerRows);
		} catch (MalformedURLException | ClassNotFoundException
				| InstantiationException | IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		scr.setInterval(120);
		resume = new JButton("Resume");
		start = new JButton("Start");
		load = new JButton("Load");
		load2 = new JButton("Load");
		save = new JButton("Save");
		restart = new JButton("New Game");
		exit = new JButton("Exit");
		exit2 = new JButton("Exit");
		entryView = new EntranceViewer(start, load, exit);
		MenuView = new MenuViewer(resume, restart, save, load2, exit2);

		resume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				running = true;
				MenuView.hide();
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {

					public void run() {

						view.getLabel().setText("" + setIntervall());

					}
				}, 500, 500);
			}
		});
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileDialog = new JFileChooser("C:\\");
				fileDialog.setFileFilter(new FileNameExtensionFilter(
						"Java Files", "ser"));
				int openChoice = fileDialog.showOpenDialog(null);
				if (openChoice == JFileChooser.APPROVE_OPTION) {
					File file = fileDialog.getSelectedFile();
					// File selectedFile = new
					// File(fileDialog.getSelectedFile().getParent());
					String path = fileDialog.getSelectedFile().toString();
					try {
						load(path);
						entryView.hide();
						runGameLoop();
					} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// runGameLoop();
					// String
					// fileName=path.substring(path.lastIndexOf('\\')+1,path.length());
					//

				}

			}
		});
		load2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resume.setVisible(true);
				JFileChooser fileDialog = new JFileChooser("E:\\");
				fileDialog.setFileFilter(new FileNameExtensionFilter(
						"Java Files", "ser"));
				int openChoice = fileDialog.showOpenDialog(null);
				if (openChoice == JFileChooser.APPROVE_OPTION) {
					File file = fileDialog.getSelectedFile();
					// File selectedFile = new
					// File(fileDialog.getSelectedFile().getParent());

					String path = fileDialog.getSelectedFile().toString();
					try {
						load(path);
						loop.stop();
						MenuView.hide();

						timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask() {

							public void run() {

								view.getLabel().setText("" + setIntervall());

							}
						}, 500, 500);
						runGameLoop();

					} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// runGameLoop();
					// String
					// fileName=path.substring(path.lastIndexOf('\\')+1,path.length());
					//

				}

			}
		});
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resume.setVisible(true);
				String fileName = JOptionPane
						.showInputDialog("Please a name to be saved with: ");
				save(fileName);
				loop.stop();

				JOptionPane.showMessageDialog(null, "Game Saved");
				// load("E:\\"+fileName);
				runGameLoop();
			}
		});
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				entryView.hide();
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {

					public void run() {

						view.getLabel().setText("" + setIntervall());

					}
				}, 500, 500);
				runGameLoop();
			}
		});

		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resume.setVisible(true);
				try {
					scr = new Source(numberOfTypes, StartOfLeftRows,
							HeightOfUpperRows, StartOfRightRows,
							HeightOfLowerRows);

					scr.setInterval(120);
					// loop.stop();
					MenuView.hide();
					running = true;
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {

						public void run() {

							view.getLabel().setText("" + setIntervall());

						}
					}, 500, 500);

				} catch (MalformedURLException | ClassNotFoundException
						| InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resume.setVisible(true);
				System.exit(0);
			}
		});

		exit2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
	}

	public void save(String fileName) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("E:\\" + fileName + ".ser");
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(scr);
			outStream.close();
			fileOut.close();
		} catch (IOException i) {

			i.printStackTrace();
		}

	}

	public void load(String path) throws InstantiationException,
			IllegalAccessException {
		try {
			FileInputStream fileInput = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileInput);
			scr = (Source) in.readObject();
			in.close();
			fileInput.close();

		} catch (IOException i) {
			// TODO Auto-generated catch block
			i.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startGame() {
		entryView.show();
	}

	public static Controller getInstance() {
		return Control;
	}

	private final int setIntervall() {
		if (scr.getInterval() == 1) {
			timer.cancel();
			String result = "DRAW !";
			if (scr.getP1().getScore() > scr.getP2().getScore()) {
				result = "Hassan Is The Winner !\n \t Congratulations";
			} else if (scr.getP1().getScore() < scr.getP2().getScore()) {
				result = "Diaa Is The Winner !\n \t Congratulations";
			}
			JOptionPane.showMessageDialog(null, result);
			resume.setVisible(false);
			ShowMenu();

			timer.cancel();
		}

		scr.setInterval(scr.getInterval() - 1);
		return scr.getInterval();
	}

	public void runGameLoop() {
		// timer=new Timer();
		// timer.purge();
		// timer = new Timer();
		// scr.setInterval(60);

		loop = new Thread() {
			public void run() {
				try {
					runGame();
				} catch (MalformedURLException | ClassNotFoundException
						| InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		// System.out.println("paused");
		loop.start();
	}

	public void runGame() throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		runFlag = true;
		startUp();
		double nextTime = (double) System.nanoTime() / 1000000000;
		while (runFlag) {
			double currentTime = (double) System.nanoTime() / 1000000000;
			if (currentTime >= nextTime) {
				if (running)
					update();
				nextTime += delta;
			} else {
				// calculate the time to sleep
				int sleepTime = (int) (1000.0 * (nextTime - currentTime));
				// sanity check
				if (sleepTime > 0) {
					// sleep until the next update
					try {
						// Thread.sleep(sleepTime);
					} catch (Exception e) {
						// do nothing
					}
				}
			}
		}
	}

	public void update() {
		if (counter > 10) {
			if (getNubmerOfProcessingShapes() < 24)
				getMorProcessing();
			counter = 0;
		} else {
			counter++;
		}

		for (Iterator<FalllingShape> iter = scr.getOperatingShapes().iterator(); iter
				.hasNext();) {
			FalllingShape b = iter.next();
			b.getState().doAction(b);
		}
		evaluate(Box1Shapes, 1);
		evaluate(Box2Shapes, 2);
		// Timer t=new
		view.draw(scr);
	}

	public void evaluate(Queue<FalllingShape> arr, int box) {
		Iterator<FalllingShape> iter = arr.iterator();
		if (iter.hasNext()) {
			FalllingShape shape1 = iter.next();
			int color1 = shape1.color;
			if (iter.hasNext()) {
				FalllingShape shape2 = iter.next();
				int color2 = shape2.color;
				if (color1 == color2) {
					if (iter.hasNext()) {
						FalllingShape shape3 = iter.next();
						int color3 = shape3.color;
						if (color2 == color3) {
							if (box == 1)
								scr.getP1().score += 1;
							else
								scr.getP2().score += 1;
							arr.clear();
							resetShape(shape1);
							resetShape(shape2);
							resetShape(shape3);
							if (box == 1) {
								scr.getP1().setHeightForBox(480);
							} else {
								scr.getP2().setHeightForBox(480);
							}
						} else {
							resetShape(shape1);
							resetShape(shape2);
							arr.remove(shape1);
							arr.remove(shape2);
							if (box == 1) {
								scr.getP1().setHeightForBox(480 - 30);
							} else {
								scr.getP2().setHeightForBox(480 - 30);
							}
							shape3.setY(480 - 10);
						}

					} else {
						if (box == 1) {
							scr.getP1().setHeightForBox(480 - 30 - 30);
						} else {
							scr.getP2().setHeightForBox(480 - 30 - 30);
						}
					}
				} else {
					resetShape(shape1);
					arr.remove(shape1);
					shape2.setY(480 - 10);
				}
			} else {
				if (box == 1) {
					scr.getP1().setHeightForBox(480 - 30);
				} else {
					scr.getP2().setHeightForBox(480 - 30);
				}
			}
		} else {
			if (box == 1) {
				scr.getP1().setHeightForBox(480);
			} else {
				scr.getP2().setHeightForBox(480);
			}
		}

	}

	public void resetShape(FalllingShape b) {
		int type = b.getType();
		b.setState(new NotProcessing());
		if (type == 0) {
			b.setX(StartOfLeftRows);
			b.setY(HeightOfUpperRows);
		} else if (type == 1) {
			b.setX(StartOfRightRows - 70);
			b.setY(HeightOfUpperRows);
		} else if (type == 2) {
			b.setX(StartOfLeftRows);
			b.setY(HeightOfLowerRows);
		} else {
			b.setX(StartOfRightRows - 70);
			b.setY(HeightOfLowerRows);
		}
	}

	public void getMorProcessing() {
		for (int i = 0; i < 4; i++) {
			for (FalllingShape b : scr.getOperatingShapes()) {
				if (b.getType() == i && b.getState() instanceof NotProcessing) {
					b.setState(new ProcessingNotFalling());
					break;
				}
			}
		}
	}

	public int getNubmerOfProcessingShapes() {
		int number = 0;
		for (FalllingShape b : scr.getOperatingShapes()) {
			if (b.getState() instanceof ProcessingFalling
					|| b.getState() instanceof ProcessingNotFalling)
				number++;
		}
		return number;
	}

	public void startUp() throws MalformedURLException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		running = true;
		view.show();
		view.addMouseListener(this);
		view.addMouseMotionListener(this);
		view.addKeyListener(this);
		MenuView.addKeyListener(this);
		StartOfRightRows = view.getWidthOfFrame();
		Box1Shapes = new LinkedList<FalllingShape>();
		Box2Shapes = new LinkedList<FalllingShape>();
		scr.getP1().setHeightForBox(480);
		scr.getP2().setHeightForBox(480);
		scr.getP1().setxForBox(0);
		scr.getP2().setxForBox(0);
	}

	public boolean LiesInBoxes(int x, int y) {

		if (y + 10 >= scr.getP1().getHeightForBox()
				&& y < scr.getP1().getHeightForBox())
			if (x + 50 >= scr.getP1().getxForBox() + 40
					&& x < scr.getP1().getxForBox() + 100)
				return true;

		if (y + 10 >= scr.getP2().getHeightForBox()
				&& y < scr.getP2().getHeightForBox())
			if (x + 50 >= scr.getP2().getxForBox() + 40
					&& x < scr.getP2().getxForBox() + 100)
				return true;

		return false;
	}

	public void ShowMenu() {
		running = false;
		MenuView.show();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (scr != null)
			scr.getP1().setxForBox(e.getPoint().x);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if (scr != null) {
			scr.getP1().setxForBox(e.getPoint().x);
			for (FalllingShape b : Box1Shapes) {
				b.setX(scr.getP1().getxForBox() + 40);
			}

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar() == 'a')
			scr.getP2().setxForBox(scr.getP2().getxForBox() - 20);
		else if (e.getKeyChar() == 'd')
			scr.getP2().setxForBox(scr.getP2().getxForBox() + 20);
		else if (e.getKeyCode() == e.VK_ESCAPE) {
			ShowMenu();
			timer.cancel();
		} else
			System.out.println("not a or d");

		for (FalllingShape b : Box2Shapes) {
			b.setX(scr.getP2().getxForBox() + 40);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
