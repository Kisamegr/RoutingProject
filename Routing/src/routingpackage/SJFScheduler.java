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
			stats.UpdateMaximumListLength(getReadyList().lengthOfQueue());
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
		Process HeadofQueue = getReadyList().getReadyList().peek(); // i prwti diergasia tou readyQueue(auti me to mikrotero burst)

		// stats.updateTotalWaitingTime(readyList.lengthOfQueue()); // Probably
		// don't need that. Will explain.

		stats.getAverageReadyQueueProcessWaitingTime(getReadyList(), currentTime); // Average waiting time for the processes currently in the list,RETURNS INT.

		if (!isPreemptive) {

			if (cpuProcess != null) {
				if (cpuProcess.getCpuRemainingTime() == 0) {
					// ka8e fora pu aferite edw mia diadikasia simenei pws exi oloklirw8ei
					if (stats != null) {
						stats.updatetotalwait(cpuProcess, currentTime); // total waiting time
						stats.updatemyfinished();
						// 8a deite
					}
					cpu.removeProcessFromCpu();

					if (stats != null) {
						stats.updateFinishedNumber(1);// edit
						stats.updateResponseTime(currentTime - cpuProcess.getArrivalTime());// edit.
					}
				}
			}

			if (cpuProcess == null) {

				Process forCPU = getReadyList().getProcessToRunInCPU();
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

		// //////////////////PREEMPTIVE////////////////////
		else if (isPreemptive) {

			if (cpuProcess != null) {

				if (cpuProcess.getCpuRemainingTime() == 0) {// MENEI TO IDIO ME TO NON-PREEMPTIVE

					// ka8e fora pu aferite edw mia diadikasia simenei pws exi oloklirw8ei
					if (stats != null) {
						stats.updatetotalwait(cpuProcess, currentTime); // total waiting time
						stats.updatemyfinished();
						// 8a deite
					}

					cpu.removeProcessFromCpu();

					if (stats != null) {
						stats.updateFinishedNumber(1);// edit
						stats.updateResponseTime(currentTime - cpuProcess.getArrivalTime());// edit.
					}
				} else if ((HeadofQueue != null) && (cpuProcess.getCpuRemainingTime() > HeadofQueue.getCpuRemainingTime())) {
					// prepei na:
					// -vgei i process apo tin cpu
					// -na mpei i alli
					//
					// Themata:
					// Statistics
					// opote mpainei mia diergasia pws kseroume oti einai i prwti fora pou mpainei mesa
					// etsi na ananewnoume to response time
					// LYSI:
					// tha vlepoume tin diafora tou: cpuTotalTime-cpuRemainingTime
					// kathe fora pou kanooume mia diergasia add stin cpu etsi wste na min
					// ananewnoume to response time otan cpuTotalTime == cpuRemainingTime

					// ***//

					cpu.removeProcessFromCpu();

					Process forCPU = getReadyList().getProcessToRunInCPU();
					// if (forCPU != null) / forCPU = HeadofQueue (prepei na elegx8ei pio panw etsi kialliws opote dn xriazete)
					{

						if (forCPU.getCpuTotalTime() == forCPU.getCpuTotalTime()) {
							stats.updateResponseTime(currentTime - forCPU.getArrivalTime());// edit.
						}

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

			if (cpuProcess == null) {
				Process forCPU = getReadyList().getProcessToRunInCPU();
				if (forCPU != null) {

					if (forCPU.getCpuTotalTime() == forCPU.getCpuTotalTime()) {
						stats.updateResponseTime(currentTime - forCPU.getArrivalTime());// edit.
					}

					cpu.addProcess(forCPU);

					cpu.setLastProcessStartTime(currentTime); // o xronos pu
																// bike teleutea
																// fora process
																// sti cpu
					if (stats != null)
						stats.updateTotalWaitingTime(forCPU.getArrivalTime()/*-currentTime*/);
				}
			}
		}

		if (stats != null) {
			stats.updateTotalWaitingTime(getReadyList().lengthOfQueue());// edit
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
