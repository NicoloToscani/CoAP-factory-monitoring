package it.beltek.ia.iotlab.edge.server.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.beltek.ia.iotlab.edge.gateway.Pm3200Gateway;
import it.beltek.ia.iotlab.edge.gateway.RejectGateway;
import it.beltek.ia.iotlab.edge.gateway.device.components.JsonRequest;


public class RejectObsResource extends CoapResource{

	RejectGateway rejectGateway;
	
	String name;
	
	String measures;
	
	private int lineVelocity;
	
    String postResource;
	
	JsonRequest jsonRequest; 
	
	

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

	
	
	@Override
	public void handleGET(CoapExchange exchange) {
		
		System.out.println("Valore risorsa della risorsa: " + this.lineVelocity);
		
		// Single resource, if we want get total count change with JSON formatting object
		exchange.respond(ResponseCode.CONTENT, Integer.toString(lineVelocity), MediaTypeRegistry.TEXT_PLAIN);
		
	
	}
	
	
	@Override
	public void handlePOST(CoapExchange exchange) {
		
		       // Devo differenziare i valori che gli passo se setpoint peso o setpoint velocità
				this.postResource = exchange.getRequestText();
				
		        Gson gson = new Gson();
				
				JsonRequest jRequest = gson.fromJson(this.postResource, JsonRequest.class);
				
				// Set weight set point value
				if(jRequest.getField().equals("setpoint")) {
					
					this.rejectGateway.getRejectModbusService().getWeightSystem().setpoint = Float.parseFloat(jRequest.getValue());
					
					System.out.println("Weight set point: " + this.rejectGateway.getRejectModbusService().getWeightSystem().setpoint);
				}
				
				else if(jRequest.getField().equals("lineVelocitySetpoint")) {
					
					this.rejectGateway.getRejectModbusService().getWeightSystem().lineVelocitySetpoint = Integer.parseInt(jRequest.getValue());
					
					System.out.println("lineVelocitySetpoint: " + this.rejectGateway.getRejectModbusService().getWeightSystem().setpoint);
				}
	}
	
	
}
