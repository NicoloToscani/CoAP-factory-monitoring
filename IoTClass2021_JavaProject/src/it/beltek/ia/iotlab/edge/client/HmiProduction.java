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

public class HmiProduction{
	
	private ThreadPoolExecutor pool;
	 
	private static final int COREPOOL = 10; 
    private static final int MAXPOOL = 10;
    private static final int IDLETIME = 5000;
    private static final int SLEEPTIME = 1000;
	
	private int lineNumber;
	private ArrayList<EntityHeader> deviceLineList;
	private HashMap<Integer, HMIMoniline> hmiMonilineMap;
	private ArrayList<HMIReject> hmiRejectsList;
	
	
	

	private CoapClient coapClientDeviceList;
	
	// URI MasterRepository
	private String masterRepositoryUri = "coap://localhost:5600/master_repository_list";
	
	private EntityHeader[] entities;
	
	public HmiProduction() {
		
		this.lineNumber = 0;
		
		this.coapClientDeviceList = new CoapClient(masterRepositoryUri);
		
		 this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		 this.deviceLineList = new ArrayList<>();
		
		 this.hmiMonilineMap = new HashMap<>();
		 
		 this.hmiRejectsList = new ArrayList<>();
		
	}
	
	
	/**
	 * HMI_2: Production supervisor
	**/
	private void run(){
		
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
		   
		    this.deviceLineList.add(entityHeader);
		    
		}
		
		System.out.println("Dimensione lista: " + this.deviceLineList.size());
		
		// HMIProduction list compose with MONILINE
		Iterator deviceListIterator = this.deviceLineList.iterator();
		
		while(deviceListIterator.hasNext()) {
			
			EntityHeader entityHeader = (EntityHeader)deviceListIterator.next();
			
			// Moniline device
			if(entityHeader.getDeviceType().equals("moniline")) {
				
				HMIMoniline hmiMoniline = new HMIMoniline();
				
				DeviceStruct monilineDeviceStruct = new DeviceStruct();
				
				DeviceStruct energyAverageDeviceStruct = new DeviceStruct();
				
				DeviceStruct plcAverageDeviceStruct = new DeviceStruct();
				
				monilineDeviceStruct.deviceName = entityHeader.getDeviceType() + "_" + entityHeader.getLineID();
				
				monilineDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				energyAverageDeviceStruct.deviceName = "energy_average_" + entityHeader.getLineID();
				
				energyAverageDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				plcAverageDeviceStruct.deviceName = "machines_state_average_" + entityHeader.getLineID();
				
				plcAverageDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				hmiMoniline.setMonilineDevice(monilineDeviceStruct);
				
				hmiMoniline.setPlcAverageDevice(plcAverageDeviceStruct);
				
				hmiMoniline.setEnergyAverageDevice(energyAverageDeviceStruct);
				
				this.hmiMonilineMap.put(entityHeader.getLineID(), hmiMoniline);  // Build HashMap with MONILINE device
				
			}
			
			// Reject device
			if(entityHeader.getDeviceType().equals("reject")) {
				
				HMIReject hmiReject = new HMIReject();
				
				DeviceStruct rejectDeviceStruct = new DeviceStruct();
				
				rejectDeviceStruct.deviceName = entityHeader.getDeviceType() + "_" + entityHeader.getLineID() + "_" + entityHeader.getMachineID();
				
				rejectDeviceStruct.devicePort = entityHeader.getCoapPortNumber();
				
				hmiReject.setRejectDevice(rejectDeviceStruct);
				
				this.hmiRejectsList.add(hmiReject);
				
			}
			
		}
		
		System.out.println("Dimensione mappa: " + this.hmiMonilineMap.size());
		
        // Stampo Map che identifica tutte le macchine della linea in esame
        Iterator <Integer> it = hmiMonilineMap.keySet().iterator();
        while(it.hasNext()) {
        	
        	int key = it.next();
        	
        	System.out.println("-------------- Line " + key + " --------------------");
        	
        	HMIMoniline hmiMoniline = hmiMonilineMap.get(key);
        	
        	hmiMoniline.clientBind();
        	
    		this.pool.execute(new Hmi2ReadThread(this));
    		this.pool.execute(new Hmi2WriteThread(this));
        	
        }
        
        // Reject network devices
        Iterator<HMIReject> hmiIterator = this.hmiRejectsList.iterator();
        
        System.out.println("Reject list: " + this.hmiRejectsList.size());
        
        while(hmiIterator.hasNext()) {
        	
        	HMIReject hmiReject = hmiIterator.next();
        	
        	hmiReject.clientBind();
        	
        	// Run multi-thread rejects resource observing
            //Hmi2ReadObserveThread(this, hmiReject).run();
        	this.pool.execute(new Hmi2ReadObserveThread(this, hmiReject));
            
            System.out.println(hmiReject.getRejectDevice().deviceName);
            
        }
        
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	
	public HashMap<Integer, HMIMoniline> getHmiMonilineMap() {
		return hmiMonilineMap;
	}
	
	public ArrayList<HMIReject> getHmiRejectsList() {
		return hmiRejectsList;
	}

	public static void main(String[] args) {
		
		new HmiProduction().run();

	}

}

