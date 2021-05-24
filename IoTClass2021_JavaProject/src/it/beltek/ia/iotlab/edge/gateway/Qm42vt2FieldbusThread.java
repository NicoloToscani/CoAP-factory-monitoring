package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Qm42vt2FieldbusThread implements Runnable {
	
	Qm42vt2Gateway qm42vt2Gateway;
	
	Timer requestTimer;
	
	public Qm42vt2FieldbusThread(Qm42vt2Gateway qm42vt2Gateway, String resourceName) {
		
		this.qm42vt2Gateway = qm42vt2Gateway;
		
		this.requestTimer = new Timer();
	}

	@Override
	public void run() {
		
		System.out.println("Pm3200FieldbusThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
		System.out.println("Stato connessione: " + this.qm42vt2Gateway.getQm42vt2ModbusService().getConnectionState());
		
		if(this.qm42vt2Gateway.getQm42vt2ModbusService().getConnectionState() == ConnectionState.Offline) {
		  
			this.qm42vt2Gateway.getQm42vt2ModbusService().Connect();
		}
		
	}
	
	private class RequestTimerTask extends TimerTask{
		
		Qm42vt2FieldbusThread qm42vt2FieldbusThread;
		
		public RequestTimerTask(Qm42vt2FieldbusThread qm42vt2FieldbusThread) {
			
			this.qm42vt2FieldbusThread = qm42vt2FieldbusThread;
		}

		@Override
		public void run() {
			
			if(this.qm42vt2FieldbusThread.qm42vt2Gateway.getQm42vt2ModbusService().getConnectionState() == ConnectionState.Online) {
			
				 this.qm42vt2FieldbusThread.qm42vt2Gateway.getQm42vt2ModbusService().Read();
				
				
				
			}
			
			else if(this.qm42vt2FieldbusThread.qm42vt2Gateway.getQm42vt2ModbusService().getConnectionState() == ConnectionState.Offline) {
				
				 this.qm42vt2FieldbusThread.qm42vt2Gateway.getQm42vt2ModbusService().Connect();
				
			}
		}
		
	}

}
