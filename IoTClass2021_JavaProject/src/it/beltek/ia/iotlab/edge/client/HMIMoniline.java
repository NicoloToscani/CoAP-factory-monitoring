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
import it.beltek.ia.iotlab.edge.gateway.moniline.resource.LineVelocityAverage;
import it.beltek.ia.iotlab.edge.gateway.moniline.resource.PlcAverage;

public class HMIMoniline {
	
	private DeviceStruct energyAverageDevice;
	private DeviceStruct plcAverageDevice;
	private DeviceStruct monilineDevice;
	private DeviceStruct lineVelocityAverageDevice;

	// Coap clients
	private CoapClient coapClientEnergyAverage;
	private CoapClient coapClientPlcAverage;
	private CoapClient copClientLineVelocityAverage;
	
	
	// Devices
	private EnergyAverage energyAverage;
	private List<PlcAverage> plcAverage;
	private LineVelocityAverage lineVelocityAverage;
	

	
	public HMIMoniline() {
		
		this.energyAverageDevice = new DeviceStruct();
		
		this.plcAverageDevice = new DeviceStruct();
		
		this.energyAverage = new EnergyAverage();
		
		this.lineVelocityAverage = new LineVelocityAverage();
		
		this.lineVelocityAverageDevice = new DeviceStruct();
				
	}
		
	public DeviceStruct getPlcAverageDevice() {
		return plcAverageDevice;
	}

	public void setPlcAverageDevice(DeviceStruct plcAverageDevice) {
		this.plcAverageDevice = plcAverageDevice;
	}

	public EnergyAverage getEnergyAverage() {
		return energyAverage;
	}


	public List<PlcAverage> getPlcAverage() {
		return plcAverage;
	}

	public void setEnergyAverage(EnergyAverage energyAverage) {
		this.energyAverage = energyAverage;
	}

	public void setPlcAverage(List<PlcAverage> plcAverage) {
		this.plcAverage = plcAverage;
	}
	
	public CoapClient getCoapClientEnergyAverage() {
		return coapClientEnergyAverage;
	}

	public void setCoapClientEnergyAverage(CoapClient coapClientEnergyAverage) {
		this.coapClientEnergyAverage = coapClientEnergyAverage;
	}

	public CoapClient getCoapClientPlcAverage() {
		return coapClientPlcAverage;
	}

	public void setCoapClientPlcAverage(CoapClient coapClientPlcAverage) {
		this.coapClientPlcAverage = coapClientPlcAverage;
	}
	
	public DeviceStruct getMonilineDevice() {
		return monilineDevice;
	}

	public void setMonilineDevice(DeviceStruct monilineDevice) {
		this.monilineDevice = monilineDevice;
	}
	
	public DeviceStruct getEnergyAverageDevice() {
		return energyAverageDevice;
	}

	public void setEnergyAverageDevice(DeviceStruct energyAverageDevice) {
		this.energyAverageDevice = energyAverageDevice;
	}
	
	public DeviceStruct getLineVelocityAverageDevice() {
		return lineVelocityAverageDevice;
	}

	public void setLineVelocityAverageDevice(DeviceStruct lineVelocityAverageDevice) {
		this.lineVelocityAverageDevice = lineVelocityAverageDevice;
	}

	public CoapClient getCopClientLineVelocityAverage() {
		return copClientLineVelocityAverage;
	}

	public void setCopClientLineVelocityAverage(CoapClient copClientLineVelocityAverage) {
		this.copClientLineVelocityAverage = copClientLineVelocityAverage;
	}

	public LineVelocityAverage getLineVelocityAverage() {
		return lineVelocityAverage;
	}

	public void setLineVelocityAverage(LineVelocityAverage lineVelocityAverage) {
		this.lineVelocityAverage = lineVelocityAverage;
	}

	
	public void clientBind() {
		
		String uriEnergyAverage = "coap://localhost:" + this.energyAverageDevice.devicePort + "/" + this.energyAverageDevice.deviceName;
		
		System.out.println("URI EnergyAverage: " + uriEnergyAverage);
        
		this.coapClientEnergyAverage = new CoapClient(uriEnergyAverage);
		
        String uriPlcAverage = "coap://localhost:" + this.plcAverageDevice.devicePort + "/" + this.plcAverageDevice.deviceName;
		
		System.out.println("URI PlcAverage: " + uriPlcAverage);
        
		this.coapClientPlcAverage = new CoapClient(uriPlcAverage);
		
        String uriLineVelocityAverage = "coap://localhost:" + this.lineVelocityAverageDevice.devicePort + "/" + this.lineVelocityAverageDevice.deviceName;
		
		System.out.println("URI LineVelocityAverage: " + uriLineVelocityAverage);
		
		this.copClientLineVelocityAverage = new CoapClient(uriLineVelocityAverage);
	}
	
	public void readEnergyAverage() {
		
		System.out.println("Chiamato readEnergyAverage");
		
		// GET moniline energy average
		Request request = new Request(Code.GET);
					
		CoapResponse coapResponseEnergyAverage = this.coapClientEnergyAverage.advanced(request);
					
		Gson gsonEnergyAverage = new Gson();
		
		System.out.println("Valore oggetto energia lettura: " + coapResponseEnergyAverage.getResponseText());
		
		this.setEnergyAverage(gsonEnergyAverage.fromJson(coapResponseEnergyAverage.getResponseText(), EnergyAverage.class));
	
	}
	
    @SuppressWarnings("unchecked")
	public void readPlcAverage() {
    	
    	System.out.println("Chiamato readPLCAverage");
		
		// GET moniline PLC average state
		Request request = new Request(Code.GET);
					
		CoapResponse coapResponseGetPlcAverage = this.coapClientPlcAverage.advanced(request);
					
		Gson gsonPlcAverage = new Gson();
		
		this.setPlcAverage((List<PlcAverage>) gsonPlcAverage.fromJson(coapResponseGetPlcAverage.getResponseText(), new TypeToken<List<PlcAverage>>(){}.getType()));
	
	}
    
    public void readLineVelocityAverage() {
		
		System.out.println("Chiamato readLineVelocityAverage");
		
		// GET moniline line velocity average
		Request request = new Request(Code.GET);
					
		CoapResponse coapResponseLineVelocityAverage = this.copClientLineVelocityAverage.advanced(request);
					
		Gson gsonLineVelocityAverage = new Gson();
		
		this.setLineVelocityAverage(gsonLineVelocityAverage.fromJson(coapResponseLineVelocityAverage.getResponseText(), LineVelocityAverage.class));
	
	}
    
}
