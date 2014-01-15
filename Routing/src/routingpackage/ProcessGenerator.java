package routingpackage;

import java.io.File;
import java.util.List;
import java.util.Random;

public class ProcessGenerator {

	private File inputFile; // where the data of new processes are going to be
							// stored
	private NewProcessTemporaryList newList;
	private SJFScheduler sjfScheduler;
	private int generationFreq;

	private Random random;

	private int currentPid;

	// constructor
	public ProcessGenerator(String filename, boolean readFile, SJFScheduler sjfScheduler) {
		// if readfile==false -> new inputFile
		// else opens inputFile

		newList = new NewProcessTemporaryList();
		this.sjfScheduler = sjfScheduler;

		generationFreq = 10;

		random = new Random(System.currentTimeMillis());

		currentPid = 0;

	}

	public void runGenerator(int currentClock) {

		// Generate new processes every generationFreq clocks
		if ((currentClock + 1) % generationFreq == 0) {
			int processNumber = random.nextInt(7) + 2;

			for (int i = 0; i < processNumber; i++) {

				Process p = new Process(currentPid++, random.nextInt(generationFreq) + currentClock + 1, random.nextInt(15) + 1);
				newList.AddNewProcess(p);

			}

			newList.printList();
		}

		// Move the first process to the ready list if its arrival time
		// equals the current time
		Process firstNewProcess = newList.peekFirst();
		while (firstNewProcess != null && firstNewProcess.getArrivalTime() == currentClock) {
			newList.getFirst();
			sjfScheduler.addProcessToReadyList(firstNewProcess);

			System.out.println("+RDY: Added process to ready list:");
			System.out.println(firstNewProcess.toString());
			
			sjfScheduler.addTotalNumberOfProcesses(1);//edit
			
			firstNewProcess = newList.peekFirst();

		}
		
		sjfScheduler.totalNumberOfProcessesUpdate();//checks the ready queue and updates, if necessary, 
													//the maximumLengthOfReadyProcessesList.
													//edit
	}

	// creating new process with pseudo-random characteristics
	public Process createProcess() {
		return null;
	}

	// Saving new processes' data to inputFile
	public void StoreProcessToFile() {

	}

	// reading inputFile
	public List<Process> parseProcessFile() {
		return null;
	}
}
