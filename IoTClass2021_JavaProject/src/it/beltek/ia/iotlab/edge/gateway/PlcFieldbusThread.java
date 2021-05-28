package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class PlcFieldbusThread implements Runnable {
	
	PlcGateway plcGateway;
	
	Timer requestTimer;
	
	public PlcFieldbusThread(PlcGateway plcGateway, String resourceName) {
		
		this.plcGateway = plcGateway;
		
		this.requestTimer = new Timer();
	}

	@Override
	public void run() {
		
		System.out.println("PlcFieldbusThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
		System.out.println("Stato connessione: " + this.plcGateway.getPlcS7Service().getConnectionState());
		
		if(this.plcGateway.getPlcS7Service().getConnectionState() == ConnectionState.Offline) {
		  
			this.plcGateway.getPlcS7Service().Connect();
		}
		
	}
	
	private class RequestTimerTask extends TimerTask{
		
		PlcFieldbusThread plcFieldbusThread;
		
		public RequestTimerTask(PlcFieldbusThread plcFieldbusThread) {
			
			this.plcFieldbusThread = plcFieldbusThread;
		}

		@Override
		public void run() {
			
			
			if(this.plcFieldbusThread.plcGateway.getPlcS7Service().getConnectionState() == ConnectionState.Online) {
			
				this.plcFieldbusThread.plcGateway.getPlcS7Service().Read();
				
				this.plcFieldbusThread.plcGateway.getPlcS7Service().Write();
				
			}
			
			else if(this.plcFieldbusThread.plcGateway.getPlcS7Service().getConnectionState() == ConnectionState.Offline) {
				
				this.plcFieldbusThread.plcGateway.getPlcS7Service().Connect();
				
			}
		}
		
	}

}
