package routingpackage;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ReadyProcessesList {

	private class ProcessComparator implements Comparator<Process> {

		@Override
		public int compare(Process o1, Process o2) {

			return o1.getCpuRemainingTime() - o2.getCpuRemainingTime();
		}

	}

	private PriorityQueue<Process> processList; // list containing ready
												// processes

	// constructor
	public ReadyProcessesList() {

		processList = new PriorityQueue<Process>(100, new ProcessComparator());

	}
	
	public PriorityQueue<Process> getReadyList()  //return the priority queue, used in statistics to get the current  total waiting time
	{
		return processList;
	}

	public int lengthOfQueue()// edit
	{
		return processList.size();// mporei na exei provlima an einai keno to
									// queue???? --- 8a epistrefei mhden, i think
	}

	// adding new ready process
	public void addProcess(Process item) {

		processList.add(item);
	}

	// return the process that is selected to be executed
	public Process getProcessToRunInCPU() {
		return processList.poll();
	}

	// print list
	public void printList() {

		int k = 1;
		for (Process p : processList) {
			ConsoleWindow.getConsole().appendGeneratorMessage(k + ") Process " + p.getPid());
			k++;
		}
	}
}
