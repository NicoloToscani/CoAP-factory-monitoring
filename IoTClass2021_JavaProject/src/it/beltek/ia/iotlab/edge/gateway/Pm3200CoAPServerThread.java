package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import it.beltek.ia.iotlab.edge.server.CoAPServer;

public class Pm3200CoAPServerThread extends CoAPServer implements Runnable{
	
	private Pm3200Gateway pm3200Gateway;
	
	private CoapServer coapServer;
	
	private CoapResource coapResource;
	
	 private int coapServerPort;
	
	public Pm3200CoAPServerThread(Pm3200Gateway pm3200Gateway, CoapResource coapResource, int coapPort) {
		
		this.pm3200Gateway = pm3200Gateway;
		
		this.coapServerPort = coapPort;
		
		this.coapServer = new CoapServer(coapServerPort);
		
		this.coapResource = coapResource;
	}
	
	

	@Override
	public void run() {
		
		System.out.println("pm3200CoAPServerThread start at " + new Date());
		
		this.coapServer.add(this.coapResource);
		
		this.coapServer.start();
		
		while(true) {
			
			System.out.println("Misura da strumento: " + this.pm3200Gateway.getPm3200ModbusService().getSchneiderPM3200().L1_L2);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
