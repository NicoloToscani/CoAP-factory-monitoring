package it.beltek.ia.iotlab.edge.gateway.moniline;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.client.HMIMachine;
import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;
import it.beltek.ia.iotlab.edge.gateway.moniline.resource.PlcAverage;

import org.eclipse.californium.core.coap.CoAP.Code;


public class MonilineGatewayReadThread implements Runnable {
	
	MonilineGateway monilineGateway;
	
	Timer requestTimer;

	
	public MonilineGatewayReadThread(MonilineGateway monilineGateway) {
		
		this.monilineGateway = monilineGateway;
		
		this.requestTimer = new Timer();
		
	}

	private void initValues() {
		
		Iterator <Integer> itMeasure = this.monilineGateway.getHmiMachineMap().keySet().iterator();
     	while(itMeasure.hasNext()) {
     		
     	        	
     	    int key = itMeasure.next();
     	    
     	    HMIMachine hmiMachine = this.monilineGateway.getHmiMachineMap().get(key);
     	    
     	    this.monilineGateway.getEnergyAverage().currentUnitMeasure = hmiMachine.getPm3200().currentUnitMeasure;
     	    
     	    this.monilineGateway.getEnergyAverage().voltageUnitMeasure = hmiMachine.getPm3200().voltageUnitMeasure;
     	   
     	    this.monilineGateway.getEnergyAverage().activePowerUnitMeasure = hmiMachine.getPm3200().activePowerUnitMeasure;
     	    
     	    this.monilineGateway.getEnergyAverage().reactivePowerUnitMeasure = hmiMachine.getPm3200().reactivePowerUnitMeasure;
     	   
     	    this.monilineGateway.getEnergyAverage().apparentPowerUnitMeasure = hmiMachine.getPm3200().apparentPowerUnitMeasure;
     	    
     	    this.monilineGateway.getEnergyAverage().frequencyUnitMeasure = hmiMachine.getPm3200().frequencyUnitMeasure;
     	   
     	    this.monilineGateway.getEnergyAverage().totalActivePowerUnitMeasure = hmiMachine.getPm3200().totalActivePowerUnitMeasure;
     	    
     	    this.monilineGateway.getEnergyAverage().I_Avg = 0;
     	    
     	    this.monilineGateway.getEnergyAverage().LL_Avg = 0;
     	   
     	    this.monilineGateway.getEnergyAverage().LN_Avg = 0;
     	  
     	    this.monilineGateway.getEnergyAverage().active_power_T = 0;
     	 
     	    this.monilineGateway.getEnergyAverage().reactive_power_T = 0;
     	 
     	    this.monilineGateway.getEnergyAverage().apparent_power_T = 0;
     	
     	    this.monilineGateway.getEnergyAverage().pf_T = 0;
     	
     	    this.monilineGateway.getEnergyAverage().Frequency = 0;
     	
     	    this.monilineGateway.getEnergyAverage().Active_power_imp_total = 0;
     	    
     	}
		
	}

