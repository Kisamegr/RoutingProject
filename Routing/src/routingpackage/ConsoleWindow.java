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
import javax.swing.text.AttributeSet;
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

		public Console() {
			super();

			this.setFont(new Font("Consolas", Font.PLAIN, 15));
			this.setBackground(Color.getHSBColor(129, 0, .2f));

		}

		public void appendToConsole(final String message, final Color color) {

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

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

	public ConsoleWindow(String name) {

		console = new Console();
		m_console = console;
		window = this;

		running = false;
		initialize();
		// System.out.println(Thread.currentThread().getName());

		frame.setTitle(name);
		frame.setVisible(true);
	}

	private void startEmulation() {

		String filepath = System.getProperty("user.dir") + File.separatorChar + "stats.txt";
		String out;
		System.out.println(filepath);
		cpu = new CPU();
		if (cStatistics.isSelected())
			stats = new Statistics(filepath);
		sjfScheduler = new SJFScheduler(cPreemptive.isSelected(), cpu, stats);

		if (cOutput.isSelected())
			out = "output.txt";
		else
			out = null;

		generator = new ProcessGenerator(out, false, sjfScheduler);

		int clockSpeed = 1000 / sClock.getValue();

		if (sClock.getValue() == sClock.getMaximum()) {
			clockSpeed = 0;
			System.out.println("asdasdastsqgfevevviiioiulopflf;lp");
		}

		clock = new Clock(clockSpeed, generator, sjfScheduler, cpu);

		clock.startClock();

		bStart.setText("Stop");
		running = true;

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
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		verticalStrut = Box.createVerticalStrut(20);
		panel_1.add(verticalStrut, BorderLayout.NORTH);

		sClock = new JSlider();
		sClock.setPaintTicks(true);
		panel_1.add(sClock, BorderLayout.SOUTH);
		sClock.setOrientation(SwingConstants.VERTICAL);
		sClock.setMinimum(1);
		sClock.setMinimumSize(new Dimension(30, 23));
		sClock.setPaintLabels(true);
		sClock.setPreferredSize(new Dimension(45, 160));
		sClock.setMaximumSize(new Dimension(25, 34));
		sClock.setMinorTickSpacing(1);
		sClock.setSnapToTicks(true);

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

		frame.setVisible(true);

		// console.appendInfoMessage("Done initializing the main window.");

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
