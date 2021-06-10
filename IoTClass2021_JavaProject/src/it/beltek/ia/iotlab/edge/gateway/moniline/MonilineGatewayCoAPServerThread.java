package it.beltek.ia.iotlab.edge.gateway.moniline;

import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import it.beltek.ia.iotlab.edge.server.CoAPServer;

public class MonilineGatewayCoAPServerThread extends CoAPServer implements Runnable{
	
	private MonilineGateway monilineGateway;
	
	private CoapServer coapServer;
	
	private CoapResource coapResource;
	
	int coapServerPort;
	
	public MonilineGatewayCoAPServerThread(MonilineGateway monilineGateway, CoapResource coapResource, int coapPort) {
		
		this.coapServerPort = coapPort;
		
		this.monilineGateway = monilineGateway;
		
		this.coapServer = new CoapServer(this.coapServerPort);
		
		this.coapResource = coapResource;
	}
	
	

	@Override
	public void run() {
		
		System.out.println("plcCoAPServerThread start at " + new Date());
		
		this.coapServer.add(this.coapResource);
		
		this.coapServer.start();
		
		while(true) {
			
			// System.out.println("Stato macchina: " + this.plcGateway.getPlcS7Service().getSiemensPLC().state);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
