package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import emulator.CPU;
import emulator.Clock;
import emulator.ProcessGenerator;
import emulator.SJFScheduler;
import emulator.Statistics;

public class Window {

	// Window variables
	private JFrame frame;
	private Console console;
	private static Console m_console;
	private static Window window;
	private JPanel panel;
	private JButton bStart;
	private JPanel options;
	private JCheckBox cPreemptive;
	private JCheckBox cStatistics;
	private JTextField tInput;
	private JPanel main_options;
	private JSlider sClock;
	private JPanel buttons;
	private Component verticalStrut_3;
	private JLabel lblClockSpeed;
	private String inputPath;
	private GenOptions genOptionsPanel;

	// Emulator variables
	private CPU cpu;
	private Statistics stats;
	private SJFScheduler sjfScheduler;
	private ProcessGenerator generator;
	private Clock clock;
	private boolean running;
	private JButton btnNewButton;
	private int clockMultiplier;
	private JRadioButton rOutput;
	private JRadioButton rInput;
	private JButton bGenOptions;
	private JLabel lblNewLabel;
	private JPanel panel_5;
	private Component verticalStrut;
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	private Component horizontalStrut_2;
	private Component horizontalStrut_3;
	private JPanel gen_options;
	private Component horizontalStrut_4;
	private Component horizontalStrut_5;

	public Window(String name) {

		console = new Console();
		m_console = console;
		window = this;

		running = false;
		initialize();
		tInput.setText("");

		sClock = new JSlider();
		options.add(sClock, BorderLayout.SOUTH);
		sClock.setPaintTicks(true);
		sClock.setPaintLabels(true);
		sClock.setFont(new Font("Tahoma", Font.BOLD, 12));
		sClock.setForeground(Color.WHITE);
		sClock.setBackground(Color.DARK_GRAY);
		sClock.setBorder(new MatteBorder(5, 0, 0, 0, new Color(128, 128, 128)));
		sClock.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
		sClock.setPreferredSize(new Dimension(45, 170));
		sClock.setMaximumSize(new Dimension(25, 34));
		sClock.setMinorTickSpacing(25);
		sClock.setMajorTickSpacing(250);
		inputPath = "";
		// System.out.println(Thread.currentThread().getName());

		frame.setTitle(name);
		frame.setVisible(true);

		clockMultiplier = 1000;
		genOptionsPanel = new GenOptions();
	}

