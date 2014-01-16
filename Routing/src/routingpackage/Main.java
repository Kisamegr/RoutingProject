package routingpackage;

import java.io.File;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String filepath = System.getProperty("user.dir") + File.separatorChar + "Out.txt";
		System.out.println(filepath);
		CPU cpu = new CPU();
		Statistics stats = new Statistics(filepath);
		SJFScheduler sjfScheduler = new SJFScheduler(false, cpu,stats);
		ProcessGenerator generator = new ProcessGenerator("output.txt", false, sjfScheduler);
		
		
		int clockSpeed = 100;
		final Clock clock = new Clock(clockSpeed, generator, sjfScheduler, cpu);

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				clock.startClock();

			}
		});
		t.start();

		try {
			Thread.sleep(clockSpeed * 2);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Main ended");
	}

}
