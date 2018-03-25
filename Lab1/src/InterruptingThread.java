import java.io.*;

import javax.swing.JTextArea;

public class InterruptingThread implements Runnable
{
	private int id;
	JTextArea output;
	
	InterruptingThread(int id, JTextArea output)
	{
		this.id = id;
		this.output = output;
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		try
		{
			while(true)
			{
				output.append("Message from Thread-->Thread-" + this.id+"\n");
				if(Thread.interrupted())
					throw new InterruptedException();
				else
					Thread.sleep(1000);
			}//end of while
		}//end of try
		catch(InterruptedException e)
		{
			output.append("thread " + this.id +" was interrrupted\n");
			return;
		}//end of catch
	}//end of run method	
}//end of InterrruptingThread