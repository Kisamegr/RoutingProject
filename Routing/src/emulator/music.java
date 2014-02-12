package emulator;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class music  {
    
    Thread t;
    Player playMP3;
    FileInputStream  fis;
    BufferedInputStream bis;
    private boolean playing;
    
		public music()
		{
		
		}
	


public void stopmusic()
{
	
	playMP3.close();
	playing=false;
	t.interrupt();
}
	
public void playmusic()
{
	 playing=true;
	 t = new Thread(new Runnable(){

		@Override
		public void run() {
			while(playing)
			try{
				fis = new FileInputStream("bgm.mp3");
				bis = new BufferedInputStream(fis);
				playMP3 = new Player(bis);
			    playMP3.play();
			    playMP3.close();
			    
			}
			catch(Exception exc){
			    exc.printStackTrace();
			    System.out.println("Failed to play the file.");
			}
			
			
		}
		
	});
	
	 t.start();
	 
}
	
	
}


			
		