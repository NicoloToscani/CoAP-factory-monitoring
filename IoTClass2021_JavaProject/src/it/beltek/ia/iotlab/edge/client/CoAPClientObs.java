package it.beltek.ia.iotlab.edge.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
;

public class CoAPClientObs extends CoapClient {
	
	private CoapClient coapClient;
	
	private String url = "coap://localhost:5687/reject1";
	
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
				
				System.out.println(response.getResponseText());
				
			}
			
			@Override
			public void onError() {
				// TODO Auto-generated method stub
				
			}
		});
		
		while(true) {
		try {
			Thread.sleep(3000);
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
