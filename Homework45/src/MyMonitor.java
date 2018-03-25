import java.net.Socket;

public class MyMonitor
{
	public class Job
	{
		Socket socket;
		Job(Socket socket)
		{
			this.socket = socket;
		}//end of constructor
	}//end of job class
	
	public class node
	{
		node next;
		node prev;
		Job job;
		node(Job job)
		{
			this.job = job;
		}
	}//end of node
	
	node head;
	node tail;
	int size = 0;
	
	public synchronized void addJob(Socket socket)
	{
		Job job = new Job(socket);
		if(size == 0)
		{
			this.head = new node(job);
			this.tail = this.head;
			this.size++;
		}//end of if
		else
		{
			node temp = new node(job);
			this.head.prev = temp;
			temp.next = this.head;
			this.head = temp;
			this.size++;
		}//end of else
		this.notifyAll();
	}//end of addJob method
	
	public synchronized Job getJob()
	{
		Job jb;
		if(this.size == 0)
		{
			return null;
		}//end of if
		else if(this.size == 1)
		{
			node temp = this.tail;
			this.head = null;
			this.size--;
			jb = temp.job;
		}
		else
		{
			node temp = this.tail;
			this.tail = this.tail.prev;
			temp.prev = null;
			this.tail.next = null;
			this.size--;
			jb = temp.job;
		
		}//end of else statement
		this.notifyAll();
		return jb;
	}//end of getJob
	
	public synchronized int getSize()
	{
		return this.size;
	}
	
}//end of my monitor
