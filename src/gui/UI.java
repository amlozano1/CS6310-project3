package gui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import base.Cell;
import base.ObjectFactory;
import base.QueryBoundary;
import base.QueryMetrics;
import base.Simulation;
import base.Utils;
import controllers.MasterController;
import data.impl.SimulationCriteria;
import exceptions.ArgumentInvalidException;
import exceptions.QueryBoundaryException;
import exceptions.ThreadException;

public class UI extends JFrame {

	private static final long serialVersionUID = 1061316348359815659L;

	private static UI guiControls = null;
	
	private JFrame frame;
	private JPanel simPanel;
	private JTabbedPane tabs;
	private JToggleButton btnStartStop, btnPauseResume;
	private JButton btnQueryGo, btnActualValuesFile;
	private JTextField txtSimulationName, txtGridSpacing, txtSimLength, txtAxialTiltSim, txtOrbitalEccSim, txtPresentationDisplayRate;
	private JTextField txtAxialTiltQuery, txtOrbitalEccQuery;
	private JTextField txtNorthBoundary, txtSouthBoundary, txtEastBoundary, txtWestBoundary;
	private JLabel lblMinTempResult, lblMaxTempResult;//, lblMeanTempTimeResult, lblMeanTempRegionResult;
	private JLabel lblMinTempResultTime, lblMaxTempResultTime;
	private JComboBox queryNameSelect;
	private JCheckBox cbDisplayAnimation, cbMinTemp, cbMaxTemp, cbAllValues, cbMeanRegionTemp, cbMeanTimeTemp;
	private JSpinner spinnerSimTimeStep, startTimeSpinner, endTimeSpinner;
	private JSlider sliderOpacity;
	private EarthPanel earthPanel = EarthPanel.getInstance();
	private TimeDisplayQuery queryTimeDisplay = TimeDisplayQuery.getInstance();
	private TimeDisplaySimulation simTimeDisplay = TimeDisplaySimulation.getInstance();
	private List<String> queryNames = new ArrayList<String>();
	private MasterController masterController;
	private Date START_DATE = null;
	
	private final String ALL_CELL_FILENAME = "AllCellInfo.csv";
	private final String START_DATE_STRING = "01-04-2014";
	
	public static UI getInstance(){
		if(guiControls == null){
			guiControls = new UI();
		}
		return guiControls;
	}
	
	public void setController(MasterController controller){
		this.masterController = controller;
	}
	
