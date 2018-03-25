
public class Alternation
{
	private class Condition
	{
		private boolean isT1turn = true;
		
		private synchronized boolean isT1Turn() {
			return isT1turn;
		}
		
		private synchronized void setT1Turn(boolean cond) {
			isT1turn = cond;
		}//end of setT1Turn
	}//end of condtion class
	
	Condition condition = new Condition();
	final Object lock = new Object();
	Thread t1;
	Thread t2;
	
	public Alternation() 
	{
		
		t1 = new Thread(new Runnable() 
		{
	        @Override
	        public void run() 
	        {
                for (int i = 1; i <= 50; i += 2) 
                {
                	while( !condition.isT1Turn() );//guarded block
                	//synchronized (lock) 
                	{
                		
                    	System.out.println("T1=" + i); 
                    	condition.setT1Turn(false);
	                    try 
	                    {
	                    	Thread.sleep(1000);
	                    } catch (InterruptedException e) {}
                	}//end of sync
                }//end of for
	        }//end of run method
	    });//end of create thread 1 runable method
	    t2 = new Thread(new Runnable() 
	    {

	        @Override
	        public void run() 
	        {
                for (int i = 2; i <= 50; i += 2)
                {
                	while (condition.isT1turn); //guarded block
                	//synchronized (lock) 
                	{
                		
   	                   
   	                    System.out.println("T2=" + i);
   	                    condition.setT1Turn(true);
                        try 
                        {
                        	Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                     
                	}//end of sync
                }//end of for loop
	        }//end of run method
	    });//end of create thread 2 with runnable
	    t1.start();
	    t2.start();
	}//end of create Alternation

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		new Alternation();
	}

}
