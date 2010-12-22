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

public class Simulation {
	
	// The simulation values
	private Double boat_direction = 0.0;
	private Double boat_speed = 1.0;
	private Double boat_x = 0.0;
	private Double boat_y = 0.0;
	private Double boat_rudder = 0.0;
	private Double wind_direction = 0.0;
	private Double wind_mean_direction = 0.0;
	private int simulatecnt =0;
	private Double wind_random = 0.0;
	
	/**
	 * Constructor
	 */
	public Simulation () {
	}

	
	/**
	 * Simulates the complete system
	 */
	public void simulate() {
		Double boat_speed_x, boat_speed_y;

		simulatecnt ++;

		// Calculate the direction
		boat_direction += (boat_rudder * boat_speed) *0.01;
		
		// Calculate the speed
		boat_speed_x = boat_speed * Math.sin(boat_direction);
		boat_speed_y = boat_speed * Math.cos(boat_direction);
		
		// Calculate the new position
		boat_x += boat_speed_x;
		boat_y += boat_speed_y;
		
		// Add a random value to the wind direction
		if (simulatecnt % 4 == 0) {
			wind_random = (Math.random()-0.5) * 2;
		}
		
		// Calculate the wind direction
		wind_direction += (wind_mean_direction +  wind_random- wind_direction) * 0.01; 
		
	}

    /**
     * Setter for rudder angle
     * 
     * @param boat_rudder
     * 		Angle of the rudder in radiant.
     */
	public void setRudder(Double boat_rudder) {
		this.boat_rudder = boat_rudder;
	}

	/**
	 * Getter for the direction of the boat
	 * 
	 * @return
	 * 		The direction in radiant
	 */
	public Double getBoat_direction() {
		return boat_direction;
	}

	/**
	 * Getter for the boat speed
	 * 
	 * @return
	 * 		The speed in m/s
	 */
	public Double getBoat_speed() {
		return boat_speed;
	}

	/**
	 * Getter for the boat position
	 * 
	 * @return
	 * 		The position in m
	 */
	public Double getBoat_x() {
		return boat_x;
	}

	/**
	 * Getter for the boat position
	 * 
	 * @return
	 * 		The position in m
	 */
	public Double getBoat_y() {
		return boat_y;
	}

	/**
	 * Getter for the rudder angle
	 * 
	 * @return
	 * 		The angle in radiant
	 */
	public Double getBoat_rudder() {
		return boat_rudder;
	}

	/**
	 * Getter for the wind angle
	 * 
	 * @return
	 * 		The angle in radiant
	 */
	public Double getWind_direction() {
		return wind_direction;
	}

}
