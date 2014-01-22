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

		ConsoleWindow.getConsole().appendCpuMessage("+CPU: Added process to cpu");
		ConsoleWindow.getConsole().appendCpuMessage(runningProcess.toString());

	}

	// extracting running process from CPU
	public Process removeProcessFromCpu() {
		Process temp = runningProcess;
		runningProcess = null;

		ConsoleWindow.getConsole().appendCpuMessage("-CPU: Removed process from cpu");
		ConsoleWindow.getConsole().appendCpuMessage(temp.toString());

		return temp;

	}

	// executing process and reducing time of its execution
	public void execute() {

		if (runningProcess != null) {
			if (runningProcess.getCpuRemainingTime() > 0) {
				runningProcess.changeCpuRemainingTime(runningProcess.getCpuRemainingTime() - 1);
				ConsoleWindow.getConsole().appendExecuteMessage("+Exec: Executed process at cpu");
				ConsoleWindow.getConsole().appendExecuteMessage(runningProcess.toString());

			}
		}

	}

	public Process peekCpuProcess() {
		return runningProcess;
	}
}
