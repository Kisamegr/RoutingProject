package emulator;

import gui.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.swing.Timer;



public class Clock implements ActionListener {

	
	
	private boolean clockRunning;
	private boolean clockPaused;
	private Timer timer;
	private int clockSpeedMilliseconds;
	private music juke;

	private ProcessGenerator generator;
	private SJFScheduler sjfScheduler;
	private CPU cpu;

	protected static int ticks; // current ticks of clock

	// constructor
	public Clock(int clockSpeedMilliseconds, ProcessGenerator generator, SJFScheduler sjfScheduler, CPU cpu) {

		
		
		this.clockSpeedMilliseconds = clockSpeedMilliseconds;
		timer = new Timer(clockSpeedMilliseconds, this);

		clockRunning = false;
		ticks = -1;

		this.generator = generator;
		this.sjfScheduler = sjfScheduler;
		this.cpu = cpu;
		
		juke = new music();

	}

	// increasing ticks of clock by 1
	public void Time_Run() {

		Window.getConsole().appendClockMessage("\n@Tick " + ticks);
		generator.runGenerator(ShowTime());
		cpu.execute();
		sjfScheduler.SJF(ShowTime());

		ticks++;

		if (ticks > generator.getGenerationFreq() * generator.getGenerationMax()) // If it passed the tick that the generator stoped generating
			if (generator.getNewlistLength() == 0 && cpu.peekCpuProcess() == null) // And if the ready list is empty, and the cpu does not have any process
				stopClock(); // Stop the clock

		Window.getWindow().updateProcessPanels();

	}

	// return ticks
	public int ShowTime() {
		return ticks;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Time_Run();
	}

	public void startClock() {
		
		juke.start();
		clockRunning = true;
		timer.start();
		Window.getConsole().appendClockMessage("\r----------- Emulation Started -----------");

	}

	public void stopClock() {
		juke.interrupt();
		clockRunning = false;
		timer.stop();
		Window.getConsole().appendClockMessage("\r----------- Emulation Finished -----------");
		Window.getWindow().stopEmulation();
	}

	public void pauseClock(boolean pause) {
		if (pause)
			timer.stop();
		else
			timer.start();

		clockPaused = pause;

	}

	public boolean isRunning() {
		return clockRunning;
	}

	public boolean isPaused() {
		return clockPaused;
	}

	public void changeMilliseconds(int m) {
		timer.setDelay(m);

	}

}
