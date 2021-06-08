package it.beltek.ia.iotlab.edge.client;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.database.EntityHeader;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

public class HmiMaintenance{
	
	private ThreadPoolExecutor pool;
	 
	private static final int COREPOOL = 10;
    private static final int MAXPOOL = 10;
    private static final int IDLETIME = 5000;
    private static final int SLEEPTIME = 1000;
	
	private int lineNumber;
	private ArrayList<EntityHeader> deviceLineList;
	private HashMap<Integer, HMIMachine> hmiMachineMap;
	
    
	private SchneiderPM3200 schneiderPM3200;
    public SchneiderPM3200 getSchneiderPM3200() {
		return schneiderPM3200;
	}


	public void setSchneiderPM3200(SchneiderPM3200 schneiderPM3200) {
		this.schneiderPM3200 = schneiderPM3200;
	}

	
	private PLC plc;
	private Drive drive;


	private CoapClient coapClientEnergy;
	private CoapClient coapClientPlc;
	private ArrayList<CoapClient> coAPCLientDrives;
	private ArrayList<CoapClient> coAPCLientVibrations;
	private CoapClient coapClientDrive;
	
	private CoapClient coapClientDeviceList;
	private CoapClient coapClientVibration;
	
	//String url = "coap://localhost:5687/.well-known/core";
	private String url1 = "coap://localhost:5683/plc";
	private String url2 = "coap://localhost:5684/powerEnergyMeter";
	private ArrayList<String> driveUrls;
	private ArrayList<String> vibrationUrls;
	
	// Da cambiare
	String driveUrl1 = "coap://localhost:5686/drive";
	String vibrationUrl1 = "coap://localhost:5685/vibrationSensor";
	
	// URI MasterRepository
	private String masterRepositoryUri = "coap://localhost:5600/master_repository_list";
	
