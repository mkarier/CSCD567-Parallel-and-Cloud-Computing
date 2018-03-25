
import java.util.LinkedList;
import java.util.Queue;


public class SharedQueue 
{
	int count = 0;
	int maxCount = 100;
	LinkedList<String> list = new LinkedList<String>();
	boolean readerAlive = true;
	Thread reader;
	Thread searcher;
	
	public synchronized void setReader(Thread reader){this.reader = reader;}
	public synchronized void setSearcher(Thread searcher){this.searcher = searcher;}
	public synchronized boolean isReaderAlive(){return readerAlive;}	
	public synchronized void setReaderState(boolean state){this.readerAlive = state;}
	
	public synchronized boolean enoughToWork()
	{
		int size = list.size();
		if(size <= 1)
			this.notifyAll();
		return size > 0; 
	}//end of enoughToWork()
	
	public synchronized boolean needMore()
	{
		int size = list.size();
		if(size > 0)
			this.notifyAll();
		return size < maxCount;
	}//end of need More
	
	public synchronized void addLast(String line){list.add(line);}//end to addLast
	
	public synchronized String getWork()
	{
		if(list.size() > 0)
			return list.removeFirst();
		else
			return null;
	}//end of getWork
	
	public synchronized void increaseCount(int count){this.count += count;}//end of increaseCount()
	
	public synchronized int getCount(){return this.count;}//end of getCount()
}//end of SharedQueue
