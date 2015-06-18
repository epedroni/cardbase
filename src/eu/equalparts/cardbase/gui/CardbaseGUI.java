package eu.equalparts.cardbase.gui;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


@SuppressWarnings("serial")
public class CardbaseGUI extends Panel {

	/**
	 * Run the GUI.
	 * 
	 * @param args arguments passed down to {@code Application.launch()}.
	 */
	public static void main(String... args) {
		Frame f = new Frame("Test");
		
		CardbaseGUI c = new CardbaseGUI();
		
		f.add(c);
		f.pack();
		
		f.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		f.setVisible(true);
	}
	
	
	public void init() {
		add(new Button("one"));
		add(new Button("two"));
	}
	
	
}
