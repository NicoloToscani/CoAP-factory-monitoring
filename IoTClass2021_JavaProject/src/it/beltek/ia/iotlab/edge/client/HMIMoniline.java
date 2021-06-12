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

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.WeightSystem;
import it.beltek.ia.iotlab.edge.gateway.device.components.JsonRequest;
import it.beltek.ia.iotlab.edge.gateway.moniline.resource.EnergyAverage;
import it.beltek.ia.iotlab.edge.gateway.moniline.resource.PlcAverage;

public class HMIMoniline {
	
	private DeviceStruct energyAverageDevice;
	private DeviceStruct plcAverageDevice;
	private DeviceStruct monilineDevice;

	// Coap clients
	private CoapClient coapClientEnergyAverage;
	private CoapClient coapClientPlcAverage;
	
	// Devices
	private EnergyAverage energyAverage;
	private String plcAverage;

	
	public HMIMoniline() {
		
		this.energyAverageDevice = new DeviceStruct();
		
		this.plcAverageDevice = new DeviceStruct();
		
		this.energyAverage = new EnergyAverage();
				
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


	public String getPlcAverage() {
		return plcAverage;
	}

	public void setEnergyAverage(EnergyAverage energyAverage) {
		this.energyAverage = energyAverage;
	}

	public void setPlcAverage(String plcAverage) {
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

	
	public void clientBind() {
		
		String uriEnergyAverage = "coap://localhost:" + this.energyAverageDevice.devicePort + "/" + this.energyAverageDevice.deviceName;
		
		System.out.println("URI EnergyAverage: " + uriEnergyAverage);
        
		this.coapClientEnergyAverage = new CoapClient(uriEnergyAverage);
		
        String uriPlcAverage = "coap://localhost:" + this.plcAverageDevice.devicePort + "/" + this.plcAverageDevice.deviceName;
		
		System.out.println("URI PlcAverage: " + uriPlcAverage);
        
		this.coapClientPlcAverage = new CoapClient(uriPlcAverage);
		
      
	}
	
	public void readEnergyAverage() {
		
		System.out.println("Chiamato readEnergyAverage");
		
		// GET moniline energy average
		Request request = new Request(Code.GET);
					
		CoapResponse coapResponseEnergyAverage = this.coapClientEnergyAverage.advanced(request);
					
		Gson gsonEnergyAverage = new Gson();
		
		this.setEnergyAverage(gsonEnergyAverage.fromJson(coapResponseEnergyAverage.getResponseText(), EnergyAverage.class));
	
	}
	
    public void readPlcAverage() {
    	
    	System.out.println("Chiamato readPLCAverage");
		
		// GET moniline PLC average state
		Request request = new Request(Code.GET);
					
		CoapResponse coapResponseGetPlcAverage = this.coapClientPlcAverage.advanced(request);
					
		Gson gsonPlcAverage = new Gson();
		
		this.setPlcAverage(gsonPlcAverage.fromJson(coapResponseGetPlcAverage.getResponseText(), String.class));
	
	}
    
}
