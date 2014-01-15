package routingpackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Statistics {

	private int numberOfFinishedProcesses;
	private float averageWaitingTime; // of processes waiting to be executed
	private int totalWaitingTime; // total time of waiting
	private int responseTime; // name says it all
	private int maximumLengthOfReadyProcessesList; // name says it all
	public int totalNumberOfProcesses; // current number of processes
	private File outputFile; // where the statistics are going to be stored

	// constructor
	public Statistics(String filename) {

		averageWaitingTime = 0;
		totalWaitingTime = 0;
		responseTime = 0;
		maximumLengthOfReadyProcessesList = 0;
		totalNumberOfProcesses = 0;
		numberOfFinishedProcesses  = 0;
		
			
		outputFile = new File(filename);
		
		
	}
	
	public void updateFinishedNumber(int n)
	{
		numberOfFinishedProcesses += n;
	}
	
	public void updateTotalWaitingTime(int n)
	{
		totalWaitingTime += n;
	}
	
	public void updateResponseTime(int n)
	{
		responseTime += n;
	}
	
	public float calculateAverageResponseTime()
	{
		if (numberOfFinishedProcesses != 0)
			return   (float)responseTime/ (float)numberOfFinishedProcesses;
		return 0;
	}

	// checking length of list and informs if it is necessary
	// "maximumLengthOfReadyProcessesList"
	public void UpdateMaximumListLength(int currentLength) {
		
		if (currentLength>maximumLengthOfReadyProcessesList)
		{
			maximumLengthOfReadyProcessesList = currentLength;
			System.out.println("maximumLengthOfReadyProcessesList updated! New value is :"+maximumLengthOfReadyProcessesList);
		}
	}

	// calculates average time of waiting
	public float CalculateAverageWaitingTime() {
		if(totalNumberOfProcesses != 0)
		{
			averageWaitingTime = (float)totalWaitingTime / (float)totalNumberOfProcesses;
		}
		return averageWaitingTime;
	}

	// add a new line with the current statistics to the outputFile
	public void WriteStatistics2File() {
		
		StringBuilder string = new StringBuilder();
		
		string.append("Total waiting time : " + totalWaitingTime);
		string.append(", Total # of processes: " +totalNumberOfProcesses );
		string.append(", Average Waiting Time: " +CalculateAverageWaitingTime() );
		string.append(", Finished P.: " + numberOfFinishedProcesses);
		string.append(", total respond time: " + responseTime);
		string.append(", Average response time: " + calculateAverageResponseTime());
		string.append(", Max length readylist: " + maximumLengthOfReadyProcessesList);
		

		System.out.println( string.toString());
		
		try {
			
			
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			
			
			FileWriter fw = new FileWriter(outputFile.getName(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("\n"+ string.toString());//twra grafei to ena panw sto allokai sto telos deixnei mono to teleytaio
			
			bw.close();//to anoigokleinei sunexeia .... xmmm
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
