package code.mainPackage;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class KButton extends JButton {

	public KButton(String text) {
		super(text);
		setFont(new Font("Arial", Font.PLAIN, 14));
		setPreferredSize(new Dimension(92, 22));
		setFocusable(false);
	}

	public KButton() {
		this("");
	}

}
