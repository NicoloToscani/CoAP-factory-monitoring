package it.beltek.ia.iotlab.edge.gateway.device;

import it.beltek.ia.iotlab.edge.gateway.ConnectionState;

public class SchneiderPM3200 {
	
	// Measure timestamp
    public String timestamp;
    
    // Connection value (measure quality)
    public ConnectionState connectionState;
	
	// Current A
	public float I1;
	public float I2;
	public float I3;
	public float I_Avg;
	
	//Voltage V
	 public float L1_L2;
     public float L2_L3;
     public float L3_L1;
     public float LL_Avg;
     public float L1_N;
     public float L2_N;
     public float L3_N;
     public float LN_Avg;
     
     // Power kW - kVAR - kVA 
     public float active_power_P1;
     public float active_power_P2;
     public float active_power_P3;
     public float active_power_T;
     public float reactive_power_P1;
     public float reactive_power_P2;
     public float reactive_power_P3;
     public float reactive_power_T;
     public float apparent_power_P1;
     public float apparent_power_P2;
     public float apparent_power_P3;
     public float apparent_power_T;
     
     // Power factor
     public float pf_P1;
     public float pf_P2;
     public float pf_P3;
     public float pf_T;
     
     // Current Unbalance %
     public float current_unbalance_I1;
     public float current_unbalance_I2;
     public float current_unbalance_I3;
     public float current_unbalance_worst;
     
     // Voltage Unbalance %
     public float voltage_unbalance_L1L2;
     public float voltage_unbalance_L2L3;
     public float voltage_unbalance_L3L1;
     public float voltage_unbalance_LL_worst;
     public float voltage_unbalance_L1N;
     public float voltage_unbalance_L2N;
     public float voltage_unbalance_L3N;
     public float voltage_unbalance_LN_worst;
     
     // Tangent Phi (Reactive Factor)
     public float tangent_phi;
     
     // Frequency Hz
     public float Frequency;
     
     // Temperature °C
     public float Temperature;
     
     // Total active imported power kWh
     public float Active_power_imp_total;
     
     // Load type 
     public String loadType;
     
}
