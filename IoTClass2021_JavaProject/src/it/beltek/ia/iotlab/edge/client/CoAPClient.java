package it.beltek.ia.iotlab.edge.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoAPClient extends CoapClient {
	
	private SchneiderPM3200 schneiderPM3200;
	
	private CoapClient coapClient;
	
	// String url = "coap://localhost::5683/.well-known/core";
	private String url = "coap://localhost::5683/powerEnergyMeter1";
	
	public CoAPClient() {
		
		this.schneiderPM3200 = new SchneiderPM3200();
		
		this.coapClient = new CoapClient(url);
	}
	
	/**
	 * Client code
	**/
	private void run(){
		
		while(true) {
			
			Request request = new Request(Code.GET);
			
			
			CoapResponse coapResponse = this.coapClient.advanced(request);
			
			 System.out.println(coapResponse.getResponseText());
			
			// JSON deserialization 
			Gson pm3200Deserialize = new Gson();
			
			this.schneiderPM3200 = pm3200Deserialize.fromJson(coapResponse.getResponseText(), SchneiderPM3200.class);
			
			System.out.println("Misura serializzata: " + this.schneiderPM3200.L1_L2);
			System.out.println("Misura serializzata carico: " + this.schneiderPM3200.loadType);
			System.out.println("Stato connessione: " + this.schneiderPM3200.connectionState);
			
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	public static void main(String[] args) {
		
		new CoAPClient().run();
	}

}
