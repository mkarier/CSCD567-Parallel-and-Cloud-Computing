

public class MyPrimeTest 
{

	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		if (args.length < 3) 
		{
			System.out.println("Usage: MyPrimeTest numThread low high \n");
			return;
		}//end of if
		int nthreads = Integer.parseInt(args[0]);
		int low = Integer.parseInt(args[1]);
		int high = Integer.parseInt(args[2]);
		Counter c = new Counter();
		
		//test cost of serial code
		long start = System.currentTimeMillis();
		int numPrimeSerial = SerialPrime.numSerailPrimes(low, high);
		long end = System.currentTimeMillis();
		long timeCostSer = end - start;
		System.out.println("Time cost of serial code: " + timeCostSer + " ms.");
		
		//test of concurrent code
		// **************************************
		// TODO: Write me here
		long paraTime = System.currentTimeMillis();
		int range = high/nthreads;
		int lowest = low;
		Thread[] thread_array = new Thread[nthreads];
		for(int i = 0; i < nthreads; i++)
		{
			thread_array[i] = new Thread(new ThreadPrime(lowest, lowest + range, c));
			thread_array[i].start();
			lowest +=range;
		}//end of for loop
		for(int i = 0; i < nthreads; i++)
		{
			thread_array[i].join();
		}//end of for loop to join the threads
		
		long timeCostCon = System.currentTimeMillis() - paraTime;
		// **************************************
		System.out.println("Time cost of parallel code: " + timeCostCon + " ms.");
		System.out.format("The speedup ration is by using concurrent programming: %5.2f. %n", (double)timeCostSer / timeCostCon);
		
		System.out.println("Number prime found by serial code is: " + numPrimeSerial);
		System.out.println("Number prime found by parallel code is " + c.total());
	}//end of main
		

}//end of MyPrimeTest
