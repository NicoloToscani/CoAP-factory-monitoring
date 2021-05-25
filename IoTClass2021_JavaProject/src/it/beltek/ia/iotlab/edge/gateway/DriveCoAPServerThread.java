package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import it.beltek.ia.iotlab.edge.server.CoAPServer;

public class DriveCoAPServerThread extends CoAPServer implements Runnable{
	
	DriveGateway driveGateway;
	
	CoapServer coapServer;
	
	CoapResource coapResource;
	
	public DriveCoAPServerThread(DriveGateway driveGateway, CoapResource coapResource) {
		
		this.driveGateway = driveGateway;
		
		this.coapServer = new CoapServer();
		
		this.coapResource = coapResource;
	}
	
	

	@Override
	public void run() {
		
		System.out.println("driveCoAPServerThread start at " + new Date());
		
		this.coapServer.add(this.coapResource);
		
		this.coapServer.start();
		
		while(true) {
			
			System.out.println("Veloctità: " + this.driveGateway.getG120cPnService().getDrive().actualSpeed);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
