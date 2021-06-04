package it.beltek.ia.iotlab.edge.client;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

import it.beltek.ia.iotlab.edge.database.EntityHeader;
import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.WeightSystem;
import it.beltek.ia.iotlab.edge.gateway.device.components.JsonRequest;

import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

public class CoAPClient extends CoapClient {
	
	private WeightSystem weightSystem;
	
	private JsonRequest jsonRequest;
	
	private CoapClient coapClient;
	
	private EntityHeader[] entities;
	
	//String url = "coap://localhost:5684/reject_1_1";
	//String url = "coap://localhost:5690/vibration_1_1_10";
	String url = "coap://localhost:5600/master_repository_list";
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
					
					Gson gson = new Gson();
					
					this.entities = gson.fromJson(coapResponseGet.getResponseText(), EntityHeader[].class);
					
					System.out.println(coapResponseGet.getResponseText());
					
					System.out.println("--- DEVICE LIST ---");
					
					int index = this.entities.length; 
					
					System.out.println("List size: " + index);
					
					
					for(int i = 0; i < index; i++) {
					   
						EntityHeader entityHeader = this.entities[i];
					 
					    System.out.println("DEVICE");
					    System.out.println("Device type: " + entityHeader.getDeviceType());
					    System.out.println("Line ID: " + entityHeader.getLineID());
					    System.out.println("Machine ID: " + entityHeader.getMachineID());
					    System.out.println("Device ID: " + entityHeader.getDeviceID());
						
					}
				
					
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
