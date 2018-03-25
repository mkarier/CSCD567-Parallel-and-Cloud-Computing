import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client
{
	boolean stillRunning = true;
	
	public synchronized boolean  getIfRunning(){return this.stillRunning;}
	public synchronized void setIfRunning(boolean state){this.stillRunning = state;}
	class Reader implements Runnable
	{
		BufferedReader in;
		Reader(BufferedReader in){this.in = in;}
		
		public void run()
		{
			String message = "";
			try
			{
				while(!(message = in.readLine()).equals(null) && getIfRunning())
				{
					System.out.println(message);
				}
			} catch (IOException | NullPointerException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setIfRunning(false);
			System.exit(1);
		}
		
	}//end of reader class
	
	class Writer implements Runnable
	{
		PrintWriter out;
		Writer(PrintWriter out){this.out = out;}
		
		public void run()
		{
			String message = "";
			Scanner kb = new Scanner(System.in);
			while((message = kb.nextLine())!=null && getIfRunning())
			{
				if(message.equals("."))
				{
					setIfRunning(false);
					System.exit(1);
				}
				out.println(message);
			}//end of while loop
			setIfRunning(false);
			System.exit(1);
		}//end of run method
	}//end of Writer class

	
    
    
    public static void main(String[] args)
    {
    	if(args.length < 1)
    	{
    		System.out.println("You need to enter the severAddress");
    		System.exit(1);
    	}//end of if
    	try
    	{
	    	Socket socket = new Socket(args[0], 9898);
		    BufferedReader in = new BufferedReader(
		                new InputStreamReader(socket.getInputStream()));
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    Client cl = new Client();
		    
		    Thread writer = new Thread(cl.new Writer(out));
		    Thread reader = new Thread(cl.new Reader(in));
		    
		    writer.start();
		    reader.start();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }//end of main

}//end of Client class
