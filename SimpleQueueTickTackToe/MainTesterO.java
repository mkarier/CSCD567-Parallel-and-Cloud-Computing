import javax.swing.JFrame;

public class MainTesterO
{
	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		 TicTacToeClient application; // declare client application
	
	     // if no command line args
	     if ( args.length == 0 )
	        application = new TicTacToeClient( "O" ); // 
	     else
	        application = new TicTacToeClient( args[ 0 ] ); // use args
	
	     application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}