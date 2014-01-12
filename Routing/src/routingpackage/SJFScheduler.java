package routingpackage;

public class SJFScheduler {

	private ReadyProcessesList readyList;
	private CPU cpu;

	private boolean isPreemptive; // if true then the router is preemptive

	// constructor
	SJFScheduler(boolean isPreemptive, CPU cpu) {

		this.isPreemptive = isPreemptive;

		readyList = new ReadyProcessesList();

		this.cpu = cpu;
	}

	// adding a process to the right position of the "ready processes list"
	public void addProcessToReadyList(Process process) {

		readyList.addProcess(process);
	}

	// executes process' swap in the CPU based on the "ready processes list" and
	// the routing algorithm(preemptive or non-preemptive)
	public void SJF() {

		Process cpuProcess = cpu.peekCpuProcess();

		if (!isPreemptive) {

			if (cpuProcess != null) {
				if (cpuProcess.getCpuRemainingTime() == 0)
					cpu.removeProcessFromCpu();

			}

			if (cpuProcess == null) {

				Process forCPU = readyList.getProcessToRunInCPU();
				if (forCPU != null) {
					cpu.addProcess(forCPU);

				}

			}

		}

	}
}
