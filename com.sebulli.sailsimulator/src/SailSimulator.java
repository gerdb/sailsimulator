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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Sail Simulator Applet
 * 
 * @author Gerd Bartelt
 */
public class SailSimulator extends JApplet implements ActionListener {
	
	private static final long serialVersionUID = -6566441023625011779L;

	// The timer animating the simulation and the simulation
	private Timer timer;		
	private Simulation simulation = new Simulation();

	// The main panel
	private JPanel mainPanel;

	//the applet's ocean pane
    private Ocean ocean;  

    // The control panel
    private JPanel controlPanel;

    // The controls of the control panel
    private JSlider rudderSlider ;
	private Compass compass;

    
    /**
     * Create the GUI. For thread safety, this method should
     * be invoked from the event-dispatching thread.
     */
    private void createGUI() {

    	// Create the main panel
    	mainPanel = new JPanel (null);
        setContentPane(mainPanel);

        // Create the ocean 
        ocean = new Ocean(this);
        ocean.setBounds(0, 0, 500, 500);

        // Create the control panel 
        controlPanel = new JPanel ();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlPanel.setBounds(500, 0, 300, 500);

        // Create the components of the control panel
        JLabel label = new JLabel("Rudder Position:");
        controlPanel.add(label);
        controlPanel.add(Box.createRigidArea(new Dimension(0,5)));

        // Create the rudder slider
        rudderSlider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
        rudderSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				simulation.setRudder(((double)rudderSlider.getValue())*0.01);
			}
        });
        controlPanel.add(rudderSlider);
        controlPanel.add(Box.createRigidArea(new Dimension(0,5)));
        
        // Create the compass
        compass = new Compass(this); 
        controlPanel.add(compass);

        
        // Add the ocean and the control panel to the mainPanel
        mainPanel.add(ocean);
        mainPanel.add(controlPanel);
    }


    /**
     * Called when this applet is loaded into the browser
     */
    public void init() {

    	// Execute a job on the event-dispatching thread:
        // Creating this applet's GUI.
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) { 
            System.err.println("createGUI didn't successfully complete");
        }

        // Set up timer to drive simulation events
        timer = new Timer(20, this);
        timer.start(); 
    }

    /**
     * Perform the 20ms timer event
     */
    public void actionPerformed(ActionEvent e) {

    	// Do the simulation
		simulation.simulate();
		
		// Update the components with the values of the simulation
		updateComponents();

		// Redraw all
        mainPanel.repaint();

    }

    /**
     * Start the applet
     */
    public void start() {
    	timer.restart();
    }

    /**
     * Stop the applet
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Update all components with the simulation results
     */
    public void updateComponents() {
    	compass.setWindDirection(simulation.getWind_direction());
    	ocean.setBoat_x(simulation.getBoat_x());
    	ocean.setBoat_y(simulation.getBoat_y());
    	ocean.setBoat_direction(simulation.getBoat_direction());
    	ocean.setBoat_rudder(simulation.getBoat_rudder());
    }
    
    /**
     * The applet information.
     */
    public String getAppletInfo() {
        return "Title: Sail Simulator v0.1, 2010-12-22\n"
               + "Author: Gerd Bartelt\n"
               + "Simulates a sail boat.";
    }

}