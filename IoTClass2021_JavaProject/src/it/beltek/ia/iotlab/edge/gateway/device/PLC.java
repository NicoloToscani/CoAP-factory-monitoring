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
	public boolean alarmPresence;
	
	// Alarms
	public boolean[] alarms;
	
	// Start command
	public boolean startCommand;
	
	// Stop command
	public boolean stopCommand;
	
	// Reset command
	public boolean reset;
	
     
}
