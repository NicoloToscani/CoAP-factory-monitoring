package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class DriveFieldbusThread implements Runnable {
	
	DriveGateway driveGateway;
	
	Timer requestTimer;
	
	public DriveFieldbusThread(DriveGateway driveGateway, String resourceName) {
		
		this.driveGateway = driveGateway;
		
		this.requestTimer = new Timer();
	}

	@Override
	public void run() {
		
		System.out.println("DriveFieldbusThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
		System.out.println("Stato connessione: " + this.driveGateway.getG120cPnService().getConnectionState());
		
		if(this.driveGateway.getG120cPnService().getConnectionState() == ConnectionState.Offline) {
		  
			this.driveGateway.getG120cPnService().Connect();
		}
		
	}
	
	private class RequestTimerTask extends TimerTask{
		
		DriveFieldbusThread driveFieldbusThread;
		
		public RequestTimerTask(DriveFieldbusThread driveFieldbusThread) {
			
			this.driveFieldbusThread = driveFieldbusThread;
		}

		@Override
		public void run() {
			
			if(this.driveFieldbusThread.driveGateway.getG120cPnService().getConnectionState() == ConnectionState.Online) {
			
				this.driveFieldbusThread.driveGateway.getG120cPnService().Read();
				
				this.driveFieldbusThread.driveGateway.getG120cPnService().Write();
				
			}
			
			else if(this.driveFieldbusThread.driveGateway.getG120cPnService().getConnectionState() == ConnectionState.Offline) {
				
				this.driveFieldbusThread.driveGateway.getG120cPnService().Connect();
				
			}
		}
		
	}

}
