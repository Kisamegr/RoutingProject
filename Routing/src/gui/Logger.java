package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	public void log(final String line) {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				File log = new File("log.txt");

				try {
					if (!log.exists())
						log.createNewFile();

					BufferedWriter bw = new BufferedWriter(new FileWriter(log, true));
					bw.append(line);
					bw.flush();
					bw.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		t.start();

	}

	public void reset() {
		File log = new File("log.txt");

		if (log.exists())
			log.delete();
	}
}
