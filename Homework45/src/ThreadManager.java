
public class ThreadManager implements Runnable
{
	int time;
	MyMonitor jobs;
	MyThreadPool pool;
	
	ThreadManager(int time, MyMonitor jobs, MyThreadPool pool)
	{
		this.time = time;
		this.jobs = jobs;
		this.pool = pool;
	}//end of ThreadManager

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			while(!pool.isStopped())
			{
				Thread.sleep(time);
				int max = pool.maxCapacity();
				int nJobs = jobs.getSize();
				int nRunning = pool.numberThreadsRunning();
				System.out.println("max = " + max);
				if(max == 5)
				{
					if(nJobs != 0 && nRunning < 2)
						pool.increaseThreadsInPool();
				}
				else if(max == 10)
				{
					if(nJobs != 0 && nJobs - nRunning < 2)
						pool.increaseThreadsInPool();
					else if(nRunning > max/2)
						pool.decreaseThreadsInPool()
				;
				}
				else if(max == 20)
				{
					if(nRunning - nJobs > max/2)
						pool.decreaseThreadsInPool();
				}
				
			}//end of while loop
		}//end of try
		catch(InterruptedException e)
		{
			
		}
		
	}
	
}//end of ThreadManager