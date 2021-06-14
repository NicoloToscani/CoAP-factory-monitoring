package it.beltek.ia.iotlab.edge.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

import org.eclipse.californium.core.coap.CoAP.Code;

public class CoAPClientPm extends CoapClient {
	
	private CoapClient coapClient;
	
	//String url = "coap://localhost:5562/.well-known/core";
	private String url = "coap://localhost:5562/reject_1_1_Velocity";
	
	public CoAPClientPm() {
		
		new SchneiderPM3200();
		
		this.coapClient = new CoapClient(url);
	}
	
	/**
	 * Client code
	**/
	private void run(){
		
		// GET
					Request request = new Request(Code.GET);
					
					CoapResponse coapResponseGet = this.coapClient.advanced(request);
					
					System.out.println("GET: " + coapResponseGet.getResponseText());
		
	}
	
	public static void main(String[] args) {
		
		new CoAPClientPm().run();
	}

}
