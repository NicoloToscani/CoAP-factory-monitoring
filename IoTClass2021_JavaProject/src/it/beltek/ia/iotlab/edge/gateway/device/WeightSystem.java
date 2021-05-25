package it.beltek.ia.iotlab.edge.gateway.device;

import it.beltek.ia.iotlab.edge.gateway.ConnectionState;

public class WeightSystem {

	// Measure timestamp
    public String timestamp;
    
    // Connection value (measure quality)
    public ConnectionState connectionState;
    
    // Actual unit weight
    public float weight;
    
    // Line velocity - unit / min
    public int lineVelocity; 
    
    // Unit total count
    public int totalCount;
    
    // Weight setpoint
    public float setpoint;
	
}
