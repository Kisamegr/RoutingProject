package routingpackage;

public class Process {

	private int pid;
	private int arrivalTime; // arrival time process-> system
	private int cpuTotalTime; // time that process uses the CPU
	private int cpuRemainingTime; // remaining time that process uses the CPU
	private int currentState; // process state (0�Created/New, 1�Ready/Waiting,
								// 2�Running, 3�Terminated)

	// constructor

	public Process(int pid, int arrivalTime, int cpuBurstTime) {
		this.pid = pid;
		this.arrivalTime = arrivalTime;
		this.cpuTotalTime = cpuBurstTime;
		this.cpuRemainingTime = cpuBurstTime;
	}

	// changing the processes' state
	public int setProcessState(int newState) {
		return 0;
	}

	// changing remaining time
	public void changeCpuRemainingTime(int newCpuRemainingTime) {

	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getCpuTotalTime() {
		return cpuTotalTime;
	}

	public void setCpuTotalTime(int cpuTotalTime) {
		this.cpuTotalTime = cpuTotalTime;
	}

	public int getCpuRemainingTime() {
		return cpuRemainingTime;
	}

	public void setCpuRemainingTime(int cpuRemainingTime) {
		this.cpuRemainingTime = cpuRemainingTime;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

}
