package it.beltek.ia.iotlab.edge.server.resource;

import java.awt.desktop.SystemSleepEvent;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.beltek.ia.iotlab.edge.gateway.Pm3200Gateway;
import it.beltek.ia.iotlab.edge.gateway.RejectGateway;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.WeightSystem;
import it.beltek.ia.iotlab.edge.gateway.device.components.JsonRequest;


public class RejectResource extends CoapResource{

	RejectGateway rejectGateway;
	
	String name;
	
	String measures;
	
	String putResource;
	
	String postResource;
	
	JsonRequest jsonRequest;
	
	public RejectResource(String name, RejectGateway rejectGateway, JsonRequest jsonRequest) {
		
		super(name);
		
		System.out.println("Resource:" + name );
		
		this.rejectGateway = rejectGateway;
		
		this.name = name;
		
		this.jsonRequest = jsonRequest;
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		
		jsonDatataFormatting();
	
		exchange.respond(ResponseCode.CONTENT, this.measures, MediaTypeRegistry.APPLICATION_JSON);
	}
	
	@Override
	public void handlePUT(CoapExchange exchange) {
			
		// Devo differenziare i valori che gli passo se setpoint peso o setpoint velocità
		this.putResource = exchange.getRequestText();
		
        Gson gson = new Gson();
		
		JsonRequest jRequest = gson.fromJson(this.putResource, JsonRequest.class);
		
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