	@Override
	public void run() {
		
		System.out.println("MonilineGatewayReadThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
	}
	

	
	// Leggo i dati dai diversi server
	private class RequestTimerTask extends TimerTask{
		
		MonilineGatewayReadThread monilineGatewayReadThread;
		
		public RequestTimerTask(MonilineGatewayReadThread hmi1ReadThread) {
			
			this.monilineGatewayReadThread = hmi1ReadThread;
		}

		@Override
		public void run() {
			
			initValues();
			
			System.out.println("Devices data read");
			
			this.monilineGatewayReadThread.monilineGateway.setPlcAverageList(new ArrayList<PlcAverage>());
			
			// Read 
			Iterator <Integer> it = this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().keySet().iterator();
	        while(it.hasNext()) {
	        	
	        	int key = it.next();
	        
	        	HMIMachine hmiMachine = this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().get(key);
	        	
	        	hmiMachine.readPLC();
	        	hmiMachine.readEnergy();
	        	hmiMachine.readReject();
	        	hmiMachine.readDrives();
	        	hmiMachine.readVibrations();
	        	
	        }
	        
	        // Display values
	     	Iterator <Integer> itMeasure = this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().keySet().iterator();
	     	while(itMeasure.hasNext()) {
	     	        	
	     	    int key = itMeasure.next();    
	     	    
	     	    HMIMachine hmiMachine = this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().get(key);
	     	    
	     	    // PLC state 
	     	    PlcAverage plcAverage = new PlcAverage();
	     	    plcAverage.state = hmiMachine.getPlc().state;
	     	    plcAverage.machineID = key;
	     	    plcAverage.lineID = this.monilineGatewayReadThread.monilineGateway.getLineNumber();
	     	    this.monilineGatewayReadThread.monilineGateway.getPlcAverageList().add(plcAverage);
	     	    
	     	    
		        // Energy average increment values
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().I_Avg = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().I_Avg + hmiMachine.getPm3200().I_Avg;
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LL_Avg = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LL_Avg + hmiMachine.getPm3200().LL_Avg;
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LN_Avg = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LN_Avg + hmiMachine.getPm3200().LN_Avg;
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().active_power_T = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().active_power_T + hmiMachine.getPm3200().active_power_T;
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().reactive_power_T = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().reactive_power_T + hmiMachine.getPm3200().reactive_power_T;
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().apparent_power_T = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().apparent_power_T + hmiMachine.getPm3200().apparent_power_T;
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().pf_T = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().pf_T + hmiMachine.getPm3200().pf_T;
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Frequency = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Frequency + hmiMachine.getPm3200().Frequency;
		        this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Active_power_imp_total = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Active_power_imp_total + hmiMachine.getPm3200().Active_power_imp_total;
		        
		        
		        // Line velocity average values
		        this.monilineGatewayReadThread.monilineGateway.getLineVelocityAverage().lineVelocityAverage = this.monilineGatewayReadThread.monilineGateway.getLineVelocityAverage().lineVelocityAverage + hmiMachine.getWeightSystem().lineVelocity;  
	
	     	}
	     	
	     	System.out.println("Map size: " + this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
	     	
	     	System.out.println("---------- ENERGY AVERAGE ----------");
			
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().I_Avg = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().I_Avg / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LL_Avg = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LL_Avg / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());  
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LN_Avg = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LN_Avg / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().active_power_T = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().active_power_T / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().reactive_power_T = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().reactive_power_T / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().apparent_power_T = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().apparent_power_T / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().pf_T = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().pf_T / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Frequency = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Frequency / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
			this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Active_power_imp_total = this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Active_power_imp_total / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
		
			System.out.println("I_Avg: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().I_Avg + " " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().currentUnitMeasure);
			System.out.println("LL_Avg: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LL_Avg + " " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().voltageUnitMeasure);
			System.out.println("LN_Avg: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().LN_Avg + " " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().voltageUnitMeasure);
			System.out.println("ActivePower_T: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().active_power_T + " " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().activePowerUnitMeasure);
			System.out.println("ReactivePower_T: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().reactive_power_T + " " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().reactivePowerUnitMeasure);
			System.out.println("ApparentPower_T: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().apparent_power_T + " " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().apparentPowerUnitMeasure);
			System.out.println("PF_T: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().pf_T);
			System.out.println("Frequency: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Frequency + " " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().frequencyUnitMeasure);
			System.out.println("Active_power_Import_Total: " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().Active_power_imp_total + " " + this.monilineGatewayReadThread.monilineGateway.getEnergyAverage().activePowerUnitMeasure);
	       
			
			System.out.println("---------- PLC MACHINE STATE ----------");
		
			Iterator<PlcAverage> plcAverageIter = this.monilineGatewayReadThread.monilineGateway.getPlcAverageList().iterator();
			
			int indexPlcAverage = 1;
			
			while(plcAverageIter.hasNext()) {
				
				PlcAverage plcAverage = plcAverageIter.next();
				
				System.out.println("Machine " + indexPlcAverage + " :" + plcAverage.state);
				
				indexPlcAverage++;
				
			}
			
			
            System.out.println("---------- LINE VELOCITY AVERAGE ----------");
			
			this.monilineGatewayReadThread.monilineGateway.getLineVelocityAverage().lineVelocityAverage = this.monilineGatewayReadThread.monilineGateway.getLineVelocityAverage().lineVelocityAverage / (this.monilineGatewayReadThread.monilineGateway.getHmiMachineMap().size());
			
			System.out.println("Line velocity average: " + this.monilineGatewayReadThread.monilineGateway.getLineVelocityAverage().lineVelocityAverage + "unit/min");
			
		}

	}
	
	
	

}
