
// Fig. 24.15: TicTacToeClient.java
// Client that let a user play Tic-Tac-Toe with another across a network.
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*; 

import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.Map;

public class TicTacToeClient extends JFrame implements Runnable 
{
   private JTextField idField; // textfield to display player's mark
   private JTextArea displayArea; // JTextArea to display output
   private JPanel boardPanel; // panel for tic-tac-toe board
   private JPanel panel2; // panel to hold board
   private Square board[][]; // tic-tac-toe board
   private Square currentSquare; // current square
   private String myMark; // this client's mark
   private String notMyMark;
   private String winningMark;
   private boolean myTurn; // determines which client's turn it is
   private final String X_MARK = "X"; // mark for first client
   private final String O_MARK = "O"; // mark for second client
   private static final int bsize = 16;
   private String[][] array_board = new String[bsize][bsize];
   
   private boolean isGameOver = false;
   private AmazonSQS My_Q; //= AmazonSQSClientBuilder.defaultClient();
   private AmazonSQS notMy_Q;
   private static final String Q_NAME_X = "TicTacToeQueueX.fifo";
   private static final String Q_NAME_O = "TicTacToeQueueO.fifo";
   private static final String messageGroup = "TicTacToeQueueMessageGroup";
   private String My_URL;
   private String notMy_URL;
   private ReceiveMessageRequest receiveMessageRequest;
   private SendMessageRequest sendMessageRequest;
   private List<Message>  messages;
   
   public static BasicAWSCredentials awsCreds = new BasicAWSCredentials(access_key, secret_key);
   // set up user-interface and board
   public TicTacToeClient( String host ) throws Exception
   { 
      displayArea = new JTextArea( 4, 30 ); // set up JTextArea
      displayArea.setEditable( false );
      add( new JScrollPane( displayArea ), BorderLayout.SOUTH );

      boardPanel = new JPanel(); // set up panel for squares in board
      boardPanel.setLayout( new GridLayout( bsize, bsize, 0, 0 ) ); //was 3

      board = new Square[ bsize ][ bsize ]; // create board

      // loop over the rows in the board
      for ( int row = 0; row < board.length; row++ ) 
      {
         // loop over the columns in the board
         for ( int column = 0; column < board[ row ].length; column++ ) 
         {
            // create square. initially the symbol on each square is a white space.
            board[ row ][ column ] = new Square( " ", row * bsize + column );
            boardPanel.add( board[ row ][ column ] ); // add square       
         } // end inner for
      } // end outer for

      idField = new JTextField(); // set up textfield
      idField.setEditable( false );
      add( idField, BorderLayout.NORTH );
      
      panel2 = new JPanel(); // set up panel to contain boardPanel
      panel2.add( boardPanel, BorderLayout.CENTER ); // add board panel
      add( panel2, BorderLayout.CENTER ); // add container panel

      setSize( 600, 600 ); // set size of window
      setVisible( true ); // show window
      if(host.equals("X"))
      {
    	  myMark = X_MARK;
    	  notMyMark = O_MARK;
      }
      else if(host.equals("O"))
      {
    	  myMark = O_MARK;
    	  notMyMark = X_MARK;
      }
      else
   	   throw new Exception("not a vailid mark");
      startClient();
   } // end TicTacToeClient constructor

   // start the client thread
   public void startClient()
   {  
      // create and start worker thread for this client
	   My_Q = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-west-2").build();
	   notMy_Q = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-west-2").build();
	  /* ListQueuesResult lq_result = sqs.listQueues();
	   System.out.println("Your SQS Queue URLs:");
	   for (String url : lq_result.getQueueUrls()) {
	       System.out.println(url);
	   }//*/

	     
      ExecutorService worker = Executors.newFixedThreadPool( 1 );
      worker.execute( this ); // execute client
   } // end method startClient

