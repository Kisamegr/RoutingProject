package gui;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Music {

	Thread t;
	Player playMP3;
	FileInputStream fis;
	BufferedInputStream bis;
	private boolean playing;

	public void stopmusic() {

		playMP3.close();
		playing = false;
		t.interrupt();
	}

	public void playmusic() {
		playing = true;
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (playing)
					try {
						fis = new FileInputStream("resource/bgm.mp3");
						bis = new BufferedInputStream(fis);
						playMP3 = new Player(bis);
						playMP3.play();
						playMP3.close();

					} catch (Exception exc) {
						exc.printStackTrace();
						System.out.println("Failed to play the file.");
					}

			}

		});

		t.start();

	}

}
