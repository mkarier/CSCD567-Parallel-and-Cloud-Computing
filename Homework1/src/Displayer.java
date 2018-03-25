import javax.swing.JTextArea;

public class Displayer implements Runnable
{
	private JTextArea output;
	private String message;
	
	Displayer(JTextArea output, String message)
	{
		this.output = output;
		this.message = message;
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			while(true)
			{
				output.append(message);
				if(Thread.interrupted())
				{
					output.append("Displayer was interrupted\n");
					return;
				}//end of if
				else
					Thread.sleep(2000);
			}//end of while
		}//end of try
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return;
		}
		
	}//end of run

}//end of displayer class
