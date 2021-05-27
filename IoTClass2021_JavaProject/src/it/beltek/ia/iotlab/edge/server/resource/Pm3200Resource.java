package it.beltek.ia.iotlab.edge.server.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.beltek.ia.iotlab.edge.gateway.Pm3200Gateway;


public class Pm3200Resource extends CoapResource{

	Pm3200Gateway pm3200Gateway;
	
	String name;
	
	String measures;
	
	public Pm3200Resource(String name, Pm3200Gateway pm3200Gateway) {
		
		super(name);
		
		System.out.println("Resource:" + name );
		
		this.pm3200Gateway = pm3200Gateway;
		
		this.name = name;
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		
		jsonDatataFormatting();
		
	    //exchange.respond(ResponseCode.CONTENT, Float.toString(this.pm3200Gateway.getPm3200ModbusService().getSchneiderPM3200().L1_L2), MediaTypeRegistry.TEXT_PLAIN);
		exchange.respond(ResponseCode.CONTENT, this.measures, MediaTypeRegistry.APPLICATION_JSON);
	}
	
	
	/**
	 * JSON data formatting for CoAP protocol
	**/
	private void jsonDatataFormatting() {
		
		// JSON serialization
		GsonBuilder gsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues();
		
		Gson pm3200Serialize = gsonBuilder.enableComplexMapKeySerialization().create();	
		
		this.measures = pm3200Serialize.toJson(this.pm3200Gateway.getPm3200ModbusService().getSchneiderPM3200());
		
		
	}

}
