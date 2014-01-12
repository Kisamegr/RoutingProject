package routingpackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Clock implements ActionListener {

	private boolean clockRunning;
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

		System.out.println("\nTick " + ticks);
		generator.runGenerator(ShowTime());
		sjfScheduler.SJF();
		cpu.execute();

		ticks++;

		if (ticks > 100)
			stopClock();

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
	}

	public void stopClock() {
		clockRunning = false;
		timer.stop();
	}

	public boolean isRunning() {
		return clockRunning;
	}

}
