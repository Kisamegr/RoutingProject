package routingpackage;

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
	private double mytotalwait = 0; // new one,experiment
	private double myavgwait; // new one, experiment
	private int myfinished; // woooofuckinghoooo

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
				// BufferedWriter eraser = new BufferedWriter(fw);//den nomizw
				// oti exei noima o buffered edw
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

	public void updatetotalwait(Process one, int currentTime) { // wanna add some comments to the git

		mytotalwait = mytotalwait + ((double) currentTime - (double) one.getArrivalTime() - one.getCpuTotalTime());
		System.out.println("MY TOTAL WAIT :" + mytotalwait);
		System.out.println("(CURRENT TIME  - ARRIVAL TIME) - BURST TIME :   " + currentTime + " - " + one.getArrivalTime() + " - " + one.getCpuTotalTime());
	}

	public double getAvgWait() {
		if (totalNumberOfProcesses > 0) {

			System.out.println("NUMBER OF PROCESSES " + myfinished);
			myavgwait = mytotalwait / myfinished;
			return myavgwait;
		} else
			return 0;
	}

	public double getAverageReadyQueueProcessWaitingTime(ReadyProcessesList one, int currentTime) { // to total waiting time ine o sinolikos xronos anamonis twn
		// diergasiwn pu vriskonte ekini tin wra sto ReadyList

		totalWaitingTime = 0; // to total ine kenurio gia ka8e tick tou clock pame se ola ta processes ke to ipologizoume a8roistika ws ekshs:

		for (Process k : one.getReadyList()) {
			totalWaitingTime = totalWaitingTime + (currentTime - k.getArrivalTime()) - (k.getCpuTotalTime() - k.getCpuRemainingTime());
		}

		double avg;
		if (one.getReadyList().size() == 0)
			avg = 0;
		else
			avg = totalWaitingTime / one.getReadyList().size();
		return avg;

	}

	/*
	 * Current - Arrival : su dinei to poso perimenei apo ti stigmh pu bike sto queue sto non-preemptive.
	 * 
	 * Stin periptwsh non-preemptive den iparxei 8ema ke auto arkei gia na ksereis poso ine to waiting time tis diergasias;
	 * 
	 * Stin periptwsh pre-emptive, o sinolikos xronos pu perimene ine o xronos Current - Arrival MEION to xrono pou i diergasia den perimene ke itan mesa sti cpu .
	 * 
	 * Autos o xronos pu itan sti cpu ipologizete apo ton Total xrono Burst pu xriazete MION to remaining xrono.
	 * 
	 * Sto non-preemptive, i deuteri auti aferesi ( total - remaining ) dinei 0 giati i ergasia den epistrefei pote sto queue 2i fora opote to sinoliko tis waiting tote 8a ine apla Current - Arrival. Elpizw na vgazei noima.
	 */

	public void updateTotalWaitingTime(int n) { /*
												 * auto den kserw kata poso ine xrisimo telika , 8a sas eksigisw at skype.
												 */
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

		string.append("Tick:" + tickCount);
		string.append(", Total waiting time : " + totalWaitingTime);
		string.append(", Total # of processes: " + totalNumberOfProcesses);
		string.append(", ALAverage Waiting Time: " + CalculateAverageWaitingTime() /* getAvgWait() */);
		string.append(", ASPAverage Waiting Time: " + /* CalculateAverageWaitingTime() */getAvgWait());
		string.append(", Finished P.: " + numberOfFinishedProcesses);
		string.append(", total respond time: " + responseTime);
		string.append(", Average response time: " + calculateAverageResponseTime());
		string.append(", Max length readylist: " + maximumLengthOfReadyProcessesList);

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
}
