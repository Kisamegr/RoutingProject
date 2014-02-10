package gui;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import emulator.Process;

public class ProcessModel extends DefaultTableModel {

	public ProcessModel() {
		super();

		addColumn("PID");
		addColumn("ARR");
		addColumn("BUR");
		addColumn("REM");
		//
		// Vector<Integer> row = new Vector<>();
		//
		// row.add(4);
		// row.add(4);
		// row.add(5);
		// row.add(3);
		//
		// addRow(row);
		// addRow(row);
		// addRow(row);
		// addRow(row);

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		return Integer.class;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}

	public void addProcess(Process process) {

		Vector<Integer> row = new Vector<>();

		row.add(process.getPid());
		row.add(process.getArrivalTime());
		row.add(process.getCpuTotalTime());
		row.add(process.getCpuRemainingTime());

		this.addRow(row);

	}

	public void addProcesses(Process[] processes) {

		clearProcesses();

		for (Process process : processes)
			addProcess(process);

	}

	public void clearProcesses() {
		while (this.getRowCount() > 0)
			this.removeRow(0);
	}

}