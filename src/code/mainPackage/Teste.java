package code.mainPackage;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Teste extends JFrame {

	public Teste() {
		super();
		setSize(300, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JButton place = new JButton("lolo");
		JPanel north = new JPanel();
		north.add(place);

		add(north, BorderLayout.NORTH);
		add(new JTextField(), BorderLayout.SOUTH);

		setVisible(true);

	}

	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		Teste window = new Teste();
	}
}
