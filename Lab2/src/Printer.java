import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Printer extends JFrame implements Runnable
{
	private JTextArea output;
	private Thread waiter;
	
	Printer(JTextArea output, Thread waiter)
	{
		this.output = output;
		this.waiter = waiter;
	}//end of constructor

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		int loopAmount = 50;
		try
		{
			for(int i = 0; i <= loopAmount; i++)
			{
				if(i == (loopAmount/2))
				{
					this.waiter.interrupt();
				}//end of if statement to check to see if it is halfway through
				output.append("Message i = " + i + ", from Thread Printer\n");
				Thread.sleep(1);
			}//end of for loop
		}//end of try
		 catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}//end of catch

	}//end of run

}//end of Printer
