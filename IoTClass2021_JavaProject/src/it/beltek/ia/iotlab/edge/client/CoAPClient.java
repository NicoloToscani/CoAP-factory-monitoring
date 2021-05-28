package it.beltek.ia.iotlab.edge.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.Request;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoAPClient extends CoapClient {
	
	private SchneiderPM3200 schneiderPM3200;
	
	private CoapClient coapClient;
	
	String url = "coap://localhost:5690/.well-known/core";
	//private String url = "coap://localhost::5685/powerEnergyMeter1";
	
	public CoAPClient() {
		
		this.schneiderPM3200 = new SchneiderPM3200();
		
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
		
		new CoAPClient().run();
	}

}
