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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;

import javax.swing.JPanel;

/**
 * Ocean control to display the sail boat in action
 * 
 * @author Gerd Bartelt
 *
 */
public class Ocean extends JPanel {

	private static final long serialVersionUID = 8232626403797607971L;

	// Position of the camera
	private Double cam_x = 0.0;
	private Double cam_y = 0.0;
	
	// Boat position and values
	private Double boat_x = 0.0; 
	private Double boat_y = 0.0; 
	private Double boat_direction = 0.0;
	private Double boat_rudder = 0.0;
	private Double sail_angle = 0.0;
	private Double sail_wind_angle = 0.0;
	private Double coord_north = 0.0;
	private Double coord_east = 0.0;

	
	
	// The ocean and the boat
	private Image backImage;
	private Image boatImage;
	
	// The sail
	private Image sailRightImage;
	private Image sailLeftImage;
	private Image sail1Image;
	private Image sail2Image;
	private Image sail3Image;
	private Image sail4Image;
	private enum Sailtype { RIGHT, LEFT, FLUTTER};
	private int flutter  = 0;
	private int anicnt = 0;
	
	

	/**
	 * Constructor 
	 * creates the Ocean
	 * 
	 * @param app
	 * 		The applet
	 */
	public Ocean(SailSimulator app) {
    	super(null);
    	setOpaque(true);
    	
        // Load the pictures
		backImage = app.getImage (app.getCodeBase(), "pics/water_1000x1000.png");
		boatImage = app.getImage (app.getCodeBase(), "pics/boat.png");
		sail1Image = app.getImage (app.getCodeBase(), "pics/sail1.png");
		sail2Image = app.getImage (app.getCodeBase(), "pics/sail2.png");
		sail3Image = app.getImage (app.getCodeBase(), "pics/sail3.png");
		sail4Image = app.getImage (app.getCodeBase(), "pics/sail4.png");
		sailRightImage = app.getImage (app.getCodeBase(), "pics/sail_right.png");
		sailLeftImage = app.getImage (app.getCodeBase(), "pics/sail_left.png");
		
    }

	/**
	 * Converts the coordinates to a string
	 * 
	 * @param poss
	 * 		Prefix used, if the coordinates are positive
	 * @param negs
	 * 		Prefix used, if the coordinates are negative
	 * @param coord
	 * 		The coordinates
	 * @return
	 * 		The coordinate string. Unit: second
	 */
	private String Coord2String (String poss, String negs, Double coord){
		String s;
			
		Long l;
		Long coordl = Math.abs(coord.longValue());
		DecimalFormat df =   new DecimalFormat  ( "00" );
		
		// Generates the seconds
		l = coordl % 60;
		s = df.format(l) + "\"";
		coordl = coordl / 60;

		// Generates the minutes
		l = coordl % 60;
		s = df.format(l) + "'" + s;
		coordl = coordl / 60;
		
		// Generates the degrees
		l = coordl;
		s = df.format(l) + "°" + s;
		if (coord >= 0)
			s = poss + s;
		else
			s = negs + s;
		
		return s;
	}
	
