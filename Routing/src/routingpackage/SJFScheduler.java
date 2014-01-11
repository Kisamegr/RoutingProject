package routingpackage;

public class SJFScheduler {

	private boolean isPreemptive; // if true then the router is preemptive

	// constructor
	SJFScheduler(boolean isPreemptive) {

		this.isPreemptive = isPreemptive;
	}

	// adding a process to the right position of the "ready processes list"
	public void addProcessToReadyList(Process process) {

	}

	// executes process' swap in the CPU based on the "ready processes list" and
	// the routing algorithm(preemptive or non-preemptive)
	public void SJF() {

	}
}
