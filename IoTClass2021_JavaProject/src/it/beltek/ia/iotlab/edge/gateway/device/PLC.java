package it.beltek.ia.iotlab.edge.gateway.device;

import java.util.List;

import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;

public class PLC {
	
	// Measure timestamp
    public String timestamp;
    
    // Connection value (measure quality)
    public ConnectionState connectionState;
	
	// Machine state
	public String state;
	
	// Alarm presence
	public boolean alarmPresence;
	
	// Alarms
	public List<Alarm> alarmList;
	
	// Start command
	public boolean startCommand;
	
	// Stop command
	public boolean stopCommand;
	
	// Reset command
	public boolean reset;
	
     
}
