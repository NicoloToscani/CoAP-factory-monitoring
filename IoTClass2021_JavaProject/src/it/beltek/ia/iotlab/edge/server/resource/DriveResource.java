package it.beltek.ia.iotlab.edge.server.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.beltek.ia.iotlab.edge.gateway.DriveGateway;
import it.beltek.ia.iotlab.edge.gateway.PlcGateway;
import it.beltek.ia.iotlab.edge.gateway.Pm3200Gateway;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;


public class DriveResource extends CoapResource{

	DriveGateway driveGateway;
	
	String name;
	
	String measures;
	
	String putResource;
	
	String postResource;
	
	public DriveResource(String name, DriveGateway driveGateway) {
		
		super(name);
		
		System.out.println("Resource:" + name );
		
		this.driveGateway = driveGateway;
		
		this.name = name;
	}
	
	
	@Override
	public void handlePOST(CoapExchange exchange) {
		
        this.postResource = exchange.getRequestText();
		
        Gson gson = new Gson();
		
		Drive driveRcv = gson.fromJson(this.postResource, Drive.class);
		
		this.driveGateway.getG120cPnService().getDrive().setpoint = driveRcv.setpoint;
		
	}
	
	@Override
	public void handlePUT(CoapExchange exchange) {
			
		this.putResource = exchange.getRequestText();
		
        Gson gson = new Gson();
		
		Drive driveRcv = gson.fromJson(this.putResource, Drive.class);
		
		this.driveGateway.getG120cPnService().getDrive().setpoint = driveRcv.setpoint;
		
			
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
		
		Gson driveSerialize = gsonBuilder.enableComplexMapKeySerialization().create();	
		
		this.measures = driveSerialize.toJson(this.driveGateway.getG120cPnService().getDrive());
		
		System.out.println("Drive resource: " + this.driveGateway.getG120cPnService().getDrive().actualSpeed);
		
		
	}

}