   // control thread that allows continuous update of displayArea
   @Override
   public void run()
   {
	   try 
	   {
		   
		   Map<String, String> attributes = new HashMap<String, String>();
           // A FIFO queue must have the FifoQueue attribute set to True
           attributes.put("FifoQueue", "true");
           // Generate a MessageDeduplicationId based on the content, if the user doesn't provide a MessageDeduplicationId
           attributes.put("ContentBasedDeduplication", "true");
           // The FIFO queue name must end with the .fifo suffix
          // attributes.put("ReceiveMessageWaitTimeSeconds", "20");
           if(myMark.equals(X_MARK))
           {
        	   CreateQueueRequest createQueueRequest = new CreateQueueRequest(Q_NAME_X).withAttributes(attributes);
        	   My_URL= My_Q.createQueue(createQueueRequest).getQueueUrl();
        	   createQueueRequest = new CreateQueueRequest(Q_NAME_O).withAttributes(attributes);
        	   notMy_URL = notMy_Q.createQueue(createQueueRequest).getQueueUrl();
        	   receiveMessageRequest = new ReceiveMessageRequest(notMy_URL);
        	   myTurn = true;
           }
           else if(myMark.equals(O_MARK))
           {
        	   CreateQueueRequest createQueueRequest = new CreateQueueRequest(Q_NAME_O).withAttributes(attributes);
        	   My_URL = My_Q.createQueue(createQueueRequest).getQueueUrl();
        	   createQueueRequest = new CreateQueueRequest(Q_NAME_X).withAttributes(attributes);
        	   notMy_URL = notMy_Q.createQueue(createQueueRequest).getQueueUrl();
        	   receiveMessageRequest = new ReceiveMessageRequest(notMy_URL);
        	   myTurn = false;
           }
           
          // if(sqs.receiveMessage(Q_URL).getMessage().compare("I am first") == 0)
       } catch (AmazonSQSException e) 
	   {
           if (!e.getErrorCode().equals("QueueAlreadyExists")) 
           {
        	   myMark = O_MARK;
        	   System.out.println(e);
               //throw e;
           }
       }
      //myMark =  "X"; //Get player's mark (X or O). We hard coded here in demo. In your implementation, you may get this mark dynamically 
                     //from the cloud service. This is the initial state of the game.

      SwingUtilities.invokeLater( 
         new Runnable() 
         {         
            public void run()
            {
               // display player's mark
               idField.setText( "You are player \"" + myMark + "\"" );
            } // end method run
         } // end anonymous inner class
      ); // end call to SwingUtilities.invokeLater
         
      //myTurn = ( myMark.equals( X_MARK ) ); // determine if client's turn
      // program the game logic below
      while ( ! isGameOver )
      {
		  //TODO: Here in this while body, you will program the game logic. 
		  // You are free to add any helper methods in this class or other classes.
		  // Basically, this client player will retrieve a message from cloud in each while iteration
		  // and process it until game over is detected.
		  // Please check the processMessage() method below to gain some clues.
    	  try
    	  {
	    	  messages = notMy_Q.receiveMessage(receiveMessageRequest).getMessages();
	    	 /*(if(messages.size() > 0 && messages.get(0).getBody().contains("Start " + myMark))
	    	  {
	    		  String messageReceiptHandle = messages.get(0).getReceiptHandle();
	              notMy_Q.deleteMessage(notMy_URL, messageReceiptHandle);
	    	  }//*/
	    	  if(messages.size() > 0 && !myTurn)
	    	  {
	    		  //System.out.println(messages.get(0).getBody());
	    		  processMessage(messages.get(0));
	    	  }
    	  }
    	  catch(Exception e)// AmazonSQSException e)
    	  {
    		  System.out.println(e);
    	  }
    	 
      } // end while
      if(winningMark.equals(notMyMark))
		  displayMessage( "Your Oppenent Won" + "\n" ); // display the message
	  else
		  displayMessage( "You won!" + "\n" ); // display the message
      
   } // end method run
   
   //TODO: You have write this method that checks the game board to detect winning status.
   private boolean checkIfGameOver(int row, int column, String mark) 
   {

       int a_left2right = checkAngleLeft2Right(row, column, mark);
       int a_right2left = checkAngleRight2Left(row, column, mark);
       int horizontalCount = checkHorizontal(row, column,  mark);
       int verticleCount = checkVerticle(row, column, mark);
	   return (a_left2right == 5) || (horizontalCount == 5) || (verticleCount == 5) || (a_right2left == 5) ;
   }
   
