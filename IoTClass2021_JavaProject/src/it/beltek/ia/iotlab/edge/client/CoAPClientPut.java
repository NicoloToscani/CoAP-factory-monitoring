package it.beltek.ia.iotlab.edge.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoAPClientPut extends CoapClient {
	
	private PLC plc;
	
	private CoapClient coapClient;
	
	// String url = "coap://localhost::5683/.well-known/core";
	private String url = "coap://localhost::5683/plc1";
	
	public CoAPClientPut() {
		
		this.plc = new PLC();
		
		this.coapClient = new CoapClient(url);
	}
	
	/**
	 * Client code
	**/
	private void run(){
		
		while(true) {
			
			CoapResponse coapResponse = this.coapClient.put("Sto scrivendo", MediaTypeRegistry.TEXT_PLAIN);
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	public static void main(String[] args) {
		
		new CoAPClientPut().run();
	}

}
