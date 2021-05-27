package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.beltek.ia.iotlab.edge.gateway.service.PlcS7Service;
import it.beltek.ia.iotlab.edge.gateway.service.Pm3200ModbusService;
import it.beltek.ia.iotlab.edge.server.resource.PlcResource;
import it.beltek.ia.iotlab.edge.server.resource.Pm3200Resource;

public class PlcGateway {
	
	// S7 protocol server data
	 private String IPAddress;
     private int Rack;
     private int Slot;
     
     private int coapServerPort;
     
     private String deviceName;
	
     private PlcS7Service plcS7Service;
     
     private ThreadPoolExecutor pool;
     
     private PlcResource plcResource;
     
     private static final int COREPOOL = 2;
     private static final int MAXPOOL = 2;
     private static final int IDLETIME = 5000;
     private static final int SLEEPTIME = 1000;
     
	 
	/**
	 * Class constructor. 
	**/
	public PlcGateway() {
		
		this.IPAddress = "192.168.100.1";
		
		this.Rack = 0;
		
		this.Slot = 0;
		
		this.deviceName = "plc";
		
		this.coapServerPort = 5683;
		
		this.plcS7Service = new PlcS7Service(IPAddress, Rack, Slot);
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		this.plcResource = new PlcResource(deviceName, this);
		
	}

	/**
	 * Run Pm3200Gateway code
	**/
	private void run() {
		
		System.out.println("PlcGateway start at " + new Date());
		
		this.pool.execute(new PlcFieldbusThread(this, this.deviceName));
		this.pool.execute(new PlcCoAPServerThread(this, plcResource, this.coapServerPort ));
		
		while(this.pool.getActiveCount() > 0) {
			
			try {
				
				System.out.println("Main");
				
				Thread.sleep(SLEEPTIME);
				
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			
			}
			
		}
		
	}
	
	
	public static void main(String[] args) {
		
		new PlcGateway().run();
		
	}
	
	public PlcS7Service getPlcS7Service() {
		
		return plcS7Service;
	}

}
