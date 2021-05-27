package it.beltek.ia.iotlab.edge.gateway.device;

import it.beltek.ia.iotlab.edge.gateway.ConnectionState;

public class WeightSystem {

	// Measure timestamp
    public String timestamp;
    
    // Connection value (measure quality)
    public ConnectionState connectionState;
    
    // Actual unit weight
    public float weight;
    public String weightUnitMeasure;
    
    // Line velocity - unit / min
    public int lineVelocity;
    public String lineVelocityUnitMeasure;
    
    // Unit total count
    public int totalCount;
    
    // Weight setpoint (Kg)
    public float setpoint;
    
    // Velocity setpoint
    public int lineVelocitySetpoint;
	
}
