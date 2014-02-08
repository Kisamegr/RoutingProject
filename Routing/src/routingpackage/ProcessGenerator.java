package routingpackage;

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

	private File inputFile; // where the data of new processes are going to be
							// stored

	private NewProcessTemporaryList newList;
	private SJFScheduler sjfScheduler;
	private int generationFreq;
	private int generationMax;
	private int generationTime;
	private int maximumBurstTime;
	private Random random;

	private int currentPid;

	// constructor
	public ProcessGenerator(String inputPath, SJFScheduler sjfScheduler) {

		newList = new NewProcessTemporaryList();
		this.sjfScheduler = sjfScheduler;

		generationFreq = 10;
		generationMax = 5;
		generationTime = 0;

		maximumBurstTime = 15;

		if (inputPath != null) {
			inputFile = new File(inputPath);

			List<Process> proc_from_file;
			proc_from_file = parseProcessFile(); // the arrayList is returned in here

			// and then its objects are passed to our newList
			for (Process temp : proc_from_file)
				newList.AddNewProcess(temp);

			newList.printList();
		}

		random = new Random(System.currentTimeMillis());

		currentPid = 0;

	}

	public void runGenerator(int currentTick) {

		// try {

		// Generate new processes every generationFreq clocks
		if (inputFile == null && (currentTick + 1) % getGenerationFreq() == 0 && generationTime < getGenerationMax()) {

			// How many processes to generate
			int processNumber = random.nextInt(generationFreq / 2) + 2;
			Process[] newProcesses = new Process[processNumber];

			// Create each process and add it to the new process list
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

		// Move the first process to the ready list if its arrival time
		// equals the current time
		Process firstNewProcess = newList.peekFirst();
		while (firstNewProcess != null && firstNewProcess.getArrivalTime() == currentTick) {
			newList.getFirst();
			sjfScheduler.addProcessToReadyList(firstNewProcess);

			// ConsoleWindow.getConsole().appendReadyQueueMessage("+RDY: Added process to ready list:");
			// ConsoleWindow.getConsole().appendReadyQueueMessage(firstNewProcess.toString());

			sjfScheduler.addTotalNumberOfProcesses(1);// edit

			firstNewProcess = newList.peekFirst();

		}

		// checks the ready queue and updates, if necessary, the maximumLengthOfReadyProcessesList. edit
		sjfScheduler.totalNumberOfProcessesUpdate();

	}

	// creating new process with pseudo-random characteristics
	public Process createProcess(int currentTick) {

		int arrTime = random.nextInt(generationFreq) + currentTick + 1;
		int burstTime = random.nextInt(maximumBurstTime) + 1;

		Process p = new Process(currentPid, arrTime, burstTime);

		currentPid++;

		return p;
	}

	// Saving new processes' data to inputFile
	public void StoreProcessToFile(Process[] processes, boolean newFile) {

		File outputFile = new File("output.txt");

		if (outputFile.exists() && newFile)
			outputFile.delete();

		try {

			if (!outputFile.exists())
				outputFile.createNewFile();

			FileWriter fw = new FileWriter(outputFile.getName(), true);
			BufferedWriter bw = new BufferedWriter(fw);

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

	// reading inputFile
	public List<Process> parseProcessFile() {
		List<Process> fileList = new ArrayList<Process>(); // initialize array
															// list

		System.out.println(inputFile);
		Scanner scan;
		try {
			Process p;
			scan = new Scanner(inputFile);// create Scanner to scan
											// Integers
			int i = 0, k = 0, arrival_time = -1, burst_cpu_time = -1;
			String line = "", ar[] = { "1", "1", "1" };

			while (scan.hasNextLine()) {
				line = scan.nextLine();

				ar = line.split("-", 3);
				System.out.println(ar[0] + "*" + ar[1] + "*" + ar[2]);
				p = new Process(Integer.valueOf(ar[0]), Integer.valueOf(ar[1]), Integer.valueOf(ar[2]));
				fileList.add(p);

			}

			/*
			 * while (scan.hasNextInt()) { i = scan.nextInt();
			 * 
			 * if (k == 0) // checks if it is in the first column { if ((arrival_time != -1) && (burst_cpu_time != -1))// if the // two // variables // have // a // value // from // the // text // file // then // it // creates // a new // temp // process // and // adds // it to // the // list { p = new Process(currentPid++, arrival_time, burst_cpu_time); fileList.add(p); } arrival_time = i; k = 1; } else if (k == 1)// second column { burst_cpu_time = i; k = 0; }
			 * 
			 * } p = new Process(currentPid++, arrival_time, burst_cpu_time); fileList.add(p);
			 */
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileList; // returs ArrayList
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
}
