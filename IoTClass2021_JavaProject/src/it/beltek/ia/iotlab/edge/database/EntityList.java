package it.beltek.ia.iotlab.edge.database;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EntityList extends CoapResource {
	
	private MasterRepository masterRepository;
	
	private String name;
	
	private String entityList;

	public EntityList(String name, MasterRepository masterRepository) {
		super(name);
		
		this.masterRepository = masterRepository;
		
		this.name = name;
		
	}
	
	// Device list
	@Override
	public void handleGET(CoapExchange exchange) {
		
        jsonDatataFormatting();
		
		exchange.respond(ResponseCode.CONTENT, this.entityList, MediaTypeRegistry.APPLICATION_JSON);
		
	}
	
	
	/**
	 * JSON data formatting for CoAP protocol
	**/
	private void jsonDatataFormatting() {
		
		// JSON serialization
		Gson gson = new Gson();
		
	    entityList = gson.toJson(this.masterRepository.getDeviceListQueue());
		
	}

}
