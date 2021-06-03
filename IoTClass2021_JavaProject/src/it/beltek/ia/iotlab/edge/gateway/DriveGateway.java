package it.beltek.ia.iotlab.edge.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.database.EntityHeader;
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
     
     private int lineID;
     
     private int machineID;
     
	
     private G120cPnService g120cPnService;
     
     private ThreadPoolExecutor pool;
     
     private DriveResource driveResource;
     
     private static final int COREPOOL = 2;
     private static final int MAXPOOL = 2;
     private static final int IDLETIME = 5000;
     private static final int SLEEPTIME = 1000;
     
     private CoapClient repositoryCoapClient;
     
     private String urlRepository = "coap://localhost:5600/master_repository";
     
	 
	/**
	 * Class constructor. 
	**/
	public DriveGateway(int coapServerPort, String deviceName, int lineID, int machineID, int driveID) {
		
		this.IPAddress = "192.168.100.1";
		
		this.Rack = 0;
		
		this.Slot = 0;
		
		this.driveId = driveID; // E.g. drive 1 = 10, drive 2 = 20, drive 3 = 30
		
		this.deviceName = deviceName;
		
		this.coapServerPort = coapServerPort;
		
		this.lineID = lineID;
		
		this.machineID = machineID;
		
		this.g120cPnService = new G120cPnService(IPAddress, Rack, Slot, driveId);
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		this.driveResource = new DriveResource(deviceName + "_" + lineID + "_" + machineID + "_" + driveID, this);
		
		this.repositoryCoapClient = new CoapClient(urlRepository);
		
	}

	/**
	 * Run DriveGateway code
	**/
	private void run() {
		
		System.out.println("DriveGateway start at " + new Date());
		
		// Master repository registration
		registerEntity();
		
		this.pool.execute(new DriveFieldbusThread(this, this.deviceName));
		this.pool.execute(new DriveCoAPServerThread(this, driveResource, this.coapServerPort));
		
		while(this.pool.getActiveCount() > 0) {
			
			try {
				
				Thread.sleep(SLEEPTIME);
				
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			
			}
			
		}
		
	}
	
	
	public static void main(String[] args) {
		
		BufferedReader bufferedReaderPort = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderName = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderLine = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderMachine = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderID = new BufferedReader(new InputStreamReader(System.in));
		
		int coapPort = 0;
		String deviceName = null;
		int lineID = 0;
		int machineID = 0;
		int driveID = 0;
	
		try {
			
			System.out.print("Insert DRIVE server CoAP port: ");
			coapPort = Integer.parseInt(bufferedReaderPort.readLine());
			
			System.out.print("Insert DRIVE server CoAP name: ");
			deviceName = bufferedReaderName.readLine();
			
			System.out.print("Insert DRIVE line ID: ");
			lineID = Integer.parseInt(bufferedReaderLine.readLine());
			
			System.out.print("Insert DRIVE machine ID: ");
			machineID = Integer.parseInt(bufferedReaderMachine.readLine());
			
			System.out.print("Insert DRIVE ID: ");
			driveID = Integer.parseInt(bufferedReaderID.readLine()) * 10;
			
			System.out.println("CoAP port: " + coapPort);
			System.out.println("CoAP name: " + deviceName);
			System.out.println("Line ID: " + lineID);
			System.out.println("Machine ID: " + machineID);
			System.out.println("Drive ID: " + driveID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new DriveGateway(coapPort, deviceName, lineID, machineID, driveID).run();
		
	}
	
	public G120cPnService getG120cPnService() {
		
		return g120cPnService;
	}
	
	// Master repository registration
		public void registerEntity() {
					
		    // POST
			EntityHeader entityHeader = new EntityHeader(this.deviceName, this.lineID, this.machineID, 0);
					
			Gson gsonPlcEntity = new Gson();
			String driveSerializeEntity = gsonPlcEntity.toJson(entityHeader);
			CoapResponse coapResponsePlcEntity = this.repositoryCoapClient.post(driveSerializeEntity, MediaTypeRegistry.APPLICATION_JSON);
					
		}

}
