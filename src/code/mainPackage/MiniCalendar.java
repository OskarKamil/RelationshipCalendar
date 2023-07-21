package code.mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class MiniCalendar implements Runnable, ActionListener {

	public static final ImageIcon SMALL_CALENDAR_ICON = new ImageIcon(IconManager.SMALL_CALENDAR_ICON);
	private int day;
	private int month;
	private int year;
	protected JLabel inputLabel;
	JButton saveButton;
	JButton cancelButton;
	KTextField dayField;
	KTextField monthField;
	KTextField yearField;
	Person source;

	public MiniCalendar(KTextField newDayTextField, KTextField newMonthTextField, KTextField newYearTextField,
			Person person) {
		dayField = newDayTextField;
		monthField = newMonthTextField;
		yearField = newYearTextField;
		source = person;
		System.out.println("test fields should be assigned");
	}

//	public MiniCalendar() {
//		System.out.println("empty constructor ");
//	}

	public void run() {
		JDialog miniCalendar = new JDialog();
		miniCalendar.setTitle("Pick a date");
		miniCalendar.setSize(300, 300);
		miniCalendar.setResizable(false);
		miniCalendar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		miniCalendar.setIconImage(SMALL_CALENDAR_ICON.getImage());
		miniCalendar.setModal(true);
		day = 0;
		month = 0;
		year = 0;

		JPanel currentInput = new JPanel();
		inputLabel = new JLabel("Chosen date: DD/MM/YYYY");
		inputLabel.setFont(new Font("Consolas", Font.BOLD, 26));
		currentInput.add(inputLabel);

		JPanel confirmCancel = new JPanel();
		saveButton = new JButton("Confirm date");
		saveButton.setEnabled(false);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (day < 10)
					dayField.setNewText("0" + Integer.toString(day));
				else
					dayField.setNewText(Integer.toString(day));
				if (month < 10)
					monthField.setNewText("0" + Integer.toString(month));
				else
					monthField.setNewText(Integer.toString(month));
				yearField.setNewText(Integer.toString(year));

				source.parseDateIfValid();
				source.updateAgeAndBirthday();
				miniCalendar.dispose();
			}
		});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				miniCalendar.dispose();

			}
		});
		confirmCancel.add(saveButton);
		confirmCancel.add(cancelButton);

		JPanel inputButtonConfirm = new JPanel(new BorderLayout());
		inputButtonConfirm.add(currentInput, BorderLayout.NORTH);
		inputButtonConfirm.add(confirmCancel, BorderLayout.SOUTH);

		JPanel dateButtons = new JPanel(new GridLayout(0, 2));
		JPanel leftJPanel = new JPanel(new GridLayout(0, 1));
		JPanel rightJPanel = new JPanel(new GridLayout(0, 1));

		JPanel days = new JPanel(new BorderLayout());
		JPanel daysPanel = new JPanel();
		daysPanel.setLayout(new GridLayout(0, 7));
		for (int i = 0; i < 31; i++) {
			JButton tempButton = new JButton(Integer.toString(i + 1));
			tempButton.setActionCommand("day");
			tempButton.setMargin(new Insets(0, 0, 0, 0));
			tempButton.addActionListener(this);
			daysPanel.add(tempButton);
		}
		days.add(daysPanel, BorderLayout.CENTER);
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Day of the month");
		days.setBorder(title);
		leftJPanel.add(days);

		JPanel months = new JPanel(new BorderLayout());
		JPanel monthsPanel = new JPanel();
		monthsPanel.setLayout(new GridLayout(0, 6));
		JButton[] monthButtons = { new JButton("January"), new JButton("February"), new JButton("March"),
				new JButton("April"), new JButton("May"), new JButton("June"), new JButton("July"),
				new JButton("August"), new JButton("September"), new JButton("October"), new JButton("November"),
				new JButton("December") };
		for (JButton button : monthButtons) {
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setActionCommand("month");
			button.addActionListener(this);
			monthsPanel.add(button);
		}

		months.add(monthsPanel, BorderLayout.CENTER);
		title = BorderFactory.createTitledBorder("Month of the year");
		months.setBorder(title);
		leftJPanel.add(months);

		JPanel years = new JPanel(new BorderLayout());
		JPanel yearsPanel = new JPanel();
		yearsPanel.setLayout(new GridLayout(0, 10));
		for (int i = Calendar.getInstance().get(Calendar.YEAR) - 100; i < Calendar.getInstance()
				.get(Calendar.YEAR); i++) {
			JButton tempButton = new JButton(Integer.toString(i + 1));
			tempButton.setMargin(new Insets(0, 0, 0, 0));
			tempButton.setActionCommand("year");
			tempButton.addActionListener(this);
			yearsPanel.add(tempButton);
		}
		years.add(yearsPanel, BorderLayout.CENTER);
		title = BorderFactory.createTitledBorder("Year");
		years.setBorder(title);
		rightJPanel.add(years);
		dateButtons.add(leftJPanel);
		dateButtons.add(rightJPanel);
		inputButtonConfirm.add(dateButtons, BorderLayout.CENTER);
		miniCalendar.add(inputButtonConfirm);

		miniCalendar.pack();
		miniCalendar.setLocationRelativeTo(source.calendar);
		miniCalendar.setLocation(miniCalendar.getX()+miniCalendar.getWidth()/2, miniCalendar.getY()+miniCalendar.getHeight()/2);
		miniCalendar.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		JButton sourceButton = (JButton) e.getSource();
		switch (sourceButton.getActionCommand()) {
		case "day": {
			day = Integer.parseInt(sourceButton.getText());
			updateInputLabel();
			break;
		}
		case "month": {
			switch (sourceButton.getText().toLowerCase()) {
			case "january":
				month = 1;
				break;
			case "february":
				month = 2;
				break;
			case "march":
				month = 3;
				break;
			case "april":
				month = 4;
				break;
			case "may":
				month = 5;
				break;
			case "june":
				month = 6;
				break;
			case "july":
				month = 7;
				break;
			case "august":
				month = 8;
				break;
			case "september":
				month = 9;
				break;
			case "october":
				month = 10;
				break;
			case "november":
				month = 11;
				break;
			case "december":
				month = 12;
				break;

			}
			updateInputLabel();
			break;
		}
		case "year": {
			year = Integer.parseInt(sourceButton.getText());
			updateInputLabel();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + e.getActionCommand());
		}

	}

	public void updateInputLabel() {
		String inputString = "";
		if (day == 0)
			inputString += "DD/";
		else if (day < 10)
			inputString += "0" + day + "/";
		else
			inputString += day + "/";

		if (month == 0)
			inputString += "MM/";
		else if (month < 10)
			inputString += "0" + month + "/";
		else
			inputString += month + "/";

		if (year == 0)
			inputString += "YYYY";
		else
			inputString += year + "";

		inputLabel.setText("Chosen date: " + inputString);

		if (year == 0 || month == 0 || day == 0)
			return;
		else {
			if (StartWindow.isDateValid(inputString, '/')) {
				inputLabel.setForeground(Color.black);
				saveButton.setEnabled(true);
			} else {
				inputLabel.setForeground(Color.red);
				saveButton.setEnabled(false);
			}

		}

	}
}
