package it.beltek.ia.iotlab.edge.server.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.beltek.ia.iotlab.edge.gateway.Pm3200Gateway;
import it.beltek.ia.iotlab.edge.gateway.RejectGateway;


public class RejectObsResource extends CoapResource{

	RejectGateway rejectGateway;
	
	String name;
	
	String measures;
	
	private int lineVelocity;
	
	

	public RejectObsResource(String name, RejectGateway rejectGateway) {
		
		super(name);
		
		System.out.println("Resource:" + name );
		
		this.rejectGateway = rejectGateway;
		
		this.name = name;
	}
	
	public int getLineVelocity() {
		return lineVelocity;
	}

	public void setLineVelocity(int lineVelocity) {
		
		this.lineVelocity = lineVelocity;
		
		System.out.println("Valore passato alla risorsa: " + this.lineVelocity );
	}

	
	// Ottengo la risorsa osservabile passondogli il valore quando il thread del server lo aggiorna ogni minuto
	@Override
	public void handleGET(CoapExchange exchange) {
		
		System.out.println("Valore risorsa della risorsa: " + this.lineVelocity);
		
		exchange.respond(ResponseCode.CONTENT, Integer.toString(lineVelocity), MediaTypeRegistry.TEXT_PLAIN);
	}
	
	
}
