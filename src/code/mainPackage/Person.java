package code.mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Person implements MouseWheelListener {

	private String initialName;
	private int isInvidual;
	private LocalDate dob = null;
	private KTextField nameTextField = new KTextField(9);
	private KTextField dayTextField = new KTextField(2);
	private KTextField monthTextField = new KTextField(2);
	private KTextField yearTextField = new KTextField(4);
	private JLabel ageTextField;
	private JLabel nextBDayTextField;

	protected JButton calendar;
	public static JPanel motherPanel;
	public static JFrame motherFrame;
	private JPanel personPanel;

	private JPanel forDate;
	private JPanel forAge;
	private JPanel forNext;
	private JPanel mainAndButtons;

	public static final ImageIcon DELETE = new ImageIcon("src/resources/delete2.png");
	public static final ImageIcon SWAP = new ImageIcon("src/resources/personRelationshipSwap2.png");

	public Person(String name, String rawDate, int isIndividual) {
		this.initialName = name;
		this.isInvidual = isIndividual;
		try {
			dob = LocalDate.parse(rawDate); // reads date as string from file
		} catch (Exception e) {
			dob = null;
		}

	}

	public String getName() {
		if (nameTextField.getText().equals(nameTextField.getDefaultValue()))
			return "";
		else
			return nameTextField.getText();
	}

	public void setName(String newName) {

		if (newName.equals(""))
			nameTextField.setToDefault();
		else {
			nameTextField.setNewText(newName);
		}

	}

	public String getDate() {
		if (dob == null)
			return "";
		else
			return dob.toString();
	}

	public void setDate(LocalDate newDob) {
		dob = newDob;

		dayTextField.setNewText(getDate().substring(8, 10)); // parse and extract from date or file
		monthTextField.setNewText(getDate().substring(5, 7)); // parse and extract from date or file
		yearTextField.setNewText(getDate().substring(0, 4)); // parse and extract from date or file

		// dateTextField.setText(dob.toString());
		// instead of this, create method that will read all fields and try to parse and
		// it will update dob object, or remove object and rely solely on the fields
	}

	public void setDate(String newDob) {
		if (newDob.equals("")) {
			dayTextField.setNewText(newDob);
			monthTextField.setNewText(newDob);
			yearTextField.setNewText(newDob);
			dob = null;
			parseDateIfValid();
		}

	}

	public String getDetailedDuration() {
		if (dob == null)
			return "enter a valid date";
		long years = ChronoUnit.YEARS.between(dob, LocalDate.now());
		long months = ChronoUnit.MONTHS.between(dob, LocalDate.now());
		long days = ChronoUnit.DAYS.between(dob, LocalDate.now());

		String detailedDuration = "";

		detailedDuration += years;
		detailedDuration += (years == 1) ? " year" : " years";
		detailedDuration += "\n";
		detailedDuration += months;
		detailedDuration += (months == 1) ? " month" : " months";
		detailedDuration += "\n";
		detailedDuration += days;
		detailedDuration += (days == 1) ? " day" : " days";

		detailedDuration = detailedDuration.trim();
		if (detailedDuration.charAt(detailedDuration.length() - 1) == ',')
			detailedDuration = detailedDuration.substring(0, detailedDuration.length() - 1) + '.';
		return detailedDuration;
	}

	public String getDuration() {
		if (dob == null)
			return "enter a valid date";
		long years = ChronoUnit.YEARS.between(dob, LocalDate.now());
		long months = ChronoUnit.MONTHS.between(dob, LocalDate.now());
		long days = ChronoUnit.DAYS.between(dob, LocalDate.now());

		String duration = "";

		LocalDate tempDob = dob;

		if (years != 0) {
			duration += years;
			duration += (years == 1) ? " year, " : " years, ";

		}
		tempDob = tempDob.plusYears(years);
		months = ChronoUnit.MONTHS.between(tempDob, LocalDate.now());
		if (months != 0) {
			duration += months;
			duration += (months == 1) ? " month, " : " months, ";

		}
		tempDob = tempDob.plusMonths(months);
		days = ChronoUnit.DAYS.between(tempDob, LocalDate.now());

		if (days != 0) {
			duration += days;
			duration += (days == 1) ? " day" : " days";

			duration += ".";
		}
		tempDob = tempDob.plusDays(days);

		duration = duration.trim();
		if (duration.equals(""))
			return " Happy Birthday ";
		if (duration.charAt(duration.length() - 1) == ',')
			duration = duration.substring(0, duration.length() - 1) + '.';

		return duration;
	}

	public String getForNext() {
		if (dob == null)
			return "enter a valid date";

		LocalDate plannedAnniversary = null;

		/**
		 * If dob has been parsed successfully, it means that the date is valid. The
		 * date may be valid to be born, but not valid to have birthday on certain
		 * years. For example, you were born on 29th of February, and next year there is
		 * no 29th of February, thus, the date parsed as next anniversary is 01/03/yyyy
		 * by default.
		 */
		if (dob.getMonthValue() == 2 && dob.getDayOfMonth() == 29) {
			if (LocalDate.now().getMonthValue() == 2 && LocalDate.now().getDayOfMonth() == 29)
				if (getIsIndividual() == 1)
					return " Happy Birthday ";
				else
					return " Happy Anniversary ";
			else {
				if (LocalDate.now().isLeapYear())
					if (LocalDate.now().isBefore(LocalDate.of(LocalDate.now().getYear(), 2, 29)))
						plannedAnniversary = LocalDate.of(LocalDate.now().getYear(), 2, 29);
					else {
						if (LocalDate.now().plusYears(1).isLeapYear())
							plannedAnniversary = LocalDate.of(LocalDate.now().plusYears(1).getYear(), 2, 29);
						else {
							plannedAnniversary = LocalDate.of(LocalDate.now().plusYears(1).getYear(), 3, 1);
						}
					}
				else {
					if (LocalDate.now().isBefore(LocalDate.of(LocalDate.now().getYear(), 3, 1)))
						plannedAnniversary = LocalDate.of(LocalDate.now().getYear(), 3, 1);
					else {
						if (LocalDate.now().plusYears(1).isLeapYear())
							plannedAnniversary = LocalDate.of(LocalDate.now().plusYears(1).getYear(), 2, 29);
						else {
							plannedAnniversary = LocalDate.of(LocalDate.now().plusYears(1).getYear(), 3, 1);
						}
					}
				}
			}
		} else {
			plannedAnniversary = LocalDate.of(LocalDate.now().getYear(), dob.getMonth(), dob.getDayOfMonth());

			if (LocalDate.now().isAfter(plannedAnniversary))
				plannedAnniversary = LocalDate.of(LocalDate.now().plusYears(1).getYear(), dob.getMonth(),
						dob.getDayOfMonth());
		}

		String duration = "";
		long years = ChronoUnit.YEARS.between(plannedAnniversary, LocalDate.now());
		long months = ChronoUnit.MONTHS.between(plannedAnniversary, LocalDate.now());
		long days = ChronoUnit.DAYS.between(plannedAnniversary, LocalDate.now());
		years = Math.abs(years);
		months = Math.abs(months);
		days = Math.abs(days);

		LocalDate tempDob = LocalDate.now();

		if (years != 0) {
			duration += years;
			duration += (years == 1) ? " year, " : " years, ";
			tempDob = tempDob.plusYears(years);
			months = ChronoUnit.MONTHS.between(tempDob, plannedAnniversary);
		}
		if (months != 0) {
			duration += months;
			duration += (months == 1) ? " month, " : " months, ";
			tempDob = tempDob.plusMonths(months);
			days = ChronoUnit.DAYS.between(tempDob, plannedAnniversary);
		}
		if (days != 0) {
			duration += days;
			duration += (days == 1) ? " day" : " days";
			tempDob = tempDob.plusDays(days);
			duration += ".";
		}

		duration = duration.trim();
		if (duration.equals(""))
			if (getIsIndividual() == 1)
				return " Happy Birthday ";
			else {
				return " Happy Anniversary ";
			}

		if (duration.charAt(duration.length() - 1) == ',')
			duration = duration.substring(0, duration.length() - 1) + '.';

		return duration;

	}

	public JPanel getJPanel() {
		JLabel nameLabel = new JLabel("Name: ");
		JLabel dateLabel = null;
		JLabel ageLabel = null;
		JLabel nextBDayLabel = null;
		if (getIsIndividual() == 1) {
			dateLabel = new JLabel("Date of birth: ");
			ageLabel = new JLabel("Age: ");
			nextBDayLabel = new JLabel("Next birthday in: ");
		} else {
			dateLabel = new JLabel("Together since: ");
			ageLabel = new JLabel("Together for: ");
			nextBDayLabel = new JLabel("Next anniversary in: ");
		}

		nameTextField.setDefaultValue("[name]");
		nameTextField.getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				update();
			}

			public void changedUpdate(DocumentEvent e) {
				update();
			}

			public void update() {
				initialName = nameTextField.getText();

			}
		});

		dayTextField.setDefaultValue("DD");
		dayTextField.setOrigin(this);
		dayTextField.addMouseWheelListener(this);
		monthTextField.setDefaultValue("MM");
		monthTextField.setOrigin(this);
		monthTextField.addMouseWheelListener(this);
		yearTextField.setDefaultValue("YYYY");
		yearTextField.setOrigin(this);
		yearTextField.addMouseWheelListener(this);

		nameTextField.setNewText(initialName);
		initialName = null;
		if (!(dob == null)) {
			dayTextField.setNewText(getDate().substring(8, 10)); // parse and extract from date or file
			monthTextField.setNewText(getDate().substring(5, 7)); // parse and extract from date or file
			yearTextField.setNewText(getDate().substring(0, 4)); // parse and extract from date or file
		} else {
			dayTextField.setNewText("");
			monthTextField.setNewText("");
			yearTextField.setNewText("");
		}
		ageTextField = new JLabel(getDuration());
		// ageTextField.setEditable(false);
		ageTextField.setFocusable(false);

		nextBDayTextField = new JLabel(getForNext());
		// nextBDayTextField.setEditable(false);
		nextBDayTextField.setFocusable(false);

		JPanel sidePanel = new JPanel(new GridLayout(0, 1));
		JPanel leftLine = new JPanel(new GridLayout(0, 1));
		JPanel rightLine = new JPanel(new GridLayout(0, 1));
		JPanel combination = new JPanel(new BorderLayout());

		leftLine.add(nameLabel, BorderLayout.WEST);
		rightLine.add(nameTextField, BorderLayout.EAST);

		leftLine.add(dateLabel, BorderLayout.WEST);
		JPanel dateFields = new JPanel(new BorderLayout());
		dateFields.add(dayTextField, BorderLayout.WEST);
		dateFields.add(monthTextField, BorderLayout.EAST);
		JPanel outerDateFields = new JPanel(new BorderLayout());
		outerDateFields.add(dateFields, BorderLayout.WEST);
		outerDateFields.add(yearTextField, BorderLayout.EAST);
		JPanel panelWithButton = new JPanel(new BorderLayout());
		JButton calendar = new JButton();
		calendar.setIcon(new ImageIcon("src/resources/calendar2.png"));
		calendar.setPreferredSize(new Dimension(35, 11));
		calendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runnable MiniCalendar = new MiniCalendar(dayTextField, monthTextField, yearTextField, Person.this);
				SwingUtilities.invokeLater(MiniCalendar);

			}
		});
		panelWithButton.add(outerDateFields, BorderLayout.WEST);
		JPanel orLabel = new JPanel(new FlowLayout());
		// orLabel.setFocusable(true);
		orLabel.add(new JLabel("or")); // this causes thicker JFields here
		panelWithButton.add(orLabel, BorderLayout.CENTER);
		panelWithButton.add(calendar, BorderLayout.EAST);
		rightLine.add(panelWithButton, BorderLayout.EAST);

		leftLine.add(ageLabel, BorderLayout.WEST);
		rightLine.add(ageTextField, BorderLayout.EAST);

		leftLine.add(nextBDayLabel, BorderLayout.WEST);
		rightLine.add(nextBDayTextField, BorderLayout.EAST);

		combination.add(leftLine, BorderLayout.WEST);
		combination.add(rightLine, BorderLayout.EAST);
		sidePanel.add(combination);
		if (getIsIndividual() == 0) {
			System.out.println("one");
			JPanel withSeparator = new JPanel(new BorderLayout());
			// JSeparator line = new JSeparator(JSeparator.VERTICAL);
			// line.setPreferredSize(new Dimension(6, 0));
			// withSeparator.add(line, BorderLayout.WEST);
			withSeparator.add(sidePanel, BorderLayout.CENTER);
			withSeparator.setBorder(new TitledBorder("Relationship"));
			withSeparator.setBackground(Color.pink);
			return withSeparator;
		}
		sidePanel.setBorder(new TitledBorder("Person"));
		sidePanel.setBackground(Color.cyan);
		return sidePanel;
	}

	public void parseDateIfValid() {
		try {
			dob = LocalDate
					.parse(yearTextField.getText() + "-" + monthTextField.getText() + "-" + dayTextField.getText());
			if (dob.isAfter(LocalDate.now()))
				throw new Exception();
		} catch (Exception e1) {
			dob = null;
			System.out.println("probably february");

		} finally {
			updateAgeAndBirthday();
		}
	}

	public void updateAgeAndBirthday() {
		System.out.println("age and bd should be updated");
		ageTextField.setText(getDuration());
		nextBDayTextField.setText(getForNext());
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		KTextField field = (KTextField) e.getSource();
		if (field.getText().equals(field.getDefaultValue()) || field.getText().equals(""))
			return;
		int number = Integer.parseInt(field.getText());

		if (e.getPreciseWheelRotation() > 0) {
			if (number < 1)
				return;
			else if (field.getDefaultValue().equals("YYYY") && (number <= Year.now().getValue() - 100))
				return;

			field.setText(Integer.toString(number - 1));
			field.tryToParseData(field);

		} else {
			if (number > 31 && field.getDefaultValue().equals("DD"))
				return;
			else if (number > 12 && field.getDefaultValue().equals("MM"))
				return;
			else if (field.getDefaultValue().equals("YYYY") && number >= (Year.now().getValue()))
				return;

			field.setText(Integer.toString(number + 1));
			field.tryToParseData(field);

		}
		parseDateIfValid();
	}

	public Component getJPanel2() {
		JPanel forNameForDate = new JPanel(new BorderLayout());

		JPanel forName = new JPanel(new GridLayout());
		forName.add(nameTextField);
		forName.setBorder(new TitledBorder("Name"));
		nameTextField.setDefaultValue("[name]");
		nameTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				update();
			}

			public void changedUpdate(DocumentEvent e) {
				update();
			}

			public void update() {
				initialName = nameTextField.getText();
			}
		});
		forNameForDate.add(forName, BorderLayout.NORTH);

		JPanel forDay = new JPanel(new GridLayout());
		forDay.add(dayTextField);
		forDay.setBorder(new TitledBorder("Day"));
		dayTextField.setColumns(2);
		dayTextField.setDefaultValue("DD");
		dayTextField.setOrigin(this);
		dayTextField.addMouseWheelListener(this);

		JPanel forMonth = new JPanel(new GridLayout());
		forMonth.add(monthTextField);
		forMonth.setBorder(new TitledBorder("Month"));
		monthTextField.setColumns(3);
		monthTextField.setDefaultValue("MM");
		monthTextField.setOrigin(this);
		monthTextField.addMouseWheelListener(this);

		JPanel forYear = new JPanel(new GridLayout());
		forYear.add(yearTextField);
		forYear.setBorder(new TitledBorder("Year"));
		yearTextField.setColumns(3);
		yearTextField.setDefaultValue("YYYY");
		yearTextField.setOrigin(this);
		yearTextField.addMouseWheelListener(this);

		forDate = new JPanel(new FlowLayout(15, 0, 0));
		if (isInvidual == 1)
			forDate.setBorder(new TitledBorder("Date of birth"));
		else
			forDate.setBorder(new TitledBorder("Together since"));

		forDate.add(forDay);
		forDate.add(forMonth);
		forDate.add(forYear);
		forDate.add(new JLabel("or"));
		calendar = new JButton();
		calendar.setIcon(new ImageIcon("src/resources/calendar2.png"));
		calendar.setPreferredSize(new Dimension(35, 25));
		calendar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Runnable MiniCalendar = new MiniCalendar(dayTextField, monthTextField, yearTextField, Person.this);
				SwingUtilities.invokeLater(MiniCalendar);

			}
		});
		forDate.add(calendar);

		forNameForDate.add(forDate);

		nameTextField.setNewText(initialName);
		initialName = null;
		if (!(dob == null)) {
			dayTextField.setNewText(getDate().substring(8, 10)); // parse and extract from date or file
			monthTextField.setNewText(getDate().substring(5, 7)); // parse and extract from date or file
			yearTextField.setNewText(getDate().substring(0, 4)); // parse and extract from date or file
		} else {
			dayTextField.setNewText("");
			monthTextField.setNewText("");
			yearTextField.setNewText("");
		}

		ageTextField = new JLabel(getDuration());
		// ageTextField.setEditable(false);
		ageTextField.setFocusable(false);

		nextBDayTextField = new JLabel(getForNext());
		// nextBDayTextField.setEditable(false);
		nextBDayTextField.setFocusable(false);

		forAge = new JPanel(new GridLayout());
		forNext = new JPanel(new GridLayout());
		forAge.add(ageTextField);
		forNext.add(nextBDayTextField);

		if (isInvidual == 1)
			forAge.setBorder(new TitledBorder("Age"));
		else
			forAge.setBorder(new TitledBorder("Together for"));

		if (isInvidual == 1)
			forNext.setBorder(new TitledBorder("Next birthday in"));
		else
			forNext.setBorder(new TitledBorder("Next anniversary in"));

		JPanel forAgeForNext = new JPanel(new BorderLayout());
		forAgeForNext.add(forAge, BorderLayout.NORTH);
		forAgeForNext.add(forNext, BorderLayout.SOUTH);

		JPanel forBoth = new JPanel(new BorderLayout());
		forBoth.add(forNameForDate, BorderLayout.NORTH);
		forBoth.add(forAgeForNext, BorderLayout.SOUTH);

		JPanel personButtons = new JPanel(new FlowLayout());
		JButton deletePerson = new JButton(DELETE);
		deletePerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteThisPersonPanel();
			}
		});
		personButtons.add(deletePerson);

		JButton swapStatus = new JButton(SWAP);
		swapStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swap();
			}
		});
		personButtons.add(swapStatus);

		mainAndButtons = new JPanel(new BorderLayout());
		mainAndButtons.add(forBoth, BorderLayout.NORTH);
		mainAndButtons.add(personButtons, BorderLayout.SOUTH);

		mainAndButtons.setOpaque(true);
		if (isInvidual == 1) {
			mainAndButtons.setBorder(new TitledBorder("Person"));
			mainAndButtons.setBackground(Color.cyan);
		}

		else {
			mainAndButtons.setBorder(new TitledBorder("Relationship"));
			mainAndButtons.setBackground(Color.pink);
		}
		personPanel = mainAndButtons;
		return mainAndButtons;
	}

	protected void swap() {
		if (getIsIndividual() == 1)
			setIsIndividual(0);
		else
			setIsIndividual(1);

		if (isInvidual == 1)
			forDate.setBorder(new TitledBorder("Date of birth"));
		else
			forDate.setBorder(new TitledBorder("Together since"));

		if (isInvidual == 1)
			forAge.setBorder(new TitledBorder("Age"));
		else
			forAge.setBorder(new TitledBorder("Together for"));

		if (isInvidual == 1)
			forNext.setBorder(new TitledBorder("Next birthday in"));
		else
			forNext.setBorder(new TitledBorder("Next anniversary in"));

		if (isInvidual == 1) {
			mainAndButtons.setBorder(new TitledBorder("Person"));
			mainAndButtons.setBackground(Color.cyan);
		} else {
			mainAndButtons.setBorder(new TitledBorder("Relationship"));
			mainAndButtons.setBackground(Color.pink);
		}

	}

	protected void deleteThisPersonPanel() {
		motherPanel.remove(personPanel);
		StartWindow.persons.remove(this);
		StartWindow.adjustInside();
		motherFrame.pack();
		motherFrame.repaint();

	}

	public int getIsIndividual() {
		return isInvidual;
	}

	public void setIsIndividual(int isIndividual) {
		this.isInvidual = isIndividual;
	}

}
