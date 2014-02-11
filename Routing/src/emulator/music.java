package emulator;

import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class music extends Thread{
	JFXPanel fxPanel;
	
		public music()
		{
			this.start();
		}
	

public void run()
			{
	   System.out.println("Stratos");
				fxPanel = new JFXPanel(); //soundpanel
				// TODO Auto-generated method stub
				File song= new File("kolaw.mp3");
				String beast = song.toURI().toString();
				Media hit = new Media(beast);
				MediaPlayer mediaPlayer = new MediaPlayer(hit);
				mediaPlayer.setVolume(100);
				mediaPlayer.play();
			}
	
	
	
}


			
		