package routingpackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Clock implements ActionListener {

	private boolean clockRunning;
	private Timer timer;
	private int clockSpeedMilliseconds;

	protected static int ticks; // current ticks of clock

	// constructor
	public Clock(int clockSpeedMilliseconds) {

		this.clockSpeedMilliseconds = clockSpeedMilliseconds;
		timer = new Timer(clockSpeedMilliseconds, this);

		setClockRunning(false);
	}

	// increasing ticks of clock by 1
	public void Time_Run() {

		this.notify();

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
		setClockRunning(true);
		timer.start();
	}

	public void stopClock() {
		setClockRunning(false);
		timer.stop();
	}

	public boolean isClockRunning() {
		return clockRunning;
	}

	public void setClockRunning(boolean clockRunning) {
		this.clockRunning = clockRunning;
	}
}
