package it.beltek.ia.iotlab.edge.gateway.moniline.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.beltek.ia.iotlab.edge.gateway.Pm3200Gateway;
import it.beltek.ia.iotlab.edge.gateway.moniline.MonilineGateway;


public class EnergyAverageResource extends CoapResource{

	MonilineGateway monilineGateway;
	
	String name;
	
	String measures;
	
	String lineID;
	
	public EnergyAverageResource(String name, MonilineGateway monilineGateway, String lineID) {
		
		super(name);
		
		System.out.println("Resource:" + name );
		
		this.monilineGateway = monilineGateway;
		
		this.name = name;
		
		this.lineID = lineID;
	}

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
		
		Gson energyAverageSerialize = gsonBuilder.enableComplexMapKeySerialization().create();	
		
		this.measures = energyAverageSerialize.toJson(this.monilineGateway.getClass());
		
		
	}

}
