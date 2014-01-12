package routingpackage;

public class CPU {

	private Process runningProcess; // running process
	private int timeToNextContextSwitch; // time to next interrupt
	private int lastProcessStartTime; // time that last process starts

	// constructor
	public CPU() {

	}

	// insert process to execute
	public void addProcess(Process process) {
		runningProcess = process;

		System.out.println("+CPU: Added process to cpu");
		System.out.println(runningProcess);

	}

	// extracting running process from CPU
	public Process removeProcessFromCpu() {
		Process temp = runningProcess;
		runningProcess = null;

		System.out.println("-CPU: Removed process from cpu");
		System.out.println(temp);

		return temp;

	}

	// executing process and reducing time of its execution
	public void execute() {

		if (runningProcess != null) {
			if (runningProcess.getCpuRemainingTime() > 0) {
				runningProcess.changeCpuRemainingTime(runningProcess.getCpuRemainingTime() - 1);
				System.out.println("+Exec: Executed process at cpu");
				System.out.println(runningProcess);

			}
		}

	}

	public Process peekCpuProcess() {
		return runningProcess;
	}
}
