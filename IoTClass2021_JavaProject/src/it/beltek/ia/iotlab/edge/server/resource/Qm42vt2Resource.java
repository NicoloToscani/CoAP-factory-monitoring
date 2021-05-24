package it.beltek.ia.iotlab.edge.server.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.beltek.ia.iotlab.edge.gateway.Qm42vt2Gateway;

public class Qm42vt2Resource extends CoapResource {
	
    Qm42vt2Gateway qm42vt2Gateway;
	
	String name;
	
	String measures;

	public Qm42vt2Resource(String name, Qm42vt2Gateway qm42vt2Gateway ) {
		
		super(name);
		
        System.out.println("Resource:" + name );
		
		this.qm42vt2Gateway = qm42vt2Gateway;
		
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
		
		Gson qm42vt2Serialize = gsonBuilder.enableComplexMapKeySerialization().create();	
		
		this.measures = qm42vt2Serialize.toJson(this.qm42vt2Gateway.getQm42vt2ModbusService().getBannerQm42vt2());
		
		
	}

}
