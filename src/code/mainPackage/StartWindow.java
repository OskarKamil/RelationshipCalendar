package code.mainPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class StartWindow implements Runnable {
	public static final String VERSION = "0.010";
	public static final String NAME = "Relationship Calendar GP";
	public static final ImageIcon ICON = new ImageIcon("src/resources/small icon.png");
	public static final ImageIcon SAVE_ICON = new ImageIcon("src/resources/save2.png");
	public static final ImageIcon EXIT_ICON = new ImageIcon("src/resources/exit2.png");
	public static final ImageIcon CLEAR_ICON = new ImageIcon("src/resources/clear2.png");
	public static final ImageIcon ABOUT_ICON = new ImageIcon("src/resources/about2.png");
	public static final ImageIcon ON_ICON = new ImageIcon("src/resources/on2.png");
	public static final ImageIcon OFF_ICON = new ImageIcon("src/resources/off2.png");
	public static final ImageIcon ADD_PERSON = new ImageIcon("src/resources/addPerson2.png");
	public static final ImageIcon ADD_RELATIONSHIP = new ImageIcon("src/resources/addRelationship2.png");
	public static final ImageIcon DELETE = new ImageIcon("src/resources/delete2.png");
	public static ArrayList<Person> persons = new ArrayList<Person>();
	public static JPanel mainPanel;

	public static short autoSave = 0;

	public void run() {
		JFrame startWindow = new JFrame();
		startWindow.setTitle(NAME);
		startWindow.setSize(800, 300);
		startWindow.setResizable(false);
		startWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		startWindow.setIconImage(ICON.getImage());
		startWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				tryToExit(startWindow);
			}
		});

		buildMenuBar(startWindow); // builds JMenuBar
		System.out.println("here1");
		readData(); // reads data from a file
		System.out.println("here2");
//      writeData(); // writes data into a file
		readPreferences();
