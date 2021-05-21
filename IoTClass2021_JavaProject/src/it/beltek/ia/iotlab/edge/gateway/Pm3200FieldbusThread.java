package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Pm3200FieldbusThread implements Runnable {
	
	Pm3200Gateway pm3200Gateway;
	
	Timer requestTimer;
	
	public Pm3200FieldbusThread(Pm3200Gateway pm3200Gateway, String resourceName) {
		
		this.pm3200Gateway = pm3200Gateway;
		
		this.requestTimer = new Timer();
	}

	@Override
	public void run() {
		
		System.out.println("Pm3200FieldbusThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
				
		System.out.println("Stato connessione: " + this.pm3200Gateway.getPm3200ModbusService().getConnectionState());
		
		if(this.pm3200Gateway.getPm3200ModbusService().getConnectionState() == ConnectionState.Offline) {
		  
			this.pm3200Gateway.getPm3200ModbusService().Connect();
		}
		
	}
	
	private class RequestTimerTask extends TimerTask{
		
		Pm3200FieldbusThread pm3200FieldbusThread;
		
		public RequestTimerTask(Pm3200FieldbusThread pm3200FieldbusThread) {
			
			this.pm3200FieldbusThread = pm3200FieldbusThread;
		}

		@Override
		public void run() {
			
			if(this.pm3200FieldbusThread.pm3200Gateway.getPm3200ModbusService().getConnectionState() == ConnectionState.Online) {
			
				this.pm3200FieldbusThread.pm3200Gateway.getPm3200ModbusService().Read();
			}
			
			else if(this.pm3200FieldbusThread.pm3200Gateway.getPm3200ModbusService().getConnectionState() == ConnectionState.Offline) {
				
				this.pm3200FieldbusThread.pm3200Gateway.getPm3200ModbusService().Connect();
				
			}
		}
		
	}

}
