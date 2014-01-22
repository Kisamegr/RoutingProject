package routingpackage;

import java.util.Comparator;
import java.util.PriorityQueue;

public class NewProcessTemporaryList {

	private class NewProcessComparator implements Comparator<Process> {

		@Override
		public int compare(Process o1, Process o2) {

			return o1.getArrivalTime() - o2.getArrivalTime();
		}

	}

	private PriorityQueue<Process> processList; // new processes list

	// constructor

	public NewProcessTemporaryList() {

		processList = new PriorityQueue<>(100, new NewProcessComparator());
	}

	// adding new process
	public void AddNewProcess(Process process) {

		processList.add(process);

	}

	public Process peekFirst() {
		return processList.peek();
	}

	// return first process of the list
	public Process getFirst() {
		return processList.poll();
	}

	// printing list with new processes
	public void printList() {

		ConsoleWindow.getConsole().appendNewListMessage("NewProcessTemporaryList:");
		int k = 1;
		for (Process p : processList) {
			ConsoleWindow.getConsole().appendNewListMessage(p.toString());
			k++;
		}

	}

}
