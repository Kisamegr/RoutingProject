package gui;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Console extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Console console;
	private Logger logger;
	private DateFormat dateFormat;
	private int maxLines;

	public Console() {
		super();

		console = this;
		logger = new Logger();
		this.setFont(new Font("Consolas", Font.PLAIN, 15));
		this.setBackground(Color.getHSBColor(129, 0, .2f));

		this.setEditable(false);

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

		logger.log(message);

	}

	public void appendCpuMessage(String message) {
		this.appendToConsole(message, Color.getHSBColor(0.6f, 0.2f, 0.8f));
	}

	public void appendSjfMessage(String message) {
		this.appendToConsole(message, Color.CYAN);
	}

	public void appendClockMessage(String message) {
		this.appendToConsole(message, Color.getHSBColor(0, 0.2f, 0.9f));
	}

	public void appendReadyQueueMessage(String message) {
		this.appendToConsole(message, Color.getHSBColor(13, 7.6f, 0.2f));
	}

	public void appendExecuteMessage(String message) {
		this.appendToConsole(message, Color.WHITE);
	}

	public void appendNewListMessage(String message) {
		this.appendToConsole(message, Color.LIGHT_GRAY);
	}

	public void resetLogger() {
		logger.reset();
	}

}