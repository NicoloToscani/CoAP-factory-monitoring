package it.beltek.ia.iotlab.edge.gateway;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import it.beltek.ia.iotlab.edge.gateway.device.components.JsonRequest;
import it.beltek.ia.iotlab.edge.gateway.service.Pm3200ModbusService;
import it.beltek.ia.iotlab.edge.gateway.service.RejectModbusService;
import it.beltek.ia.iotlab.edge.server.resource.Pm3200Resource;
import it.beltek.ia.iotlab.edge.server.resource.RejectObsResource;
import it.beltek.ia.iotlab.edge.server.resource.RejectResource;

public class RejectGateway {
	
	// Modbus TCP-IP server data
	 private String IPAddress;
     private int Port;
     
     private String deviceName;
     
     private int coapServerPort;
	
     private RejectModbusService rejectModbusService;
     
     private ThreadPoolExecutor pool;
     
     private RejectResource rejectResource;
     private RejectObsResource obsResource; // Ogni minuto deve andare ad aggiornare la risorsa leggendo quanti pezzi ho in quel minuto
     
     private JsonRequest jsonRequest;
     
     private static final int COREPOOL = 2;
     private static final int MAXPOOL = 2;
     private static final int IDLETIME = 5000;
     private static final int SLEEPTIME = 1000;
     
	 
	/**
	 * Class constructor. 
	**/
	public RejectGateway() {
		
		this.IPAddress = "192.168.100.3";
		
		this.Port = 502;
		
		this.deviceName = "reject";
		
		this.coapServerPort = 5687;
		
		this.jsonRequest = new JsonRequest();
		
		this.rejectModbusService = new RejectModbusService(IPAddress, Port);
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		this.rejectResource = new RejectResource(this.deviceName, this, this.jsonRequest);
		
		this.obsResource = new RejectObsResource(deviceName + "Velocity", this);
		
	}
	
	 public RejectObsResource getObsResource() {
			return obsResource;
		}

	/**
	 * Run RejectGateway code
	**/
	private void run() {
		
		System.out.println("RejectGateway start at " + new Date());
		
		this.pool.execute(new RejectFieldbusThread(this, this.deviceName));
		this.pool.execute(new RejectCoAPServerThread(this, this.rejectResource, this.obsResource, this.coapServerPort));
		
		while(this.pool.getActiveCount() > 0) {
			
			try {
				
				System.out.println("Main");
				
				Thread.sleep(SLEEPTIME);
				
				
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			
			}
			
		}
		
	}
	
	
	public static void main(String[] args) {
		
		new RejectGateway().run();
		
	}
	
	public RejectModbusService getRejectModbusService() {
		
		return rejectModbusService;
	}

}
