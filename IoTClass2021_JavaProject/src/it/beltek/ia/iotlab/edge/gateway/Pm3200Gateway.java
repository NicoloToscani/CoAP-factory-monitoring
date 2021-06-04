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
import it.beltek.ia.iotlab.edge.gateway.service.Pm3200ModbusService;
import it.beltek.ia.iotlab.edge.server.resource.Pm3200Resource;

public class Pm3200Gateway {
	
	// Modbus TCP-IP server data
	 private String IPAddress;
     private int Port;
     
     private String deviceName;
     
     private int coapServerPort;
     
     private int lineID;
     private int machineID;
	
     private Pm3200ModbusService pm3200ModbusService;
     
     private ThreadPoolExecutor pool;
     
     private Pm3200Resource pm3200Resource;
     
     private static final int COREPOOL = 2;
     private static final int MAXPOOL = 2;
     private static final int IDLETIME = 5000;
     private static final int SLEEPTIME = 1000;
     
     private CoapClient repositoryCoapClient;
     
     private String urlRepository = "coap://localhost:5600/master_repository";
     
	 
	/**
	 * Class constructor. 
	**/
	public Pm3200Gateway(int coapServerPort, String deviceName, int lineID, int machineID) {
		
		this.IPAddress = "192.168.100.3";
		
		this.Port = 502;
		
		this.deviceName = deviceName;
		
		this.coapServerPort = coapServerPort;
		
		this.lineID = lineID;
		
		this.machineID = machineID;
		
		this.pm3200ModbusService = new Pm3200ModbusService(IPAddress, Port);
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		this.pm3200Resource = new Pm3200Resource(this.deviceName + "_" + this.lineID + "_" + this.machineID, this);
		
		this.repositoryCoapClient = new CoapClient(urlRepository);
		
	}

	/**
	 * Run Pm3200Gateway code
	**/
	private void run() {
		
		System.out.println("Pm3200Gateway start at " + new Date());
		
		// Master repository registration
		registerEntity();
		
		this.pool.execute(new Pm3200FieldbusThread(this, this.deviceName));
		this.pool.execute(new Pm3200CoAPServerThread(this, pm3200Resource, this.coapServerPort));
		
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
		
		BufferedReader bufferedReaderPort = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderName = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderLine = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bufferedReaderMachine = new BufferedReader(new InputStreamReader(System.in));
		
		int coapPort = 0;
		String deviceName = null;
		int lineID = 0;
		int machineID = 0;
	
		try {
			
			System.out.print("Insert ENERGY server CoAP port: ");
			coapPort = Integer.parseInt(bufferedReaderPort.readLine());
			
			System.out.print("Insert ENERGY server CoAP name: ");
			deviceName = bufferedReaderName.readLine();
			
			System.out.print("Insert ENERGY line ID: ");
			lineID = Integer.parseInt(bufferedReaderLine.readLine());
			
			System.out.print("Insert ENERGY machine ID: ");
			machineID = Integer.parseInt(bufferedReaderMachine.readLine());
			
			System.out.println("CoAP port: " + coapPort);
			System.out.println("CoAP name: " + deviceName);
			System.out.println("Line ID: " + lineID);
			System.out.println("Machine ID: " + machineID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Pm3200Gateway(coapPort, deviceName, lineID, machineID).run();
			
	}
	
	public Pm3200ModbusService getPm3200ModbusService() {
		
		return pm3200ModbusService;
	}
	
	// Master repository registration
	public void registerEntity() {
				
		// POST
		EntityHeader entityHeader = new EntityHeader(this.coapServerPort, this.deviceName, this.lineID, this.machineID, 0);
				
		Gson gsonPlcEntity = new Gson();
		String energySerializeEntity = gsonPlcEntity.toJson(entityHeader);
		CoapResponse coapResponsePlcEntity = this.repositoryCoapClient.post(energySerializeEntity, MediaTypeRegistry.APPLICATION_JSON);
				
		}

}
