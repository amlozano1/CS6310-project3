package gui;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * A {@link JPanel} composed of the the earth and sun display components.
 * 
 * @author Andrew Bernard
 */
public class EarthPanel extends JPanel {

  private static EarthPanel earthPanel = new EarthPanel( );
	
  private static final long serialVersionUID = -1108120537851962997L;  
  private SunDisplay sunDisplay;
  private EarthGridDisplay earthGD;
  private TimeDisplay timeDisplay;
  private static final int DEFAULT_GRID_SPACING = 15; //degrees
  
  /**
   * Constructor - sets up the panel with the earth and sun display components using a
   * {@link BoxLayout} with {@link BoxLayout#PAGE_AXIS}.
   * 
   * @param minSize used in calling {@link #setMinimumSize(Dimension)}
   * @param maxSize used in calling {@link #setMaximumSize(Dimension)}
   * @param prefSize used in calling {@link #setPreferredSize(Dimension)}
   */
  private EarthPanel(Dimension minSize, Dimension maxSize, Dimension prefSize) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setMinimumSize(minSize);
    setMaximumSize(maxSize);
    setPreferredSize(prefSize);
    
    earthGD = new EarthGridDisplay(DEFAULT_GRID_SPACING);
    earthGD.setAlignmentX(Component.LEFT_ALIGNMENT);
        
    sunDisplay = new SunDisplay(earthGD.getWidth());
    sunDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    timeDisplay = new TimeDisplay();
    timeDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    add(sunDisplay);
    add(earthGD);
    add(timeDisplay);
  }
  
  private EarthPanel() {
	  super();
  }
  
  public void init ( Dimension minSize, Dimension maxSize, Dimension prefSize ) {
	    
	    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	    setMinimumSize(minSize);
	    setMaximumSize(maxSize);
	    setPreferredSize(prefSize);
	    
	    earthGD = new EarthGridDisplay(DEFAULT_GRID_SPACING);
	    earthGD.setAlignmentX(Component.LEFT_ALIGNMENT);
	        
	    sunDisplay = new SunDisplay(earthGD.getWidth());
	    sunDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    timeDisplay = new TimeDisplay();
	    timeDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    add(sunDisplay);
	    add(earthGD);
	    add(timeDisplay);
  }
  
  public static EarthPanel getInstance( ) {
	  return earthPanel;
  }
  
  /**
   * Draws the grid.
   * 
   * @param degreeSeparation the latitude and longitude degree separations 
   * between the cells in the grid
   */
  public void drawGrid(int degreeSeparation) {
	  earthGD.setGranularity(degreeSeparation);
	  sunDisplay.drawSunPath(earthGD.getWidth());
	  repaint();
  }
  
  /**
   * Gets the radius of the earth.
   * 
   * @return the radius of the earth in pixels
   */
  public int getRadius() {
    return earthGD.getRadius();
  }
      
  /**
   * Updates the display with the values from the temperature grid.
   * 
   * @param grid the grid to get the new temperature values from
   */
  public void updateGrid(TemperatureGrid grid) {
	  earthGD.updateGrid(grid);
  }
  
  /**
   * Moves the sun's position the specified number of degrees.
   * 
   * @param degrees the number of degrees to move the sun
   */
  public void moveSunPosition(float degrees) {
    sunDisplay.moveSunPosition(degrees);
    repaint();
  }
  
  public void addTime( int minutes ) {
	  timeDisplay.addTime(minutes);
  }
  
  /**
   * Resets the earth display and sun position.
   */
  public void reset() {
    sunDisplay.reset();
    earthGD.reset();
    timeDisplay.reset();
    repaint();
  }
  
  /**
   * Sets the opacity of the map image on a scale of 0 to 1, with 0 being 
   * completely transparent.
   * 
   * @param value the opacity value
   */
  public void setMapOpacity(float value) {
	  earthGD.setMapOpacity(value);
    repaint();
  }

}
