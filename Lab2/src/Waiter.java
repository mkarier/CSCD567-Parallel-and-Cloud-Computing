import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Waiter extends JFrame implements Runnable
{
	private JTextArea output;
	
	Waiter(JTextArea output)
	{
		this.output = output;
	}//end of Waiter

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		while(true)
		{
			if(Thread.interrupted())
			{
				this.output.append("Printer got his work half done!\n");
				this.output.append("Waiter has done its work, terminating.\n");
				return;
			}//end of if
		}//end of while

	}//end of run

}//end of Waiter
