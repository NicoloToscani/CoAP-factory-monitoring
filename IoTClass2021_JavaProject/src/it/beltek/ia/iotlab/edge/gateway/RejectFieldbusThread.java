package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class RejectFieldbusThread implements Runnable {
	
	RejectGateway rejectGateway;
	
	Timer requestTimer;
	
	Timer obsTimer;
	
	int velocityStart;
	
	public RejectFieldbusThread(RejectGateway rejectGateway, String resourceName) {
		
		this.rejectGateway = rejectGateway;
		
		this.requestTimer = new Timer();
		
		this.obsTimer = new Timer();
	}

	@Override
	public void run() {
		
		System.out.println("RejectFieldbusThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
		obsTimer.schedule(new ObsTimerTask(this), 0, 60000);  // 1 min
		
		System.out.println("Stato connessione: " + this.rejectGateway.getRejectModbusService().getWeightSystem());
		
		if(this.rejectGateway.getRejectModbusService().getConnectionState() == ConnectionState.Offline) {
		  
			this.rejectGateway.getRejectModbusService().Connect();
		}
		
	}
	
	private class RequestTimerTask extends TimerTask{
		
		RejectFieldbusThread rejectFieldbusThread;
		
		public RequestTimerTask(RejectFieldbusThread rejectFieldbusThread) {
			
			this.rejectFieldbusThread = rejectFieldbusThread;
		}

		@Override
		public void run() {
			
			if(this.rejectFieldbusThread.rejectGateway.getRejectModbusService().getConnectionState() == ConnectionState.Online) {
			
				 this.rejectFieldbusThread.rejectGateway.getRejectModbusService().Read();
				 this.rejectFieldbusThread.rejectGateway.getRejectModbusService().Write();
				 
			}
			
			else if(this.rejectFieldbusThread.rejectGateway.getRejectModbusService().getConnectionState() == ConnectionState.Offline) {
				
				this.rejectFieldbusThread.rejectGateway.getRejectModbusService().Connect();
				
			}
		}
		
	}
	
	
	private class ObsTimerTask extends TimerTask{
		
		RejectFieldbusThread rejectFieldbusThread;
		
		public ObsTimerTask(RejectFieldbusThread rejectFieldbusThread) {
			
			this.rejectFieldbusThread = rejectFieldbusThread;
		}

		@Override
		public void run() {
			
			// 1 minute schedule
			
			this.rejectFieldbusThread.rejectGateway.getRejectModbusService().getWeightSystem().lineVelocity = this.rejectFieldbusThread.rejectGateway.getRejectModbusService().getWeightSystem().totalCount - this.rejectFieldbusThread.velocityStart;
			
			System.out.println("Line velocity: " + this.rejectFieldbusThread.rejectGateway.getRejectModbusService().getWeightSystemSimulate().lineVelocity + " unit/s");
			
			this.rejectFieldbusThread.velocityStart = this.rejectFieldbusThread.rejectGateway.getRejectModbusService().getWeightSystemSimulate().totalCount;
			
			// Resourse update
			this.rejectFieldbusThread.rejectGateway.getObsResource().setLineVelocity(this.rejectFieldbusThread.velocityStart);
			this.rejectFieldbusThread.rejectGateway.getObsResource().changed();
			
			
		}
		
	}

}
