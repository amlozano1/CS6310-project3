package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;

/**
 * Use this class to display an image of the earth with a grid drawn on top. All the methods that could
 * be used to update the grid are given package level access - these methods should be interacted with 
 * from the {@link EarthPanel}.
 * 
 * @author Andrew Bernard
 */
public class EarthGridDisplay extends JPanel {
  private static final long serialVersionUID = -1108120968981962997L;
  private static final float OPACITY = 0.2f;  
  private static final int DEFAULT_CELL_TEMP = 15; //degrees in celsius
  private TemperatureColorPicker colorPicker = TemperatureColorPicker.getInstance();
  
  private BufferedImage imgTransparent;
  private BufferedImage earthImage;
  private BufferedImage sunImage;
  private float opacity = OPACITY;
  private float[] offsets = new float[4];
  private int degreeSeparation;
  private int pixelsPerCellX; //number of pixels per latitudal division
  private int pixelsPerCellY; //number of pixels per longitudal division
  private int imgWidth; // in pixels
  private int imgHeight; // in pixels
  private int numCellsX;
  private int numCellsY;
  private int radius;
  private boolean paintInitialColors = true;
  private TemperatureGrid grid;
  
  /**
   * Constructs a display grid with a default grid spacing.
   * 
   * @param defaultGridPacing in degrees
   */
  EarthGridDisplay(int defaultGridPacing) {
    earthImage = new EarthImage().getBufferedImage();    
    sunImage = new SunImage().getBufferedImage();
    setGranularity(defaultGridPacing);
    setIgnoreRepaint(true);
  }
  
  /**
   * Sets the granularity of the grid.
   * 
   * @param degreeSeparation the latitude and longitude degree separations 
   * between the cells in the grid
   */
  void setGranularity(int degreeSeparation) {
    this.degreeSeparation = degreeSeparation;
    
    numCellsX = 360 / degreeSeparation;      
    pixelsPerCellX = earthImage.getWidth() / numCellsX;
    imgWidth = numCellsX * pixelsPerCellX;

    numCellsY = 180 / degreeSeparation;
    pixelsPerCellY = earthImage.getHeight() / numCellsY;
    imgHeight = numCellsY * pixelsPerCellY;
    radius = imgHeight/2;
    
    //create an image capable of transparency; then draw our image into it
    imgTransparent = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics g = imgTransparent.getGraphics();
    g.drawImage(earthImage, 0, 0, imgWidth, imgHeight, null);  
  }
  
  public void paint(Graphics g) {
	//the order in which these are called does matter
	g.drawImage(earthImage, 0, 0, imgWidth, imgHeight, null);
	
	if(paintInitialColors)
		initCellColors(g);
	else {
		fillCellColors(g);
		drawSun(g);
	}
	//drawTransparentImage(g);
	drawGrid(g);
  }
  
  private void initCellColors(Graphics g) {
    g.setColor(colorPicker.getColor(DEFAULT_CELL_TEMP, opacity));
    g.fillRect(0, 0, imgWidth, imgHeight);
  }
  
  /**
   * Updates the display with the values from the temperature grid.
   * 
   * @param grid the grid to get the new temperature values from
   */
  void updateGrid(TemperatureGrid grid) {
    this.grid = grid;
    paintInitialColors = false;    
    this.repaint();
  }
  
  /**
   * Gets the radius of the earth.
   * 
   * @return the radius of the earth in pixels
   */
  int getRadius() {
    return radius;
  }

  /**
   * This is used implicitly by swing to do it's layout job properly
   */
  public int getWidth() {
    return imgWidth;
  }
  
  private void fillCellColors(Graphics g) {
    int cellX=0, cellY=0;
    int cellWidth = pixelsPerCellX;
    int xIndex = 0;
    
    int lat = 90;
    for (int y = 0; y < numCellsY; y++) {
    	int top = (int)Util.getDistToEquator(lat, radius);
    	lat -= degreeSeparation;
    	int bot = (int)Util.getDistToEquator(lat, radius);
    	int cellHeight = top-bot; 
    	      
    	for (int x = 0; x < numCellsX ; x++){
    		
    		xIndex = x + numCellsX/2;
    		
    		if ( xIndex >= numCellsX ) {
    			xIndex = xIndex - numCellsX;
    		}
    		
	        double newTemp = grid.getTemperature(xIndex, y);
	        int colorValue = new Double(newTemp).intValue();
	        
	        g.setColor(colorPicker.getColor(colorValue, opacity));
	        g.fillRect(cellX, cellY, cellWidth, cellHeight);

	        //System.out.println("("+cellX+","+cellY+","+cellWidth+","+cellHeight+")");
	        cellX += cellWidth;
	       //System.out.print("\n["+x+", "+y+"] color: " + colorValue);
	        top = bot;
	      }      
      cellY += cellHeight;
      cellX = 0;
    }
  }

  private void drawGrid(Graphics g) {
    g.setColor(new Color(0,0,0,80));
    //draw longitude lines
    for(int x = 0; x <= imgWidth; x += pixelsPerCellX) {
      g.drawLine(x, 0, x, imgHeight);      
    }
    
    //draw scaled latitude lines
    for(int lat = 0; lat <= 90; lat += degreeSeparation) {
      int y = (int)Util.getDistToEquator(lat, radius);
      g.drawLine(0, radius-y, imgWidth, radius-y);
      g.drawLine(0, radius+y, imgWidth, radius+y);

    }
    
    g.setColor(Color.blue);
    g.drawLine(imgWidth/2, 0, imgWidth/2, imgHeight); //prime meridian
    g.drawLine(0, imgHeight/2, imgWidth, imgHeight/2); // equator
  }
  
  private void drawSun(Graphics g){
	  //TODO calculate xcoor and y coord from long/lat
	  int xcoord = (int)(grid.getSunLongitude());
	  int ycoord = (int)(grid.getSunLatitude());
      g.drawImage(sunImage, 100/*replace with xcoord*/, 100/*replace with ycoord*/, 30, 30, this);
  }
  
  /**
   * Sets the opacity of the map image on a scale of 0 to 1, with 0 being 
   * completely transparent.
   * 
   * @param value the opacity value
   */
  void setColorOpacity(float value) {
    opacity = value;
  }

  void reset() {
    paintInitialColors = true;
  }
  
  
  
}
