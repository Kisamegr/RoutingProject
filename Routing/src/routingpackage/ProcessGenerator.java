package routingpackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ProcessGenerator {

	private File inputFile; // where the data of new processes are going to be
							// stored
	private boolean read_from_file;

	private NewProcessTemporaryList newList;
	private SJFScheduler sjfScheduler;
	private int generationFreq;
	private int generationMax;
	private int generationTime;
	private String firstLine="ProcessId - Arrival Time - Burst Time";
	private Random random;

	private int currentPid;

	// constructor
	public ProcessGenerator(String filename, boolean readFile, SJFScheduler sjfScheduler) {
		// if readfile==false -> new inputFile
		// else opens inputFile

		newList = new NewProcessTemporaryList();
		this.sjfScheduler = sjfScheduler;

		generationFreq = 10;
		generationMax = 20;
		generationTime = 0;

		read_from_file = readFile;
		//START
		try {
		inputFile = new File(filename);
		
		
		if (!inputFile.exists())
		{
				inputFile.createNewFile();
		}
		
		
		if (!read_from_file)
		{
			
			FileWriter fw;
			
				fw = new FileWriter(filename);//
				fw.write(firstLine+"\r\n");
				fw.flush();
				fw.close();
			
		}else generationMax =1;//IMPORTANT. Den moiazei i kaliteri dunati epilogi. Xreiazetai gia na 
								//stamataei meta to telos olwn twn diergasiwn.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//END
		random = new Random(System.currentTimeMillis());

		currentPid = 0;

	}

	public void runGenerator(int currentClock) {

		if (read_from_file == false) {
			
			try {
				FileWriter fw = new FileWriter(inputFile.getName(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				
				// Generate new processes every generationFreq clocks
				if ((currentClock + 1) % getGenerationFreq() == 0 && generationTime < getGenerationMax()) {

					int processNumber= random.nextInt(7) + 2;
					int arrTime ;
					int burstTime ;
					
					for(int i =0 ; i < processNumber; i++){
						arrTime = random.nextInt(getGenerationFreq()) + currentClock + 1;
						burstTime = random.nextInt(15) + 1;
						
						Process p = new Process(currentPid, arrTime, burstTime);
						
						bw.append(String.valueOf(currentPid)+"-");
						bw.append(String.valueOf(arrTime)+"-");
						bw.append(String.valueOf(burstTime)+"\r\n");
						
						currentPid++;
						newList.AddNewProcess(p);
						 
					}//while(i < processNumber);
					newList.printList();
					generationTime++;
				}
				
				
				
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ((read_from_file == true) && (currentClock == -1))// It does this, at the
															// start of the
															// program
		{
			List<Process> proc_from_file;
			proc_from_file = parseProcessFile(); // the arrayList is returned in
													// here
			for (Process temp : proc_from_file) // and then its objects are
												// passed to our newList
			{
				newList.AddNewProcess(temp);
			}
			newList.printList();
		}

		// Move the first process to the ready list if its arrival time
		// equals the current time
		Process firstNewProcess = newList.peekFirst();
		while (firstNewProcess != null && firstNewProcess.getArrivalTime() == currentClock) {
			newList.getFirst();
			sjfScheduler.addProcessToReadyList(firstNewProcess);

			//ConsoleWindow.getConsole().appendReadyQueueMessage("+RDY: Added process to ready list:");
			//ConsoleWindow.getConsole().appendReadyQueueMessage(firstNewProcess.toString());

			sjfScheduler.addTotalNumberOfProcesses(1);// edit

			firstNewProcess = newList.peekFirst();

		}

		sjfScheduler.totalNumberOfProcessesUpdate();// checks the ready queue
													// and updates, if
													// necessary,
													// the
													// maximumLengthOfReadyProcessesList.
													// edit
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
		List<Process> fileList = new ArrayList<Process>(); // initialize array
															// list

		//String filepath = System.getProperty("user.dir") + File.separatorChar + inputFile.getAbsolutePath(); // get
																								// the
																								// directory
																								// of
																								// the
																								// inputFile.txt
		System.out.println(inputFile);
		Scanner scan;
		try {
			Process p;
			scan = new Scanner(inputFile);// create Scanner to scan
													// Integers
			int i = 0, k = 0, arrival_time = -1, burst_cpu_time = -1;
			String line="", ar[] = {"1","1","1"};
			
			while (scan.hasNextLine()){
				line = scan.nextLine();
				if (!line.equals(firstLine))
				{
					
					ar =  line.split("-", 3);
					System.out.println(ar[0]+"*"+ar[1]+"*"+ar[2]);
					p = new Process(Integer.valueOf(ar[0]),Integer.valueOf(ar[1]),Integer.valueOf(ar[2]));
					fileList.add(p);
					
				}
				
			}
			
			
			/*while (scan.hasNextInt()) {
				i = scan.nextInt();

				if (k == 0) // checks if it is in the first column
				{
					if ((arrival_time != -1) && (burst_cpu_time != -1))// if the
																		// two
																		// variables
																		// have
																		// a
																		// value
																		// from
																		// the
																		// text
																		// file
																		// then
																		// it
																		// creates
																		// a new
																		// temp
																		// process
																		// and
																		// adds
																		// it to
																		// the
																		// list
					{
						p = new Process(currentPid++, arrival_time, burst_cpu_time);
						fileList.add(p);
					}
					arrival_time = i;
					k = 1;
				} else if (k == 1)// second column
				{
					burst_cpu_time = i;
					k = 0;
				}

			}
			p = new Process(currentPid++, arrival_time, burst_cpu_time);
			fileList.add(p);*/
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