    /**
     * Paint the component
     * 
     * @param g
     * 		Reference to the graphics
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Counts the frames
        anicnt++;
        
        // Position of the boat relative to the screen 
		Double x_pos_boat, y_pos_boat;
		Double x_pos_water, y_pos_water;

		// Track the camera
		if (cam_x < boat_x - 100.0)
			cam_x = boat_x - 100.0;
		if (cam_x > boat_x + 100.0)
			cam_x = boat_x + 100.0;
		
		if (cam_y < boat_y - 100.0)
			cam_y = boat_y - 100.0;
		if (cam_y > boat_y + 100.0)
			cam_y = boat_y + 100.0;
		
		// Calculate the position of the boat symbol on the screen
		x_pos_boat = (Double)(boat_x - cam_x)+ 500/2;
		y_pos_boat = -(Double)(boat_y - cam_y) + 500/2;
			
		// Calculate the position of the water on the screen
		x_pos_water = cam_x;
		y_pos_water = -cam_y;
		
		// Copy the part of the ocean from the ocean picture, but do only use 
		// parts inside the picture area
		if (x_pos_water >= 0)
			x_pos_water = x_pos_water % 500;
		else
			x_pos_water = 500- ((-x_pos_water) % 500);

		if (y_pos_water >= 0)
			y_pos_water = y_pos_water % 500;
		else
			y_pos_water = 500- ((-y_pos_water) % 500);

		
		// Draw the water
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage (backImage, 0, 0, 499, 499, x_pos_water.intValue() + 0, y_pos_water.intValue() + 0, x_pos_water.intValue() + 499, y_pos_water.intValue() + 499, this);
		
		
		
		
		// Draw the rudder
        Double rudder_x_start, rudder_y_start;
        Double rudder_x_end, rudder_y_end;
        
        rudder_x_start =  x_pos_boat + 85  * Math.sin(- boat_direction);
        rudder_y_start =  y_pos_boat + 85  * Math.cos(- boat_direction);

        rudder_x_end =  rudder_x_start + 20*Math.sin(boat_rudder - boat_direction);
        rudder_y_end =  rudder_y_start + 20*Math.cos(boat_rudder - boat_direction);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(5));
		g2d.drawLine(rudder_x_start.intValue(), rudder_y_start.intValue(), rudder_x_end.intValue(), rudder_y_end.intValue());
		

		// Draw the boat 
		
		// Create the AffineTransform instance 
		AffineTransform affineTransform = new AffineTransform(); 
		// Rotate the image 
		affineTransform.translate(x_pos_boat -48, y_pos_boat-boatImage.getHeight(this)/2);
		affineTransform.rotate(boat_direction, 48, boatImage.getHeight(this)/2); 
		// Draw the boat using the AffineTransform 
		g2d.drawImage (boatImage, affineTransform, this);
        
		// Draw the sail 
		
		// Create the AffineTransform instance 
		affineTransform = new AffineTransform();

		Sailtype sailtype = Sailtype.FLUTTER;
		
		if (Math.sin(sail_wind_angle) > 0.1)
			sailtype = Sailtype.LEFT;

		if (Math.sin(sail_wind_angle) < -0.1)
			sailtype = Sailtype.RIGHT;
		
		Image sailImage = null;
		int rotate_x = 0;
		int rotate_y = 0;

		// Use the picture of the sail, depending on the sailtype
		switch (sailtype) {
		case LEFT:
			sailImage = sailLeftImage;
			rotate_x = 21;
			rotate_y = 1;
			break;
		case RIGHT:
			sailImage = sailRightImage;
			rotate_x = 3;
			rotate_y = 1;
			break;
		case FLUTTER:
			
			// Animate the flutter
			if (anicnt % 4 == 0)
				flutter ++;
			
			if (flutter >= 4)
				flutter = 0;
			if (flutter == 0) sailImage = sail1Image;
			if (flutter == 1) sailImage = sail2Image;
			if (flutter == 2) sailImage = sail3Image;
			if (flutter == 3) sailImage = sail4Image;
			rotate_x = 6;
			rotate_y = 1;
			break;
		}

		// Rotate the image 
		affineTransform.translate(x_pos_boat - rotate_x, y_pos_boat-rotate_y-25);
		affineTransform.rotate(boat_direction , rotate_x, rotate_y+25); 
		affineTransform.rotate(sail_angle , rotate_x, rotate_y); 
		// Draw the sail using the AffineTransform 
		g2d.drawImage (sailImage, affineTransform, this);
		
		// Print the coordinates
        g2d.setFont(new Font("Serif", Font.BOLD, 11));
        
		g2d.drawString(Coord2String("N","S", coord_north), 400, 450);
		g2d.drawString(Coord2String("E","W", coord_east), 400, 470);
		
    }

    /**
     * Setter for boat x position
     * 
     * @param boat_x
     * 		x position of the boat
     */
	public void setBoat_x(Double boat_x) {
		this.boat_x = boat_x;
	}

    /**
     * Setter for boat north coordinates
     * 
     * @param coord_north
     * 		North coordinates of the boat
     */
	public void setCoord_north(Double coord_north) {
		this.coord_north = coord_north;
	}

    /**
     * Setter for boat east coordinates
     * 
     * @param coord_east
     * 		East coordinates of the boat
     */
	public void setCoord_east(Double coord_east) {
		this.coord_east = coord_east;
	}

    /**
     * Setter for boat y position
     * 
     * @param boat_y
     * 		y position of the boat
     */
	public void setBoat_y(Double boat_y) {
		this.boat_y = boat_y;
	}

    /**
     * Setter for the sail angle
     * 
     * @param sail_angle
     * 		angle of the sail relative to the boat
     */
	public void setSail_angle(Double sail_angle) {
		this.sail_angle = sail_angle;
	}

    /**
     * Setter for the sail to wind angle
     * 
     * @param sail_angle
     * 		angle of the sail relative to the boat and the wind
     */
	public void setSailWind_angle(Double sail_wind_angle) {
		this.sail_wind_angle = sail_wind_angle;
	}

	/**
     * Setter for boat direction
     * 
     * @param boat_direction
     * 		Direction of the boat
     */
	public void setBoat_direction(Double boat_direction) {
		this.boat_direction = boat_direction;
	}

    /**
     * Setter for boat rudder angle
     * 
     * @param boat_rudder
     * 		Angle of the rudder in radiant.
     */
	public void setBoat_rudder(Double boat_rudder) {
		this.boat_rudder = boat_rudder;
	}
    
}

