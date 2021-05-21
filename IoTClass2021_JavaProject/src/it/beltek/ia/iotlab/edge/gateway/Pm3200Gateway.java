package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import it.beltek.ia.iotlab.edge.server.resource.Pm3200Resource;
import it.beltekia.iotlab.edge.gateway.service.Pm3200ModbusService;

public class Pm3200Gateway {
	
	// Modbus server data
	 private String IPAddress;
     private int Port;
     
     private String deviceName;
	
     private Pm3200ModbusService pm3200ModbusService;
     
     private ThreadPoolExecutor pool;
     
     private Pm3200Resource pm3200Resource;
     
     private static final int COREPOOL = 2;
     private static final int MAXPOOL = 2;
     private static final int IDLETIME = 5000;
     private static final int SLEEPTIME = 1000;
     
	 
	/**
	 * Class constructor. 
	**/
	public Pm3200Gateway() {
		
		this.IPAddress = "192.168.100.3";
		
		this.Port = 502;
		
		this.deviceName = "powerEnergyMeter1";
		
		this.pm3200ModbusService = new Pm3200ModbusService(IPAddress, Port);
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		this.pm3200Resource = new Pm3200Resource(this.deviceName, this);
		
	}

	
	/**
	 * Run Pm3200Gateway code
	**/
	private void run() {
		
		System.out.println("Pm3200Gateway start at " + new Date());
		
		this.pool.execute(new Pm3200FieldbusThread(this, this.deviceName));
		this.pool.execute(new Pm3200CoAPServerThread(this, pm3200Resource));
		
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
		
		new Pm3200Gateway().run();
		
		
	}
	
	public Pm3200ModbusService getPm3200ModbusService() {
		
		return pm3200ModbusService;
	}

}
