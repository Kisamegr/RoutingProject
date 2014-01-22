package routingpackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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
					console.replaceSelection("[" + dateFormat.format(Calendar.getInstance().getTime()) + "]" + message + "\n");

					console.setEditable(false);

				}

			});

		}

		public void appendCpuMessage(String message) {
			this.appendToConsole(" <CPU> " + message, Color.WHITE);
		}

		public void appendSjfMessage(String message) {
			this.appendToConsole(" <SJF> " + message, Color.BLUE);
		}

		public void appendClockMessage(String message) {
			this.appendToConsole(" <CLOCK> " + message, Color.getHSBColor(0, 0.7f, 0.9f));
		}

		public void appendGeneratorMessage(String message) {
			this.appendToConsole(" <P.Gen> " + message, Color.getHSBColor(0, 0.7f, 0.9f));
		}

	}

	private JFrame frmSd;
	private Console console;
	private static Console m_console;
	private JPanel panel;
	private JButton bStart;
	private JPanel panel_1;
	private JCheckBox cPreemptive;
	private Component verticalStrut;
	private JCheckBox cStatistics;
	private JTextField tInput;
	private JPanel panel_2;
	private JButton btnInputFile;
	private Component verticalStrut_1;
	private JSlider slider;
	private Component verticalStrut_2;
	private JPanel panel_3;
	private Component verticalStrut_3;
	private JLabel lblClockSpeed;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public ConsoleWindow(String name) {

		console = new Console();
		m_console = console;

		initialize();
		// System.out.println(Thread.currentThread().getName());

		frmSd.setTitle(name);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSd = new JFrame("Gdx Server");
		frmSd.getContentPane().setBackground(Color.BLACK);
		frmSd.setBackground(Color.BLACK);
		frmSd.setBounds(100, 100, 897, 500);
		frmSd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frmSd.getContentPane().add(scrollPane, BorderLayout.CENTER);

		scrollPane.setViewportView(console);

		panel = new JPanel();
		frmSd.getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));

		panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));

		bStart = new JButton("START");
		bStart.setFont(new Font("Tahoma", Font.PLAIN, 18));
		bStart.setPreferredSize(new Dimension(57, 38));
		panel_3.add(bStart);

		verticalStrut_3 = Box.createVerticalStrut(10);
		panel_3.add(verticalStrut_3, BorderLayout.SOUTH);

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		verticalStrut = Box.createVerticalStrut(30);
		panel_1.add(verticalStrut, BorderLayout.NORTH);

		panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(8, 0, 0, 0));

		cPreemptive = new JCheckBox("Pre-Emptive");
		cPreemptive.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(cPreemptive);

		cStatistics = new JCheckBox("Statisctics   ");
		cStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(cStatistics);

		verticalStrut_1 = Box.createVerticalStrut(20);
		panel_2.add(verticalStrut_1);

		tInput = new JTextField();
		tInput.setEditable(false);
		panel_2.add(tInput);
		tInput.setColumns(10);

		btnInputFile = new JButton("Input File");
		btnInputFile.setPreferredSize(new Dimension(77, 26));
		panel_2.add(btnInputFile);

		verticalStrut_2 = Box.createVerticalStrut(20);
		panel_2.add(verticalStrut_2);

		lblClockSpeed = new JLabel("Clock Speed");
		lblClockSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblClockSpeed);

		slider = new JSlider();
		slider.setMinimum(1);
		slider.setMinimumSize(new Dimension(30, 23));
		slider.setPaintLabels(true);
		slider.setPreferredSize(new Dimension(100, 23));
		slider.setMaximumSize(new Dimension(25, 23));
		slider.setMinorTickSpacing(1);
		slider.setSnapToTicks(true);
		panel_2.add(slider);

		frmSd.setVisible(true);

		// console.appendInfoMessage("Done initializing the main window.");

	}

	private void excecuteCommandInput(String command) {

	}

	public static Console getConsole() {
		synchronized (m_console) {
			return m_console;

		}
	}

}
