package emulator;

public class SJFScheduler {

	private ReadyProcessesList readyList;
	private CPU cpu;
	private Statistics stats;

	private boolean isPreemptive; // if true then the router is preemptive

	// constructor
	public SJFScheduler(boolean isPreemptive, CPU cpu, Statistics stats) {

		this.isPreemptive = isPreemptive;
		this.stats = stats;

		readyList = new ReadyProcessesList();

		this.cpu = cpu;

	}

	// adding a process to the right position of the "ready processes list"
	public void addProcessToReadyList(Process process) {

		readyList.addProcess(process);
	}

	public void totalNumberOfProcessesUpdate()
	{
		if (stats != null)
			stats.UpdateMaximumListLength(getReadyList().lengthOfQueue());
	}

	public void addTotalNumberOfProcesses(int n)// 
	{
		if (stats != null)
			stats.totalNumberOfProcesses += n;
	}

	// executes process' swap in the CPU based on the "ready processes list" and
	// the routing algorithm(preemptive or non-preemptive)
	public void SJF(int currentTime) {

		Process cpuProcess = cpu.peekCpuProcess();
		Process HeadofQueue = getReadyList().getReadyList().peek(); // first process of the ready queue/the one with the lowest burst

		if (!isPreemptive) {

			if (cpuProcess != null) {
				if (cpuProcess.getCpuRemainingTime() == 0) {
					// zero remaining time  means that the process is finished, therefore update number of finished processes and total waiting time

					if (stats != null) {
						stats.updatetotalwait(cpuProcess, currentTime); // total waiting time update
						stats.updatemyfinished();    // update number of finished processes
						
					}
					cpu.removeProcessFromCpu();

					if (stats != null) {
						stats.updateFinishedNumber(1);// edit
						stats.updateResponseTime(currentTime - cpuProcess.getArrivalTime());// edit.
					}
				}
			}

			cpuProcess = cpu.peekCpuProcess();

			if (cpuProcess == null) {

				Process forCPU = getReadyList().getProcessToRunInCPU();
				if (forCPU != null) {

					cpu.addProcess(forCPU);

					cpu.setLastProcessStartTime(currentTime); 

				}

			}

		}

		// //////////////////PREEMPTIVE////////////////////
		else if (isPreemptive) {

			if (cpuProcess != null) {

				if (cpuProcess.getCpuRemainingTime() == 0) {// same as non-preemptive

					// zero remaining time  means that the process is finished, therefore update number of finished processes and total waiting time
					if (stats != null) {
						stats.updatetotalwait(cpuProcess, currentTime); // total waiting time update
						stats.updatemyfinished();   // update number of finished processes
						
					}

					cpu.removeProcessFromCpu();

					if (stats != null) {
						stats.updateFinishedNumber(1);
						stats.updateResponseTime(currentTime - cpuProcess.getArrivalTime());
					}
				} else if ((HeadofQueue != null) && (cpuProcess.getCpuRemainingTime() > HeadofQueue.getCpuRemainingTime())) {//null check + compare the remaining CPU times between the current CPU process and the one on the top of the Queue

					readyList.addProcess(cpu.removeProcessFromCpu()); //remove current Process and put it again at ready list

					Process forCPU = getReadyList().getProcessToRunInCPU();//add the one from the top of the queue
					{

						if (forCPU.getCpuTotalTime() == forCPU.getCpuTotalTime()) {
							if (stats != null)
								stats.updateResponseTime(currentTime - forCPU.getArrivalTime());
						}

						cpu.addProcess(forCPU);

						cpu.setLastProcessStartTime(currentTime); 

					}

				}

			}

			cpuProcess = cpu.peekCpuProcess();

			if (cpuProcess == null) {  //if CPU isn't occupied
				Process forCPU = getReadyList().getProcessToRunInCPU();  //get process from the top of the Queue 
				if (forCPU != null) {

					if (forCPU.getCpuTotalTime() == forCPU.getCpuTotalTime()) {
						if (stats != null)
							stats.updateResponseTime(currentTime - forCPU.getArrivalTime());
					}

					cpu.addProcess(forCPU); //add to CPU

					cpu.setLastProcessStartTime(currentTime); 
				}
			}
		}

		if (stats != null) {
			stats.updateTotalWaitingTime(getReadyList().lengthOfQueue());
			stats.WriteStatistics2File(currentTime);
		}

	}

	public ReadyProcessesList getReadyList() {
		return readyList;
	}

	public void setReadyList(ReadyProcessesList readyList) {
		this.readyList = readyList;
	}

}
