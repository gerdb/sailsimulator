/*
 * 
 *  SailSimulator - Free Invoicing Software 
 *  Copyright (C) 2010  Gerd Bartelt
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JApplet;
import javax.swing.JComponent;

/**
 * Compass control to display the wind direction
 * 
 * @author Gerd Bartelt
 *
 */
public class Earth extends JComponent {

	private static final long serialVersionUID = 2039288774668284129L;

	// The 2 back images
	private Image earth0Image;
	private Image earth1Image;
	
	private Double northCoordinates = 0.0;
	private Double eastCoordinates = 0.0;
	
	// Reference to the applet (for loading the images)
	private JApplet app;
	

	/**
	 * Constructor 
	 * creates the compass
	 * 
	 * @param app
	 * 		The applet
	 */
    public Earth(JApplet app) {
        super ();
        this.app = app;
        
        // Load the pictures
        earth0Image = app.getImage (app.getCodeBase(), "pics/earth0.png");
        earth1Image = app.getImage (app.getCodeBase(), "pics/earth1.png");
        this.setSize(192, 92);
    }
    
    /**
     * Set the coordinates
     * 
     * @param north
     * 		The north coordinates
     * @param east
     * 		The east coordinates
     */
    public void setCoordinates (Double north, Double east) {
    	northCoordinates = north / (180* 60 * 60) * Math.PI;
    	eastCoordinates = east / (180* 60 * 60) * Math.PI;
    }
    
    /**
     * Paint the component
     * 
     * @param g
     * 		Reference to the graphics
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Create a Java2D graphic object
        Graphics2D g2D = (Graphics2D)g;
		g2D.drawImage(earth0Image, 0, 0, app);
		g2D.drawImage(earth1Image, 100, 0, app);
		
		// Draw the cursor
		Double north = northCoordinates + 0.05;
		Double east = eastCoordinates - 0.03;
		
		Double xpos;
		Double ypos;
		ypos = 46.0 - 46 * Math.sin(north);
		if ( Math.cos(east) > 0.0 )
			xpos = 46.0 + 46 * Math.sin(east) * Math.cos(north);
		else
			xpos = 100 + 46.0 - 46 * Math.sin(east) * Math.cos(north);
		
        g2D.setColor(Color.RED);
        g2D.setStroke(new BasicStroke(1));
		g2D.drawLine(xpos.intValue()-10, ypos.intValue(), xpos.intValue()+10, ypos.intValue());
		g2D.drawLine(xpos.intValue(), ypos.intValue()-10, xpos.intValue(), ypos.intValue()+10);
    }

}

