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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;

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
	private JPanel options_panel;
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
	private DefaultTableCellRenderer centerRenderer;
	private ProcessModel cpuModel;
	private ProcessModel rdyModel;
	private ProcessModel newModel;
	private Music juke; // Music player
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
	private JPanel table_panel;
	private JPanel tables;
	private JScrollPane cpuScroll;
	private JTable cpuTable;
	private JScrollPane rdyScroll;
	private JTable rdyTable;
	private JScrollPane newScroll;
	private JTable newTable;
	private JLabel lblGrids;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JLabel lblCpu;
	private JLabel lblReadyList;
	private JLabel lblNewList;
	private JPanel panel_6;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JPanel panel_8;
	private JLabel lblNewLabel_5;
	private JPanel panel_7;
	private JPanel stats_panel;
	private JLabel lAvgWait;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;
	private JLabel lAvgRes;
	private JPanel clock_panel;
	private JCheckBox cAudio;
	private JLabel label;

	public Window(String name) {

		juke = new Music(); // Music

		console = new Console();
		m_console = console;
		window = this;

		running = false;

		initialize();
		tInput.setText("");

		label = new JLabel("");
		main_options.add(label);

		cAudio = new JCheckBox("Music");
		cAudio.setSelected(true);
		cAudio.setBorderPainted(true);
		cAudio.setBackground(Color.DARK_GRAY);
		cAudio.setBorder(new MatteBorder(0, 0, 2, 0, new Color(128, 128, 128)));
		cAudio.setFont(new Font("Tahoma", Font.BOLD, 12));
		cAudio.setForeground(new Color(100, 149, 237));
		cAudio.setHorizontalAlignment(SwingConstants.CENTER);
		main_options.add(cAudio);

		clock_panel = new JPanel();
		clock_panel.setBackground(Color.DARK_GRAY);
		options.add(clock_panel, BorderLayout.SOUTH);
		clock_panel.setLayout(new BorderLayout(0, 0));

		sClock = new JSlider();
		clock_panel.add(sClock);
		sClock.setPaintTicks(true);
		sClock.setPaintLabels(true);
		sClock.setFont(new Font("Tahoma", Font.BOLD, 12));
		sClock.setForeground(Color.WHITE);
		sClock.setBackground(Color.DARK_GRAY);
		sClock.setBorder(null);
		sClock.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		sClock.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {

				if (running)
					clock.changeMilliseconds(getClockMillisFromSlider());
			}
		});
		sClock.setOrientation(SwingConstants.VERTICAL);
		sClock.setMinimum(0);
		sClock.setMaximum(1000);
		sClock.setMinimumSize(new Dimension(30, 23));
		sClock.setPreferredSize(new Dimension(45, 245));
		sClock.setMaximumSize(new Dimension(25, 34));
		sClock.setMinorTickSpacing(25);
		sClock.setMajorTickSpacing(250);
		sClock.setValue(850);

		lblClockSpeed = new JLabel("Clock Speed");
		lblClockSpeed.setBorder(new MatteBorder(0, 0, 2, 0, new Color(128, 128, 128)));
		clock_panel.add(lblClockSpeed, BorderLayout.NORTH);
		lblClockSpeed.setForeground(new Color(245, 245, 245));
		lblClockSpeed.setBackground(Color.DARK_GRAY);
		lblClockSpeed.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblClockSpeed.setHorizontalAlignment(SwingConstants.CENTER);

		inputPath = "";

		frame.setTitle(name);
		frame.setVisible(true);

		clockMultiplier = 1000;
		genOptionsPanel = new GenOptions();
	}

	private void startEmulation() {

		console.resetLogger();

		cpu = new CPU();
		if (cStatistics.isSelected() && cPreemptive.isSelected()) {
			String filepath = System.getProperty("user.dir") + File.separatorChar + "stats-preemptive.txt";
			stats = new Statistics(filepath);
		} else if (cStatistics.isSelected() && !cPreemptive.isSelected()) {
			String filepath = System.getProperty("user.dir") + File.separatorChar + "stats-non-preemptive.txt";
			stats = new Statistics(filepath);
		}

		sjfScheduler = new SJFScheduler(cPreemptive.isSelected(), cpu, stats);

		if (rInput.isSelected())
			generator = new ProcessGenerator(inputPath, sjfScheduler);
		else
			generator = new ProcessGenerator(genOptionsPanel.getGenerationFreq(), genOptionsPanel.getGenerationMax(), genOptionsPanel.getMaxBurst(), genOptionsPanel.getMinBurst(), genOptionsPanel.getMaxGenProc(), genOptionsPanel.getMinGenProc(), sjfScheduler);

		int clockMillis = getClockMillisFromSlider();
		clock = new Clock(clockMillis, generator, sjfScheduler, cpu);

		clock.startClock();

		bStart.setText("Stop");
		running = true;

		if (clockMillis == 1000)
			clock.pauseClock(true);

		if (cAudio.isSelected()) {
			juke.playmusic();
			cAudio.setEnabled(false);
		}

		enableOptions(false);

	}

	public void stopEmulation() {

		if (clock.isRunning())
			clock.stopClock();
		bStart.setText("Start");
		running = false;

		if (cAudio.isSelected())
			juke.stopmusic();

		enableOptions(true);

	}

	private void enableOptions(boolean enable) {
		cAudio.setEnabled(enable);
		cPreemptive.setEnabled(enable);
		cStatistics.setEnabled(enable);
		bGenOptions.setEnabled(enable);
		rInput.setEnabled(enable);
		rOutput.setEnabled(enable);
	}

	private void clearWindow() {
		console.setText("");
		clearProcessPanels();
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
		frame.setBounds(100, 100, 1121, 632);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table_panel = new JPanel();
		table_panel.setBorder(new MatteBorder(5, 4, 5, 0, new Color(128, 128, 128)));
		table_panel.setBackground(Color.DARK_GRAY);
		table_panel.setPreferredSize(new Dimension(170, 10));
		frame.getContentPane().add(table_panel, BorderLayout.WEST);
		table_panel.setLayout(new BorderLayout(0, 0));

		panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		table_panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		lblGrids = new JLabel("PROCESSES");
		lblGrids.setBorder(new MatteBorder(1, 1, 3, 5, new Color(128, 128, 128)));
		lblGrids.setForeground(Color.LIGHT_GRAY);
		lblGrids.setBackground(Color.DARK_GRAY);
		panel_1.add(lblGrids);
		lblGrids.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblGrids.setHorizontalAlignment(SwingConstants.CENTER);
		lblGrids.setHorizontalTextPosition(SwingConstants.CENTER);

		panel_7 = new JPanel();
		panel_7.setBorder(new MatteBorder(0, 1, 0, 0, Color.GRAY));
		panel_1.add(panel_7, BorderLayout.SOUTH);
		panel_7.setLayout(new BorderLayout(0, 0));
		// BorderLayout l = (BorderLayout) panel_7.getLayout();
		// panel_7.remove(l.getLayoutComponent(BorderLayout.WEST));

		panel_6 = new JPanel();
		panel_7.add(panel_6);
		panel_6.setBackground(Color.DARK_GRAY);
		panel_6.setLayout(new GridLayout(1, 5, 0, 0));

		lblNewLabel_1 = new JLabel("PID");
		panel_6.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_1.setBorder(new MatteBorder(0, 0, 0, 1, new Color(128, 128, 128)));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_1.setBackground(Color.DARK_GRAY);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel_2 = new JLabel("ARR");
		panel_6.add(lblNewLabel_2);
		lblNewLabel_2.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_2.setBackground(Color.DARK_GRAY);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel_3 = new JLabel("BUR");
		panel_6.add(lblNewLabel_3);
		lblNewLabel_3.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_3.setBackground(Color.DARK_GRAY);
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel_4 = new JLabel("REM");
		panel_6.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_4.setBackground(Color.DARK_GRAY);
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel_5 = new JLabel("");
		panel_7.add(lblNewLabel_5, BorderLayout.EAST);
		lblNewLabel_5.setMaximumSize(new Dimension(19, 0));
		lblNewLabel_5.setOpaque(true);
		lblNewLabel_5.setBackground(Color.GRAY);
		lblNewLabel_5.setPreferredSize(new Dimension(19, 0));

		tables = new JPanel();
		tables.setBackground(Color.DARK_GRAY);
		table_panel.add(tables, BorderLayout.CENTER);
		tables.setLayout(new BorderLayout(0, 0));

		panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		panel_2.setPreferredSize(new Dimension(10, 35));
		tables.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));

		cpuScroll = new JScrollPane();
		cpuScroll.setBorder(new MatteBorder(1, 1, 1, 19, Color.GRAY));
		cpuScroll.setBackground(Color.DARK_GRAY);
		cpuScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		panel_2.add(cpuScroll);
		cpuScroll.setForeground(Color.GREEN);

		cpuModel = new ProcessModel();
		rdyModel = new ProcessModel();
		newModel = new ProcessModel();

		cpuTable = new JTable();
		cpuTable.setFillsViewportHeight(true);
		cpuTable.setShowVerticalLines(false);
		cpuTable.setBackground(Color.DARK_GRAY);
		cpuTable.setRequestFocusEnabled(false);
		cpuTable.setForeground(Color.BLACK);
		cpuScroll.add(cpuTable);
		cpuScroll.setViewportView(cpuTable);
		cpuTable.setModel(cpuModel);
		// cpuTable.setEnabled(false);
		cpuTable.setTableHeader(null);
		cpuTable.setForeground(Color.getHSBColor(0.6f, 0.2f, 0.8f));

		lblCpu = new JLabel("CPU");
		lblCpu.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblCpu.setForeground(new Color(245, 245, 245));
		lblCpu.setBackground(Color.DARK_GRAY);
		lblCpu.setBorder(new MatteBorder(1, 1, 1, 19, Color.GRAY));
		lblCpu.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblCpu, BorderLayout.NORTH);

		panel_8 = new JPanel();
		panel_8.setBackground(Color.DARK_GRAY);
		tables.add(panel_8, BorderLayout.CENTER);
		panel_8.setLayout(new GridLayout(2, 1, 0, 0));

		panel_3 = new JPanel();
		panel_3.setBackground(Color.DARK_GRAY);
		panel_8.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		rdyScroll = new JScrollPane();
		rdyScroll.setBackground(Color.DARK_GRAY);
		rdyScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_3.add(rdyScroll);

		rdyTable = new JTable();
		rdyTable.setShowVerticalLines(false);
		rdyTable.setBackground(Color.DARK_GRAY);
		rdyTable.setFillsViewportHeight(true);
		rdyScroll.add(rdyTable);
		rdyScroll.setViewportView(rdyTable);
		rdyTable.setModel(rdyModel);
		// rdyTable.setEnabled(false);
		rdyTable.setTableHeader(null);
		rdyTable.setForeground(Color.getHSBColor(13, 7.6f, 0.2f));

		lblReadyList = new JLabel("READY LIST");
		lblReadyList.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblReadyList.setForeground(new Color(245, 245, 245));
		lblReadyList.setBorder(new MatteBorder(1, 1, 1, 19, Color.GRAY));
		lblReadyList.setBackground(Color.DARK_GRAY);
		lblReadyList.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblReadyList, BorderLayout.NORTH);

		panel_4 = new JPanel();
		panel_4.setBackground(Color.DARK_GRAY);
		panel_8.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		newScroll = new JScrollPane();
		newScroll.setBackground(Color.DARK_GRAY);
		newScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_4.add(newScroll);

		newTable = new JTable();
		newTable.setShowVerticalLines(false);
		newTable.setFillsViewportHeight(true);
		newTable.setBackground(Color.DARK_GRAY);
		newScroll.add(newTable);
		newScroll.setViewportView(newTable);
		newTable.setModel(newModel);
		// newTable.setEnabled(false);
		newTable.setTableHeader(null);
		newTable.setForeground(Color.LIGHT_GRAY);

		lblNewList = new JLabel("NEW LIST");
		lblNewList.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewList.setForeground(new Color(245, 245, 245));
		lblNewList.setBorder(new MatteBorder(1, 1, 1, 19, new Color(128, 128, 128)));
		lblNewList.setBackground(Color.DARK_GRAY);
		lblNewList.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(lblNewList, BorderLayout.NORTH);

		stats_panel = new JPanel();
		stats_panel.setBorder(new MatteBorder(4, 1, 1, 5, Color.GRAY));
		table_panel.add(stats_panel, BorderLayout.SOUTH);
		stats_panel.setLayout(new GridLayout(4, 1, 0, 0));

		lblNewLabel_6 = new JLabel("Average Waiting Time");
		lblNewLabel_6.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_6.setOpaque(true);
		lblNewLabel_6.setBackground(Color.DARK_GRAY);
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		stats_panel.add(lblNewLabel_6);

		lAvgWait = new JLabel("");
		lAvgWait.setForeground(new Color(245, 245, 245));
		lAvgWait.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lAvgWait.setOpaque(true);
		lAvgWait.setBackground(Color.DARK_GRAY);
		lAvgWait.setHorizontalAlignment(SwingConstants.CENTER);
		stats_panel.add(lAvgWait);

		lblNewLabel_7 = new JLabel("Average Response Time");
		lblNewLabel_7.setBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY));
		lblNewLabel_7.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_7.setOpaque(true);
		lblNewLabel_7.setBackground(Color.DARK_GRAY);
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		stats_panel.add(lblNewLabel_7);

		lAvgRes = new JLabel("");
		lAvgRes.setForeground(new Color(245, 245, 245));
		lAvgRes.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lAvgRes.setOpaque(true);
		lAvgRes.setBackground(Color.DARK_GRAY);
		lAvgRes.setHorizontalAlignment(SwingConstants.CENTER);
		stats_panel.add(lAvgRes);

		for (int k = 0; k < cpuTable.getColumnModel().getColumnCount(); k++)
			cpuTable.getColumnModel().getColumn(k).setCellRenderer(centerRenderer);

		for (int k = 0; k < rdyTable.getColumnModel().getColumnCount(); k++)
			rdyTable.getColumnModel().getColumn(k).setCellRenderer(centerRenderer);

		for (int k = 0; k < newTable.getColumnModel().getColumnCount(); k++)
			newTable.getColumnModel().getColumn(k).setCellRenderer(centerRenderer);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.DARK_GRAY);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		scrollPane.setViewportView(console);

		options_panel = new JPanel();
		options_panel.setForeground(Color.WHITE);
		options_panel.setBackground(Color.DARK_GRAY);
		options_panel.setBorder(null);
		frame.getContentPane().add(options_panel, BorderLayout.EAST);
		options_panel.setLayout(new BorderLayout(0, 0));

		buttons = new JPanel();
		buttons.setForeground(Color.WHITE);
		buttons.setBackground(Color.DARK_GRAY);
		buttons.setBorder(new MatteBorder(0, 5, 5, 5, Color.GRAY));
		options_panel.add(buttons, BorderLayout.SOUTH);
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
				clearWindow();
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
		options_panel.add(options, BorderLayout.CENTER);
		options.setLayout(new BorderLayout(0, 0));

		lblNewLabel = new JLabel(" OPTIONS ");
		lblNewLabel.setBorder(new MatteBorder(0, 0, 3, 0, new Color(128, 128, 128)));
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
		main_options.setLayout(new GridLayout(8, 0, 0, 0));

		cPreemptive = new JCheckBox("Pre-Emptive");
		cPreemptive.setFont(new Font("Tahoma", Font.BOLD, 11));
		cPreemptive.setForeground(Color.WHITE);
		cPreemptive.setBackground(Color.DARK_GRAY);
		cPreemptive.setSelected(true);
		cPreemptive.setHorizontalAlignment(SwingConstants.CENTER);
		main_options.add(cPreemptive);

		cStatistics = new JCheckBox("Statistics     ");
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
		tInput.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tInput.setForeground(Color.LIGHT_GRAY);
		tInput.setBackground(Color.DARK_GRAY);
		tInput.setHorizontalAlignment(SwingConstants.CENTER);
		tInput.setEditable(false);
		main_options.add(tInput);
		tInput.setColumns(10);

		ButtonGroup group = new ButtonGroup();
		group.add(rInput);
		group.add(rOutput);

		frame.setVisible(true);
	}

	public void updateProcessPanels() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				if (cpu.peekCpuProcess() == null) {
					if (cpuModel.getRowCount() != 0)
						cpuModel.clearProcesses();
				} else {
					if (cpuModel.getRowCount() != 0) {
						cpuModel.clearProcesses();
						cpuModel.addProcess(cpu.peekCpuProcess());
					} else
						cpuModel.addProcess(cpu.peekCpuProcess());

				}

				if (sjfScheduler.getReadyList().getProcesses().length == 0) {
					if (rdyModel.getRowCount() != 0)
						rdyModel.clearProcesses();
				} else {
					rdyModel.addProcesses(sjfScheduler.getReadyList().getProcesses());
				}

				if (generator.getNewList().getProcesses().length == 0) {
					if (newModel.getRowCount() != 0)
						newModel.clearProcesses();
				} else {
					newModel.addProcesses(generator.getNewList().getProcesses());
				}

				lAvgWait.setText(Double.toString(sjfScheduler.getStatistics().CalculateAverageWaitingTime()));
				lAvgRes.setText(Float.toString(sjfScheduler.getStatistics().CalculateAverageResponseTime()));

			}
		});

	}

	public void clearProcessPanels() {
		cpuModel.clearProcesses();
		rdyModel.clearProcesses();
		newModel.clearProcesses();
		lAvgWait.setText("");
		lAvgRes.setText("");
	}

	private int getClockMillisFromSlider() {

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
