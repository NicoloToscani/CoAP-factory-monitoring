package it.beltek.ia.iotlab.edge.database;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

public class MasterRepository {
	
	private ConcurrentLinkedQueue<EntityHeader> deviceListQueue;
	
	private CoapServer coapServer;
	
	private int coapServerPort;
	
	private EntityHeaderResource entityHeaderResource;
	
	private String reasourceName = "master_repository";
	
	
	public MasterRepository() {
		
		this.deviceListQueue = new ConcurrentLinkedQueue<>();
		
		this.coapServerPort = 5600;
		
		this.coapServer = new CoapServer(this.coapServerPort);
		
		this.entityHeaderResource = new EntityHeaderResource(reasourceName, this);
		
	}
	
	public ConcurrentLinkedQueue<EntityHeader> getDeviceListQueue() {
		
		return deviceListQueue;
	
	}
	
	
	public static void main(String[] args) {
		
		MasterRepository masterRepository = new MasterRepository();
		
		masterRepository.coapServer.add(masterRepository.entityHeaderResource);
		
		masterRepository.coapServer.start();
		
		System.out.println("MasterRepository start at " + new Date());
		
		while(true) {
			
			System.out.println("--- ENTITY DATABASE ---");
			System.out.println("Registered entity: " + masterRepository.getDeviceListQueue().size());
			
			try {
				
				Iterator<EntityHeader> iteratorDevices = masterRepository.getDeviceListQueue().iterator();
				
				while(iteratorDevices.hasNext()) {
				
					EntityHeader entityHeader = iteratorDevices.next();
					
					System.out.println("--- ENTITY ---");
					System.out.println("Device type: " + entityHeader.getDeviceType());
					System.out.println("Line ID: " + entityHeader.getLineID());
					System.out.println("Machine ID: " + entityHeader.getMachineID());
					System.out.println("Device ID: " + entityHeader.getDeviceID());
				}
				
				
				Thread.sleep(5000);
			
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
		}
		
	}
	
}
