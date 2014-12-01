package gui;

import javax.swing.JLabel;
import base.Utils;

public class TimeDisplaySimulation extends JLabel {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1978690268570186847L;
	private static TimeDisplaySimulation self = null;
	
	public static TimeDisplaySimulation getInstance(){
		if (self == null){
			self = new TimeDisplaySimulation();
		}
		return self;
	}
	
	private TimeDisplaySimulation() {
		setTime(0);
	}
	
	public void setTime(long time){
		this.setText(Utils.toDateString(time));
	}
}
