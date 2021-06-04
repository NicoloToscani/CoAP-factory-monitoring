package it.beltek.ia.iotlab.edge.client;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;

import org.eclipse.californium.core.coap.CoAP.Code;


public class Hmi1ReadThread implements Runnable {
	
	HmiMaintenance hmi1Maintenance;
	
	Timer requestTimer;

	
	public Hmi1ReadThread(HmiMaintenance hmi1Maintenance) {
		
		this.hmi1Maintenance = hmi1Maintenance;
		
		this.requestTimer = new Timer();
		
	}

	@Override
	public void run() {
		
		System.out.println("Hmi1ReadThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
	}
	
	// Leggo i dati dai diversi server
	private class RequestTimerTask extends TimerTask{
		
		Hmi1ReadThread hmi1ReadThread;
		
		public RequestTimerTask(Hmi1ReadThread hmi1ReadThread) {
			
			this.hmi1ReadThread = hmi1ReadThread;
		}

		@Override
		public void run() {
			
			// Lettura e visualizzazione dati su HMI della macchina
			// GET PLC
			Request request1 = new Request(Code.GET);
			
			CoapResponse coapResponseGetPlc = this.hmi1ReadThread.hmi1Maintenance.getCoapClientPlc().advanced(request1);
			
			Gson gsonPlc = new Gson();
			
			this.hmi1ReadThread.hmi1Maintenance.setPlc(gsonPlc.fromJson(coapResponseGetPlc.getResponseText(), PLC.class));
			
			System.out.println("Machine state: " + this.hmi1ReadThread.hmi1Maintenance.getPlc().state);
			
			// GET Pm3200
			Request request2 = new Request(Code.GET);
						
			CoapResponse coapResponseGetEnergy = this.hmi1ReadThread.hmi1Maintenance.getCoapClientEnergy().advanced(request2);
						
			Gson gsonEnergy = new Gson();
						
			this.hmi1ReadThread.hmi1Maintenance.setSchneiderPM3200(gsonEnergy.fromJson(coapResponseGetEnergy.getResponseText(), SchneiderPM3200.class));
			
			System.out.println("---------- ENERGY ----------");
			System.out.println("I1: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().I1 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().currentUnitMeasure);
			System.out.println("I2: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().I2 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().currentUnitMeasure);
			System.out.println("I3: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().I3 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().currentUnitMeasure);			
			System.out.println("I_Avg: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().I_Avg + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().currentUnitMeasure);
			System.out.println("L1_L2: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().L1_L2 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("L2_L3: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().L2_L3 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("L3_L1: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().L3_L1 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("LL_Avg: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().LL_Avg + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("L1_N: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().L1_N + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("L1_N: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().L1_N + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("L2_N: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().L2_N + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("L3_N: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().L3_N + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("LN_Avg: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().LN_Avg + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().voltageUnitMeasure);
			System.out.println("ActivePower_P1: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().active_power_P1 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().activePowerUnitMeasure);
			System.out.println("ActivePower_P2: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().active_power_P2 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().activePowerUnitMeasure);
			System.out.println("ActivePower_P3: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().active_power_P3 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().activePowerUnitMeasure);
			System.out.println("ActivePower_T: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().active_power_T + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().activePowerUnitMeasure);
			System.out.println("ReactivePower_P1: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().reactive_power_P1 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().reactivePowerUnitMeasure);
			System.out.println("ReactivePower_P2: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().reactive_power_P2 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().reactivePowerUnitMeasure);
			System.out.println("ReactivePower_P3: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().reactive_power_P3 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().reactivePowerUnitMeasure);
			System.out.println("ReactivePower_T: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().reactive_power_T + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().reactivePowerUnitMeasure);
			System.out.println("ApparentPower_P1: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().apparent_power_P1 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().apparentPowerUnitMeasure);
			System.out.println("ApparentPower_P2: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().apparent_power_P2 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().apparentPowerUnitMeasure);
			System.out.println("ApparentPower_P3: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().apparent_power_P3 + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().apparentPowerUnitMeasure);
			System.out.println("ApparentPower_T: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().apparent_power_T + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().apparentPowerUnitMeasure);
			System.out.println("PF_1: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().pf_P1);
			System.out.println("PF_2: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().pf_P2);
			System.out.println("PF_3: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().pf_P3);
			System.out.println("PF_T: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().pf_T);
			System.out.println("Frequency: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().Frequency + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().frequencyUnitMeasure);
			System.out.println("Temperature: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().Temperature + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().teperatureUnitMeasure);
			System.out.println("Active_power_Import_Total: " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().Active_power_imp_total + " " + this.hmi1ReadThread.hmi1Maintenance.getSchneiderPM3200().totalActivePowerUnitMeasure);
			
			
			System.out.println("---------- VIBRATION ANALYSIS ----------");
			
			System.out.println("---------- DRIVE  ----------");
			
			/*
			// GET PLC
            Request request3 = new Request(Code.GET);
			
			CoapResponse coapResponseGetDrive = this.hmi1ReadThread.hmi1Maintenance.getCoapClientDrive().advanced(request3);
			
			Gson gsonDrive = new Gson();
			
			this.hmi1ReadThread.hmi1Maintenance.setDrive(gsonDrive.fromJson(coapResponseGetDrive.getResponseText(), Drive.class));
			System.out.println("Actual frequency: " + this.hmi1ReadThread.hmi1Maintenance.getDrive().actualSpeed + " Hz");
						
				*/					
			
			
			List<Alarm> alarmList = this.hmi1ReadThread.hmi1Maintenance.getPlc().alarmList; 
			
			Iterator<Alarm> iterator = alarmList.iterator();
			
			System.out.println("---------- ALARMS LIST ----------");
			while(iterator.hasNext()) {
				
				Alarm alarm = iterator.next();
				
				System.out.println("Valore: " + alarm.getValue() + " ID: " + alarm.getId() + " Desc: " + alarm.getDescription() + " Timestamp: " + alarm.getTimestamp());
				
			}
			
		}
		
	}
	
	
	private class ObsTimerTask extends TimerTask{
		
		Hmi1ReadThread rejectFieldbusThread;
		
		public ObsTimerTask(Hmi1ReadThread rejectFieldbusThread) {
			
			this.rejectFieldbusThread = rejectFieldbusThread;
		}

		@Override
		public void run() {
			
			
		}
		
	}

}
