package it.beltek.ia.iotlab.edge.gateway;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.database.EntityHeader;
import it.beltek.ia.iotlab.edge.gateway.service.PlcS7Service;
import it.beltek.ia.iotlab.edge.server.resource.PlcResource;


public class PlcGateway {
	
	// S7 protocol server data
	 private String IPAddress;
     private int Rack;
     private int Slot;
     
     private int coapServerPort;
     
     private String deviceName;
     
     int lineID;
     int machineID;
	
     private PlcS7Service plcS7Service;
     
     private ThreadPoolExecutor pool;
     
     private PlcResource plcResource;
     
     private static final int COREPOOL = 2;
     private static final int MAXPOOL = 2;
     private static final int IDLETIME = 5000;
     private static final int SLEEPTIME = 1000;
     
     private CoapClient repositoryCoapClient;
     
     private String urlRepository = "coap://localhost:5600/master_repository";
	 
	/**
	 * Class constructor. 
	**/
	public PlcGateway(int coapServerPort, String deviceName, int lineID, int machineID) {
		
		this.IPAddress = "192.168.100.1";
		
		this.Rack = 0;
		
		this.Slot = 0;
		
		this.deviceName = deviceName;
		
		this.coapServerPort = coapServerPort;
		
		this.lineID = lineID;
		
		this.machineID = machineID;
		
		this.plcS7Service = new PlcS7Service(IPAddress, Rack, Slot);
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		this.plcResource = new PlcResource(deviceName + "_" + this.lineID + "_" + this.machineID, this);
		
		this.repositoryCoapClient = new CoapClient(urlRepository);
		
	}

	/**
	 * Run Pm3200Gateway code
	**/
	private void run() {
		
		System.out.println("PlcGateway start at " + new Date());
		
		// Master repository registration
		registerEntity();
		
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
		
		BufferedReader bufferedReaderPort = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderName = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderLine = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderMachine = new BufferedReader(new InputStreamReader(System.in));
		
		int coapPort = 0;
		String deviceName = null;
		int lineID = 0;
		int machineID = 0;
	
		try {
			
			System.out.print("Insert PLC server CoAP port: ");
			coapPort = Integer.parseInt(bufferedReaderPort.readLine());
			
			System.out.print("Insert PLC server CoAP name: ");
			deviceName = bufferedReaderName.readLine();
			
			System.out.print("Insert PLC line ID: ");
			lineID = Integer.parseInt(bufferedReaderLine.readLine());
			
			System.out.print("Insert PLC machine ID: ");
			machineID = Integer.parseInt(bufferedReaderMachine.readLine());
			
			System.out.println("CoAP port: " + coapPort);
			System.out.println("CoAP name: " + deviceName);
			System.out.println("Line ID: " + lineID);
			System.out.println("Machine ID: " + machineID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		new PlcGateway(coapPort, deviceName, lineID, machineID).run();
		
	}
	
	public PlcS7Service getPlcS7Service() {
		
		return plcS7Service;
	}
	
	// Master repository registration
	public void registerEntity() {
		
		// POST
		EntityHeader entityHeader = new EntityHeader(this.deviceName, this.lineID, this.machineID, 0);
		
		Gson gsonPlcEntity = new Gson();
		String plcSerializeEntity = gsonPlcEntity.toJson(entityHeader);
		CoapResponse coapResponsePlcEntity = this.repositoryCoapClient.post(plcSerializeEntity, MediaTypeRegistry.APPLICATION_JSON);
		
	}

}