   private int checkAngleRight2Left(int row, int column, String mark)
   {
	   //TODO:
	   int count = 0;
	   for(int x= row, y = column; x < array_board.length && y >= 0 && array_board[x][y] != null; x++, y--)
	   {
		   if(array_board[x][y].equals(mark))
			   count++;
		   else break;
	   }//end of for loop
	   for(int x= row, y = column; x >= 0 && y < array_board.length && array_board[x][y] != null; --x, ++y)
	   {
		   if(array_board[x][y].equals(mark))
			   count++;
		   else break;
	   }
	   return count;
   }
   
   private int checkAngleLeft2Right(int row, int column, String mark)
   {
	   //TODO:
	   
	   int count = 0;
	   for(int x= row, y = column; x < array_board.length && y < array_board.length && array_board[x][y] != null; x++, y++)
	   {
		   if(array_board[x][y].equals(mark))
			   count++;
		   else break;
	   }//end of for loop
	   for(int x= row, y = column; x >= 0 && y >= 0 && array_board[x][y] != null; --x, --y)
	   {
		   if(array_board[x][y].equals(mark))
			   count++;
		   else break;
	   }
	   return count;
   }
   
   private int checkHorizontal(int row, int column, String mark)
   {
	   //TODO:
	   int count = 0;
	   for(int x = column; (x < array_board.length) &&  array_board[row][x] != null; ++x)
	   {
		   if(array_board[row][x].equals(mark))
			   count++;
		   else 
			   break;
	   }
	   for(int x = column; (x >= 0) && array_board[row][x] != null; x--)
	   {
		   if(array_board[row][x].equals(mark))
			   count++;
		   else
			   break;
	   }
	   return count;
   }
   
   private int checkVerticle(int row, int column, String mark)
   {
	   //TODO:
	   int count = 0;
	   for(int y = row; (y < array_board.length) &&  array_board[y][column] != null; ++y)
	   {
		   if(array_board[y][column].equals(mark))
			   count++;
		   else
			   break;
	   }
	   for(int y = column; (y >= 0) && array_board[y][column] != null; y--)
	   {
		   if(array_board[y][column].equals(mark))
			   count++;
		   else 
			   break;
	   }
	   return count;
   }
   
   // This method is not used currently, but it may give you some hints regarding
   // how one client talks to other client through cloud service(s).
   private void processMessage(Message messageObj)
   {
      // valid move occurred
	  String message = messageObj.getBody();
      if ( message.equals( "Opponent Won" ) ) 
      {
         displayMessage( "Game over, Opponent won.\n" );
         // then highlight the winning locations down below.
         
      } // end if
      else if (message.charAt(0) == notMyMark.charAt(0) ) //this means the opponent moved
      {
    	 //System.out.println("Got opponent's move");
         String[] locations = getOpponentMove(messageObj); // Here get move location from opponent
         int row = Integer.parseInt(locations[1]);
         int column = Integer.parseInt(locations[2]);
         array_board[row][column] = notMyMark;
         setMark(  board[ row ][ column ], 
            ( myMark.equals( X_MARK ) ? O_MARK : X_MARK ) ); // mark move                
         displayMessage( "Opponent moved. row = "+ row+ " column = "+ column + "\n" );
         myTurn = true; // now this client's turn
         isGameOver = checkIfGameOver(row, column, notMyMark);
         if(isGameOver)
        	 winningMark = notMyMark;
      } // end else if
      else
         displayMessage( message + "\n" ); // display the message
   } // end method processMessage

   //Here get move location from opponent
   private String[] getOpponentMove(Message messageObj) {
	   //TODO: Please write your code here
	   try
	   {
		   String messageReceiptHandle = messageObj.getReceiptHandle();
		   String move = messageObj.getBody();
           notMy_Q.deleteMessage(notMy_URL, messageReceiptHandle);
           //messages = notMy_Q.receiveMessage(notMy_URL).getMessages();
           
           String[] array = move.split(",");
           System.out.println("array[0] = " + array[0]);
           System.out.println("array[1] = " + array[1]);
           System.out.println("array[2] = " + array[2]);
           return array;
	   }
	   catch(Exception e)
	   {
		   System.out.println(e);
	   }
	   return null;
   }
   // manipulate outputArea in event-dispatch thread
   private void displayMessage( final String messageToDisplay )
   {
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run() 
            {
               displayArea.append( messageToDisplay ); // updates output
            } // end method run
         }  // end inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method displayMessage

