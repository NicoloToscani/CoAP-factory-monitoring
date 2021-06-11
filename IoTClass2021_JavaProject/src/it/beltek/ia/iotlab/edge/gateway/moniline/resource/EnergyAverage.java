package it.beltek.ia.iotlab.edge.gateway.moniline.resource;

public class EnergyAverage {
	
	    // Average current A
		public float I_Avg;
		public String currentUnitMeasure;
		
		// Average voltage V
	     public float LL_Avg;
	     public float LN_Avg;
	     public String voltageUnitMeasure;
	     
	     // Average power kW - kVAR - kVA 
	     public float active_power_T;
	     public String activePowerUnitMeasure;
	     public float reactive_power_T;
	     public String reactivePowerUnitMeasure;
	     public float apparent_power_T;
	     public String apparentPowerUnitMeasure;
	     
	     // Average power factor
	     public float pf_T;
	     
	     // Average frequency Hz
	     public float Frequency;
	     public String frequencyUnitMeasure;
	     
	     // Total active imported power kWh
	     public float Active_power_imp_total;
	     public String totalActivePowerUnitMeasure;
	     
	     public int lineID;
	     
}
