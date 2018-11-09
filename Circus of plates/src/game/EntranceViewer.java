

    package game;
     
    import java.awt.Color;
    import java.awt.Dimension;
    import java.awt.FlowLayout;
    import java.awt.GridLayout;
    import java.awt.Image;
    import java.awt.Toolkit;
     
    import javax.swing.ImageIcon;
    import javax.swing.JButton;
    import javax.swing.JFrame;
    import javax.swing.JLabel;
     
    public class EntranceViewer extends JFrame{
           
            public EntranceViewer(JButton start,JButton load,JButton exit){
                    start.setBackground(Color.PINK);
                    load.setBackground(Color.PINK);
                    exit.setBackground(Color.PINK);
                    this.setLocation(350, 100);
                    this.getContentPane().setLayout(null);
    //              setContentPane(new JLabel(new ImageIcon("ea-logo-dark.jpg")));
                    Image img = new ImageIcon("1.jpg").getImage().getScaledInstance(600,
                                    600, Image.SCALE_SMOOTH);
                    ImageIcon im =new ImageIcon(img);
                    setContentPane(new JLabel(im));
                   
                   
                    this.setSize(600, 600);
                    start.setSize(100, 50);
                    load.setSize(100, 50);
                    exit.setSize(100, 50);
                    start.setLocation(240, 350);
                    load.setLocation(240, 410);
                    exit.setLocation(240,470);
                    this.add(start);
                    this.add(load);
                    this.add(exit);
                    this.setFocusable(true);
                    this.requestFocus();
            }
     
    }

