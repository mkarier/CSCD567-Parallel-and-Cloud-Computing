import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server program which accepts requests from clients to
 * capitalize strings.  When clients connect, a new thread is
 * started to handle an interactive dialog in which the client
 * sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform
 * dependent.  If you ran it from a console window with the "java"
 * interpreter, Ctrl+C generally will shut it down.
 */
public class Server {

    /**
     * Application method to run the server runs in an infinite loop
     * listening on port 9898.  When a connection is requested, it
     * spawns a new thread to do the servicing and immediately returns
     * to listening.  The server keeps a unique client number for each
     * client that connects just to show interesting logging
     * messages.  It is certainly not necessary to do this.
     */
    public static void main(String[] args) throws Exception 
    {
        System.out.println("The server is running.");
        MyMonitor jobQueue = new MyMonitor();
        MyThreadPool pool = new MyThreadPool(jobQueue);
        ThreadManager manager = new ThreadManager(5, jobQueue, pool);
        pool.startPool();
        Thread managerThread = new Thread(manager);
        managerThread.start();
        ServerSocket listener = new ServerSocket(9898);
        try 
        {
            while (true) 
            {
                jobQueue.addJob(listener.accept());
                System.out.println("Your client is accepted and is added to the queue");
            }//end while true
        }//end of try 
        finally 
        {
            listener.close();
        }//end of finally
    }//end of main
}
