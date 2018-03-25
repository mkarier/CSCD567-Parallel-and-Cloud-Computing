import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Lab2 extends JFrame
{
	
	private JTextArea output;
	
	Lab2(String name)
	{
		super(name);
		//JTextArea output = new JTextArea(20,30) // Can this statement replace the next one?
		
		this.output = new JTextArea(20,30);                      //create JTextArea in which all messages are shown.
		DefaultCaret caret = (DefaultCaret)output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  // JTextArea always set focus on the last message appended.
		
		//
		add(new JScrollPane(output)); // add a Scroll bar to JFrame, scrolling associated with JTextArea object
		setSize(500, 500);            // when lines of messages exceeds the line capacity of JFrame, scroll bar scroll down.
		setVisible(true);
	}//end of constructor

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		try
		{
			Lab2 window = new Lab2("display");
			window.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			});
				
			Thread waiter = new Thread(new Waiter(window.output));
			Thread printer = new Thread(new Printer(window.output, waiter));
			waiter.start();
			printer.start();
		
			waiter.join();
			printer.join();
			if(!waiter.isAlive() && !printer.isAlive())
				window.output.append("Both Waiter and Printer have finished their work!\n");
		}//end of try
		catch(InterruptedException e)
		{
			
		}//end of catch
		
	}//end of main

}//end of lab2
