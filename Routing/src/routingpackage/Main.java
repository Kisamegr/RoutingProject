package routingpackage;

import java.awt.EventQueue;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				ConsoleWindow cw = new ConsoleWindow("SJF Emulator");

			}
		});

		// Thread t = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// clock.startClock();
		//
		// }
		// });
		// t.start();
		//
		// try {
		// Thread.sleep(clockSpeed * 2);
		//
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// System.out.println("Main ended");
	}

}
