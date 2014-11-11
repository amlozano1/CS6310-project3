package gui;

import gui.EarthPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class UI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1061316348359815659L;

	private JFrame frame;
	private JToggleButton btnStartStop, btnPauseResume;
	private JTextField txtGridSpacing, txtSimLength, txtAxialTiltSim, txtOrbitalEccSim;
	private JTextField txtAxialTiltQuery, txtOrbitalEccQuery;
	private JComboBox<String> queryNameSelect;
	private JCheckBox cbDisplayAnimation;
	private JSpinner spinnerSimTimeStep;
	private JSlider sliderOpacity;
	private EarthPanel earthPanel;
	private String[] queryNames = new String[0];

	private Runtime guiRuntime = Runtime.getRuntime();
	
	public UI(){
		super("Earth Simulation");
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMaximumSize(getMaximumSize());
		this.setResizable(true);
		this.setLocation(10, 10);
		this.setSize(1200, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension dim = new Dimension (800,825);
		EarthPanel.getInstance().init(dim, dim, dim);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints layoutConstraint = new GridBagConstraints();
		layoutConstraint.ipadx = 5;
		layoutConstraint.ipady = 5;
		layoutConstraint.fill = GridBagConstraints.HORIZONTAL;
		layoutConstraint.gridx=0;
		layoutConstraint.gridy=0;
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Simulation Controls", this.createSimControlsComponent());
		tabs.addTab("Query Controls", this.createQueryControlsComponent());
		this.add(tabs,layoutConstraint);
		layoutConstraint.gridx=1;
		layoutConstraint.gridy=0;
		layoutConstraint.gridheight = GridBagConstraints.REMAINDER;
		this.add(this.createVisualizerDisplay(),layoutConstraint);
		this.setVisible(true);
	}
	
	private JComponent createVisualizerDisplay(){
		JPanel component = new JPanel();
		EarthPanel.getInstance().drawGrid(15);
		component.add(EarthPanel.getInstance());
		
		return component;
	}
	
	private JComponent createQueryControlsComponent(){
		JPanel component = new JPanel();
		component.setLayout(new GridBagLayout());
		GridBagConstraints layoutConstraint = new GridBagConstraints();
		layoutConstraint.ipadx = 5;
		layoutConstraint.ipady = 5;
		layoutConstraint.fill = GridBagConstraints.HORIZONTAL;
		int currentY = 0;
		
		//add the label for Simulation Name
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelSimName = new JLabel("Simulation Name");
		component.add(labelSimName, layoutConstraint);
		
		//add the textbox for Simulation Name
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		queryNameSelect = new JComboBox<String>(queryNames);
		component.add(queryNameSelect, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Axial Tilt
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelAxialTilt = new JLabel("Axial Tilt");
		component.add(labelAxialTilt, layoutConstraint);
		
		//add the textbox for Axial Tilt
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtAxialTiltQuery = new JTextField("23.44");
		component.add(txtAxialTiltQuery, layoutConstraint);
		
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
		txtOrbitalEccQuery = new JTextField("0.167");
		component.add(txtOrbitalEccQuery, layoutConstraint);

		//updated currentY
		currentY += layoutConstraint.gridheight;

				
		return component;
	}
	
	private JComponent createSimControlsComponent() {
		
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
		JLabel labelAxialTilt = new JLabel("Axial Tilt");
		component.add(labelAxialTilt, layoutConstraint);
		
		//add the textbox for Axial Tilt
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtAxialTiltSim = new JTextField("23.44");
		component.add(txtAxialTiltSim, layoutConstraint);
		
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
		txtOrbitalEccSim = new JTextField("0.167");
		component.add(txtOrbitalEccSim, layoutConstraint);

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
		spinnerSimTimeStep.setModel(new SpinnerNumberModel(1440, 1, 525600, 1));
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

		//add the Checkbox for display animation
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		layoutConstraint.gridwidth = 2;
		cbDisplayAnimation = new JCheckBox("Check to Enable Display Animation");
		cbDisplayAnimation.setSelected(true);
		component.add(cbDisplayAnimation, layoutConstraint);
		layoutConstraint.gridwidth = 1;
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
		layoutConstraint.gridwidth = 2;
		JLabel labelOpacitySlider = new JLabel("Adjust Temp Filter Transparency");
		component.add(labelOpacitySlider, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;		
		
		//Add Opacity Slider
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		layoutConstraint.gridwidth = 2;
		sliderOpacity = new JSlider();
		sliderOpacity.addChangeListener(chngSliderOpacity);
		sliderOpacity.setValue(70);
		component.add(sliderOpacity, layoutConstraint);		
		
		layoutConstraint.gridwidth = 1;
		
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
			if(btnStartStop.isSelected()){				
				//Validate the values entered before starting processes
				String valuesMessage = validValues();
				if( valuesMessage == null){
					btnStartStop.setText("Stop");
					btnPauseResume.setEnabled(true);
					
					//Disabled settings fields during sim
					txtSimLength.setEnabled(false);
					txtAxialTiltSim.setEnabled(false);
					txtGridSpacing.setEnabled(false);
					txtOrbitalEccSim.setEnabled(false);
					cbDisplayAnimation.setEnabled(false);
					spinnerSimTimeStep.setEnabled(false);
					
					EarthPanel.getInstance().drawGrid(Integer.parseInt(txtGridSpacing.getText()));
				}else{
					btnStartStop.setText("Start");
					btnStartStop.setSelected(false);
					JOptionPane.showMessageDialog(frame, valuesMessage);
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
				txtAxialTiltSim.setEnabled(true);
				txtOrbitalEccSim.setEnabled(true);
				cbDisplayAnimation.setEnabled(true);
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
		}catch(NumberFormatException e){
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
		}catch(NumberFormatException e){
			return "Error processing Grid Spacing. Please input a positive integer value.";
		}
		
		try{
			float axialTilt = Float.parseFloat(txtAxialTiltSim.getText());
			if(Math.abs(axialTilt) > 180)
				return "The axial tilt value must be between -180 and 180 (inclusive).";
		}catch(NumberFormatException e){
			return "Error processing Axial Tilt.";
		}
		
		try{
			float orbitalEcc = Float.parseFloat(txtOrbitalEccSim.getText());
			if((orbitalEcc < (float)0) || (orbitalEcc >= 1))
				return "The orbital eccentricity value must be between a non-negative real number less than 1 and it is "+orbitalEcc+".";
		}catch(NumberFormatException e){
			return "Error processing Orbital Eccentricity.";
		}
		
		return null;
	}
}
