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
import it.beltek.ia.iotlab.edge.gateway.service.Qm42vt2ModbusService;
import it.beltek.ia.iotlab.edge.server.resource.Qm42vt2Resource;

public class Qm42vt2Gateway {
	
	     // Modbus TCP-IP server data
		 private String IPAddress;
	     private int Port;
	     
	     private String deviceName;
	     
	     private int coapServerPort;
	     
	     
	     private int lineID;
	     private int machineID;
	     private int motorID;
		
	     private Qm42vt2ModbusService qm42vt2ModbusService;
	     
	     private ThreadPoolExecutor pool;
	     
	     private Qm42vt2Resource qm42vt2Resource;
	     
	     private static final int COREPOOL = 2;
	     private static final int MAXPOOL = 2;
	     private static final int IDLETIME = 5000;
	     private static final int SLEEPTIME = 1000;
	     
	     private CoapClient repositoryCoapClient;
	     
	     private String urlRepository = "coap://localhost:5600/master_repository";
	     
	     /**
	 	 * Class constructor. 
	 	**/
	 	public Qm42vt2Gateway(int coaperverPort, String deviceName, int lineID, int machineID, int motorID) {
	 		
	 		this.IPAddress = "192.168.100.4";
	 		
	 		this.Port = 502;
	 		
	 		this.deviceName = deviceName;
	 		
	 		this.coapServerPort = coaperverPort;
	 		
	 		this.lineID = lineID;
	 		
	 		this.machineID = machineID;
	 		
	 		this.motorID = motorID;
	 		
	 		this.qm42vt2ModbusService = new Qm42vt2ModbusService(IPAddress, Port);
	 		
	 		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	 		
	 		this.qm42vt2Resource = new Qm42vt2Resource(this.deviceName + "_" + this.lineID + "_" + this.machineID + "_" + this.motorID, this);
	 		
	 		this.repositoryCoapClient = new CoapClient(urlRepository);
	 		
	 	}

	 	/**
	 	 * Run Qm42vt2Gateway code
	 	**/
	 	private void run() {
	 		
	 		System.out.println("Qm42vt2Gateway start at " + new Date());
	 		
	 	    // Master repository registration
			registerEntity();
	 		
	 		this.pool.execute(new Qm42vt2FieldbusThread(this, this.deviceName));
	 		this.pool.execute(new Qm42vt2CoAPServerThread(this, qm42vt2Resource, this.coapServerPort));
	 		
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
			BufferedReader bufferedReaderID = new BufferedReader(new InputStreamReader(System.in));
			
			int coapPort = 0;
			String deviceName = null;
			int lineID = 0;
			int machineID = 0;
			int motorID = 0;
		
			try {
				
				System.out.print("Insert VIBRATION server CoAP port: ");
				coapPort = Integer.parseInt(bufferedReaderPort.readLine());
				
				System.out.print("Insert VIBRATION server CoAP name: ");
				deviceName = bufferedReaderName.readLine();
				
				System.out.print("Insert VIBRATION line ID: ");
				lineID = Integer.parseInt(bufferedReaderLine.readLine());
				
				System.out.print("Insert VIBRATION machine ID: ");
				machineID = Integer.parseInt(bufferedReaderMachine.readLine());
				
				System.out.print("Insert VIBRATION ID: ");
				motorID = Integer.parseInt(bufferedReaderID.readLine()) * 10;
				
				System.out.println("CoAP port: " + coapPort);
				System.out.println("CoAP name: " + deviceName);
				System.out.println("Line ID: " + lineID);
				System.out.println("Machine ID: " + machineID);
				System.out.println("Drive ID: " + motorID);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		
	 		new Qm42vt2Gateway(coapPort, deviceName, lineID, machineID, motorID).run();
	 		
	 	}
	 	
	 	public Qm42vt2ModbusService getQm42vt2ModbusService() {
	 		
	 		return qm42vt2ModbusService;
	 	}
	 	
	 // Master repository registration
		public void registerEntity() {
				
			// POST
			EntityHeader entityHeader = new EntityHeader(this.coapServerPort, this.deviceName, this.lineID, this.machineID, this.motorID);
				
			Gson gsonPlcEntity = new Gson();
			String vibrationSerializeEntity = gsonPlcEntity.toJson(entityHeader);
			CoapResponse coapResponsePlcEntity = this.repositoryCoapClient.post(vibrationSerializeEntity, MediaTypeRegistry.APPLICATION_JSON);
				
		}

}
