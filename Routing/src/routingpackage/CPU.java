package routingpackage;

public class CPU {

	private Clock clock;
	private Process runningProcess; // running process
	private int timeToNextContextSwitch; // time to next interrupt
	private int lastProcessStartTime; // time that last process starts

	// constructor
	public CPU(Clock clock) {

		this.clock = clock;

	}

	// insert process to execute
	public void addProcess(Process process) {

	}

	// extracting running process from CPU
	public Process removeProcessFromCpu() {
		return null;
	}

	// executing process and reducing time of its execution
	public void execute() {

		try {

			while (clock.isClockRunning()) {
				clock.wait();

			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
