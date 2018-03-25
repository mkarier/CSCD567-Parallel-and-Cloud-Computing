import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Reader implements Runnable
{
	String fname;
	SharedQueue q;
	
	Reader(String fname, SharedQueue q)
	{
		this.fname = fname;
		this.q = q;
	}//Reader
	
	public void run()
	{
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(this.fname));
			String input;
			while ((input = reader.readLine()) != null)
			{
				try
				{
					while (!q.needMore())
						this.wait();
					q.addLast(input);
				} //end try 
				catch(InterruptedException | IllegalMonitorStateException e)
				{
					// TODO Auto-generated catch block
					//System.out.println("Something to know where i am" +e.getCause());
				} //end catch
			}//end of while to read
			reader.close();
		}//end of try
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//end of while loop
		q.setReaderState(false);
	}//end run()
}//end Reader
