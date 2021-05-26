package it.beltek.ia.iotlab.edge.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoAPClientObs extends CoapClient {
	
	private CoapClient coapClient;
	
	//String url = "coap://localhost::5686/.well-known/core";
	private String url = "coap://localhost::5686/reject1Velocity";
	
	public CoAPClientObs() {
		
		this.coapClient = new CoapClient(url);
	}
	
	/**
	 * Client code
	**/
	private void run(){
		
		while(true) {
			
			this.coapClient.observe(new CoapHandler() {
				
				@Override
				public void onLoad(CoapResponse response) {
					
					System.out.println(response.getResponseText());
					
				}
				
				@Override
				public void onError() {
					System.out.println("Resource observer error");					
				}
			});
			
			
			try {
				
				Thread.sleep(5000);
				
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
