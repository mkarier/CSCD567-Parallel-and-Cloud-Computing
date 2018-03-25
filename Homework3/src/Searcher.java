import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Searcher extends Thread implements Runnable
{
	Pattern pattern;
	SharedQueue q;
	
	Searcher(String pattern, SharedQueue q)
	{
		this.pattern = Pattern.compile(pattern);
		this.q = q;
	}//end of searcher pattern
	
	public void run()
	{
		while(q.isReaderAlive() || q.enoughToWork())
		{
			try
			{
				while(!q.enoughToWork())
					this.wait();
				String line = q.getWork(); 
				Matcher match = this.pattern.matcher(line);
				int count = 0;
				while(match.find())
					count++;
				q.increaseCount(count);
			}
			catch(InterruptedException | IllegalMonitorStateException e)
			{
				//System.out.println(e.getCause());
			}//end of catch
		}//end of while loop
		String line = q.getWork(); 
		if(line != null)
		{
			Matcher match = this.pattern.matcher(line);
			int count = 0;
			while(match.find())
				count++;
			q.increaseCount(count);
		}//end of if
		
	}//end of run()
	
}//end of Searcher class
