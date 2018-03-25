

public class ThreadPrime extends Thread 
{
	private int low;
	private int high;
	private int numFound = 0;
	private Counter c;
	
	// each thread only  takes care of one subrange (low, high)
	public ThreadPrime (int lowLocal, int highLocal, Counter ct) 
	{
		this.low = lowLocal;
		this.high = highLocal;
		c = ct;
	}//end of ThreadPrime constructions;

	private boolean checkPrime(int n)
	{
		if (n%2==0) return false;
	    //if not, then just check the odds
	    for(int i=3;i*i<=n;i+=2) 
	    {
	        if(n%i==0)
	            return false;
	    }//end of for loop
	    return true;
    }
	public void run()
	{
		//TODO: write me here
		for(int i = this.low; i <= this.high; i++)
		{
			if(checkPrime(i))
			{
				numFound++;
			}//end of if statement
		}//end of for loop
		c.increment(numFound);
	}//end of run
		
}//end of ThreadPrime class
