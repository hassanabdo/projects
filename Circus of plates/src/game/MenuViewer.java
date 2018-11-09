package game;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MenuViewer extends JFrame{
	
	public MenuViewer(JButton resume,JButton restart,JButton save,JButton load,JButton exit){
		this.setLocation(350, 100);
		this.getContentPane().setLayout(null);
		Image img = new ImageIcon("Game_Backgrounds.jpg").getImage().getScaledInstance(600,
				600, Image.SCALE_SMOOTH);
		ImageIcon im =new ImageIcon(img);
		setContentPane(new JLabel(im));
		this.setSize(600, 600);
		setResizable(false);
		resume.setSize(100, 50);
		restart.setSize(100, 50);
		save.setSize(100, 50);
		load.setSize(100, 50);
		exit.setSize(100, 50);
		resume.setLocation(240, 90);
		restart.setLocation(240, 150);
		save.setLocation(240, 210);
		load.setLocation(240, 270);
		exit.setLocation(240, 330);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.add(resume);
		this.add(restart);
		this.add(save);
		this.add(load);
		this.add(exit);
		this.setFocusable(true);
		this.requestFocus();
	}
	
}
