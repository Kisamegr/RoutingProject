package emulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ProcessGenerator {

	private File inputFile;
	private NewProcessTemporaryList newList;
	private SJFScheduler sjfScheduler;
	private int generationFreq;
	private int generationMax;
	private int generationTime;
	private int maxBurstTime;
	private int minBurstTime;
	private int maxGenProc;
	private int minGenProc;
	private Random random;
	private int currentPid;

	// Constructor for reading the input
	public ProcessGenerator(String inputPath, SJFScheduler sjfScheduler) {

		this.sjfScheduler = sjfScheduler;
		initialize();

		inputFile = new File(inputPath);

		// Parse the input file and get the processes
		List<Process> proc_from_file;
		proc_from_file = parseProcessFile();

		// Then pass the processes to the newList
		for (Process temp : proc_from_file)
			newList.AddNewProcess(temp);

	}

	// Constructor for generating new processes and writing to the output file
	public ProcessGenerator(int generationFreq, int generationMax, int maxBurstTime, int minBurstTime, int maxGenProc, int minGenProc, SJFScheduler sjfScheduler) {

		this.sjfScheduler = sjfScheduler;
		initialize();

		this.generationFreq = generationFreq;
		this.generationMax = generationMax;
		this.maxBurstTime = maxBurstTime;
		this.minBurstTime = minBurstTime;
		this.maxGenProc = maxGenProc;
		this.minGenProc = minGenProc;

	}

	// Basic initialization
	private void initialize() {
		newList = new NewProcessTemporaryList();
		random = new Random(System.currentTimeMillis());
		currentPid = 0;
	}

	// The main function, called in every tick of the clock
	// Does 2 things:
	// 1: IF GENERATION IS ENABLED, it generates new processes, puts them in the new list, and writes them to the output file
	// 2: Checks if the any process in the top of the new list needs to be send to the ready process list, in order to simulate the arrival time.
	public void runGenerator(int currentTick) {

		// Generate new processes every generationFreq clocks
		if (inputFile == null && (currentTick + 1) % getGenerationFreq() == 0 && generationTime < getGenerationMax()) {

			// How many processes to generate, calculated randomly
			int processNumber = random.nextInt(maxGenProc - minGenProc) + minGenProc;
			Process[] newProcesses = new Process[processNumber];

			// Create a random process and add it to the new process list
			for (int i = 0; i < processNumber; i++) {
				Process p = createProcess(currentTick);
				newList.AddNewProcess(p);
				newProcesses[i] = p;
			}

			// Store the new generated processes to the output file
			if (generationTime == 0)
				StoreProcessToFile(newProcesses, true);
			else
				StoreProcessToFile(newProcesses, false);

			newList.printList();
			generationTime++;
		}

		if (currentTick == -1 && inputFile != null) {
			newList.printList();
		}

		// Move the first process to the ready list if its arrival time
		// equals the current time
		Process firstNewProcess = newList.peekFirst();
		while (firstNewProcess != null && firstNewProcess.getArrivalTime() == currentTick) {
			newList.getFirst();
			sjfScheduler.addProcessToReadyList(firstNewProcess);

			// Window.getConsole().appendReadyQueueMessage("+RDY: Added process to ready list:");
			// Window.getConsole().appendReadyQueueMessage(firstNewProcess.toString());

			sjfScheduler.addTotalNumberOfProcesses(1);// edit

			firstNewProcess = newList.peekFirst();

		}

		// checks the ready queue and updates, if necessary, the maximumLengthOfReadyProcessesList.
		sjfScheduler.totalNumberOfProcessesUpdate();

	}

	// Creates a new process with pseudo-random characteristics
	public Process createProcess(int currentTick) {

		// Calculate the arrival and burst time randomly
		int arrTime = random.nextInt(generationFreq) + currentTick + 1;
		int burstTime = random.nextInt(maxBurstTime) + minBurstTime;

		Process p = new Process(currentPid, arrTime, burstTime);

		currentPid++;

		return p;
	}

	// Saving the new processes to the output file
	// The data is stored with 3 integers, separated with '-'
	// The 3 integers, in their respective order, are: PID - ARRIVAL TIME - BURST TIME
	public void StoreProcessToFile(Process[] processes, boolean newFile) {

		File outputFile = new File("output.txt");

		// If an old output file exists, delete it
		if (outputFile.exists() && newFile)
			outputFile.delete();

		try {

			if (!outputFile.exists())
				outputFile.createNewFile();

			FileWriter fw = new FileWriter(outputFile.getName(), true);
			BufferedWriter bw = new BufferedWriter(fw);

			// Write each process
			for (int i = 0; i < processes.length; i++) {

				bw.append(String.valueOf(processes[i].getPid()) + "-");
				bw.append(String.valueOf(processes[i].getArrivalTime()) + "-");
				bw.append(String.valueOf(processes[i].getCpuTotalTime()) + "\r\n");
			}

			fw.flush();
			bw.flush();
			fw.close();
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Reading the input file
	// The data is stored with 3 integers, separated with '-'
	// The 3 integers, in their respective order, are: PID - ARRIVAL TIME - BURST TIME
	public List<Process> parseProcessFile() {
		List<Process> fileList = new ArrayList<Process>();

		Scanner scan;
		try {
			Process p;

			// Create Scanner to scan Integers
			scan = new Scanner(inputFile);

			String line = "", ar[] = { "1", "1", "1" };

			while (scan.hasNextLine()) {
				line = scan.nextLine();

				ar = line.split("-", 3);
				System.out.println(ar[0] + "*" + ar[1] + "*" + ar[2]);
				p = new Process(Integer.valueOf(ar[0]), Integer.valueOf(ar[1]), Integer.valueOf(ar[2]));
				fileList.add(p);

			}

			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileList; // returns ArrayList
	}

	public int getGenerationFreq() {
		return generationFreq;
	}

	public void setGenerationFreq(int generationFreq) {
		this.generationFreq = generationFreq;
	}

	public int getGenerationMax() {
		return generationMax;
	}

	public void setGenerationMax(int generationMax) {
		this.generationMax = generationMax;
	}

	public int getRandomArrivalTime() {
		return 2;
	}

	public int getNewlistLength() {
		return newList.lengthOfQueue();
	}

	public NewProcessTemporaryList getNewList() {
		return newList;
	}

}
