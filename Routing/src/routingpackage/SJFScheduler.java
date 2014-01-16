package routingpackage;

public class SJFScheduler {

	private ReadyProcessesList readyList;
	private CPU cpu;
	private Statistics stats;
	
	private boolean isPreemptive; // if true then the router is preemptive

	// constructor
	SJFScheduler(boolean isPreemptive, CPU cpu,Statistics stats) {

		this.isPreemptive = isPreemptive;
		this.stats = stats;
		
		readyList = new ReadyProcessesList();

		this.cpu = cpu;
	}

	// adding a process to the right position of the "ready processes list"
	public void addProcessToReadyList(Process process) {

		readyList.addProcess(process);
	}
	
	
	public void totalNumberOfProcessesUpdate()//edit. Xrisimopoieitai ston generator
	{
		stats.UpdateMaximumListLength(readyList.lengthOfQueue());
	}
	
	public void addTotalNumberOfProcesses(int n)//edit. Ston generator.
	{
		stats.totalNumberOfProcesses +=  n;//ayto to pedio einai public apo tin ekfwnisi 
	}

	
	// executes process' swap in the CPU based on the "ready processes list" and
	// the routing algorithm(preemptive or non-preemptive)
	public void SJF(int currentTime) {
		
		Process cpuProcess = cpu.peekCpuProcess();
		
		stats.updateTotalWaitingTime(readyList.lengthOfQueue());//edit
		
		
		if (!isPreemptive) {

			if (cpuProcess != null) {
				if (cpuProcess.getCpuRemainingTime() == 0)
				{
					cpu.removeProcessFromCpu();
				stats.updateFinishedNumber(1);//edit
				stats.updateResponseTime(currentTime - cpuProcess.getArrivalTime());//edit.
				}
					

			}

			if (cpuProcess == null) {

				Process forCPU = readyList.getProcessToRunInCPU();
				if (forCPU != null) {
					cpu.addProcess(forCPU);
					stats.updateTotalWaitingTime(forCPU.getArrivalTime()/*-currentTime*/);//edit

				}

			}

		}
		
		
		stats.updateTotalWaitingTime(readyList.lengthOfQueue());//edit
		
		stats.WriteStatistics2File();

	}
}
