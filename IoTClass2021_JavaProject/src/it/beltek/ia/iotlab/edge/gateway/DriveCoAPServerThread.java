package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import it.beltek.ia.iotlab.edge.server.CoAPServer;

public class DriveCoAPServerThread extends CoAPServer implements Runnable{
	
	private DriveGateway driveGateway;
	
	private CoapServer coapServer;
	
	private CoapResource coapResource;
	
	private int coapServerPort;
	
	public DriveCoAPServerThread(DriveGateway driveGateway, CoapResource coapResource, int coapPort) {
		
		this.coapServerPort = coapPort;
		
		this.driveGateway = driveGateway;
		
		this.coapServer = new CoapServer(this.coapServerPort);
		
		this.coapResource = coapResource;
		
	}
	
	

	@Override
	public void run() {
		
		System.out.println("driveCoAPServerThread start at " + new Date());
		
		this.coapServer.add(this.coapResource);
		
		this.coapServer.start();
		
		while(true) {
			
			System.out.println("Velocità: " + this.driveGateway.getG120cPnService().getDrive().actualSpeed);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
