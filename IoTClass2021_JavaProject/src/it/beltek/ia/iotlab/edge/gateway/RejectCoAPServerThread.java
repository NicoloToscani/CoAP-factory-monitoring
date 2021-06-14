package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import it.beltek.ia.iotlab.edge.server.CoAPServer;
import it.beltek.ia.iotlab.edge.server.resource.RejectObsResource;
import it.beltek.ia.iotlab.edge.server.resource.RejectResource;

public class RejectCoAPServerThread extends CoAPServer implements Runnable{
	
	private RejectGateway rejectGateway;
	
	private CoapServer coapServer;
	
	private CoapResource coapResource;
	
	private CoapResource coapResourceObs;
	
	private int coapServerPort;
	
	public RejectCoAPServerThread(RejectGateway rejectGateway, CoapResource coapResource, CoapResource coapResourceObs, int coapPort) {
		
		this.rejectGateway = rejectGateway;
		
		this.coapServerPort = coapPort;
		
		this.coapServer = new CoapServer(coapServerPort);
		
		this.coapResource = coapResource;
		
		this.coapResourceObs = coapResourceObs;
		
		this.coapResourceObs.setObservable(true);
		
		this.coapResourceObs.getAttributes().setObservable();
		
		
	}
	

	@Override
	public void run() {
		
		System.out.println("RejectCoAPServerThread start at " + new Date());
		
		this.coapServer.add(this.coapResource);
		
		this.coapServer.add(this.coapResourceObs);
			
		this.coapServer.start();
		
		while(true) {
			
			System.out.println("Totale pezzi prodotti: " + this.rejectGateway.getRejectModbusService().getWeightSystem().totalCount);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
