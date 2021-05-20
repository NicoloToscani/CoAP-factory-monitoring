package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;

public class Pm3200FieldbusThread implements Runnable{
	
	Pm3200Gateway pm3200Gateway;
	
	public Pm3200FieldbusThread(Pm3200Gateway pm3200Gateway) {
		
		this.pm3200Gateway = pm3200Gateway;
	}

	@Override
	public void run() {
		
		System.out.println("Pm3200FieldbusThread start at " + new Date());
		
		this.pm3200Gateway.getPm3200ModbusService().Connect();
		
		while(true) {
			
			this.pm3200Gateway.getPm3200ModbusService().Read();
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	
		
		
		
	}

}
