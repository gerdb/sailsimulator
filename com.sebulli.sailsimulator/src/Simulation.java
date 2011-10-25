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
	private Double boat_speed = 0.0;
	private Double boat_speed_orth = 0.0;
	private Double boat_x = 0.0;
	private Double boat_y = 0.0;
	private Double coord_north = 0.0;
	private Double coord_east = 0.0;
	private Double boat_rudder = 0.0;
	private Double wind_setpoint_direction = 0.0;
	private int simulatecnt = 0;
	private Double random_wind_speed = 0.0;
	private Double random_wind_direction = 0.0;
	private Double wind_speed = 0.0;
	private Double wind_rel_speed = 0.0;
	private Double wind_direction = 0.0;
	private Double wind_rel_direction = 0.0;
	private Double wind_setpoint_speed = 5.0;
	private Double sail_rope_setpoint = 0.5;
	private Double sail_rope = 0.0;
	private Double sail_angle = 0.0;
	private Double wind_sail_force = 0.0;
	private Double sail_wind_angle = 0.0;
	private Double wind_sail_boat_force = 0.0;
	private Double boat_speed_force = 0.0;
	private Double boat_speed_orth_force = 0.0;
	private Double wind_sail_aero_force = 0.0;
	private Double wind_sail_aero_boat_force = 0.0;
	private Double boat_wind_angle = 0.0;
	private Double boat_wind_force = 0.0;
	private Double cross_wind_force = 0.0;
	private Double boat_roll_angle = 0.0;
	private Double centrifugal_force = 0.0;
	private Double boat_roll_factor = 0.0;
	private Double boat_force_orth = 0.0;
	
	
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
		boat_direction += (boat_rudder * boat_speed) *0.01 - boat_force_orth * 0.003;
		// The centrifugal force
		centrifugal_force = - (boat_rudder * boat_speed) * 2;

		
		
		// Calculate the speed
		boat_speed_x = boat_speed * Math.sin(boat_direction);
		boat_speed_y = boat_speed * Math.cos(boat_direction); 
		
		// Calculate the new position
		boat_x += boat_speed_x;
		boat_y += boat_speed_y;
		
		boat_x += boat_speed_orth * Math.cos(boat_direction);
		boat_y += boat_speed_orth * Math.sin(boat_direction);
		
		// Calculate the coordinates
		coord_east += boat_speed_x * 0.1;
		coord_north += boat_speed_y * 0.1;
		
		//coord_east += 0.1;
		//coord_north += 0.01;
		
		// Add a random value to the wind direction
		if (simulatecnt % 4 == 0) {
			// Calculate the wind direction, add a random value
			Double wind_speed_limitted = wind_speed;
			if (wind_speed_limitted > 5.0)
				wind_speed_limitted = 5.0;
			wind_speed_limitted+= wind_speed *0.3;
			
			random_wind_direction = 0.1 * (Math.random()-0.5) * wind_speed_limitted;
		}
		wind_direction += (wind_setpoint_direction +  random_wind_direction - wind_direction) * 0.105; 
		
		// Add a random value to the wind speed
		if (simulatecnt % 10 == 0) {
			// Calculate the wind speed, add a random value
			random_wind_speed = (Math.random()) * 0.5 + 0.5;
		}
		wind_speed += ( (wind_setpoint_speed * random_wind_speed) - wind_speed) * 0.1; 
		
		// Calculate the maximum angle of the sail
		if (Math.abs((sail_rope_setpoint * 2.0) - sail_rope) < 0.01)
			sail_rope += ((sail_rope_setpoint * 2.0) - sail_rope);
		else {
			if (((sail_rope_setpoint * 2.0) - sail_rope) > 0)
				sail_rope += 0.01;
			else
				sail_rope -= 0.01;
				
		}
		
		// Calculate the wind, relative to the boat
		Double wind_rel_x, wind_rel_y;
		wind_rel_x = wind_speed * Math.cos(wind_direction) - boat_speed * Math.cos(boat_direction);
		wind_rel_y = wind_speed * Math.sin(wind_direction) - boat_speed * Math.sin(boat_direction);
		wind_rel_speed = Math.sqrt((wind_rel_x * wind_rel_x) + (wind_rel_y * wind_rel_y));
		wind_rel_direction = Math.atan2(wind_rel_y , wind_rel_x);
		
		// Winds blows onto the sail and changes the direction
		sail_wind_angle = boat_direction + sail_angle - wind_rel_direction ;
		wind_sail_force =  wind_rel_speed * Math.sin(sail_wind_angle);
		sail_angle += wind_sail_force * 0.01;
		
		// Limit it to the sail_rope
		if (sail_angle > sail_rope)
			sail_angle = sail_rope;
		if (sail_angle < -sail_rope)
			sail_angle = -sail_rope;
		
		// Force of the wind in the sail in the direction of the boat
		wind_sail_boat_force = 2.0 * wind_sail_force * Math.sin(sail_angle);

		// Aerodynamic force in the sail
		wind_sail_aero_force =  wind_rel_speed * Math.abs(Math.cos(sail_wind_angle));
		if (Math.abs(Math.sin(sail_wind_angle)) <= 0.1)
			wind_sail_aero_force = 0.0;
		wind_sail_aero_boat_force =  wind_sail_aero_force * Math.abs(Math.sin(sail_angle));

		// Wind force on the boat
		boat_wind_angle = wind_rel_direction - boat_direction;
		boat_wind_force =  0.1 * wind_rel_speed * Math.cos(boat_wind_angle);

		// The cross wind
		cross_wind_force = wind_rel_speed * Math.sin(boat_wind_angle) * (Math.abs(Math.cos(sail_angle)) + 0.1);
		boat_force_orth = (boat_roll_factor + 0.5) * cross_wind_force * 0.05;
		
		// Force of the water 
		boat_speed_force = - 5.0 * boat_speed * Math.abs(boat_speed);
		boat_speed_orth_force = - 5.0 * boat_speed_orth * Math.abs(boat_speed_orth);

		// Calculate the roll angle of the boat
		boat_roll_angle += (Math.atan(0.1 * (cross_wind_force + centrifugal_force)) - boat_roll_angle) * 0.1;
		boat_roll_factor = Math.abs(Math.cos(boat_roll_angle));
			
		// All the forces will accelerate the boat
		boat_speed += (	boat_roll_factor * (wind_sail_boat_force + 
						wind_sail_aero_boat_force) + 
						boat_speed_force +
						boat_wind_force
						) *0.001;

		// The orthogonal speed
		boat_speed_orth += ( boat_force_orth + boat_speed_orth_force ) *0.1;
		
		
		
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
     * Setter for wind direction
     * 
     * @param wind_direction
     * 		The wind direction in radiant
     */
	public void setWindDirection(Double wind_direction) {
		this.wind_setpoint_direction = wind_direction;
	}

    /**
     * Setter for wind speed
     * 
     * @param wind_speed
     * 		Wind speed in m/s.
     */
	public void setWindSpeed(Double wind_speed) {
		this.wind_setpoint_speed = wind_speed;
	}

	/**
     * Setter for sail rope
     * 
     * @param sail_rope
     * 		The length of the sail rope from 0.0 to 1.0
     */
	public void setSailRope(Double sail_rope) {
		this.sail_rope_setpoint = sail_rope;
	}

	/**
	 * Getter for the angle of the sail
	 * 
	 * @return
	 * 		The angle in radiant
	 */
	public Double getSailAngle() {
		return sail_angle;
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
	 * Getter for the boat position
	 * 
	 * @return
	 * 		The position 
	 */
	public Double getCoordNorth() {
		return coord_north;
	}

	/**
	 * Getter for the boat position
	 * 
	 * @return
	 * 		The position 
	 */
	public Double getCoordEast() {
		return coord_east;
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

	/**
	 * Getter for the wind speed
	 * 
	 * @return
	 * 		The speed in m/s
	 */
	public Double getWind_speed() {
		return wind_speed;
	}
	
	/**
	 * Getter for the sail relative to the wind angle
	 * 
	 * @return
	 * 		The angle in radiant
	 */
	public Double getSailWind_angle() {
		return sail_wind_angle;
	}

	/**
	 * Getter for the roll angle of the boat
	 * 
	 * @return
	 * 		The angle in radiant
	 */
	public Double getRoll_angle() {
		return boat_roll_angle;
	}
	

}
