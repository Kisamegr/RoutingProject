package routingpackage;

import java.io.File;

public class Statistics {


	private float averageWaitingTime; //of processes waiting to be executed
	private int totalWaitingTime; //total time of waiting
	private int responseTime; //name says it all
	private int maximumLengthOfReadyProcessesList; //name says it all
	public int totalNumberOfProcesses; //current number of processes
	private File outputFile; //where the statistics are going to be stored
	
	
	//contructor
	public Statistics(String filename)
	{
		
	}

	//checking length of list and informs if it is necessary "maximumLengthOfReadyProcessesList" 
	public void UpdateMaximumListLength()
	{
		
	}

	
	//calculates average time of waiting
	public float CalculateAverageWaitingTime()
	{
		return 0;
	}
	
	
	//add a new line with the current statistics to the outputFile
	public void WriteStatistics2File()
	{
		
	}
}