   // utility method to set mark on board in event-dispatch thread
   private void setMark( final Square squareToMark, final String mark )
   {
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run()
            {
               squareToMark.setMark( mark ); // set mark in square
            } // end method run
         } // end anonymous inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method setMark

   // Send message to cloud service indicating clicked square
   public void sendClickedSquare( int location )
   {
      // if it is my turn
      if ( myTurn ) 
      {
         //TODO: Below you send the clicked location to the cloud service that will notify the opponent,
    	     // Or the opponent will retrieve the move location itself.
         // Please write your own code below.
    	  //while(!validMove(location));
    	/*  int row = 0;
          int column = 0;
          if(location != 0)
          {*/
    	  int row = location / bsize; // calculate row
     	  int column = location % bsize; // calculate column
         // }
    	  sendMessageRequest = new SendMessageRequest(My_URL, myMark+","+row+","+column);
    	  sendMessageRequest.setMessageGroupId(messageGroup);
    	  My_Q.sendMessage(sendMessageRequest);
    	  isGameOver = checkIfGameOver(row, column, myMark);
    	  if(isGameOver)
    		  winningMark = myMark;
    	  myTurn = false; // not my turn anymore
      } // end if
   } // end method sendClickedSquare
   
   boolean validMove(int validLocation)
   {
	   int row = validLocation/bsize;
	   int column = validLocation%bsize;
	   return array_board[row][column] == null;
   }

   // set current Square
   public void setCurrentSquare( Square square )
   {
      currentSquare = square; // set current square to argument
   } // end method setCurrentSquare

   // private inner class for the squares on the board
   private class Square extends JPanel 
   {
      private String mark; // mark to be drawn in this square
      private int location; // location of square
   
      public Square( String squareMark, int squareLocation )
      {
         mark = squareMark; // set mark for this square
         location = squareLocation; // set location of this square

         addMouseListener( 
            new MouseAdapter() {
               public void mouseReleased( MouseEvent e )
               {
                  /*setCurrentSquare( Square.this ); // set current square
                  TicTacToeClient.this.setMark( currentSquare, myMark );
                  displayMessage("You clicked at location: " + getSquareLocation() + "\n");*/
                  
                  //TODO: You may have to send location of this square to 
                  // the cloud service that will notify the opponent player.
                  int clickLocation = getSquareLocation();
                  if(validMove(clickLocation) && myTurn) // you have write your own method isValidMove().
                  {
                	  setCurrentSquare( Square.this ); // set current square
                	  int row = 0;
                      int column = 0;
                      if(clickLocation != 0)
                      {
                     	 row = clickLocation / bsize; // calculate row
                     	 column = clickLocation % bsize; // calculate column
                      }
                	  array_board[row][column] = myMark;
                      TicTacToeClient.this.setMark( currentSquare, myMark );
                      displayMessage("You clicked at location: " + clickLocation + "\n");
                      sendClickedSquare( clickLocation );
                  }
                  
               } // end method mouseReleased
            } // end anonymous inner class
         ); // end call to addMouseListener
      } // end Square constructor

      // return preferred size of Square
      public Dimension getPreferredSize() 
      { 
         return new Dimension( 30, 30 ); // return preferred size
      } // end method getPreferredSize

      // return minimum size of Square
      public Dimension getMinimumSize() 
      {
         return getPreferredSize(); // return preferred size
      } // end method getMinimumSize

      // set mark for Square
      public void setMark( String newMark ) 
      { 
         mark = newMark; // set mark of square
         repaint(); // repaint square
      } // end method setMark
   
      // return Square location
      public int getSquareLocation() 
      {
         return location; // return location of square
      } // end method getSquareLocation
   
      // draw Square
      public void paintComponent( Graphics g )
      {
         super.paintComponent( g );

         g.drawRect( 0, 0, 29, 29 ); // draw square
         g.drawString( mark, 11, 20 ); // draw mark   
      } // end method paintComponent
   } // end inner-class Square
   
   

} // end class TicTacToeClient