	public void updateMetricResults(){
		if(tabs.getSelectedIndex() != 1)
			return;
		
		QueryMetrics metrics = QueryMetrics.getInstance();

		if(cbMinTemp.isSelected()){
			if(metrics.getMin() == null)
				lblMinTempResult.setText("None Found.");
			else{
				String result = metrics.getMin().getTemperature().toString()+"degrees at ("+metrics.getMin().getLatitude()+","+metrics.getMin().getLongitude()+")";
				lblMinTempResult.setText(result);
				lblMinTempResultTime.setText(convertTimeToDate(metrics.getMinTime()).toString());
				//System.out.println("MIN: "+result);
			}
		}
		
		if(cbMaxTemp.isSelected()){
			if(metrics.getMax() == null)
				lblMaxTempResult.setText("None Found.");
			else{
				String result = metrics.getMax().getTemperature().toString() + " degrees at ("+metrics.getMax().getLatitude()+","+metrics.getMax().getLongitude()+")";
				lblMaxTempResult.setText(result);
				lblMaxTempResultTime.setText(convertTimeToDate(metrics.getMaxTime()).toString());
			}
		}
		
		
		try {
			File allInfoOutFile = new File (ALL_CELL_FILENAME);
			
			if(!allInfoOutFile.exists()){
				allInfoOutFile.createNewFile();
			}
		
			BufferedWriter writer = new BufferedWriter(new FileWriter(allInfoOutFile));
	
			boolean printHeader = true;
			List<Cell[]> allCells = metrics.getAll();
			int columnCount = 0;
			for (Cell[] cells : allCells) {
				columnCount = cells.length;
				Long simTime = metrics.getSimTimes().get(allCells.indexOf(cells));
				if(printHeader){
					if(cbAllValues.isSelected() || cbMeanTimeTemp.isSelected()){
						writer.write("Longitude,");
						for (int i = 0; i < cells.length; i++) {
							//print column headers
							writer.write(cells[i].getLongitude() + ",");
						}
						writer.newLine();

						writer.write("Latitude,");
						for (int i = 0; i < cells.length; i++) {
							//print column headers
							writer.write(cells[i].getLatitude() + ",");
						}
					}
					if(cbMeanRegionTemp.isSelected())
						writer.write(",Region's Mean");
					writer.newLine();
					printHeader = false;
				}
				//print row header
				if(cbAllValues.isSelected() || cbMeanRegionTemp.isSelected())
					writer.write(Utils.toDateString(simTime));
				if(cbAllValues.isSelected()){
					for (int i = 0; i < cells.length; i++) {
						writer.write("," + cells[i].getTemperature());
					}
				}
				if(cbMeanRegionTemp.isSelected()){
					if(cbAllValues.isSelected()){
						writer.write(",," + metrics.getMeanForRegion(simTime) );
					}else{
						if(cbMeanTimeTemp.isSelected()){
							for (int i = 0; i < cells.length; i++) {
								writer.write(",");
							}
							writer.write(",," + metrics.getMeanForRegion(simTime) );
						}else{
							writer.write(","+metrics.getMeanForRegion(simTime));
						}
					}
				}
				writer.newLine();
			}
			writer.newLine();
			
			if(cbMeanTimeTemp.isSelected()){
				writer.write("Time's Mean,");
				for (int i = 0; i < columnCount; i++) {
					writer.write(metrics.getMeanForTime(i) + ",");
				}
			}
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private UI(){
		super("Earth Simulation");
		
		
		try {
			this.START_DATE = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(START_DATE_STRING);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMaximumSize(getMaximumSize());
		this.setResizable(true);
		this.setLocation(10, 10);
		this.setSize(1300, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension dim = new Dimension (800,825);
		earthPanel.init(dim, dim, dim);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints layoutConstraint = new GridBagConstraints();
		
		layoutConstraint.fill = GridBagConstraints.HORIZONTAL;
		
		layoutConstraint.gridx=0;
		layoutConstraint.gridy=0;
		tabs = new JTabbedPane();
		
		simPanel = new JPanel();
		simPanel.setLayout(new GridBagLayout());
		simPanel.add(createSimControlsComponent());
		tabs.add("Simulation",simPanel);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(createQueryControlsComponent());
		tabs.add("Query",panel);
		this.add(tabs, layoutConstraint);
		
		layoutConstraint.gridx=1;
		layoutConstraint.gridy=0;
		this.add(createVisualizerDisplay(), layoutConstraint);
		
		this.setVisible(true);
	}
	
	private JComponent createVisualizerDisplay(){
		JPanel component = new JPanel();
	
		earthPanel.drawGrid(15);
		component.add(earthPanel);
		
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
			
		//Add display time label
		layoutConstraint.gridx=0;
		layoutConstraint.gridy=currentY;
		layoutConstraint.gridheight=1;
		JLabel lblDispTime = new JLabel("Current Time on Display");
		component.add(lblDispTime, layoutConstraint);
		
		//Add display time
		layoutConstraint.gridx=1;
		layoutConstraint.gridy=currentY;
		layoutConstraint.gridheight=1;
		component.add(queryTimeDisplay, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Simulation Name
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelSimName = new JLabel("Simulation Name");
		component.add(labelSimName, layoutConstraint);
		
		//add the textbox for Simulation Name
		queryNames = ObjectFactory.getSimulationDAO().getSimulationNames();
		queryNames.add(0, null);
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		queryNameSelect = new JComboBox(queryNames.toArray(new String[queryNames.size()]));
		queryNameSelect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadSimulationSettingsFromQueryNameList();
				
			}
		});
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
		
		//add the label for start Date
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelQueryStartDate = new JLabel("Start Date");
		component.add(labelQueryStartDate, layoutConstraint);
		
		//add the spinner for start Date
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		startTimeSpinner = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "MM-dd-yyyy HH:mm");
		startTimeSpinner.setEditor(startTimeEditor);
		startTimeSpinner.setValue(START_DATE);
		component.add(startTimeSpinner, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for End Date
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelQueryEndDate = new JLabel("End Date");
		component.add(labelQueryEndDate, layoutConstraint);
		
		//add spinner for end date
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		endTimeSpinner = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "MM-dd-yyyy HH:mm");
		endTimeSpinner.setEditor(endTimeEditor);
		endTimeSpinner.setValue(new Date(START_DATE.getTime()+60000)); //add one minute to help ensure start and end time differ (mainly so when I test I don't have to change it every time)
		
		component.add(endTimeSpinner, layoutConstraint);
				
		//updated currentY
		currentY += layoutConstraint.gridheight;
		
		//add boundary information message
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		layoutConstraint.gridwidth = 2;
		JLabel labelBoundaryMessage = new JLabel("Leave the textboxes blank if you would like to include the whole Earth.");
		component.add(labelBoundaryMessage, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;		
		
		//add boundary information message
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		layoutConstraint.gridwidth = 2;
		JLabel labelBoundaryMessage2 = new JLabel("You may also leave a pair (North, South) or (East,West) blank to include all.");
		component.add(labelBoundaryMessage2, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;		
		
		//add the label for North boundary
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		layoutConstraint.gridwidth = 1;
		JLabel labelNorthBoundary = new JLabel("Northern Boundary Latitude");
		component.add(labelNorthBoundary, layoutConstraint);
		
		//add textbox for North boundary
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtNorthBoundary = new JTextField("");
		component.add(txtNorthBoundary, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;		
		
		//add the label for South boundary
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelSouthBoundary = new JLabel("Southern Boundary Latitude");
		component.add(labelSouthBoundary, layoutConstraint);
		
		//add textbox for South boundary
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtSouthBoundary = new JTextField("");
		component.add(txtSouthBoundary, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;		
				
		//add the label for East boundary
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelEastBoundary = new JLabel("Eastern Boundary Longitude");
		component.add(labelEastBoundary, layoutConstraint);
		
		//add textbox for East boundary
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtEastBoundary = new JTextField("");
		component.add(txtEastBoundary, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;		
				
		//add the label for West boundary
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelWestBoundary = new JLabel("Western Boundary Latitude");
		component.add(labelWestBoundary, layoutConstraint);
		
		//add textbox for West boundary
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtWestBoundary = new JTextField("");
		component.add(txtWestBoundary, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;		
		
		//add query button
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		layoutConstraint.gridwidth = 2;
		btnQueryGo = new JButton("Query");
		btnQueryGo.addActionListener(actListQuery);
		component.add(btnQueryGo, layoutConstraint);
		layoutConstraint.gridwidth = 1;
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add header info 1st col
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelCheckHeader = new JLabel("Check values to calculate");
		component.add(labelCheckHeader, layoutConstraint);
		
		//add header info 2nd col
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelValuesHeader = new JLabel("Calculated Values");
		component.add(labelValuesHeader, layoutConstraint);
		
		//updated currentY
		currentY += layoutConstraint.gridheight;		
		
		//add the label for Min Temp
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		cbMinTemp = new JCheckBox("Minimum Temperature");
		cbMinTemp.setSelected(true);
		component.add(cbMinTemp, layoutConstraint);
		
		//add the place holder for Min Temp value
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		lblMinTempResult = new JLabel("--");
		component.add(lblMinTempResult, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Min Temp timestamp
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel lblMinTempTime = new JLabel("          Min coordindates");
		component.add(lblMinTempTime, layoutConstraint);
				
		//add the place holder for Min Temp timestamp value
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		lblMinTempResultTime = new JLabel("--");
		component.add(lblMinTempResultTime, layoutConstraint);
				
		//update currentY
		currentY += layoutConstraint.gridheight;	
		
		//add the label for Max Temp
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 2;
		cbMaxTemp = new JCheckBox("Maximum Temperature");
		cbMaxTemp.setSelected(true);
		component.add(cbMaxTemp, layoutConstraint);
		
		//add the place holder for Max Temp
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 2;
		lblMaxTempResult = new JLabel("--");
		component.add(lblMaxTempResult, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Max Temp timestamp
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel lblMaxTempTime = new JLabel("          Max coordindates");
		component.add(lblMaxTempTime, layoutConstraint);
				
		//add the place holder for Max Temp timestamp value
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		lblMaxTempResultTime = new JLabel("--");
		component.add(lblMaxTempResultTime, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Actual Values
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		cbAllValues = new JCheckBox("Actual Values of all cells for all times");
		cbAllValues.setSelected(true);
		component.add(cbAllValues, layoutConstraint);
		
		//add the place holder Actual Values
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 3;
		btnActualValuesFile = new JButton("Open File");
		btnActualValuesFile.setEnabled(false);
		btnActualValuesFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openOutputFile(ALL_CELL_FILENAME);
			}
		});
		component.add(btnActualValuesFile, layoutConstraint);
		layoutConstraint.gridheight = 1;
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for Mean Temp over Region
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		cbMeanRegionTemp = new JCheckBox("Mean Temperature(Region)");
		cbMeanRegionTemp.setSelected(true);
		component.add(cbMeanRegionTemp, layoutConstraint);
		
		
		//update currentY
		currentY += layoutConstraint.gridheight;
		
		//add the label for mean temp over time
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		cbMeanTimeTemp = new JCheckBox("Mean Temperature(Time)");
		cbMeanTimeTemp.setSelected(true);
		component.add(cbMeanTimeTemp, layoutConstraint);
		
		//update currentY
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
		

		//Add display time label
		layoutConstraint.gridx=0;
		layoutConstraint.gridy=currentY;
		layoutConstraint.gridheight=1;
		JLabel lblDispTime = new JLabel("Current Time on Display");
		component.add(lblDispTime, layoutConstraint);
		
		//Add display time
		layoutConstraint.gridx=1;
		layoutConstraint.gridy=currentY;
		layoutConstraint.gridheight=1;
		component.add(simTimeDisplay, layoutConstraint);
		
		//update currentY
		currentY += layoutConstraint.gridheight;
				
		//add the label for Simulation Name
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelSimulationName = new JLabel("Simulation Name");
		component.add(labelSimulationName, layoutConstraint);
		
		//add the textbox for Simulation Name
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtSimulationName = new JTextField();
		component.add(txtSimulationName, layoutConstraint);
		
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

		//add the label for Presentation Display Rate
		layoutConstraint.gridx = 0;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		JLabel labelPresDispRate = new JLabel("Presentation Display Rate");
		component.add(labelPresDispRate, layoutConstraint);
		
		//add the textbox for Presentation Display Rate
		layoutConstraint.gridx = 1;
		layoutConstraint.gridy = currentY;
		layoutConstraint.gridheight = 1;
		txtPresentationDisplayRate = new JTextField("5");
		component.add(txtPresentationDisplayRate, layoutConstraint);
		
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
	        earthPanel.setMapOpacity(sliderOpacity.getValue() * .01f);
	    }
	};
	
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
					updateSimInputAvailability(false);
					
					//masterController.start(SIMULATION_AXIAL_TILT, SIMULATION_ORBITAL_ECCENTRICITY, SIMULATION_NAME, SIMULATION_GRID_SPACING, SIMULATION_TIME_STEP, SIMULATION_LENGTH, PRESENTATION_DISPLAY_RATE);
					try {
						masterController.start(Double.parseDouble(txtAxialTiltSim.getText()), Double.parseDouble(txtOrbitalEccSim.getText()), txtSimulationName.getText(), Integer.parseInt(txtGridSpacing.getText()), (Integer)spinnerSimTimeStep.getValue(), 0, Integer.parseInt(txtSimLength.getText()), Integer.parseInt(txtPresentationDisplayRate.getText()), cbDisplayAnimation.isSelected());
						if(cbDisplayAnimation.isSelected()){							
							//Update earth map with new gridspacing
							earthPanel.drawGrid(Integer.parseInt(txtGridSpacing.getText()));

							masterController.setPresentationControllerDisplayRate(Integer.parseInt(txtPresentationDisplayRate.getText()));
						}
					} catch (NumberFormatException e){
						System.out.println("NumberFormatException: The validate didn't throw an error but the master controller start call did.");
					} catch(ArgumentInvalidException e) {
						System.out.println("ArgumentInvalidException: The validate didn't throw an error but the master controller start call did.");
					}catch (ThreadException e) {
						e.printStackTrace();
						System.out.println("ThreadException: The validate didn't throw an error but the master controller start call did.");
					}
					queryNameSelect.addItem(txtSimulationName.getText());
				}else{
					btnStartStop.setText("Start");
					btnStartStop.setSelected(false);
					JOptionPane.showMessageDialog(frame, valuesMessage);
				}
			}else{//Stop has been pressed
				try {
					masterController.stop();
				} catch (ThreadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				btnStartStop.setText("Start");
				EarthPanel.getInstance().reset();
				//Reset pause button
				btnPauseResume.setEnabled(false);
				btnPauseResume.setSelected(false);
				btnPauseResume.setText("Pause");
				
				//Enable settings fields during sim
				updateSimInputAvailability(true);
			}
		}
	}; 
	
	private ActionListener actListPauseResume = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//Pause Selected
			try{
				if(btnPauseResume.isSelected()) { 
					masterController.pause();
					btnPauseResume.setText("Resume");
				} else { //Resume Selected
					masterController.resume();
					btnPauseResume.setText("Pause");
				}
			}catch (ThreadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private ActionListener actListQuery = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) { 

			if(!areQueryDatesValid() || !areLongLatValid()){
				return;//Exit upon invalid dates
			}
			
			if(queryNameSelect.getSelectedItem()!=null){
				//TODO: Load simulation by name
				
				Simulation sim = ObjectFactory.getSimulationDAO().getSimulationByName(queryNameSelect.getSelectedItem().toString());				
				startQuerySimulation(sim);
			}else{
				//TODO: See if simulation exists
				SimulationCriteria criteria = new SimulationCriteria();
				
				criteria.withAxialTilt(Double.parseDouble(txtAxialTiltQuery.getText()));
				criteria.withOrbitalEccentricity(Double.parseDouble(txtOrbitalEccQuery.getText()));
				List<Simulation> simulations = ObjectFactory.getSimulationDAO().findSimulationBy(criteria);
				
				if(simulations.isEmpty()){
					int response = JOptionPane.showConfirmDialog(frame, 
							"The simulation settings you have requested are not in available would you like to run the simulation now?",
							"Confirm",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION){
						txtAxialTiltSim.setText(txtAxialTiltQuery.getText());
						txtOrbitalEccSim.setText(txtOrbitalEccQuery.getText());
						tabs.setSelectedIndex(0);
						txtSimulationName.requestFocusInWindow();
					}
				}else{
					//grabbing Simulation 0 since orderby clause grabs best result
					startQuerySimulation(simulations.get(0));
				}
			}
		}
	};
	
	/*
	 * Validates the Grid Spacing and Simulation Time Step
	 */
	private String validValues(){
		if(txtSimulationName.getText().trim().equals(""))
			return "Simulation name cannot be blank.";
		if(ObjectFactory.getSimulationDAO().getSimulationNames().indexOf(txtSimulationName.getText()) >= 0){
			return "Simulation Name already exists.";
		}			
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
		
		try{
			int presDispRate = Integer.parseInt(txtPresentationDisplayRate.getText());
			if(presDispRate <1)
				return "The Presentation Display Rate must be greater than 0.";
		}catch(NumberFormatException e){
			return "Error processing Presentation Display Rate.";
		}
		
		return null;
	}
	
	private void updateSimInputAvailability(boolean status){
		txtSimLength.setEnabled(status);
		txtAxialTiltSim.setEnabled(status);
		txtGridSpacing.setEnabled(status);
		txtOrbitalEccSim.setEnabled(status);
		cbDisplayAnimation.setEnabled(status);
		spinnerSimTimeStep.setEnabled(status);
		txtPresentationDisplayRate.setEnabled(status);
	}
	
	private void updateQueryInputAvailability(boolean status){
		txtAxialTiltQuery.setEnabled(status);
		txtOrbitalEccQuery.setEnabled(status);
	}
	
	private void updateQueryOutputAvailability(boolean status){
		//TODO: un-comment the line below to have the button follow the rest 
		//btnQueryGo.setEnabled(status);
		cbMinTemp.setEnabled(status);
		cbMaxTemp.setEnabled(status);
		cbAllValues.setEnabled(status);
		//cbMeanRegionTemp.setEnabled(status);
		//cbMeanTimeTemp.setEnabled(status);
		if(cbAllValues.isSelected() || cbMeanRegionTemp.isSelected() || cbMeanTimeTemp.isSelected())
			btnActualValuesFile.setEnabled(status);
		/*
		if(cbMeanRegionTemp.isSelected())
			btnMeanTempRegionResult.setEnabled(status);
		if(cbMeanTimeTemp.isSelected())
			btnMeanTempTimeResult.setEnabled(status);
		*/
	}
	
	public void completeSimulation(){
		updateSimInputAvailability(true);
		btnStartStop.setSelected(false);
		btnStartStop.setText("Start");
		
		updateQueryOutputAvailability(true);		
	}
	
	private void loadSimulationSettingsFromQueryNameList(){
		if(queryNameSelect.getSelectedItem() == null){
			updateQueryInputAvailability(true);
			return;
		}
		Simulation sim = ObjectFactory.getSimulationDAO().getSimulationByName(queryNameSelect.getSelectedItem().toString());
		if(sim != null){
			txtAxialTiltQuery.setText(sim.getSimulationParameters().getAxialTilt()+"");
			txtOrbitalEccQuery.setText(sim.getSimulationParameters().getOrbitalEccentricity()+"");
			updateQueryInputAvailability(false);
		}else
			System.out.println("its null.");
	}

	private boolean areQueryDatesValid(){
		Date start = (Date)startTimeSpinner.getValue();
		Date end = (Date)endTimeSpinner.getValue();
		if(start.before(START_DATE)){
			JOptionPane.showMessageDialog(frame, "The start time must be on or after 1/4/2014");
			return false;//Exit method because of invalid data
		}
		
		if(!end.after(start)){
			JOptionPane.showMessageDialog(frame, "The end time must come after the start time.");
			return false;//Exit method because of invalid data
		}
	
		return true;
	}
	
	private boolean areLongLatValid(){
		if((txtEastBoundary.getText().toString().equals("")) != (txtWestBoundary.getText().toString().equals(""))){
			JOptionPane.showMessageDialog(frame, "Both East and West have to be blank or neither blank.");
			return false;
		}
		
		if((txtNorthBoundary.getText().toString().equals("")) != (txtSouthBoundary.getText().toString().equals(""))){
			JOptionPane.showMessageDialog(frame, "Both North and South have to be blank or neither blank.");
			return false;
		}
		
		if(!txtEastBoundary.getText().toString().equals("")){
			double east, west;
			
			try{
				east = Double.parseDouble(txtEastBoundary.getText().toString());
				if(Math.abs(east)>180)
					throw new NumberFormatException();
			} catch(NumberFormatException e){
				JOptionPane.showMessageDialog(frame, "East not set to a valid value. It must be between -180 and 180.");
				return false;
			}
			
			try{
				west = Double.parseDouble(txtWestBoundary.getText().toString());
				if(Math.abs(west)>180)
					throw new NumberFormatException();
			} catch(NumberFormatException e){
				JOptionPane.showMessageDialog(frame, "West not set to a valid value. It must be between -180 and 180.");
				return false;
			}
			
			if(east == west){
				JOptionPane.showMessageDialog(frame, "East and West boundary cannot be equal.");
				return false;
			}
		}
		
		if(!txtNorthBoundary.getText().toString().equals("")){
			double north, south;
			
			try{
				north = Double.parseDouble(txtNorthBoundary.getText().toString());
				if(Math.abs(north)>90)
					throw new NumberFormatException();
			} catch(NumberFormatException e){
				JOptionPane.showMessageDialog(frame, "North not set to a valid value. It must be between -90 and 90.");
				return false;
			}
			
			try{
				south = Double.parseDouble(txtSouthBoundary.getText().toString());
				if(Math.abs(south)>90)
					throw new NumberFormatException();
			} catch(NumberFormatException e){
				JOptionPane.showMessageDialog(frame, "South not set to a valid value. It must be between -90 and 90.");
				return false;
			}
			
			if(north == south){
				JOptionPane.showMessageDialog(frame, "North and South boundary cannot be equal.");
				return false;
			}
		}
		
		return true;
	}
	
	private void startQuerySimulation(Simulation sim){
		Date start = (Date)startTimeSpinner.getValue();
		Date end = (Date)endTimeSpinner.getValue();
		
		int startTime = (int)(start.getTime()-(START_DATE.getTime()))/(60*1000);//to convert from milliseconds to minutes
		int simulationLength = (int)((end.getTime()-start.getTime())/(60*1000));//to convert from milliseconds to minutes

//		double axialTilt = sim.getSimulationParameters().getAxialTilt();
//		double orbitalEccentricity = sim.getSimulationParameters().getOrbitalEccentricity();
//		int gridSpacing = sim.getSimulationParameters().getGridSpacing();
//		int simulationTimestep = sim.getSimulationParameters().getTimeStep();
//		int presentationDisplayRate = 1;
		boolean displayPresentation = true;
		
		Double east  = (txtEastBoundary.getText().equals(""))  ? null : Double.parseDouble(txtEastBoundary.getText());
		Double west  = (txtWestBoundary.getText().equals(""))  ? null : Double.parseDouble(txtWestBoundary.getText());
		Double north = (txtNorthBoundary.getText().equals("")) ? null :	Double.parseDouble(txtNorthBoundary.getText());
		Double south = (txtSouthBoundary.getText().equals("")) ? null : Double.parseDouble(txtSouthBoundary.getText());		
		
		if((startTime>=0) && (simulationLength>0)){
			try {
				QueryBoundary regionBounds = new QueryBoundary(north, south, east, west);
				lblMinTempResult.setText("--");
				lblMinTempResultTime.setText("--");
				lblMaxTempResult.setText("--");
				lblMaxTempResultTime.setText("--");
//				masterController.start(axialTilt, orbitalEccentricity, queryNameSelect.getSelectedItem().toString(), gridSpacing, simulationTimestep, startTime, simulationLength, presentationDisplayRate, displayPresentation);
				long queryStart = Utils.toSimulationTime(start);
				long queryEnd =  Utils.toSimulationTime(end);
				masterController.query(sim, queryStart, queryEnd, regionBounds, displayPresentation);
				updateQueryOutputAvailability(false);
				
			} catch (ArgumentInvalidException e) {				
				e.printStackTrace();
			} catch (ThreadException e) {
				e.printStackTrace();
			} catch (QueryBoundaryException e) {
				e.printStackTrace();
			}
		}
		

	}
	
	private void openOutputFile(String filename){
		File file = new File(filename);
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().open(file);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, "An error has occurred the file ("+filename+") is not accessible.");
			}
		}
	}
	
	private Date convertTimeToDate(long time){
		return new Date(START_DATE.getTime()+time*60*1000);
	}
			
}
