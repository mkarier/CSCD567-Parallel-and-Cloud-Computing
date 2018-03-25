
public class Monitor
{
	private Thread[] threads;
	private int size;
	private int current = -1;
	
	Monitor(Thread[] threads)
	{
		this.threads = threads;
		this.size = threads.length;
	}//end of monitor constructor
	
	public synchronized int getCurrent()
	{
		current = (current +1) % size;
		this.notifyAll();
		return current;
	}//end of synchronized
	
	public synchronized boolean amICurrent(int id)
	{
		return current == id;
	}
}//end o fmonitor class
