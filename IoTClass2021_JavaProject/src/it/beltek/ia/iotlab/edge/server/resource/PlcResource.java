package it.beltek.ia.iotlab.edge.server.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.beltek.ia.iotlab.edge.gateway.PlcGateway;
import it.beltek.ia.iotlab.edge.gateway.Pm3200Gateway;


public class PlcResource extends CoapResource{

	PlcGateway plcGateway;
	
	String name;
	
	String measures;
	
	String putResource;
	
	String postReasource;
	
	public PlcResource(String name, PlcGateway plcGateway) {
		
		super(name);
		
		System.out.println("Resource:" + name );
		
		this.plcGateway = plcGateway;
		
		this.name = name;
	}
	
	// POST
	@Override
	public void handlePOST(CoapExchange exchange) {
		
		// exchange.respond(ResponseCode.CREATED, this.postReasource, MediaTypeRegistry.TEXT_PLAIN);
		
		this.postReasource = exchange.getRequestText();
		
		System.out.println("Ricezione post: " + this.postReasource);
		
	}
	
	// POST
		@Override
		public void handlePUT(CoapExchange exchange) {
			
			this.putResource = exchange.getRequestText();
			
			System.out.println("Ricezione post: " + this.putResource);
			
		}


	// GET
	@Override
	public void handleGET(CoapExchange exchange) {
		
		jsonDatataFormatting();
		
		exchange.respond(ResponseCode.CONTENT, this.measures, MediaTypeRegistry.APPLICATION_JSON);
	}
	
	
	/**
	 * JSON data formatting for CoAP protocol
	**/
	private void jsonDatataFormatting() {
		
		// JSON serialization
		GsonBuilder gsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues();
		
		Gson plcSerialize = gsonBuilder.enableComplexMapKeySerialization().create();	
		
		this.measures = plcSerialize.toJson(this.plcGateway.getPlcS7Service().getSiemensPLC());
		
		
	}

}
