package it.beltek.ia.iotlab.edge.gateway;



import java.util.Date;

import it.beltek.ia.iotlab.edge.gateway.device.driver.s7.S7;
import it.beltekia.iotlab.edge.gateway.service.PlcS7Service;

public class Gateway {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Start S7Service at " + new Date());
		
		
        String IPAddress = "192.168.100.1";
		
		int Rack = 0;
		int Slot = 0;
		
		PlcS7Service plc = new PlcS7Service(IPAddress, Rack, Slot);
		
		plc.Connect();
		
		while(true) {
			
			plc.readData();
			
			System.out.println("Stato macchina: " + plc.getSiemensPLC().state);
			
			//Boolean statoLuce1 = S7.GetBitAt(buffer, 1, 0);
			
			//Boolean statoLuce2 = S7.GetBitAt(buffer, 1, 1);
			
			
			
			//System.out.println("Stato luce 2: " + statoLuce2);
			
			try {
				
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

	}

}
