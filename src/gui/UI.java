package gui;

import gui.EarthPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.Timer;



public class UI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1061316348359815659L;

	private JFrame frame;
	private JToggleButton btnStartStop, btnPauseResume;
	private JTextField txtGridSpacing, txtSimLength, txtAxialTilt, txtOrbitalEcc;
	private JSpinner spinnerDate, spinnerSimTimeStep;
	private JSlider sliderOpacity;
	private EarthPanel earthPanel;

	private Runtime guiRuntime = Runtime.getRuntime();
	
	public UI(){
		super("Earth Simulation");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMaximumSize(getMaximumSize());
		this.setResizable(true);
		this.setLocation(10, 10);
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension dim = new Dimension (800,825);
		EarthPanel.getInstance().init(dim, dim, dim);
		
		this.setLayout(new GridLayout(2, 0));
		this.add(this.createControlsComponent());
		this.add(this.createVisualizerDisplay());
		this.setVisible(true);
	}
	
	private JComponent createVisualizerDisplay(){
		JPanel component = new JPanel();
		EarthPanel.getInstance().drawGrid(15);
		component.add(EarthPanel.getInstance());
		
		return component;
	}
	
	private JComponent createControlsComponent() {
		
		JPanel component = new JPanel();
		component.setLayout(new GridBagLayout());
		GridBagConstraints layoutConstraint = new GridBagConstraints();
		layoutConstraint.ipadx = 5;
		layoutConstraint.ipady = 5;
		layoutConstraint.fill = GridBagConstraints.HORIZONTAL;
		int currentY = 0;
		
		//add the label for Axial Tilt
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelPresDispRate = new JLabel("Axial Tilt");
		component.add(labelPresDispRate, layoutConstraint);
		
		//add the textbox for Axial Tilt
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtAxialTilt = new JTextField("23.44");
		component.add(txtAxialTilt, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Orbital Eccentricity
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelOrbitalEcc = new JLabel("Orbital Eccentricity");
		component.add(labelOrbitalEcc, layoutConstraint);
		
		//add the textbox for Orbital Eccentricity
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtOrbitalEcc = new JTextField("0.167");
		component.add(txtOrbitalEcc, layoutConstraint);
	
		//updated currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Simulation Time Step
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelSimTimeStepTop = new JLabel("Simulation Time Step");
		component.add(labelSimTimeStepTop, layoutConstraint);
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY+1;
		layoutConstraint.gridheight = 1;
		JLabel labelSimTimeStepBot = new JLabel("Select value from 1 to 525,600");
		component.add(labelSimTimeStepBot, layoutConstraint);
		
		//add the spinner for Simulation Time Step
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 2;
		spinnerSimTimeStep = new JSpinner();
		spinnerSimTimeStep.setModel(new SpinnerNumberModel(1, 1, 525600, 1));
		component.add(spinnerSimTimeStep, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Grid Spacing
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelGridSpacing = new JLabel("Grid Spacing");
		component.add(labelGridSpacing, layoutConstraint);
		
		//add the textbox for Grid Spacing
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtGridSpacing = new JTextField("15");
		component.add(txtGridSpacing, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Sim Length
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelSimLength = new JLabel("Simulation Length in Solar Months");
		component.add(labelSimLength, layoutConstraint);
		
		//add the textbox for Sim Length
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtSimLength = new JTextField("12");
		component.add(txtSimLength, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;
		
		//add Start Button
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		btnStartStop = new JToggleButton("Start");
		btnStartStop.setEnabled(true);
		btnStartStop.addActionListener(actListStartStop);
		component.add(btnStartStop,layoutConstraint);

		
		//add Stop Button
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		btnPauseResume = new JToggleButton("Pause");
		btnPauseResume.addActionListener(actListPauseResume);
		btnPauseResume.setEnabled(false);
		component.add(btnPauseResume,layoutConstraint);

		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//Add Opacity Slider Label
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelOpacitySlider = new JLabel("Adjust Temp Filter Transparency");
		component.add(labelOpacitySlider, layoutConstraint);
		
		//Add Opacity Slider
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		sliderOpacity = new JSlider();
		sliderOpacity.addChangeListener(chngSliderOpacity);
		sliderOpacity.setValue(70);
		component.add(sliderOpacity, layoutConstraint);		
		
		return component;
	}
	
	private ChangeListener chngSliderOpacity = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	float val = (float)0.01 * source.getValue();
	            EarthPanel.getInstance().setMapOpacity(val);		            
	        }  
		}
	};
	
	//Handle timer event
    public void actionPerformed(ActionEvent e) {

    	System.out.println("Memory Usage: "+(guiRuntime.totalMemory()-guiRuntime.freeMemory()));
    }
	
	private ActionListener actListStartStop = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//Start has been pressed
			if(btnStartStop.isSelected())
			{				
				//Validate the values entered before starting processes
				String valuesMessage = validValues();
				if( valuesMessage == null)
				{
					btnStartStop.setText("Stop");
					btnPauseResume.setEnabled(true);
					
					//Disabled settings fields during sim
					txtSimLength.setEnabled(false);
					txtAxialTilt.setEnabled(false);
					txtGridSpacing.setEnabled(false);
					spinnerSimTimeStep.setEnabled(false);
					
					EarthPanel.getInstance().drawGrid(Integer.parseInt(txtGridSpacing.getText()));
					
					if(!valuesMessage.equals(""))
					{
						btnStartStop.setText("Start");
						btnStartStop.setSelected(false);
						JOptionPane.showMessageDialog(frame, valuesMessage);						
					}
						
				}
			}else{//Stop has been pressed
				btnStartStop.setText("Start");
				EarthPanel.getInstance().reset();
				
				//Reset pause button
				btnPauseResume.setEnabled(false);
				btnPauseResume.setSelected(false);
				btnPauseResume.setText("Pause");
				
				//Enable settings fields during sim
				txtSimLength.setEnabled(true);
				txtAxialTilt.setEnabled(true);
				txtGridSpacing.setEnabled(true);
				spinnerSimTimeStep.setEnabled(true);
			}
		}
	}; 
	
	private ActionListener actListPauseResume = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//Pause Selected
			if(btnPauseResume.isSelected()) 
			{ 
				btnPauseResume.setText("Resume");
			} else { //Resume Selected
				btnPauseResume.setText("Pause");
			}
		}
	};
	
	
	/*
	 * Validates the Grid Spacing and Simulation Time Step
	 */
	private String validValues(){
		try{
			int simTimeStep = (Integer)spinnerSimTimeStep.getValue();
			if(simTimeStep < 1)
				return "Simulation Time Step must be greater than 0";
			if(simTimeStep > 525600)
				return "Simulation Time Step must be less than or equal to 525,600.";
		}catch(Exception e){
			return "Error processing Simulation Time Step.";
		}
		
		try{
			int gridSpacingEntered = Integer.parseInt(txtGridSpacing.getText());
			int gridSpacing = gridSpacingEntered;
			while(180 % gridSpacing != 0){
				gridSpacing--;
			}
			if(gridSpacing != gridSpacingEntered){
				int response = JOptionPane.showConfirmDialog(frame, 
						"The value you entered ("+gridSpacingEntered+") does not divide 180 evenly. Would you like to use "+gridSpacing+"?",
						"Confirm",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION)
					return "";
				else{
					txtGridSpacing.setText(gridSpacing+"");
				}
			}
		}catch(Exception e){
			return "Error processing Grid Spacing. Please input a positive integer value.";
		}
		
		return null;
	}

}
