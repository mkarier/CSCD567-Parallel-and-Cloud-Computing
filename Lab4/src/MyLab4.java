
public class MyLab4 extends Thread
{
	
	private static Monitor monitor;
	int numberLoop = 10;
	int id;
	
	public static void setLock(Monitor lock)
	{
		MyLab4.monitor = lock;
	}
	
	
	MyLab4(int id, int numbThreads)
	{
		this.id = id;
	}//end of constructor
	
	public void run()
	{
		for(int i = 1; i <= numberLoop; i++)
		{
			
				while(!monitor.amICurrent(this.id));
				{
					try
					{
						this.wait();
					}
					catch(Exception e)
					{
						
					}
				}
				System.out.println(i + " message from thread " + id);
				monitor.getCurrent();
			
		}//end of for loop to make sure that it goes through them all
	}//end of run method
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		if(args.length < 1)
		{
			System.out.println("Usage: MyLab4 numbThreads ");
			return;
		}//end of if statement to make sure there was enough args
		
		int numThread = Integer.parseInt(args[0]);
		Thread[] threads = new Thread[numThread];
		//threads[0] = new Thread(new MyLab4(0, numThread));
		//threads[0].start();
		MyLab4.setLock( new Monitor(threads));
		for(int i = 0; i < numThread; i++)
		{
			//TODO:
			threads[i] = new Thread(new MyLab4(i, numThread));
			threads[i].start();
		}//end of for loop
		MyLab4.monitor.getCurrent();
		
	}//end of main

}//end of class
