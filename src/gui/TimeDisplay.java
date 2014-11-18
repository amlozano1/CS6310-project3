package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;

import javax.swing.JPanel;

public class TimeDisplay extends JPanel {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar currentDate;
	
	public TimeDisplay() {
		
		currentDate = Calendar.getInstance();
		
		reset();
		
		//dateField = new JLabel(currentDate.get(Calendar.HOUR_OF_DAY)+":"+currentDate.get(Calendar.MINUTE)+"-"+currentDate.get(Calendar.DAY_OF_MONTH)+"-"+(currentDate.get(Calendar.MONTH)+1)+" "+currentDate.get(Calendar.YEAR));
	}
	
	public void reset() {
		// reset the time back to December 31st 1999
		currentDate.set(1999, 11, 31, 12, 0, 0);
		this.repaint();
	}
	
	public void addTime ( int minutes ) {
		currentDate.set(1999, 11, 31, 12, 0, 0);
		currentDate.add(Calendar.MINUTE, minutes);
		this.repaint();
	}
	
	public void paint ( Graphics g ) {
		g.setColor(Color.black);
		g.drawString("Current Time:  "+String.format("%02d:%02d %02d-%02d-%02d", currentDate.get(Calendar.HOUR_OF_DAY),currentDate.get(Calendar.MINUTE),currentDate.get(Calendar.DAY_OF_MONTH),(currentDate.get(Calendar.MONTH)+1),currentDate.get(Calendar.YEAR)), 0, 12);
	}
	
}
