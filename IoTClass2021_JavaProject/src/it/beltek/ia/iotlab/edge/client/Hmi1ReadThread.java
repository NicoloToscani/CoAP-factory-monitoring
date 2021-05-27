package it.beltek.ia.iotlab.edge.client;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;

import org.eclipse.californium.core.coap.CoAP.Code;


public class Hmi1ReadThread implements Runnable {
	
	HmiMachine hmi1Maintenance;
	
	Timer requestTimer;
	
	int driveNUmber;
	
	public Hmi1ReadThread(HmiMachine hmi1Maintenance, int driveNumber) {
		
		this.hmi1Maintenance = hmi1Maintenance;
		
		this.requestTimer = new Timer();
		
		this.driveNUmber = driveNumber;
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
			
			Gson gson = new Gson();
			
			this.hmi1ReadThread.hmi1Maintenance.setPlc(gson.fromJson(coapResponseGetPlc.getResponseText(), PLC.class));
			
			List<Alarm> alarmList = this.hmi1ReadThread.hmi1Maintenance.getPlc().alarmList; 
			
			Iterator<Alarm> iterator = alarmList.iterator();
			
			System.out.println("---------- ALARMS LIST ----------");
			while(iterator.hasNext()) {
				
				Alarm alarm = iterator.next();
				
				System.out.println("Valore: " + alarm.getValue() + " ID: " + alarm.getId() + " Desc: " + alarm.getDescription());
				
			}
			
			
            // GET energy
			Request request2 = new Request(Code.GET);
			
			CoapResponse coapResponseGetEnergy = this.hmi1ReadThread.hmi1Maintenance.getCoapClientEnergy().advanced(request2);
						
			System.out.println("Energy: " + coapResponseGetEnergy.getResponseText());
			
			
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
