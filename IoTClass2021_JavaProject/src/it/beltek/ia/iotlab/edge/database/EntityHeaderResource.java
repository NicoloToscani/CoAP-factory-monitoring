package it.beltek.ia.iotlab.edge.database;


import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;

import com.google.gson.Gson;


public class EntityHeaderResource extends ConcurrentCoapResource{

	private String name;
	
	private String postResource;
	
	private MasterRepository masterRepository;
	
	public EntityHeaderResource(String name, MasterRepository masterRepository) {
		super(name);
		
		this.name = name;
		
		this.masterRepository = masterRepository;
		
		System.out.println("Creata risosrsa: " + this.name);
		
	}
	
	@Override
	public void handlePOST(CoapExchange exchange) {
		
		System.out.println("Chiamato post");
		
        this.postResource = exchange.getRequestText();
		
        Gson gson = new Gson();
		
		EntityHeader entityRcv = gson.fromJson(this.postResource, EntityHeader.class);
		
		this.masterRepository.getDeviceListQueue().add(entityRcv);
		
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
		
		exchange.respond(ResponseCode.CONTENT, "Rispsosta get", MediaTypeRegistry.TEXT_PLAIN);
		
	}
	
}
