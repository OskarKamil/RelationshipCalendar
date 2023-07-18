package code.mainPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.Year;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import code.dateDocumentFilters.DayDocumentFilter;
import code.dateDocumentFilters.MonthDocumentFilter;
import code.dateDocumentFilters.YearDocumentFilter;

@SuppressWarnings("serial")
public class KTextField extends JTextField {

	String defaultValue;
	Person origin;

	public KTextField(String text) {
		super(text);
	}

	public KTextField(int superInt) {
		super(superInt);
		this.setEditable(true);
		this.addFocusListener(displayDefault);
		this.setToDefault();
		this.addActionListener(confirmInputActionListener);
	}

	public KTextField() {
		super();
	}

	public void setOrigin(Object newOrigin) {
		origin = (Person) newOrigin;
	}

	private Person getOrigin() {
		return origin;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setToDefault() {
		this.setText(defaultValue);
		this.setFont(new Font(this.getFont().getName(), Font.ITALIC, 12));
		this.setForeground(Color.gray);
	}

	public void setNewText(String text) {
		System.out.println("lolo");
		if (text.equals("")) {
			setToDefault();
			return;
		}
		setText(text);
		this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 12));
		this.setForeground(Color.black);
	}
	
	public void tryToParseData(KTextField field) {
		int number = Integer.parseInt(field.getText());
		if (number < 1 && !(field.getDefaultValue().equals("YYYY")))
			field.setText("01");
		else if (number < 10 && field.getText().length() == 1)
			field.setText("0" + field.getText());
		else if (number > 31 && field.getDefaultValue().equals("DD"))
			field.setText("31");
		else if (number > 12 && field.getDefaultValue().equals("MM"))
			field.setText("12");
		else if (field.getDefaultValue().equals("YYYY") && (number < Year.now().getValue()-110 || number > (Year.now().getValue()) + 5))
			field.setText(Integer.toString(Year.now().getValue()));
		getOrigin().parseDateIfValid();
		/*
		 * this is not finished yet, currently under construction.
		 */
	}

	FocusListener displayDefault = new FocusListener() {
		public void focusLost(FocusEvent e) {
			KTextField field = (KTextField) e.getSource();
			field.setBackground(Color.white);
			AbstractDocument doc = (AbstractDocument) field.getDocument();
			doc.setDocumentFilter(new DocumentFilter());
			if (field.getText().equals(""))
				field.setToDefault();
			else {
				if (!field.getDefaultValue().equals("[name]"))
					tryToParseData(field);
			}

		}

	

		public void focusGained(FocusEvent e) {
			KTextField field = (KTextField) e.getSource();
			;
			field.setBackground(new Color(230, 230, 230));
			if (field.getText().equals(defaultValue)) {
				field.setText("");
				field.setFont(new Font(field.getFont().getName(), Font.PLAIN, 12));
				field.setForeground(Color.black);
			} else {
				field.setFont(new Font(field.getFont().getName(), Font.PLAIN, 12));
				field.setForeground(Color.black);
				field.selectAll();
			}

			switch (field.getDefaultValue()) {
			case "[name]": {
				break;
			}
			case "DD": {
				AbstractDocument doc = (AbstractDocument) field.getDocument();
				doc.setDocumentFilter(new DayDocumentFilter());
				break;
			}
			case "MM": {
				AbstractDocument doc = (AbstractDocument) field.getDocument();
				doc.setDocumentFilter(new MonthDocumentFilter());
				break;
			}
			case "YYYY": {
				AbstractDocument doc = (AbstractDocument) field.getDocument();
				doc.setDocumentFilter(new YearDocumentFilter());
				break;
			}
			default:
				System.err.println("default value in KTEXTFIELD SWTICH");
				break;
			}

		}
	};

	ActionListener confirmInputActionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			((JTextField) e.getSource()).setFocusable(false);
			((JTextField) e.getSource()).setFocusable(true);

		}
	};

}
