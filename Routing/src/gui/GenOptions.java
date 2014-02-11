package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class GenOptions extends JDialog {

	private class ChangeHandler implements MouseListener, KeyListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {

			updateSliders();

		}

		@Override
		public void keyPressed(KeyEvent e) {
			updateSliders();

		}

		@Override
		public void keyReleased(KeyEvent e) {
			updateSliders();

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

	}

	private JDialog thisDialog;
	private JSlider sGenFreq;
	private JSlider sGenMax;
	private JSlider sMaxBurst;
	private JSlider sMinBurst;
	private JSlider sMaxGenProc;
	private JSlider sMinGenProc;

	private JPanel sliders;

	private int sliderRange;

	public GenOptions() {
		setResizable(false);
		thisDialog = this;

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new MatteBorder(5, 5, 5, 5, Color.GRAY));
		mainPanel.setPreferredSize(new Dimension(525, 280));
		mainPanel.setLayout(new BorderLayout(0, 0));
		getContentPane().add(mainPanel);

		ChangeHandler changeHandler = new ChangeHandler();
		sliderRange = 25;

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setForeground(Color.WHITE);
		mainPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel names = new JPanel();
		names.setBackground(Color.DARK_GRAY);
		names.setForeground(Color.WHITE);
		panel.add(names, BorderLayout.WEST);
		names.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblGeneratorFrequency = new JLabel(" Generator Frequency: ");
		lblGeneratorFrequency.setBorder(new MatteBorder(1, 0, 2, 1, new Color(128, 128, 128)));
		lblGeneratorFrequency.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGeneratorFrequency.setBackground(Color.DARK_GRAY);
		lblGeneratorFrequency.setForeground(Color.WHITE);
		lblGeneratorFrequency.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGeneratorFrequency.setToolTipText("The generator will create new random processes every n ticks, where n = generator frequency");
		names.add(lblGeneratorFrequency);

		JLabel lblGenerationMax = new JLabel(" Generation Times: ");
		lblGenerationMax.setBorder(new MatteBorder(0, 0, 2, 1, new Color(128, 128, 128)));
		lblGenerationMax.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGenerationMax.setBackground(Color.DARK_GRAY);
		lblGenerationMax.setForeground(Color.WHITE);
		lblGenerationMax.setToolTipText("How many generations will be done.");
		lblGenerationMax.setHorizontalAlignment(SwingConstants.RIGHT);
		names.add(lblGenerationMax);

		JLabel lblMaximumBurstTime = new JLabel("Maximum Burst Time: ");
		lblMaximumBurstTime.setBorder(new MatteBorder(0, 0, 2, 1, new Color(128, 128, 128)));
		lblMaximumBurstTime.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMaximumBurstTime.setBackground(Color.DARK_GRAY);
		lblMaximumBurstTime.setForeground(Color.WHITE);
		lblMaximumBurstTime.setToolTipText("The maximum burst time a process can have.");
		lblMaximumBurstTime.setHorizontalAlignment(SwingConstants.RIGHT);
		names.add(lblMaximumBurstTime);

		JLabel lblMunimumBurstTime = new JLabel("Minimum Burst Time: ");
		lblMunimumBurstTime.setBorder(new MatteBorder(0, 0, 2, 1, new Color(128, 128, 128)));
		lblMunimumBurstTime.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMunimumBurstTime.setBackground(Color.DARK_GRAY);
		lblMunimumBurstTime.setForeground(Color.WHITE);
		lblMunimumBurstTime.setToolTipText("The minimum burst time a process can have.");
		lblMunimumBurstTime.setHorizontalAlignment(SwingConstants.RIGHT);
		names.add(lblMunimumBurstTime);

		JLabel lblNewLabel = new JLabel(" Max Generated Processes: ");
		lblNewLabel.setBorder(new MatteBorder(0, 0, 2, 1, Color.GRAY));
		lblNewLabel.setBackground(Color.DARK_GRAY);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setToolTipText("The maximum possible number of processes, to be generated every generator frequency ticks");
		names.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel(" Min Generated Processes: ");
		lblNewLabel_1.setToolTipText("The minimum possible number of processes, to be generated every generator frequency ticks");
		lblNewLabel_1.setBorder(new MatteBorder(0, 0, 2, 1, Color.GRAY));
		lblNewLabel_1.setBackground(Color.DARK_GRAY);
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		names.add(lblNewLabel_1);

		sliders = new JPanel();
		sliders.setBackground(Color.DARK_GRAY);
		sliders.setForeground(Color.WHITE);
		panel.add(sliders, BorderLayout.CENTER);
		sliders.setLayout(new GridLayout(0, 1, 2, 0));

		sGenFreq = new JSlider();
		sGenFreq.setBorder(new MatteBorder(1, 1, 2, 1, new Color(128, 128, 128)));
		sGenFreq.setFont(new Font("Tahoma", Font.BOLD, 10));
		sGenFreq.setBackground(Color.DARK_GRAY);
		sGenFreq.setForeground(Color.WHITE);
		sGenFreq.setSnapToTicks(true);
		sGenFreq.setMinorTickSpacing(1);
		sGenFreq.setMajorTickSpacing(sliderRange / 2);
		sGenFreq.setMinimum(-10);
		sGenFreq.setPaintTicks(true);
		sGenFreq.setPaintLabels(true);
		sGenFreq.addMouseListener(changeHandler);
		sGenFreq.addKeyListener(changeHandler);
		sliders.add(sGenFreq);

		sGenMax = new JSlider();
		sGenMax.setBorder(new MatteBorder(0, 1, 2, 1, Color.GRAY));
		sGenMax.setFont(new Font("Tahoma", Font.BOLD, 10));
		sGenMax.setBackground(Color.DARK_GRAY);
		sGenMax.setForeground(Color.WHITE);
		sGenMax.setSnapToTicks(true);
		sGenMax.setPaintTicks(true);
		sGenMax.setPaintLabels(true);
		sGenMax.setMinorTickSpacing(1);
		sGenMax.setMinimum(1);
		sGenMax.setMajorTickSpacing(sliderRange / 2);
		sGenMax.addMouseListener(changeHandler);
		sGenMax.addKeyListener(changeHandler);
		sliders.add(sGenMax);

		sMaxBurst = new JSlider();
		sMaxBurst.setBorder(new MatteBorder(0, 1, 2, 1, Color.GRAY));
		sMaxBurst.setFont(new Font("Tahoma", Font.BOLD, 10));
		sMaxBurst.setBackground(Color.DARK_GRAY);
		sMaxBurst.setForeground(Color.WHITE);
		sMaxBurst.setSnapToTicks(true);
		sMaxBurst.setPaintTicks(true);
		sMaxBurst.setPaintLabels(true);
		sMaxBurst.setMinorTickSpacing(1);
		sMaxBurst.setMinimum(1);
		sMaxBurst.setMajorTickSpacing(sliderRange / 2);
		sMaxBurst.addMouseListener(changeHandler);
		sMaxBurst.addKeyListener(changeHandler);
		sliders.add(sMaxBurst);

		sMinBurst = new JSlider();
		sMinBurst.setBorder(new MatteBorder(0, 1, 2, 1, Color.GRAY));
		sMinBurst.setFont(new Font("Tahoma", Font.BOLD, 10));
		sMinBurst.setBackground(Color.DARK_GRAY);
		sMinBurst.setForeground(Color.WHITE);
		sMinBurst.setSnapToTicks(true);
		sMinBurst.setPaintTicks(true);
		sMinBurst.setPaintLabels(true);
		sMinBurst.setMinorTickSpacing(1);
		sMinBurst.setMinimum(1);
		sMinBurst.setMajorTickSpacing(sliderRange / 2);
		sMinBurst.addMouseListener(changeHandler);
		sMinBurst.addKeyListener(changeHandler);
		sliders.add(sMinBurst);

		sMaxGenProc = new JSlider();
		sMaxGenProc.setFont(new Font("Tahoma", Font.BOLD, 11));
		sMaxGenProc.setForeground(Color.WHITE);
		sMaxGenProc.setBorder(new MatteBorder(0, 1, 2, 1, Color.GRAY));
		sMaxGenProc.setPaintTicks(true);
		sMaxGenProc.setPaintLabels(true);
		sMaxGenProc.setBackground(Color.DARK_GRAY);
		sMaxGenProc.setMinorTickSpacing(1);
		sMaxGenProc.setMinimum(1);
		sMaxGenProc.setMajorTickSpacing(sliderRange / 2);
		sMaxGenProc.addMouseListener(changeHandler);
		sMaxGenProc.addKeyListener(changeHandler);
		sliders.add(sMaxGenProc);

		sMinGenProc = new JSlider();
		sMinGenProc.setFont(new Font("Tahoma", Font.BOLD, 11));
		sMinGenProc.setForeground(Color.WHITE);
		sMinGenProc.setBorder(new MatteBorder(0, 1, 2, 1, Color.GRAY));
		sMinGenProc.setPaintTicks(true);
		sMinGenProc.setPaintLabels(true);
		sMinGenProc.setBackground(Color.DARK_GRAY);
		sMinGenProc.setMinorTickSpacing(1);
		sMinGenProc.setMinimum(1);
		sMinGenProc.setMajorTickSpacing(sliderRange / 2);
		sMinGenProc.addMouseListener(changeHandler);
		sMinGenProc.addKeyListener(changeHandler);
		sliders.add(sMinGenProc);

		JPanel buttons = new JPanel();
		buttons.setBackground(Color.DARK_GRAY);
		panel.add(buttons, BorderLayout.SOUTH);
		buttons.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		buttons.add(panel_2, BorderLayout.EAST);

		JButton bOk = new JButton("OK");
		bOk.setForeground(Color.DARK_GRAY);
		bOk.setFont(new Font("Tahoma", Font.BOLD, 11));
		bOk.setBackground(Color.DARK_GRAY);
		bOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveOptions();
				thisDialog.setVisible(false);
			}
		});
		bOk.setPreferredSize(new Dimension(65, 23));
		panel_2.add(bOk);

		JButton bCancel = new JButton("Cancel");
		bCancel.setForeground(Color.DARK_GRAY);
		bCancel.setFont(new Font("Tahoma", Font.BOLD, 11));
		bCancel.setBackground(Color.DARK_GRAY);
		bCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadOptions();
				thisDialog.setVisible(false);
			}
		});
		panel_2.add(bCancel);

		JTextArea txtrTheGeneratorSettings = new JTextArea();
		txtrTheGeneratorSettings.setFocusable(false);
		txtrTheGeneratorSettings.setEditable(false);
		txtrTheGeneratorSettings.setForeground(Color.LIGHT_GRAY);
		txtrTheGeneratorSettings.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtrTheGeneratorSettings.setOpaque(false);
		txtrTheGeneratorSettings.setText("  The generator settings are used when the \"Output \r\n  Processes\" option is selected in the main window.");
		buttons.add(txtrTheGeneratorSettings, BorderLayout.WEST);

		loadOptions();

		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setBackground(Color.DARK_GRAY);
		this.pack();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				loadOptions();
			}
		});
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

	}

	private void updateSliders() {

		for (int i = 0; i < sliders.getComponentCount(); i++) {
			JSlider slider = (JSlider) sliders.getComponent(i);

			if (slider.getValue() < 1)
				if (slider != sMinGenProc)
					slider.setValue(1);
				else
					slider.setValue(0);

			if (slider == sGenFreq && slider.getValue() < 2)
				slider.setValue(2);

			if (slider == sMaxBurst && slider.getValue() < sMinBurst.getValue() + 1)
				slider.setValue(sMinBurst.getValue() + 1);

			if (slider == sMinBurst && slider.getValue() > sMaxBurst.getValue() - 1)
				slider.setValue(sMaxBurst.getValue() - 1);

			if (slider == sMaxGenProc && slider.getValue() < sMinGenProc.getValue() + 1)
				slider.setValue(sMinGenProc.getValue() + 1);

			if (slider == sMinGenProc && slider.getValue() > sMaxGenProc.getValue() - 1)
				slider.setValue(sMaxGenProc.getValue() - 1);

			slider.setMinimum(slider.getValue() - sliderRange / 2);
			slider.setMaximum(slider.getValue() + sliderRange / 2);

		}
	}

	public void loadOptions() {

		File options = new File("generation_options.ini");

		if (options.exists()) {

			try {
				Scanner scanner = new Scanner(options);

				for (int i = 0; i < sliders.getComponentCount(); i++)
					((JSlider) sliders.getComponent(i)).setValue(Integer.valueOf(scanner.nextLine()));

				updateSliders();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else
			createOptionsFile();
	}

	private void saveOptions() {

		File options = new File("generation_options.ini");

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(options));

			for (int i = 0; i < sliders.getComponentCount(); i++)
				bw.append(String.valueOf(((JSlider) sliders.getComponent(i)).getValue()) + "\n");

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createOptionsFile() {

		sGenFreq.setValue(10);
		sGenMax.setValue(5);
		sMaxBurst.setValue(15);
		sMinBurst.setValue(1);
		sMaxGenProc.setValue(20);
		sMinGenProc.setValue(0);

		saveOptions();
		updateSliders();
	}

	public int getGenerationFreq() {
		return sGenFreq.getValue();
	}

	public int getGenerationMax() {
		return sGenMax.getValue();
	}

	public int getMaxBurst() {
		return sMaxBurst.getValue();
	}

	public int getMinBurst() {
		return sMinBurst.getValue();
	}

	public int getMaxGenProc() {
		return sMaxGenProc.getValue();
	}

	public int getMinGenProc() {
		return sMinGenProc.getValue();
	}

}
