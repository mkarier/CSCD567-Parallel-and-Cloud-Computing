import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


/*
 * The point of this class is to run the main and look at different key strokes
 * Once the key stroke starts it should suspend the other thread until the user hits enter
 * It will also spawn another thread that will produce the GUI and keep printing the message.
 * 
 */
public class MainWindow extends JFrame implements KeyListener
{
	private JTextArea output;
	private String message ="";
	private boolean pressedEnter = false;
	private Thread display;
	
	MainWindow(String name)
	{
		super(name);
		//JTextArea output = new JTextArea(20,30) // Can this statement replace the next one?
		
		output = new JTextArea(20,30);                      //create JTextArea in which all messages are shown.
		DefaultCaret caret = (DefaultCaret)output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  // JTextArea always set focus on the last message appended.
		
		//
		add(new JScrollPane(output)); // add a Scroll bar to JFrame, scrolling associated with JTextArea object
		setSize(500, 500);            // when lines of messages exceeds the line capacity of JFrame, scroll bar scroll down.
		setVisible(true);
		output.addKeyListener(this);
	}//end of constructor

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}//end of keyTyped

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(!this.display.isInterrupted())
		{
			this.display.interrupt();
			//this.display.join();
			System.out.println("display thread is interrupted");
		}
	}//end of keyPressed

	@Override
	public void keyReleased(KeyEvent e)
	{
		System.out.println("keyReleased");
		// TODO Auto-generated method stub
		char input = e.getKeyChar();
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			System.out.println("User pressed enter");
			if(this.message.equals("exit"))
				System.exit(0);
			this.message = this.message + "\n";
			try
			{
				this.display.join();
				this.display = new Thread(new Displayer(this.output, this.message));
				this.display.start();
				this.message = "";
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}//end of if statement
		else
			this.message = this.message + input;

	}//end of keyReleased

	public static void main(String[] args) throws InterruptedException
	{
		// TODO Auto-generated method stub
		MainWindow window = new MainWindow("main");
		window.display = new Thread(new Displayer(window.output, "Good morning\n"));
		window.display.start();
		window.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}//end of main

}//end of MainWindow