//		writePreferences();
		buildInside(startWindow); // creates the content, labels, text fields etc
		showVersion(startWindow); // shows version at the bottom of the frame

		startWindow.pack();
		startWindow.setLocationRelativeTo(null);
		startWindow.setVisible(true);

	}

	protected void tryToExit(JFrame startWindow) {

		int select = 0;
		if (autoSave == 1)
			select = 0;
		else {
			Object[] options = { "Save", "Don't save", "Cancel" };
			select = JOptionPane.showOptionDialog(startWindow, "Save changes?", "Save",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0);
		}

		switch (select) {
		case 0: {
			writeData();
		}
		case 1: {
			System.exit(0);
			break;
		}
		case 2: {
			JOptionPane.getRootFrame().dispose();
			break;
		}
		default:
			JOptionPane.getRootFrame().dispose();
			break;
		}
	}

	private void writePreferences() {

		PrintWriter fileOut = null;
		File source = new File("src/resources/pref.txt");

		try {
			fileOut = new PrintWriter(new FileOutputStream(source), false);

			System.out.println("Read successfully");

			fileOut.println(autoSave);
			System.out.println("Read successfully into pref.txt.");

		} catch (FileNotFoundException e) {
			System.err.println("No file found. Creating default file.");
			try {
				source.createNewFile();

			} catch (IOException e1) {
				System.err.println("Could not create. Closing the program.");
				System.exit(1);
			}

		}

		fileOut.close();

	}

	private void readPreferences() {
		Scanner fileIn = null;
		File source = new File("src/resources/pref.txt");

		if (!(source.exists()))
			try {
				source.createNewFile();
				System.out.println("Created new file.");
			} catch (IOException e2) {
				System.err.println("Could not create data file.");
			}

		try {
			fileIn = new Scanner(new FileInputStream(source));
			System.out.println("Read successfully");
			if (fileIn.hasNextLine()) {
				autoSave = Short.parseShort(fileIn.nextLine());
				System.out.println(autoSave);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not read from file");
		}

		fileIn.close();

	}

	private void readData() {

		Scanner fileIn = null;
		File source = new File("src/resources/persons.txt");

		if (!(source.exists()))
			try {
				source.createNewFile();
				System.out.println("Created new file.");
			} catch (IOException e2) {
				System.err.println("Could not create data file.");
			}

		try {
			fileIn = new Scanner(new FileInputStream(source));
			System.out.println("Read successfully");

			do {
				int isInvidual = 1;
				String name = "";
				String date = "";

				if (fileIn.hasNextLine()) {
					try {
						isInvidual = Integer.parseInt(fileIn.nextLine());
					} catch (Exception e) {
						isInvidual = 1;
					}

				}

				if (fileIn.hasNextLine()) {
					name = fileIn.nextLine();

				}

				if (fileIn.hasNextLine()) {
					date = fileIn.nextLine();

				}

				try {
					persons.add(new Person(name, date, isInvidual));
				} catch (DateTimeParseException e) {
					persons.add(new Person(name, "", 1));
				} catch (Exception e) {
					persons.add(new Person(name, "", 1));
				}

			} while (fileIn.hasNextLine());

		} catch (FileNotFoundException e) {
			System.err.println("Could not read from file");
		}

		fileIn.close();
	}

	private void writeData() {

		PrintWriter fileOut = null;
		File source = new File("src/resources/persons.txt");

		try {
			fileOut = new PrintWriter(new FileOutputStream(source), false);

			System.out.println("Read successfully");

			for (int i = 0; i < persons.size(); i++) {
				fileOut.println(persons.get(i).getIsIndividual());
				fileOut.println(persons.get(i).getName());
				fileOut.println(persons.get(i).getDate());

			}
			System.out.println("Read successfully into persons.txt.");

		} catch (FileNotFoundException e) {
			System.err.println("No file found. Creating default file.");
			try {
				source.createNewFile();

			} catch (IOException e1) {
				System.err.println("Could not create. Closing the program.");
				System.exit(1);
			}

		}

		fileOut.close();
	}

	private static void showVersion(JFrame startWindow) {
		JLabel footerVersion = new JLabel();
		footerVersion.setOpaque(false);
		footerVersion.setFont(new Font("Arial", Font.PLAIN, 14));
		// footerVersion.setFont(new Font("Ebrima", Font.PLAIN, 14));
		footerVersion.setText(" Version " + VERSION);

		JLabel footerCopyright = new JLabel();
		footerCopyright.setOpaque(false);
		footerCopyright.setFont(new Font("Arial", Font.PLAIN, 14));
		// footerCopyright.setFont(new Font("Ebrima", Font.PLAIN, 14));
		footerCopyright.setText("© 2021 Oskar Jargilo ");

		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBackground(Color.lightGray);

		footerPanel.add(footerCopyright, BorderLayout.EAST);
		footerPanel.add(footerVersion, BorderLayout.WEST);
		startWindow.add(footerPanel, BorderLayout.SOUTH);

	}

	private void buildInside(JFrame startWindow) {

		buildToolbar(startWindow);

		mainPanel = new JPanel();
		Person.motherPanel = mainPanel;
		Person.motherFrame = startWindow;
		for (int i = 0; i < persons.size(); i++) {
			mainPanel.add(persons.get(i).getJPanel2());
		}
		adjustInside();
		startWindow.add(mainPanel, BorderLayout.CENTER);
	}

	private void buildToolbar(JFrame startWindow) {
		JToolBar headerBar = new JToolBar("example");
		headerBar.setFloatable(false);
		headerBar.setRollover(true);

		JButton autosaveButton = new KButton("AutoSave is Off");
		autosaveButton.setHorizontalTextPosition(JButton.LEFT);
		autosaveButton.setPreferredSize(new Dimension(225, 22));
		if (autoSave == 1) {

			autosaveButton.setText("AutoSave is ");
			autosaveButton.setName("on");
			autosaveButton.setIcon(ON_ICON);
		} else {
			autosaveButton.setText("AutoSave is ");
			autosaveButton.setName("off");
			autosaveButton.setIcon(OFF_ICON);
		}
		autosaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (autosaveButton.getName().equals("off")) {
					autosaveButton.setText("AutoSave is ");
					autosaveButton.setName("on");
					autosaveButton.setIcon(ON_ICON);
					autoSave = 1;
				} else {
					autosaveButton.setText("AutoSave is ");
					autosaveButton.setName("off");
					autosaveButton.setIcon(OFF_ICON);
					autoSave = 0;
				}
				writePreferences();

			}
		});

		JButton saveChangesButton = new JButton(SAVE_ICON);
		saveChangesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeData();
			}
		});

		JButton cleanAllButton = new JButton(CLEAR_ICON);
		cleanAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAll(startWindow);

			}
		});

		JButton aboutButton = new JButton(ABOUT_ICON);
		aboutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayAboutFrame(startWindow);

			}
		});

		JButton addPersonButton = new JButton(ADD_PERSON);
		addPersonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				persons.add(new Person("", "", 1));
				mainPanel.add(persons.get(persons.size() - 1).getJPanel2());
				adjustInside();
				startWindow.pack();
				// startWindow.setLocationRelativeTo(null);
			}
		});

		JButton addRelationshipButton = new JButton(ADD_RELATIONSHIP);
		addRelationshipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				persons.add(new Person("", "", 0));
				mainPanel.add(persons.get(persons.size() - 1).getJPanel2());
				adjustInside();
				startWindow.pack();
				// startWindow.setLocationRelativeTo(null);
			}
		});
		headerBar.add(autosaveButton);

		headerBar.addSeparator();
		headerBar.add(saveChangesButton);
		headerBar.addSeparator();
		headerBar.add(addPersonButton);
		headerBar.add(addRelationshipButton);
		headerBar.add(cleanAllButton);

		startWindow.add(headerBar, BorderLayout.PAGE_START);

	}

	private void buildMenuBar(JFrame startWindow) {
		JMenuBar menuBar = new JMenuBar();
		JMenu mFile = new JMenu("File");
		JMenu mEdit = new JMenu("Edit");
		JMenu mHelp = new JMenu("Help");

		JMenuItem mSave = new JMenuItem("Save changes");
		mSave.setIcon(SAVE_ICON);
		mSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeData();
			}
		});

		JMenuItem mExit = new JMenuItem("Exit");
		mExit.setIcon(EXIT_ICON);
		mExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tryToExit(startWindow);
			}
		});

		JMenuItem mAbout = new JMenuItem("About");
		mAbout.setIcon(ABOUT_ICON);
		mAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayAboutFrame(startWindow);
			}
		});

		JMenuItem mClearAll = new JMenuItem("Clear all");
		mClearAll.setIcon(CLEAR_ICON);
		mClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAll(startWindow);

			}
		});

		mFile.add(mSave);
		mFile.addSeparator();
		mFile.add(mExit);
		mEdit.add(mClearAll);
		mHelp.add(mAbout);

		menuBar.add(mFile);
		menuBar.add(mEdit);
		menuBar.add(mHelp);

		startWindow.setJMenuBar(menuBar);

	}

	public void displayAboutFrame(JFrame startWindow) {
		JDialog aboutWindow = new JDialog();
		aboutWindow.setTitle("About");
		aboutWindow.setSize(250, 130);
		aboutWindow.setResizable(false);
		aboutWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		aboutWindow.setModal(true);

		aboutWindow.setIconImage(ICON.getImage());

		String text = "<html>" + NAME + "<br>© 2021 Oskar Jargilo" + "<br>Email: *coming soon*" + "<br><br>Version: "
				+ VERSION + "</html>";

		JLabel label = new JLabel(text);
		label.setFont(new Font("Ebrima", Font.PLAIN, 14));
		label.setIcon(ICON);
		label.setHorizontalTextPosition(JLabel.RIGHT);
		label.setVerticalTextPosition(JLabel.TOP);
		label.setIconTextGap(20);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel textPanel = new JPanel();
		textPanel.add(label);

		mainPanel.add(textPanel, BorderLayout.CENTER);

		KButton closeButton = new KButton("OK");
		closeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				aboutWindow.dispose();

			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(closeButton);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		aboutWindow.add(mainPanel);

		aboutWindow.pack();
		aboutWindow.setSize(aboutWindow.getWidth() + 50, aboutWindow.getHeight() + 0);
		aboutWindow.setLocationRelativeTo(startWindow);
		aboutWindow.setVisible(true);

	}

	public void clearAll(JFrame startWindow) {
		int select = 0;

		Object[] options = { "Clear all", "Don't clear / Cancel" };
		select = JOptionPane.showOptionDialog(startWindow, "Clear all fields?", "Clear all",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0);

		switch (select) {
		case 0: {
			for (int i = 0; i < persons.size(); i++) {
				persons.get(i).setName("");
				persons.get(i).setDate("");
			}
		}
		case 1: {
			JOptionPane.getRootFrame().dispose();
			break;
		}
		default:
			JOptionPane.getRootFrame().dispose();
			break;
		}
	}

	public static boolean isDateValid(String dateString, char c) {
		DateFormat formatter = null;
		if (c == '-')
			formatter = new SimpleDateFormat("dd-MM-yyyy");
		else
			formatter = new SimpleDateFormat("dd/MM/yyyy");
		formatter.setLenient(false);
		try {
			formatter.parse(dateString);
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate date = LocalDate.parse(dateString, formatter1);
			if (LocalDate.now().isBefore(date))
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static void adjustInside() {
		if (mainPanel.getComponentCount() <= 5) {
			mainPanel.setLayout(new FlowLayout(1,0,0));
		
		} else if (mainPanel.getComponentCount() > 5)
			mainPanel.setLayout(new GridLayout(0, 5));
	}

	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			System.out.println("worked");
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		StartWindow window = new StartWindow();
		SwingUtilities.invokeLater(window);

	}
}
