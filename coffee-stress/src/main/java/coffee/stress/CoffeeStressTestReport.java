package coffee.stress;

public class CoffeeStressTestReport {

	private static final String URL = "http://localhost:8080/sugar-sample/sample/";
	private static final int NUMBER_OF_REQUESTS = 100;

	public static void main(String[] args) throws InterruptedException {
		CoffeeStressTestReport stressTest = new CoffeeStressTestReport();

		long begin = System.currentTimeMillis();

		for (int i=0;i<200;i++)
			stressTest.doRequestStress();

		long end = System.currentTimeMillis() - begin;

		System.out.println("");
		System.out.println("TOTAL Elapsed Test: " + ((float)end/1000));
	}

	public void doRequestStress() throws InterruptedException {
		RequestData data = new RequestData(URL);
		
		System.out.println("Requesting http://localhost:8080/sample/sample/...");

		long begin = System.currentTimeMillis();
		for (int i=0;i<NUMBER_OF_REQUESTS; i++) {
			RequestThread thread = new RequestThread();
			thread.setData(data);
			thread.start();
		}

		synchronized (data) {
			while (data.getNumberOfRequests() < NUMBER_OF_REQUESTS)
				data.wait();
		}
		
		long elapsedTime = (System.currentTimeMillis()-begin);
		
		System.out.println("Stress Test Report:");
		System.out.println("  >> Number os requests: "+ NUMBER_OF_REQUESTS);
		System.out.println("  >> Request Time Average: "+ ((float)elapsedTime/NUMBER_OF_REQUESTS));
		System.out.println("  >> Elapsed Time (s): "+ ((float)elapsedTime/1000));
		System.out.println("  >> Elapsed Time (ms): " + elapsedTime);
	}

}
