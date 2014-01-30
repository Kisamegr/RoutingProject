package routingpackage;

public class SJFScheduler {

	private ReadyProcessesList readyList;
	private CPU cpu;
	private Statistics stats;

	private boolean isPreemptive; // if true then the router is preemptive

	// constructor
	SJFScheduler(boolean isPreemptive, CPU cpu, Statistics stats) {

		this.isPreemptive = isPreemptive;
		this.stats = stats;

		readyList = new ReadyProcessesList();

		this.cpu = cpu;
	}

	// adding a process to the right position of the "ready processes list"
	public void addProcessToReadyList(Process process) {

		readyList.addProcess(process);
	}

	public void totalNumberOfProcessesUpdate()// edit. Xrisimopoieitai ston
												// generator
	{
		if (stats != null)
			stats.UpdateMaximumListLength(readyList.lengthOfQueue());
	}

	public void addTotalNumberOfProcesses(int n)// edit. Ston generator.
	{
		if (stats != null)
			stats.totalNumberOfProcesses += n;// ayto to pedio einai public apo
												// tin
												// ekfwnisi
	}

	// executes process' swap in the CPU based on the "ready processes list" and
	// the routing algorithm(preemptive or non-preemptive)
	public void SJF(int currentTime) {

		Process cpuProcess = cpu.peekCpuProcess();

		// stats.updateTotalWaitingTime(readyList.lengthOfQueue()); // Probably
		// don't need that. Will explain.

		stats.getAverageReadyQueueProcessWaitingTime(readyList, currentTime); // Average
																				// waiting
																				// time
																				// for
																				// the
																				// processes
																				// currently
																				// in
																				// the
																				// list,
																				// RETURNS
																				// INT.

		if (!isPreemptive) {

			if (cpuProcess != null) {
				if (cpuProcess.getCpuRemainingTime() == 0) {
					cpu.removeProcessFromCpu();

					if (stats != null) {
						stats.updateFinishedNumber(1);// edit
						stats.updateResponseTime(currentTime - cpuProcess.getArrivalTime());// edit.
					}
				}
			}

			if (cpuProcess == null) {

				Process forCPU = readyList.getProcessToRunInCPU();
				if (forCPU != null) {

					cpu.addProcess(forCPU);

					cpu.setLastProcessStartTime(currentTime); // o xronos pu
																// bike teleutea
																// fora process
																// sti cpu
					if (stats != null)
						stats.updateTotalWaitingTime(forCPU.getArrivalTime()/*-currentTime*/);// edit

				}

			}

		}

		if (stats != null) {
			stats.updateTotalWaitingTime(readyList.lengthOfQueue());// edit
			stats.WriteStatistics2File(currentTime);
		}

	}
}
