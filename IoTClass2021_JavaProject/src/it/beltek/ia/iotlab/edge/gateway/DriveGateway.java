package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.beltek.ia.iotlab.edge.gateway.service.G120cPnService;
import it.beltek.ia.iotlab.edge.server.resource.DriveResource;

public class DriveGateway {
	
	 // S7 protocol server data
	 private String IPAddress;
     private int Rack;
     private int Slot;
     
     private String deviceName;
     
     private int driveId;
     
     private int coapServerPort;
	
     private G120cPnService g120cPnService;
     
     private ThreadPoolExecutor pool;
     
     private DriveResource driveResource;
     
     private static final int COREPOOL = 2;
     private static final int MAXPOOL = 2;
     private static final int IDLETIME = 5000;
     private static final int SLEEPTIME = 1000;
     
	 
	/**
	 * Class constructor. 
	**/
	public DriveGateway() {
		
		this.IPAddress = "192.168.100.1";
		
		this.Rack = 0;
		
		this.Slot = 0;
		
		this.driveId = 10; // E.g. drive 1 = 10, drive 2 = 20, drive 3 = 30
		
		this.deviceName = "drive1";
		
		this.coapServerPort = 5684;
		
		this.g120cPnService = new G120cPnService(IPAddress, Rack, Slot, driveId);
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		this.driveResource = new DriveResource(deviceName, this);
		
	}

	/**
	 * Run Pm3200Gateway code
	**/
	private void run() {
		
		System.out.println("DriveGateway start at " + new Date());
		
		this.pool.execute(new DriveFieldbusThread(this, this.deviceName));
		this.pool.execute(new DriveCoAPServerThread(this, driveResource, this.coapServerPort));
		
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
		
		new DriveGateway().run();
		
	}
	
	public G120cPnService getG120cPnService() {
		
		return g120cPnService;
	}

}
