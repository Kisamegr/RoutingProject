package emulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Statistics {

	private int numberOfFinishedProcesses;
	private float averageWaitingTime; // of processes waiting to be executed
	private int totalWaitingTime; // total time of waiting
	private int responseTime; // name says it all
	private int maximumLengthOfReadyProcessesList; // name says it all
	public int totalNumberOfProcesses; // current number of processes (i'll be using that)
	private File outputFile; // where the statistics are going to be stored
	private double mytotalwait = 0; // total waiting time for finished processes
	private double myavgwait; // average waiting time
	private int myfinished; // finished processes

	// constructor
	public Statistics(String filename) {

		averageWaitingTime = 0;
		totalWaitingTime = 0;
		responseTime = 0;
		maximumLengthOfReadyProcessesList = 0;
		totalNumberOfProcesses = 0;
		numberOfFinishedProcesses = 0;
		myavgwait = 0;
		myfinished = 0;
		outputFile = new File(filename);

		// i mia epilogi einai ayti: an yparxei to arxeio to diagrafw kai meta
		// ksanaftiaxnw arxeio
		/*
		 * if (outputFile.exists()) { try { outputFile.delete(); outputFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); } }
		 */

		// kai ayti einai i alli epilogi
		if (outputFile.exists()) {
			FileWriter fw;
			try {
				fw = new FileWriter(outputFile.getName());
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				fw.write("Time: " + dateFormat.format(date));
				fw.flush();
				fw.close();
				// eraser.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void updateFinishedNumber(int n) {
		numberOfFinishedProcesses += n;
	}

	public void updatemyfinished() {
		myfinished += 1;
	}

	public void updatetotalwait(Process one, int currentTime) {                          
                                                                                                                    // called whenever a process has 0 remaining time/is finished.
		                                                                                                            // Waiting time for any finished process is : (Current Time - Arrival Time) - Time spent in CPU
		mytotalwait = mytotalwait + ((double) currentTime - (double) one.getArrivalTime() - one.getCpuTotalTime()); // updates total waiting time by
		                                                                                                            // adding the waiting time of the finished process to the total
	}                                                                                                               // 

	public double getAvgWait() {  // calculates the average waiting time , ( number finished processes /  sum of all their waiting times )
		if ((totalNumberOfProcesses > 0) && (myfinished!=0)) {
			System.out.println("NUMBER OF PROCESSES " + myfinished);
			myavgwait = mytotalwait / myfinished;
			return myavgwait;
		} else
			return 0;
	}	

	public void updateTotalWaitingTime(int n) { 
												 
												
		totalWaitingTime += n;
	}

	public void updateResponseTime(int n) {
		responseTime += n;
	}

	public float calculateAverageResponseTime() {
		if (numberOfFinishedProcesses != 0)
			return (float) responseTime / (float) numberOfFinishedProcesses;
		return 0;
	}

	// checking length of list and informs if it is necessary
	// "maximumLengthOfReadyProcessesList"
	public void UpdateMaximumListLength(int currentLength) {

		if (currentLength > maximumLengthOfReadyProcessesList) {
			maximumLengthOfReadyProcessesList = currentLength;
			System.out.println("maximumLengthOfReadyProcessesList updated! New value is :" + maximumLengthOfReadyProcessesList);
		}
	}

	// calculates average time of waiting
	public float CalculateAverageWaitingTime() {
		if (totalNumberOfProcesses != 0) {
			averageWaitingTime = (float) totalWaitingTime / (float) totalNumberOfProcesses;
		}
		return averageWaitingTime;
	}

	// add a new line with the current statistics to the outputFile
	public void WriteStatistics2File(int tickCount) {

		StringBuilder string = new StringBuilder();

		string.append("Tick:" + tickCount + ",");	
		string.append(alignment(string,10));
		string.append("Total waiting time : " + totalWaitingTime + ",");
		string.append(alignment(string,38));
		string.append("Total # of processes: " + totalNumberOfProcesses + ",");
		string.append(alignment(string,65));
		string.append("Average Waiting Time: " + getAvgWait() + ",");
		string.append(alignment(string,111));
		string.append("Finished P.: " + numberOfFinishedProcesses + ",");
		string.append(alignment(string,130));
		string.append("total response time: " + responseTime + ",");
		string.append(alignment(string,156));
		string.append("Average response time: " + calculateAverageResponseTime() + ",");
		string.append(alignment(string,192));
		string.append("Max length readylist: " + maximumLengthOfReadyProcessesList);

		System.out.println(string.toString());

		try {

			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}

			FileWriter fw = new FileWriter(outputFile.getName(), true);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("\r\n" + string.toString());// twra grafei to ena panw sto
													// allokai sto telos deixnei
													// mono to teleytaio

			bw.close();// to anoigokleinei sunexeia .... xmmm

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public StringBuilder alignment(StringBuilder str, int spaces)
	{
		StringBuilder returnString = new StringBuilder();
		int temp=str.length();
		for(int k=0; k<spaces-temp;k++)
		{
			returnString.append(" ");
		}
		return returnString;
	}
}
