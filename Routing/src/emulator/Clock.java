package emulator;

import gui.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Clock implements ActionListener {

	private boolean clockRunning;
	private boolean clockPaused;
	private Timer timer;
	private int clockSpeedMilliseconds;

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

	}

	// increasing ticks of clock by 1
	public void Time_Run() {

		Window.getConsole().appendClockMessage("\nTick " + ticks);
		generator.runGenerator(ShowTime());
		cpu.execute();
		sjfScheduler.SJF(ShowTime());

		ticks++;

		if (ticks > generator.getGenerationFreq() * generator.getGenerationMax()) // If it passed the tick that the generator stoped generating
			if (sjfScheduler.getReadyList().lengthOfQueue() == 0 && cpu.peekCpuProcess() == null) // And if the ready list is empty, and the cpu does not have any process
				stopClock(); // Stop the clock

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
		clockRunning = true;
		timer.start();
		Window.getConsole().appendClockMessage("---------- Emulation Started ----------");

	}

	public void stopClock() {
		clockRunning = false;
		timer.stop();
		Window.getConsole().appendClockMessage("---------- Emulation Finished ----------");
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
