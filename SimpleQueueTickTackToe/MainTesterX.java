

import javax.swing.JFrame;

public class MainTesterX
{

	public static void main(String[] args) throws Exception
	{
		// TODO Auto-generated method stub
		 TicTacToeClient application; // declare client application

	     // if no command line args
	     if ( args.length == 0 )
	        application = new TicTacToeClient( "X" ); //  
	     else
	        application = new TicTacToeClient( args[ 0 ] ); // use args

	     application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

}
