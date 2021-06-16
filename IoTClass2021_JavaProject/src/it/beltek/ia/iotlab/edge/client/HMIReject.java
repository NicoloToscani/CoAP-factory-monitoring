package it.beltek.ia.iotlab.edge.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP.Code;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.WeightSystem;
import it.beltek.ia.iotlab.edge.gateway.device.components.JsonRequest;
import it.beltek.ia.iotlab.edge.gateway.moniline.resource.EnergyAverage;
import it.beltek.ia.iotlab.edge.gateway.moniline.resource.PlcAverage;

public class HMIReject {
	
	private DeviceStruct rejectDevice;

	// Coap client
	private CoapClient coapClientReject;

	
	// Device
	private WeightSystem weightSystemReject;
	
	private String observeResourceReject;

	public HMIReject() {
		
		this.rejectDevice = new DeviceStruct();
		
		this.weightSystemReject = new WeightSystem();
				
	}
	
	public DeviceStruct getRejectDevice() {
		return rejectDevice;
	}

	public CoapClient getCoapClientReject() {
		return coapClientReject;
	}

	public WeightSystem getWeightSystemReject() {
		return weightSystemReject;
	}

	public void setRejectDevice(DeviceStruct rejectDevice) {
		this.rejectDevice = rejectDevice;
	}

	public void setCoapClientReject(CoapClient coapClientReject) {
		this.coapClientReject = coapClientReject;
	}

	public void setWeightSystemReject(WeightSystem weightSystemReject) {
		this.weightSystemReject = weightSystemReject;
	}
	
	public String getObserveResourceReject() {
		return observeResourceReject;
	}

	public void setObserveResourceReject(String observeResourceReject) {
		this.observeResourceReject = observeResourceReject;
	}
		

	public void clientBind() {
		
		String uriReject = "coap://localhost:" + this.rejectDevice.devicePort + "/" + this.rejectDevice.deviceName;
		
		System.out.println("URI Reject: " + uriReject);
		
		this.observeResourceReject = uriReject;
	}
	
	
	public void writeLineVelocitySetpoint(String lineVelocitySetpoint) {
		
        JsonRequest lineVelocityRequest = new JsonRequest();
    	
    	// PUT drive velocity
        lineVelocityRequest.setField("lineVelocitySetpoint");
        lineVelocityRequest.setValue(lineVelocitySetpoint);
	    
	    Gson gsonDrive = new Gson();
	    String lineVelocitySerialize = gsonDrive.toJson(lineVelocityRequest);
	    
	    CoapResponse coapResponseLineVelocityPost = this.getCoapClientReject().post(lineVelocitySerialize, MediaTypeRegistry.APPLICATION_JSON);
		
	}
	
    public void writeThrSetpoint(String thrSetpoint) {
    	
        JsonRequest thrSetpointRequest = new JsonRequest();
    	
    	// PUT drive velocity
        thrSetpointRequest.setField("setpoint");
        thrSetpointRequest.setValue(thrSetpoint);
	    
	    Gson gsonDrive = new Gson();
	    String thrSetpointSerialize = gsonDrive.toJson(thrSetpointRequest);
	    
	    CoapResponse coapResponseThrSetpointPost = this.getCoapClientReject().post(thrSetpointSerialize, MediaTypeRegistry.APPLICATION_JSON);
		
	}
	
}
	
	
