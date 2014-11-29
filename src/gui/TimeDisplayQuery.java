package gui;

import javax.swing.JLabel;

import base.Utils;

public class TimeDisplayQuery extends JLabel {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static TimeDisplayQuery self = null;
	
	public static TimeDisplayQuery getInstance(){
		System.out.println("New time display");
		if (self == null){
			self = new TimeDisplayQuery();
		}
		return self;
	}
	
	private TimeDisplayQuery() {
		setTime(0);
	}
	
	public void setTime(long time){
		this.setText(Utils.toDateString(time));
	}
}
