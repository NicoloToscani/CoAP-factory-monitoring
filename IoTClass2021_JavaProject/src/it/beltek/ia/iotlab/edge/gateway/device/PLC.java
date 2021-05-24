package it.beltek.ia.iotlab.edge.gateway.device;

import it.beltek.ia.iotlab.edge.gateway.ConnectionState;

public class PLC {
	
	// Measure timestamp
    public String timestamp;
    
    // Connection value (measure quality)
    public ConnectionState connectionState;
	
	// Machine state
	public int state;
	
	// Alarm presence
	public Boolean alarmPresence;
	
	// Alarms
	Boolean[] alarms;
	
	// Start command
	public Boolean startCommand;
	
	// Stop command
	public Boolean stopCommand;
	
	// Reset command
	public Boolean reset;
	
     
}
