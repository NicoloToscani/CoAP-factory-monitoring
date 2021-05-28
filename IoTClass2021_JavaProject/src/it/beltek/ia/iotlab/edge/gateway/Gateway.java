package it.beltek.ia.iotlab.edge.gateway;



import java.util.Date;

import it.beltek.ia.iotlab.edge.gateway.device.driver.s7.S7;
import it.beltek.ia.iotlab.edge.gateway.service.PlcS7Service;

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
			
			plc.Read();
			plc.Write();
			
			System.out.println("Comando Reset: " + plc.getSiemensPLC().reset);
			System.out.println("Stato macchina: " + plc.getSiemensPLC().state);
			System.out.println("Presenza allarmi: " + plc.getSiemensPLC().alarmPresence);
			
			
			
			
			try {
				
				Thread.sleep(5000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

	}

}
