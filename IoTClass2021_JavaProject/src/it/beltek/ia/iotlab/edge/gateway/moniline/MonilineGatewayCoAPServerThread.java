package it.beltek.ia.iotlab.edge.gateway.moniline;

import java.awt.List;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.Endpoint;

import it.beltek.ia.iotlab.edge.server.CoAPServer;

public class MonilineGatewayCoAPServerThread extends CoAPServer implements Runnable{
	
	private MonilineGateway monilineGateway;
	
	private CoapServer coapServer;
	
	private CoapResource energyAverageresource;
	
	private CoapResource machineStateEnergyReasource;
	
	private int coapServerPort;
	
	java.util.List<Endpoint> endpoints;
	
	public MonilineGatewayCoAPServerThread(MonilineGateway monilineGateway, CoapResource energyAverageResource, CoapResource machinesStateAverageResource, int coapServerPort) {
		
		this.monilineGateway = monilineGateway;
		
		this.coapServerPort = coapServerPort;
		
		this.coapServer = new CoapServer(this.coapServerPort);
		
		this.energyAverageresource = energyAverageResource;
		
		this.machineStateEnergyReasource = machinesStateAverageResource;
		
		//this.endpoints = (java.util.List<Endpoint>) coapServer.getEndpoints();
		
		
	}
	
	

	@Override
	public void run() {
		
		System.out.println("monileGatewayCoAPServerThread start at " + new Date());
		
		this.coapServer.add(this.energyAverageresource, this.machineStateEnergyReasource);
		
		this.coapServer.start();
		
		while(true) {
			
			 System.out.println("Server MONILINE in esecuzione");
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void getEndopoits() {
		
		Iterator<Endpoint> endpointIter = this.endpoints.iterator();
		
		while(endpointIter.hasNext()) {
			
			Endpoint endpoint = endpointIter.next();
			
			System.out.println("Endpoint: " + endpoint.toString());			
			
		}
		
		
		
		
	}

}
