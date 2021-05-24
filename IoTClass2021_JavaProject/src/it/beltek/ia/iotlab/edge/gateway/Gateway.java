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
			
			plc.Read();
			plc.Write();
			
			System.out.println("Comando Reset: " + plc.getSiemensPLC().reset);
			
			System.out.println("Stato macchina: " + plc.getSiemensPLC().state);
			System.out.println("Presenza allarmi: " + plc.getSiemensPLC().alarmPresence);
			
			System.out.println("Allarme 0: " + plc.getSiemensPLC().alarms[0]);
			System.out.println("Allarme 1: " + plc.getSiemensPLC().alarms[1]);
			System.out.println("Allarme 2: " + plc.getSiemensPLC().alarms[2]);
			System.out.println("Allarme 3: " + plc.getSiemensPLC().alarms[3]);
			System.out.println("Allarme 4: " + plc.getSiemensPLC().alarms[4]);
			System.out.println("Allarme 5: " + plc.getSiemensPLC().alarms[5]);
			System.out.println("Allarme 6: " + plc.getSiemensPLC().alarms[6]);
			System.out.println("Allarme 7: " + plc.getSiemensPLC().alarms[7]);
			System.out.println("Allarme 8: " + plc.getSiemensPLC().alarms[8]);
			System.out.println("Allarme 9: " + plc.getSiemensPLC().alarms[9]);
			
			
			try {
				
				Thread.sleep(5000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

	}

}
