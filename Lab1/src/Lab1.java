import java.awt.event.*;
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Lab1 extends JFrame implements KeyListener
{	
	private static Thread[] threads = new Thread[2];
	private static int numPressed = 0;
	private JTextArea output;
	
	public Lab1(String name)
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
	
	public static void main(String[] args) 
	{
		Lab1 out = new Lab1("Lab1");
		threads[0] = new Thread(new InterruptingThread(1, out.output));
		threads[1] = new Thread(new InterruptingThread(2, out.output));
		threads[0].start();
		threads[1].start();
		
		out.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
	}//end of main method	

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		// TODO Auto-generated method stub
		if(e.getKeyChar() == '1')
			threads[0].interrupt();
		else if(e.getKeyCode() == '2')
			threads[1].interrupt();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}//end of Lab1 class

