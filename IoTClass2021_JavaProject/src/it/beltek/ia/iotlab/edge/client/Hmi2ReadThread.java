package it.beltek.ia.iotlab.edge.client;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;

import org.eclipse.californium.core.coap.CoAP.Code;


public class Hmi2ReadThread implements Runnable {
	
	HmiProduction hmiProduction;
	
	Timer requestTimer;

	
	public Hmi2ReadThread(HmiProduction hmiProduction) {
		
		this.hmiProduction = hmiProduction;
		
		this.requestTimer = new Timer();
		
	}

	@Override
	public void run() {
		
		System.out.println("Hmi1ReadThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
	}
	
	private class RequestTimerTask extends TimerTask{
		
		Hmi2ReadThread hmi2ReadThread;
		
		public RequestTimerTask(Hmi2ReadThread hmi2ReadThread) {
			
			this.hmi2ReadThread = hmi2ReadThread;
		}

		@Override
		public void run() {
			
			// Read 
			Iterator <Integer> it = this.hmi2ReadThread.hmiProduction.getHmiMonilineMap().keySet().iterator();
	        while(it.hasNext()) {
	        	
	        	int key = it.next();
	        
	        	HMIMoniline hmiMoniline = this.hmi2ReadThread.hmiProduction.getHmiMonilineMap().get(key);
	        	
	            hmiMoniline.readEnergyAverage();
	            hmiMoniline.readPlcAverage();
	        	
	        }
	        
	        // Display values
	     	Iterator <Integer> itMeasure = this.hmi2ReadThread.hmiProduction.getHmiMonilineMap().keySet().iterator();
	     	while(itMeasure.hasNext()) {
	     	        	
	     	    int key = itMeasure.next();
	     	        
	     	    HMIMoniline hmiMoniline = this.hmi2ReadThread.hmiProduction.getHmiMonilineMap().get(key);
	     	    
	     	    System.out.println("-------------- Line " + key + " --------------------");
	     	    
	     	    int plcListSize = hmiMoniline.getPlcAverage().size();
	     	    
	     	    for(int i = 0; i < plcListSize; i++) {
	     	    	
	     	    	System.out.println("---------- PLC's ----------");
					System.out.println("PLC lineID: " + hmiMoniline.getPlcAverage().get(i).lineID);
					System.out.println("PLC machineID: " + hmiMoniline.getPlcAverage().get(i).machineID);
					System.out.println("PLC state: " + hmiMoniline.getPlcAverage().get(i).state);
	     	    		
	     	    }
	     	   
	     	    
		        System.out.println("---------- ENERGY ----------");
		        System.out.println("LL_Avg: " + hmiMoniline.getEnergyAverage().LL_Avg + " " + hmiMoniline.getEnergyAverage().voltageUnitMeasure);
		        System.out.println("I_Avg: " + hmiMoniline.getEnergyAverage().I_Avg + " " + hmiMoniline.getEnergyAverage().currentUnitMeasure);
				System.out.println("LN_Avg: " + hmiMoniline.getEnergyAverage().LN_Avg + " " + hmiMoniline.getEnergyAverage().voltageUnitMeasure);
				System.out.println("ActivePower_T: " + hmiMoniline.getEnergyAverage().active_power_T + " " + hmiMoniline.getEnergyAverage().activePowerUnitMeasure);
				System.out.println("ReactivePower_T: " + hmiMoniline.getEnergyAverage().reactive_power_T + " " + hmiMoniline.getEnergyAverage().reactivePowerUnitMeasure);
				System.out.println("ApparentPower_T: " + hmiMoniline.getEnergyAverage().apparent_power_T + " " + hmiMoniline.getEnergyAverage().apparentPowerUnitMeasure);
				System.out.println("PF_T: " + hmiMoniline.getEnergyAverage().pf_T);
				System.out.println("Frequency: " + hmiMoniline.getEnergyAverage().Frequency + " " + hmiMoniline.getEnergyAverage().frequencyUnitMeasure);
				System.out.println("Active_power_Import_Total: " + hmiMoniline.getEnergyAverage().Active_power_imp_total + " " + hmiMoniline.getEnergyAverage().totalActivePowerUnitMeasure);
				
				
	     }
	       
		}
		
	}
	
	
	private class ObsTimerTask extends TimerTask{
		
		Hmi2ReadThread rejectFieldbusThread;
		
		public ObsTimerTask(Hmi2ReadThread rejectFieldbusThread) {
			
			this.rejectFieldbusThread = rejectFieldbusThread;
		}

		@Override
		public void run() {
			
			
		}
		
	}

}
