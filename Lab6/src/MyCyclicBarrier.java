import java.util.concurrent.BrokenBarrierException;

public class MyCyclicBarrier
{
	int parties;
	Runnable barrierAction;
	int notReached;
	boolean inCycle = false, broken = false;
	
	MyCyclicBarrier(int parties, Runnable barrierAction)
	{
		this.parties = parties;
		this.barrierAction = barrierAction;
		this.notReached = parties;
	}//end of constructor
	
	public synchronized int await() throws InterruptedException, BrokenBarrierException
	{
		int arrived_rank = parties - notReached;
		while(inCycle == false && notReached != parties )
		{
			System.out.println("first while");
				wait();
			
		}//end of while loop*/
		
		if(inCycle == false)
			inCycle = true;
		if(notReached > 1)
		{
				notReached--;
			while(inCycle)
			{
				try
				{
					wait();
				}
				catch(InterruptedException e)
				{
					this.broken = true;
					throw e;
				}
				if(this.broken)
					throw new BrokenBarrierException();
				if(inCycle == false)
					notReached++;
				if(notReached == parties)
					notifyAll();
			}//end of while
		}//end of if notReached
		else if(notReached == 1)
		{
			inCycle = false;
			barrierAction.run();
			notifyAll();
		}
		
		return arrived_rank;
	}//end of await
}//end of MyCyclicBarrior
