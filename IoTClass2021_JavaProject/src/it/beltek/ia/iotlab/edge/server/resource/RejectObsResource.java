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
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		
		jsonDatataFormatting();
		
		//exchange.respond(ResponseCode.CONTENT, Float.toString(this.pm3200Gateway.getPm3200ModbusService().getSchneiderPM3200().L1_L2), MediaTypeRegistry.TEXT_PLAIN);
		exchange.respond(ResponseCode.CONTENT, Integer.toString(lineVelocity), MediaTypeRegistry.TEXT_PLAIN);
	}
	
	
	/**
	 * JSON data formatting for CoAP protocol
	**/
	private void jsonDatataFormatting() {
		
		// JSON serialization
		GsonBuilder gsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues();
		
		Gson pm3200Serialize = gsonBuilder.enableComplexMapKeySerialization().create();	
		
		this.measures = pm3200Serialize.toJson(this.rejectGateway.getRejectModbusService().getWeightSystem());
		
		
	}

}
