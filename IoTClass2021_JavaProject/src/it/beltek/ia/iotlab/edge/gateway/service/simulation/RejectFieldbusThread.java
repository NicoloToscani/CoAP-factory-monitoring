package it.beltek.ia.iotlab.edge.gateway.service.simulation;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import it.beltek.ia.iotlab.edge.gateway.service.RejectModbusService;


public class RejectFieldbusThread implements Runnable {
	
	private Timer requestTimer;
	
	private RejectModbusService rejectModbusService;
	
	private Random unitWeightRandom;
	
	
	
	public RejectFieldbusThread(RejectModbusService rejectModbusService) {
		
		this.rejectModbusService = rejectModbusService;
		
		this.requestTimer = new Timer();
		
		this.unitWeightRandom = new Random();
	}

	@Override
	public void run() {
		
		System.out.println("Reject simulation start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 10000);  // 5 s
		
	}
	
	private class RequestTimerTask extends TimerTask{
		
		RejectFieldbusThread rejectFieldbusThread;
		
		public RequestTimerTask(RejectFieldbusThread rejectFieldbusThread) {
			
			this.rejectFieldbusThread = rejectFieldbusThread;
		}

		@Override
		public void run() {
			
			System.out.println("New unit on conveyor");
			
			float unitWeight = this.rejectFieldbusThread.unitWeightRandom.nextFloat() * 100.0f;
			
			System.out.println("Weight: " + unitWeight);
			
			if(checkUnitWeight(unitWeight, this.rejectFieldbusThread.rejectModbusService.getWeightSystemSimulate().setpoint) == true) {
				
				// Total units
				this.rejectFieldbusThread.rejectModbusService.getWeightSystemSimulate().totalCount ++;
				
				// Last unit weight
				this.rejectFieldbusThread.rejectModbusService.getWeightSystemSimulate().weight = unitWeight;
			}
			
		}
		
	}
	
	private boolean checkUnitWeight(float unitWeight, float thr) {
		
		if(unitWeight <= 85) {
			
			return true;
		}
		
		else {
			
			return false;
		}
		
	}

}
