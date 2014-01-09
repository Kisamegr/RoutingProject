package routingpackage;

public class Process {
	
	private int arrivalTime; //arrival time process-> system
	private int cpuTotalTime; //time that process uses the CPU
	private int cpuRemainingTime; // remaining time that process uses the CPU
	private int currentState; // process state (0–Created/New, 1–Ready/Waiting, 2–Running, 3–Terminated)
	
	//constructor
	
	public Process(int pid, int arrivalTime, int cpuBurstTime)
	{
		
	}
	
	
	//changing the processes' state
	public int setProcessState (int newState)
	{
		return 0;
	}
	
	
	//changing remaining time
	public void changeCpuRemainingTime(int newCpuRemainingTime)
	{
		
	}
	

}