	private EntityHeader[] entities;
	
	
	public HmiMaintenance() {
		
		this.lineNumber = 0;
		
		this.schneiderPM3200 = new SchneiderPM3200();
		
		this.drive = new Drive();
		
		this.plc = new PLC();
		
		this.coapClientPlc = new CoapClient(url1);
		
		this.coapClientEnergy = new CoapClient(url2);
		
		this.coapClientDrive = new CoapClient(driveUrl1);
		
		this.coapClientDeviceList = new CoapClient(masterRepositoryUri);
		
		this.coAPCLientDrives = new ArrayList<>();
		
		this.coAPCLientVibrations = new ArrayList<>();
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		this.deviceLineList = new ArrayList<>();
		
		this.hmiMachineMap = new HashMap<>();
		
	}
	
	
	/**
	 * HMI_1: Maintenance supervisor
	**/
	private void run(){
		
		// HMI1 ID - Line identifier
		BufferedReader bufferedReaderHmiId = new BufferedReader(new InputStreamReader(System.in));
			
		System.out.print("Insert HMI_1 line ID: ");
			
		try {
				
			this.lineNumber = Integer.parseInt(bufferedReaderHmiId.readLine());
			
		} catch (IOException e) {
					
			e.printStackTrace();
			
		}
			
		System.out.println("HMI ID: " + this.lineNumber);
			
		// Get device list from MasterRepository
		Request request = new Request(Code.GET);
		
		CoapResponse coapResponseGet = this.coapClientDeviceList.advanced(request);
		
		System.out.println("GET: " + coapResponseGet.getResponseText());
		
		Gson gson = new Gson();
		
		this.entities = gson.fromJson(coapResponseGet.getResponseText(), EntityHeader[].class);
		
		System.out.println(coapResponseGet.getResponseText());
		
		System.out.println("--- DEVICE LIST ---");
		
		int index = this.entities.length; 
		
		System.out.println("List size: " + index);
		
		for(int i = 0; i < index; i++) {
		   
			EntityHeader entityHeader = this.entities[i];
		 
		    System.out.println("DEVICE");
		    System.out.println("Coap server Port: " + entityHeader.getCoapPortNumber());
		    System.out.println("Device type: " + entityHeader.getDeviceType());
		    System.out.println("Line ID: " + entityHeader.getLineID());
		    System.out.println("Machine ID: " + entityHeader.getMachineID());
		    System.out.println("Device ID: " + entityHeader.getDeviceID());
		    
		    // Add line ID device into a line list
		    if(entityHeader.getLineID() == this.lineNumber) {
		    	
		    	this.deviceLineList.add(entityHeader);
		    	
		    }
			
		}
		
		System.out.println("Device line " + this.lineNumber + " : " + this.deviceLineList.size());
		
		// HMIMachine list compose with PLC, each machine have n.1 PLC
		Iterator deviceListIterator = this.deviceLineList.iterator();
		
		while(deviceListIterator.hasNext()) {
			
			EntityHeader entityHeader = (EntityHeader)deviceListIterator.next();
			
			if(entityHeader.getDeviceType().equals("plc")) {
				
				HMIMachine hmiMachine = new HMIMachine();
				
				DeviceStruct plcDeviceStruct = new DeviceStruct();
				
				plcDeviceStruct.deviceName = entityHeader.getDeviceType() + "_" + entityHeader.getLineID() + "_" + entityHeader.getMachineID();
				
				plcDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				hmiMachine.setPlcDevice(plcDeviceStruct);
				
				this.hmiMachineMap.put(entityHeader.getMachineID(), hmiMachine);  // Build HashMap with PLC device
				
			}
			
		}
		
		// Energy consumption device machine id
		Iterator listIterator = this.deviceLineList.iterator();
		
        while(listIterator.hasNext()) {
			
			EntityHeader entityHeader = (EntityHeader)listIterator.next();
			
			// Energy device
			if(entityHeader.getDeviceType().equals("energy")) {
				
				DeviceStruct energyDeviceStruct = new DeviceStruct();
				
				energyDeviceStruct.deviceName = entityHeader.getDeviceType() + "_" + entityHeader.getLineID() + "_" + entityHeader.getMachineID();
				
				energyDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				HMIMachine hmiMachine = hmiMachineMap.get(entityHeader.getMachineID());
				
				hmiMachine.setEnergyDevice(energyDeviceStruct);
				
			}
			
			// Discharge system
            if(entityHeader.getDeviceType().equals("reject")) {
				
				DeviceStruct rejectDeviceStruct = new DeviceStruct();
				
				rejectDeviceStruct.deviceName = entityHeader.getDeviceType() + "_" + entityHeader.getLineID() + "_" + entityHeader.getMachineID();
				
				rejectDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				HMIMachine hmiMachine = hmiMachineMap.get(entityHeader.getMachineID());
				
				hmiMachine.setDischargeDevice(rejectDeviceStruct);
				
			}
            
            // Drives
            if(entityHeader.getDeviceType().equals("drive")) {
				
				DeviceStruct driveDeviceStruct = new DeviceStruct();
				
				driveDeviceStruct.deviceName = entityHeader.getDeviceType() + "_" + entityHeader.getLineID() + "_" + entityHeader.getMachineID() + "_" + entityHeader.getDeviceID();
				
				driveDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				HMIMachine hmiMachine = hmiMachineMap.get(entityHeader.getMachineID());
				
				hmiMachine.getDriveDevices().add(driveDeviceStruct);
			}
            
            // Vibration sensors
            if(entityHeader.getDeviceType().equals("vibration")) {
				
				DeviceStruct vibrationDeviceStruct = new DeviceStruct();
				
				vibrationDeviceStruct.deviceName = entityHeader.getDeviceType() + "_" + entityHeader.getLineID() + "_" + entityHeader.getMachineID() + "_" + entityHeader.getDeviceID();
				
				vibrationDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				HMIMachine hmiMachine = hmiMachineMap.get(entityHeader.getMachineID());
				
				hmiMachine.getSensorDevices().add(vibrationDeviceStruct);
			}
            
		}
        
        // Stampo Map che identifica tutte le macchine della linea in esame
        Iterator <Integer> it = hmiMachineMap.keySet().iterator();
        while(it.hasNext()) {
        	
        	int key = it.next();
        	
        	System.out.println("-------------- Machine " + key + " --------------------");
        	
        	HMIMachine hmiMachine = hmiMachineMap.get(key);
        	
        	System.out.println("PLC macchina: "+ key + " namePLC: " + hmiMachine.getPlcDevice().deviceName);
        	System.out.println("Energy macchina: "+ key + " nameEnergy: " + hmiMachine.getEnergyDevice().deviceName);
        	System.out.println("Reject macchina: "+ key + " nameReject: " + hmiMachine.getDischargeDevice().deviceName);
        	
        	// Machine drives
        	ArrayList<DeviceStruct> drives = hmiMachine.getDriveDevices();
        	Iterator<DeviceStruct> iteratorDevices = drives.iterator();
        	
        	while(iteratorDevices.hasNext()) {
        		
        		DeviceStruct drive = iteratorDevices.next();
        		System.out.println("Drive macchina: "+ key + " nameReject: " + drive.deviceName);
        		
        	}
        	
        }
        
        
		this.pool.execute(new Hmi1ReadThread(this));
		this.pool.execute(new Hmi1WriteThread(this));
		 
	}
	
	public CoapClient getCoapClientEnergy() {
		return coapClientEnergy;
	}


	public ArrayList<CoapClient> getCoAPCLientDrives() {
		return coAPCLientDrives;
	}

	public ArrayList<CoapClient> getCoAPCLientVibrations() {
		return coAPCLientVibrations;
	}
	
	public CoapClient getCoapClientPlc() {
		return coapClientPlc;
	}
	
	public PLC getPlc() {
		return plc;
	}
	

	public void setPlc(PLC plc) {
		this.plc = plc;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public CoapClient getCoapClientDrive() {
		return coapClientDrive;
	}
	
	public Drive getDrive() {
		return drive;
	}
	
	public void setDrive(Drive drive) {
		this.drive = drive;
	}
	
	public HashMap<Integer, HMIMachine> getHmiMachineMap() {
		return hmiMachineMap;
	}

	public static void main(String[] args) {
		
		new HmiMaintenance().run();

	}

}

