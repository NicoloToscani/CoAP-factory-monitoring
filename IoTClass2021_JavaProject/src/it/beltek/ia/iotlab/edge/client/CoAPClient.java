package it.beltek.ia.iotlab.edge.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.WeightSystem;
import it.beltek.ia.iotlab.edge.gateway.device.components.JsonRequest;

import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoAPClient extends CoapClient {
	
	private WeightSystem weightSystem;
	
	private JsonRequest jsonRequest;
	
	private CoapClient coapClient;
	
	//String url = "coap://localhost:5684/reject_1_1";
	String url = "coap://localhost:5690/vibration_1_1_10";
	//private String url = "coap://localhost::5685/powerEnergyMeter1";
	
	public CoAPClient() {
		
	
		this.weightSystem = new WeightSystem();
		
		this.coapClient = new CoapClient(url);
		
		this.jsonRequest = new JsonRequest();
	}
	
	/**
	 * Client code
	**/
	private void run(){
		
		// GET
					Request request = new Request(Code.GET);
					
					CoapResponse coapResponseGet = this.coapClient.advanced(request);
					
					System.out.println("GET: " + coapResponseGet.getResponseText());
					
					// PUT weight
					this.jsonRequest.setField("setpoint");
					this.jsonRequest.setValue("85.2");
	
					Gson gsonDrive = new Gson();
					String weightSerialize = gsonDrive.toJson(this.jsonRequest);
					
					CoapResponse coapResponseWeightPut = this.coapClient.put(weightSerialize, MediaTypeRegistry.APPLICATION_JSON);
		
	}
	
	public static void main(String[] args) {
		
		new CoAPClient().run();
	}

}
