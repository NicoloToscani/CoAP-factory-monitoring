package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.beltek.ia.iotlab.edge.server.resource.Pm3200Resource;
import it.beltek.ia.iotlab.edge.server.resource.Qm42vt2Resource;
import it.beltekia.iotlab.edge.gateway.service.Pm3200ModbusService;
import it.beltekia.iotlab.edge.gateway.service.Qm42vt2ModbusService;

public class Qm42vt2Gateway {
	
	     // Modbus TCP-IP server data
		 private String IPAddress;
	     private int Port;
	     
	     private String deviceName;
		
	     private Qm42vt2ModbusService qm42vt2ModbusService;
	     
	     private ThreadPoolExecutor pool;
	     
	     private Qm42vt2Resource qm42vt2Resource;
	     
	     private static final int COREPOOL = 2;
	     private static final int MAXPOOL = 2;
	     private static final int IDLETIME = 5000;
	     private static final int SLEEPTIME = 1000;
	     
	     /**
	 	 * Class constructor. 
	 	**/
	 	public Qm42vt2Gateway() {
	 		
	 		this.IPAddress = "192.168.100.4";
	 		
	 		this.Port = 502;
	 		
	 		this.deviceName = "vibrationSensor1";
	 		
	 		this.qm42vt2ModbusService = new Qm42vt2ModbusService(IPAddress, Port);
	 		
	 		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	 		
	 		this.qm42vt2Resource = new Qm42vt2Resource(this.deviceName, this);
	 		
	 	}

	 	/**
	 	 * Run Pm3200Gateway code
	 	**/
	 	private void run() {
	 		
	 		System.out.println("Qm42vt2Gateway start at " + new Date());
	 		
	 		this.pool.execute(new Qm42vt2FieldbusThread(this, this.deviceName));
	 		this.pool.execute(new Qm42vt2CoAPServerThread(this, qm42vt2Resource));
	 		
	 		while(this.pool.getActiveCount() > 0) {
	 			
	 			try {
	 				
	 				// Stampo i dati di energia
	 				
	 				System.out.println("Main");
	 				
	 				Thread.sleep(SLEEPTIME);
	 				
	 				
	 			} catch (InterruptedException e) {
	 				
	 				e.printStackTrace();
	 			
	 			}
	 			
	 		}
	 		
	 	}
	 	
	 	
	 	
	 	public static void main(String[] args) {
	 		
	 		new Qm42vt2Gateway().run();
	 		
	 		
	 	}
	 	
	 	public Qm42vt2ModbusService getQm42vt2ModbusService() {
	 		
	 		return qm42vt2ModbusService;
	 	}

}
