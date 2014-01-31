package routingpackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ConsoleWindow {

	public class Console extends JTextPane {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private DateFormat dateFormat;
		private int maxLines;

		public Console() {
			super();

			this.setFont(new Font("Consolas", Font.PLAIN, 15));
			this.setBackground(Color.getHSBColor(129, 0, .2f));

			maxLines = 500;

			this.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {

					final DocumentEvent event = arg0;
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Document document = event.getDocument();
							javax.swing.text.Element root = document.getDefaultRootElement();

							while (root.getElementCount() > maxLines) {

								int end = root.getElement(0).getEndOffset();

								try {
									document.remove(0, end);
								} catch (BadLocationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

						}
					});

				}

				@Override
				public void changedUpdate(DocumentEvent arg0) {
					// TODO Auto-generated method stub

				}
			});

		}

		public void appendToConsole(final String message, final Color color) {

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					long start = System.currentTimeMillis();
					// console.setText(console.getText() + "[" +
					// dateFormat.format(Calendar.getInstance().getTime()) +
					// "]  ");

					console.setEditable(true);
					StyleContext sc = StyleContext.getDefaultStyleContext();
					AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

					aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
					aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

					int len = console.getDocument().getLength();
					console.setCaretPosition(len);
					console.setCharacterAttributes(aset, false);
					console.replaceSelection(message + "\n");

					console.setEditable(false);

					System.out.println(System.currentTimeMillis() - start);

					// if (console.getDocument().getLength() > 6000)
					// console.setText("");

				}

			});

		}

		public void appendCpuMessage(String message) {
			this.appendToConsole(message, Color.WHITE);
		}

		public void appendSjfMessage(String message) {
			this.appendToConsole(message, Color.CYAN);
		}

		public void appendClockMessage(String message) {
			this.appendToConsole(message, Color.getHSBColor(0, 0.2f, 0.9f));
		}

		public void appendReadyQueueMessage(String message) {
			this.appendToConsole(message, Color.getHSBColor(1f, 0.4f, 0.1f));
		}

		public void appendExecuteMessage(String message) {
			this.appendToConsole(message, Color.getHSBColor(0.6f, 0.2f, 0.9f));
		}

		public void appendNewListMessage(String message) {
			this.appendToConsole(message, Color.RED);
		}

	}

	// Window variables
	private JFrame frame;
	private Console console;
	private static Console m_console;
	private static ConsoleWindow window;
	private JPanel panel;
	private JButton bStart;
	private JPanel panel_1;
	private JCheckBox cPreemptive;
	private Component verticalStrut;
	private JCheckBox cStatistics;
	private JTextField tInput;
	private JPanel panel_2;
	private JButton bInput;
	private Component verticalStrut_1;
	private JSlider sClock;
	private Component verticalStrut_2;
	private JPanel panel_3;
	private Component verticalStrut_3;
	private JLabel lblClockSpeed;

	// Emulator variables
	private CPU cpu;
	private Statistics stats;
	private SJFScheduler sjfScheduler;
	private ProcessGenerator generator;
	private Clock clock;
	private JCheckBox cOutput;
	private boolean running;
	private JButton btnNewButton;
	private int clockMultiplier;
	private JPanel panel_4;
	private Component verticalStrut_4;

	public ConsoleWindow(String name) {

		console = new Console();
		m_console = console;
		window = this;

		running = false;
		initialize();
		// System.out.println(Thread.currentThread().getName());

		frame.setTitle(name);
		frame.setVisible(true);

		clockMultiplier = 1000;
	}

	private void startEmulation() {

		String filepath = System.getProperty("user.dir") + File.separatorChar + "stats.txt";
		String out;
		System.out.println(filepath);
		cpu = new CPU();
		if (cStatistics.isSelected())
			stats = new Statistics(filepath);
		sjfScheduler = new SJFScheduler(cPreemptive.isSelected(), cpu, stats);

		/*if (cOutput.isSelected())
			out = "output.txt";
		else
			out = null;*/
		
		if(cOutput.isSelected())
			generator = new ProcessGenerator("output.txt", false, sjfScheduler);
		else
			generator = new ProcessGenerator("output.txt", true, sjfScheduler);

		
		//generator = new ProcessGenerator(out, false, sjfScheduler);

		int clockMillis = getClockMillis();
		clock = new Clock(clockMillis, generator, sjfScheduler, cpu);

		clock.startClock();

		bStart.setText("Stop");
		running = true;

		if (clockMillis == 1000)
			clock.pauseClock(true);

	}

	public void stopEmulation() {

		if (clock.isRunning())
			clock.stopClock();
		bStart.setText("Start");
		running = false;

	}

	private void clearConsole() {
		console.setText("");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("SJF Emulator");
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBackground(Color.BLACK);
		frame.setBounds(100, 100, 897, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		scrollPane.setViewportView(console);

		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));

		panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		bStart = new JButton("START");
		bStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!running)
					startEmulation();
				else
					stopEmulation();

			}
		});
		bStart.setFont(new Font("Tahoma", Font.PLAIN, 18));
		bStart.setPreferredSize(new Dimension(57, 38));
		panel_3.add(bStart);

		verticalStrut_3 = Box.createVerticalStrut(5);
		panel_3.add(verticalStrut_3, BorderLayout.SOUTH);

		btnNewButton = new JButton("Clear");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearConsole();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel_3.add(btnNewButton, BorderLayout.NORTH);

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		verticalStrut = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut, BorderLayout.NORTH);

		panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(8, 0, 0, 0));

		cPreemptive = new JCheckBox("Pre-Emptive");
		cPreemptive.setSelected(true);
		cPreemptive.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(cPreemptive);

		cStatistics = new JCheckBox("Statistics    ");
		cStatistics.setSelected(true);
		cStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(cStatistics);

		cOutput = new JCheckBox("Output       ");
		cOutput.setSelected(true);
		cOutput.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(cOutput);

		verticalStrut_1 = Box.createVerticalStrut(12);
		panel_2.add(verticalStrut_1);

		tInput = new JTextField();
		tInput.setEditable(false);
		panel_2.add(tInput);
		tInput.setColumns(10);

		bInput = new JButton("Input File");
		bInput.setPreferredSize(new Dimension(77, 26));
		panel_2.add(bInput);

		verticalStrut_2 = Box.createVerticalStrut(20);
		panel_2.add(verticalStrut_2);

		lblClockSpeed = new JLabel("Clock Speed");
		lblClockSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblClockSpeed);

		panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));

		sClock = new JSlider();
		panel_4.add(sClock);
		sClock.setPaintTicks(true);
		sClock.setValue(500);
		sClock.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {

				if (running) {
					clock.changeMilliseconds(getClockMillis());
				}

			}
		});
		sClock.setOrientation(SwingConstants.VERTICAL);
		sClock.setMinimum(0);
		sClock.setMaximum(1000);
		sClock.setMinimumSize(new Dimension(30, 23));
		sClock.setPaintLabels(true);
		sClock.setPreferredSize(new Dimension(45, 160));
		sClock.setMaximumSize(new Dimension(25, 34));
		sClock.setMinorTickSpacing(10);
		sClock.setMajorTickSpacing(200);
		sClock.setSnapToTicks(true);

		verticalStrut_4 = Box.createVerticalStrut(15);
		panel_4.add(verticalStrut_4, BorderLayout.SOUTH);

		frame.setVisible(true);

		// console.appendInfoMessage("Done initializing the main window.");

	}

	private int getClockMillis() {

		int clockMillis = 0;

		if (running && sClock.getValue() == 0) {
			clock.pauseClock(true);
		} else {
			if (running && clock.isPaused())
				clock.pauseClock(false);

			clockMillis = 500 - sClock.getValue() / 2;
		}
		return clockMillis;
	}

	public static Console getConsole() {
		synchronized (m_console) {
			return m_console;

		}
	}

	public static ConsoleWindow getWindow() {
		return window;
	}

}
