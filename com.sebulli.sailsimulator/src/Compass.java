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
public class Compass extends JComponent {

	private static final long serialVersionUID = 2775073576487768152L;
	
	// The back image and the neadle
	private Image backImage;
	private Image neadleImage;
	
	// Reference to the applet (for loading the images)
	private JApplet app;
	
	// The angle of the neadle
	private Double angle = 0.0;

	/**
	 * Constructor 
	 * creates the compass
	 * 
	 * @param app
	 * 		The applet
	 */
    public Compass(JApplet app) {
        super ();
        this.app = app;
        
        // Load the pictures
        backImage = app.getImage (app.getCodeBase(), "pics/compass.png");
        neadleImage = app.getImage (app.getCodeBase(), "pics/compass_neadle.png");
        this.setSize(100, 100);
    }
    

    /**
     * Set the wind direction
     * 
     * @param direction
     * 		The direction in radiant
     */
    public void setWindDirection (Double direction) {
    	angle = direction;
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
		g2D.drawImage(backImage, 0, 0, app);
		
		// Create the AffineTransform instance 
		AffineTransform affineTransform = new AffineTransform(); 
		
		// Rotate the image 
		affineTransform.translate(64-11, 61-38);
		affineTransform.rotate(angle, 11, 38); 

		// Draw the image using the AffineTransform 
		g2D.drawImage (neadleImage, affineTransform, app);

    }

}