	private void startEmulation() {

		console.resetLogger();
		String filepath = System.getProperty("user.dir") + File.separatorChar + "stats.txt";
		String out;
		System.out.println(filepath);
		cpu = new CPU();
		if (cStatistics.isSelected())
			stats = new Statistics(filepath);
		sjfScheduler = new SJFScheduler(cPreemptive.isSelected(), cpu, stats);

		/*
		 * if (cOutput.isSelected()) out = "output.txt"; else out = null;
		 */

		if (rInput.isSelected())
			generator = new ProcessGenerator(inputPath, sjfScheduler);
		else
			generator = new ProcessGenerator(genOptionsPanel.getGenerationFreq(), genOptionsPanel.getGenerationMax(), genOptionsPanel.getMaxBurst(), genOptionsPanel.getMinBurst(), sjfScheduler);

		// generator = new ProcessGenerator(out, false, sjfScheduler);

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
		frame.setResizable(false);
		frame.setMinimumSize(new Dimension(900, 500));
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBackground(Color.BLACK);
		frame.setBounds(100, 100, 900, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.DARK_GRAY);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		scrollPane.setViewportView(console);

		panel = new JPanel();
		panel.setForeground(Color.WHITE);
		panel.setBackground(Color.DARK_GRAY);
		panel.setBorder(null);
		frame.getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));

		buttons = new JPanel();
		buttons.setForeground(Color.WHITE);
		buttons.setBackground(Color.DARK_GRAY);
		buttons.setBorder(new MatteBorder(0, 5, 5, 5, Color.GRAY));
		panel.add(buttons, BorderLayout.SOUTH);
		buttons.setLayout(new BorderLayout(0, 0));

		bStart = new JButton("START");
		bStart.setBorder(null);
		bStart.setForeground(Color.DARK_GRAY);
		bStart.setBackground(Color.DARK_GRAY);
		bStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!running)
					startEmulation();
				else
					stopEmulation();

			}
		});

		horizontalStrut_1 = Box.createHorizontalStrut(5);
		horizontalStrut_1.setForeground(Color.WHITE);
		horizontalStrut_1.setBackground(Color.DARK_GRAY);
		buttons.add(horizontalStrut_1, BorderLayout.EAST);

		horizontalStrut = Box.createHorizontalStrut(5);
		horizontalStrut.setForeground(Color.WHITE);
		horizontalStrut.setBackground(Color.DARK_GRAY);
		buttons.add(horizontalStrut, BorderLayout.WEST);
		bStart.setFont(new Font("Tahoma", Font.BOLD, 20));
		bStart.setPreferredSize(new Dimension(57, 38));
		buttons.add(bStart);

		verticalStrut_3 = Box.createVerticalStrut(5);
		verticalStrut_3.setForeground(Color.WHITE);
		verticalStrut_3.setBackground(Color.DARK_GRAY);
		buttons.add(verticalStrut_3, BorderLayout.SOUTH);

		panel_5 = new JPanel();
		panel_5.setForeground(Color.WHITE);
		panel_5.setBackground(Color.DARK_GRAY);
		buttons.add(panel_5, BorderLayout.NORTH);
		panel_5.setLayout(new BorderLayout(0, 0));

		btnNewButton = new JButton("Clear");
		btnNewButton.setForeground(Color.DARK_GRAY);
		btnNewButton.setBackground(Color.DARK_GRAY);
		panel_5.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearConsole();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));

		horizontalStrut_2 = Box.createHorizontalStrut(5);
		horizontalStrut_2.setForeground(Color.WHITE);
		horizontalStrut_2.setBackground(Color.DARK_GRAY);
		panel_5.add(horizontalStrut_2, BorderLayout.WEST);

		horizontalStrut_3 = Box.createHorizontalStrut(5);
		horizontalStrut_3.setForeground(Color.WHITE);
		horizontalStrut_3.setBackground(Color.DARK_GRAY);
		panel_5.add(horizontalStrut_3, BorderLayout.EAST);

		verticalStrut = Box.createVerticalStrut(5);
		verticalStrut.setForeground(Color.WHITE);
		verticalStrut.setBackground(Color.DARK_GRAY);
		panel_5.add(verticalStrut, BorderLayout.NORTH);

		options = new JPanel();
		options.setForeground(Color.WHITE);
		options.setBackground(Color.DARK_GRAY);
		options.setBorder(new MatteBorder(6, 5, 5, 5, Color.GRAY));
		panel.add(options, BorderLayout.CENTER);
		options.setLayout(new BorderLayout(0, 0));

		lblNewLabel = new JLabel(" OPTIONS ");
		lblNewLabel.setBorder(new MatteBorder(0, 0, 2, 0, Color.GRAY));
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setBackground(Color.DARK_GRAY);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		options.add(lblNewLabel, BorderLayout.NORTH);

		main_options = new JPanel();
		main_options.setForeground(Color.WHITE);
		main_options.setBackground(Color.DARK_GRAY);
		main_options.setBorder(null);
		options.add(main_options, BorderLayout.CENTER);
		main_options.setLayout(new GridLayout(7, 0, 0, 0));

		cPreemptive = new JCheckBox("Pre-Emptive");
		cPreemptive.setFont(new Font("Tahoma", Font.BOLD, 11));
		cPreemptive.setForeground(Color.WHITE);
		cPreemptive.setBackground(Color.DARK_GRAY);
		cPreemptive.setSelected(true);
		cPreemptive.setHorizontalAlignment(SwingConstants.CENTER);
		main_options.add(cPreemptive);

		cStatistics = new JCheckBox("Statistics    ");
		cStatistics.setBorder(new MatteBorder(0, 0, 3, 0, new Color(128, 128, 128)));
		cStatistics.setFont(new Font("Tahoma", Font.BOLD, 11));
		cStatistics.setForeground(Color.WHITE);
		cStatistics.setBackground(Color.DARK_GRAY);
		cStatistics.setSelected(true);
		cStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		main_options.add(cStatistics);

		gen_options = new JPanel();
		gen_options.setBackground(Color.DARK_GRAY);
		main_options.add(gen_options);
		gen_options.setLayout(new BorderLayout(0, 0));

		bGenOptions = new JButton("Gen. Options");
		bGenOptions.setBorder(null);
		bGenOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// JOptionPane.showConfirmDialog(null, genOptionsPanel, "Generator Options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				genOptionsPanel.setLocationRelativeTo(frame);
				genOptionsPanel.setVisible(true);
			}
		});
		gen_options.add(bGenOptions);
		bGenOptions.setFont(new Font("Tahoma", Font.BOLD, 11));
		bGenOptions.setForeground(Color.DARK_GRAY);
		bGenOptions.setBackground(Color.GRAY);

		horizontalStrut_4 = Box.createHorizontalStrut(5);
		gen_options.add(horizontalStrut_4, BorderLayout.WEST);

		horizontalStrut_5 = Box.createHorizontalStrut(5);
		gen_options.add(horizontalStrut_5, BorderLayout.EAST);

		rOutput = new JRadioButton("Output Processes");
		rOutput.setForeground(Color.WHITE);
		rOutput.setBackground(Color.DARK_GRAY);
		rOutput.setSelected(true);
		main_options.add(rOutput);
		rOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tInput.setText("");
				inputPath = "";
			}
		});

		rInput = new JRadioButton(" Input Processes");
		rInput.setForeground(Color.WHITE);
		rInput.setBackground(Color.DARK_GRAY);
		main_options.add(rInput);
		rInput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					tInput.setText(fileChooser.getSelectedFile().getName());
					inputPath = fileChooser.getSelectedFile().getPath();
				} else {
					rOutput.setSelected(true);
				}

			}
		});

		tInput = new JTextField();
		tInput.setBorder(new MatteBorder(2, 0, 2, 0, new Color(128, 128, 128)));
		tInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tInput.setForeground(Color.LIGHT_GRAY);
		tInput.setBackground(Color.DARK_GRAY);
		tInput.setHorizontalAlignment(SwingConstants.CENTER);
		tInput.setEditable(false);
		main_options.add(tInput);
		tInput.setColumns(10);

		lblClockSpeed = new JLabel("Clock Speed");
		lblClockSpeed.setForeground(Color.WHITE);
		lblClockSpeed.setBackground(Color.DARK_GRAY);
		lblClockSpeed.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblClockSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		main_options.add(lblClockSpeed);

		frame.setVisible(true);

		// console.appendInfoMessage("Done initializing the main window.");

		ButtonGroup group = new ButtonGroup();
		group.add(rInput);
		group.add(rOutput);
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

	public static Window getWindow() {
		return window;
	}

}
