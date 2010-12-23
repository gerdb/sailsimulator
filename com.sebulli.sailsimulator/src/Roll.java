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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JApplet;
import javax.swing.JComponent;

/**
 * Compass control to display the wind direction
 * 
 * @author Gerd Bartelt
 *
 */
public class Roll extends JComponent {

	private static final long serialVersionUID = -2432678572650457134L;
	
	// The water image and the boat
	private Image boatImage;
	private Image waterImage;
	
	// Reference to the applet (for loading the images)
	private JApplet app;
	
	// The roll angle
	private Double angle = 0.0;

	/**
	 * Constructor 
	 * creates the roll component
	 * 
	 * @param app
	 * 		The applet
	 */
    public Roll(JApplet app) {
        super ();
        this.app = app;
        
        // Load the pictures
        boatImage = app.getImage (app.getCodeBase(), "pics/boat_roll.png");
        waterImage = app.getImage (app.getCodeBase(), "pics/boat_roll_water.png");
        this.setSize(100, 100);
    }
    

    /**
     * Set the roll angle
     * 
     * @param angle
     * 		The angle in radiant
     */
    public void setRollAngle (Double angle) {
    	this.angle = angle;
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
		
		// Create the AffineTransform instance 
		AffineTransform affineTransform = new AffineTransform(); 
		
		// Rotate the image 
		affineTransform.translate(64-38, 100-90);
		affineTransform.rotate(angle, 38, 90); 

		// Draw the image using the AffineTransform 
		g2D.drawImage (boatImage, affineTransform, app);

		// Draw the water
		g2D.drawImage(waterImage, 0, 99, app);

    }

}

