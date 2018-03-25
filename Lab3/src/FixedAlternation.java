
public class FixedAlternation
{

	boolean isT1turn;
	final Object lock = new Object();
	Thread t1;
	Thread t2;
	
	public FixedAlternation() 
	{
		
		isT1turn = true;
		t1 = new Thread(new Runnable() 
		{
	        @Override
	        public void run() 
	        {
	        	for (int i = 1; i <= 50; i += 2)  
                {
	        		//while( ! isT1turn ); 
	        		synchronized(lock)
                	{
                		 //guarded block	
	                	try
	                	{
		                    System.out.println("T1=" + i);
		                    isT1turn = false;
		                    lock.notifyAll();
		                    lock.wait();
	                	}
	                	catch (Exception e) {System.out.println(3);}
                	}//end of for
                }//end of sync
	        }//end of run method
	    });//end of create thread 1 runnable method
	    t2 = new Thread(new Runnable() 
	    {

	        @Override
	        public void run() 
	        {
                for (int i = 2; i <= 50; i += 2)
                {
                	//while (isT1turn);//guarded block
                	synchronized (lock) 
                	{
                		   
	                    try
	                    {
	                    	//lock.wait();
		                    System.out.println("T2=" + i);
		                    isT1turn = true;
		                    lock.notifyAll();
		                    lock.wait();
	                    }
	                    catch(Exception e) {System.out.println(e);}
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
		new FixedAlternation();
	}

}
