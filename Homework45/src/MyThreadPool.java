import java.io.*;
import java.net.Socket;

public class MyThreadPool
{
	int maxCapacity = 5;
	int freeNumberThreads = 0;
	WorkerThread holders[] = new WorkerThread[maxCapacity];
	boolean stopped = false;
	
	MyMonitor jobQueue;
	
	MyThreadPool(MyMonitor jobQueue){this.jobQueue = jobQueue;}
	public synchronized boolean isStopped(){return this.stopped;}
	private synchronized void setStopped(boolean stop){this.stopped = stop;}
	
	public class WorkerThread extends Thread
	{
		boolean keepAlive = true;
		
		public synchronized void setKeepAlive(boolean keepAlive) {this.keepAlive = keepAlive;}
		
		@Override
		public void run()
		{
			try
			{
				while(!isStopped() && keepAlive)
				{
					try
					{
						MyMonitor.Job jb = jobQueue.getJob();
						if(jb == null)
						{
							//System.out.println("This Thread is going to wait.");
							Thread.sleep(5000);
						}//end of if 
						else
						{
							try
							{
								System.out.println("This thread has a client");
								decreaseNumbThreads();
								Socket client = jb.socket;
								PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
								BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
								writer.println("Your client is now active.");
								while(!isStopped())
								{
									String input = reader.readLine();
									String output = doWork(input);
									if(output != null)
									{
										writer.println(output);
									}
									else
										break;
								}
								client.close();
							}//end of try
							catch(IOException | NullPointerException f)
							{
								System.out.println(f);
								increaseNumbThreads();
								System.out.println("But the thread is still alive");
							}
					
						}//end of else
					}
					catch(IllegalMonitorStateException  e)
					{
						System.out.println(e);
						
					}
				}//end of while
			}//end of try
			catch(InterruptedException e)
			{
				System.out.println("Thread is dying");
			}//end of catch
		}//end of run
		
		private String doWork(String Message)
		{
			//TODO:
			String array[] = Message.split(",");
			String outcome = null;
			if(array[0].equals("KILL"))
			{
				stopPool();
				return null;
			}//end of if
			else if(array.length < 3)
			{
				return "Invalid command";
			}
			int left = Integer.parseInt(array[1]);
			int right = Integer.parseInt(array[2]);
			int answr;
			switch(array[0])
			{
				case "ADD":
						answr = left + right;
						outcome = left + " + " + right + " = " + answr;
						break;
				case "SUB":
						answr = left - right;
						outcome = left + " - " + right + " = " + answr;
						break;
				case "DIV":
						answr = left /right;
						outcome = left + " - " + right + " = " + answr;
						break;
				case "MUL":
						answr = left * right;
						outcome = left + " * " + right + " = " + answr;
						break;
				default:
						outcome = "Invalid command";
						break;
						
			}
			return outcome;
		}
	}//end of inner class
	
	public synchronized void startPool()
	{
		//TODO:start all available threads in the pool and worker threads
		System.out.println("Starting the pool");
		for(int i = 0; i < holders.length; i++)
		{
			if(holders[i] == null)
			{
				holders[i] = new WorkerThread();
				holders[i].start();
				this.freeNumberThreads++;
			}//end of if
		}//end of for loop
	}//end of startPool()
	
	public synchronized void increaseNumbThreads()
	{
		this.freeNumberThreads++;
	}
	
	public synchronized void decreaseNumbThreads()
	{
		this.freeNumberThreads--;
	}
	public void increaseThreadsInPool()
	{
		//TODO: double increase the the threads in the Pool
		WorkerThread nuArray[] = new WorkerThread[maxCapacity *2];
		for(int i = 0; i < holders.length; i++)
		{
			nuArray[i] = this.holders[i];
		}//end of for loop
		this.holders = nuArray;
		this.maxCapacity = this.maxCapacity *2;
		startPool();
	}//end of increaseThreadsInPool
	
	public void decreaseThreadsInPool()
	{
		//TODO: halve the threads in pool according to threshold
		WorkerThread nuArray[] = new WorkerThread[this.maxCapacity/2];
		int i = 0;
		for( ; i < this.holders.length/2; i++)
		{
			nuArray[i] = this.holders[i];
		}//end for loop
		for( ; i < this.holders.length; i++)
		{
			if(this.holders[i] != null)
			{
				try
				{
					this.holders[i].setKeepAlive(false);
					this.holders[i].join();
				}//end of try
				catch(InterruptedException e){}
				this.freeNumberThreads--;
			}//end of if
		}//end of for loop
		this.holders = nuArray;
		this.maxCapacity = this.maxCapacity/2;
	}//end of decreaseThreadsInPool
	
	public synchronized void stopPool()
	{
		//TODO: terminate all threads in the pool gracefully
		//when a send a KILL command is sent through the client to the server
		System.out.println("starting to kill the threads");
		
		setStopped(true);
		for(int i = 0; i < this.holders.length; i++)
		{
			if(this.holders[i] != null)
			{
				try
				{
					this.holders[i].interrupt();
					this.holders[i].join();
				}
				catch(InterruptedException e)
				{
		
				}
				this.freeNumberThreads--;
			}//end of if
		}//end of for;
		System.out.println("Killed all threads");
		System.exit(1);
	}//end of stopPool
	
	public synchronized int numberThreadsRunning()
	{
		//TODO: find the number of threads running
		return this.freeNumberThreads;
	}//end of numberThreadsRUnning
	
	public synchronized int maxCapacity()
	{
		return this.maxCapacity;
	}//end of maxCapacity
	
}//end of MyThreadPool
