package it.beltek.ia.iotlab.edge.client;

import java.util.Date;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
;

public class CoAPClientObs extends CoapClient {
	
	private CoapClient coapClient;
	
	private String url = "coap://localhost:5662/reject_1_1_Velocity";
	
	Request request = new Request(Code.GET);
	
	public CoAPClientObs() {
		
		this.coapClient = new CoapClient(url);
	}
	
	/**
	 * Client code
	**/
	private void run(){
		
		coapClient.observe(new CoapHandler() {
			
			@Override
			public void onLoad(CoapResponse response) {
				
				System.out.println("Line velocity: " + response.getResponseText() + new Date());
				
			}
			
			@Override
			public void onError() {
				System.out.print("reject velocity error");
				
			}
		});
		
		while(true) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
	
	}
	
	public static void main(String[] args) {
		
		new CoAPClientObs().run();
	}

}
