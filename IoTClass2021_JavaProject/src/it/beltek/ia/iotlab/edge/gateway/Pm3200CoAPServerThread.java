package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;

public class Pm3200CoAPServerThread implements Runnable{
	
	Pm3200Gateway pm3200Gateway;
	
	public Pm3200CoAPServerThread(Pm3200Gateway pm3200Gateway) {
		
		this.pm3200Gateway = pm3200Gateway;
	}
	
	

	@Override
	public void run() {
		
		System.out.println("pm3200CoAPServerThread start at " + new Date());
		
		while(true) {
			
			System.out.println("Misura coap: " + this.pm3200Gateway.getPm3200ModbusService().getSchneiderPM3200().L1_L2);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
