
public class Counter 
{
	private int c = 0;

    public synchronized void increment( int n) 
    {
        //TODO: write me here
    	c += n;
    }//end of increment class

    public synchronized int total() 
    {
        //TODO: write me here
    	
    	return c;
    }//end of total class
}//end of Counter class
