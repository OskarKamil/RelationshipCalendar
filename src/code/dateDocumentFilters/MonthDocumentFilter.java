package code.dateDocumentFilters;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class MonthDocumentFilter extends DocumentFilter {
	
	String regex = "^[0-9]+$";

	public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {

		String text = fb.getDocument().getText(0, fb.getDocument().getLength());
		text += str;
		if ((fb.getDocument().getLength() + str.length() - length) <= 2 && text.matches(regex)) {
			super.replace(fb, offs, length, str, a);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {

		String text = fb.getDocument().getText(0, fb.getDocument().getLength());
		text += str;
		if ((fb.getDocument().getLength() + str.length()) <= 2 && text.matches(regex)) {
			super.insertString(fb, offs, str, a);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}

}
