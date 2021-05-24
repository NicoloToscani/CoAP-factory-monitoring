package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

import it.beltek.ia.iotlab.edge.server.CoAPServer;

public class Qm42vt2CoAPServerThread extends CoAPServer implements Runnable{
	
	Qm42vt2Gateway qm42vt2Gateway;
	
	CoapServer coapServer;
	
	CoapResource coapResource;
	
	public Qm42vt2CoAPServerThread(Qm42vt2Gateway qm42vt2Gateway, CoapResource coapResource) {
		
		this.qm42vt2Gateway = qm42vt2Gateway;
		
		this.coapServer = new CoapServer();
		
		this.coapResource = coapResource;
	}
	

	@Override
	public void run() {
		
		System.out.println("Qm42vt2CoAPServerThread start at " + new Date());
		
		this.coapServer.add(this.coapResource);
		
		this.coapServer.start();
		
		while(true) {
			
			System.out.println("Misura da strumento temperatura: " + this.qm42vt2Gateway.getQm42vt2ModbusService().getBannerQm42vt2().Temperature_C);
			System.out.println("Misura da strumento X_Axis_Kurtosis: " + this.qm42vt2Gateway.getQm42vt2ModbusService().getBannerQm42vt2().X_Axis_Kurtosis);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
