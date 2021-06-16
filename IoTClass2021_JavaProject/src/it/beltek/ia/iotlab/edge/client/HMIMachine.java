package it.beltek.ia.iotlab.edge.client;

import java.util.ArrayList;
import java.util.Iterator;

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

public class HMIMachine {
	
	private DeviceStruct energyDevice;
	private DeviceStruct plcDevice;
	private DeviceStruct dischargeDevice;
	private ArrayList<DeviceStruct> driveDevices;
	private ArrayList<DeviceStruct> sensorDevices;
	
	// Coap clients
	private CoapClient coapClientEnergy;
	private CoapClient coapClientPlc;
	private CoapClient coapClientReject;
	private CoapClient coapClientVibration;
	private ArrayList<CoapClient> coapCLientDrives;
	private ArrayList<CoapClient> coapCLientVibrations;
	
	// Devices
	private PLC plc;
	private SchneiderPM3200 pm3200;
	private WeightSystem weightSystem;
	private ArrayList<Drive> drives;
	private ArrayList<BannerQm42vt2> sensors;
	
	 
	
	
	public HMIMachine() {
		
		this.energyDevice = new DeviceStruct();
		
		this.plcDevice = new DeviceStruct();
		
		this.dischargeDevice = new DeviceStruct();
		
		this.driveDevices = new ArrayList<>();
		
		this.sensorDevices = new ArrayList<>();
		
		this.plc = new PLC();
		
		this.weightSystem = new WeightSystem();
		
		this.pm3200 = new SchneiderPM3200();
		
		this.drives = new ArrayList<>();
		
		this.sensors = new ArrayList<>();
	}
	
	public DeviceStruct getEnergyDevice() {
		return energyDevice;
	}

	public DeviceStruct getPlcDevice() {
		return plcDevice;
	}

	public DeviceStruct getDischargeDevice() {
		return dischargeDevice;
	}

	public ArrayList<DeviceStruct> getDriveDevices() {
		return driveDevices;
	}

	public ArrayList<DeviceStruct> getSensorDevices() {
		return sensorDevices;
	}

	public void setEnergyDevice(DeviceStruct energyDevice) {
		this.energyDevice = energyDevice;
	}

	public void setPlcDevice(DeviceStruct plcDevice) {
		this.plcDevice = plcDevice;
	}

	public void setDischargeDevice(DeviceStruct dischargeDevice) {
		this.dischargeDevice = dischargeDevice;
	}

	public void setDriveDevices(ArrayList<DeviceStruct> driveDevices) {
		this.driveDevices = driveDevices;
	}

	public void setSensorDevices(ArrayList<DeviceStruct> sensorDevices) {
		this.sensorDevices = sensorDevices;
	}
	
	public CoapClient getCoapClientEnergy() {
		return coapClientEnergy;
	}

	public CoapClient getCoapClientPlc() {
		return coapClientPlc;
	}

	public CoapClient getCoapClientReject() {
		return coapClientReject;
	}

	public CoapClient getCoapClientVibration() {
		return coapClientVibration;
	}

	public ArrayList<CoapClient> getCoapCLientDrives() {
		return coapCLientDrives;
	}

	public ArrayList<CoapClient> getCoapCLientVibrations() {
		return coapCLientVibrations;
	}

	public PLC getPlc() {
		return plc;
	}

	public SchneiderPM3200 getPm3200() {
		return pm3200;
	}

	public WeightSystem getWeightSystem() {
		return weightSystem;
	}

	public ArrayList<Drive> getDrives() {
		return drives;
	}

	public ArrayList<BannerQm42vt2> getSensors() {
		return sensors;
	}

	public void setCoapClientEnergy(CoapClient coapClientEnergy) {
		this.coapClientEnergy = coapClientEnergy;
	}

	public void setCoapClientPlc(CoapClient coapClientPlc) {
		this.coapClientPlc = coapClientPlc;
	}

	public void setCoapClientReject(CoapClient coapClientReject) {
		this.coapClientReject = coapClientReject;
	}

	public void setCoapClientVibration(CoapClient coapClientVibration) {
		this.coapClientVibration = coapClientVibration;
	}

	public void setCoapCLientDrives(ArrayList<CoapClient> coapCLientDrives) {
		this.coapCLientDrives = coapCLientDrives;
	}

	public void setCoapCLientVibrations(ArrayList<CoapClient> coapCLientVibrations) {
		this.coapCLientVibrations = coapCLientVibrations;
	}

	public void setPlc(PLC plc) {
		this.plc = plc;
	}

	public void setPm3200(SchneiderPM3200 pm3200) {
		this.pm3200 = pm3200;
	}

	public void setWeightSystem(WeightSystem weightSystem) {
		this.weightSystem = weightSystem;
	}

	public void setDrives(ArrayList<Drive> drives) {
		this.drives = drives;
	}

	public void setSensors(ArrayList<BannerQm42vt2> sensors) {
		this.sensors = sensors;
	}
	
	
	public void clientBind() {
		
		String uriPlc = "coap://localhost:" + this.plcDevice.devicePort + "/" + this.plcDevice.deviceName;
		
		System.out.println("URI PLC: " + uriPlc);
        
		this.coapClientPlc = new CoapClient(uriPlc);
		
		String uriEnergy = "coap://localhost:" + this.energyDevice.devicePort + "/" + this.energyDevice.deviceName;
		
		this.coapClientEnergy = new CoapClient(uriEnergy);
		
		String uriReject = "coap://localhost:" + this.dischargeDevice.devicePort + "/" + this.dischargeDevice.deviceName;
		
		this.coapClientReject = new CoapClient(uriReject);
		
		// Drives
		this.coapCLientDrives = new ArrayList<>();
		
		Iterator<DeviceStruct> driveIter = driveDevices.iterator();
		
		while(driveIter.hasNext()) {
			
			DeviceStruct drive = driveIter.next();
			
			String uriDrive = "coap://localhost:" + drive.devicePort + "/" + drive.deviceName;
			
			CoapClient driveClient = new CoapClient(uriDrive);
			
			coapCLientDrives.add(driveClient);
		}
		
		// Vibration sensors
		this.coapCLientVibrations = new ArrayList<>();
		
		Iterator<DeviceStruct> sensorIter = sensorDevices.iterator();
		
        while(sensorIter.hasNext()) {
			
			DeviceStruct sensor = sensorIter.next();
			
			String uriSensor = "coap://localhost:" + sensor.devicePort + "/" + sensor.deviceName;
			
			CoapClient sensorClient = new CoapClient(uriSensor);
			
			coapCLientVibrations.add(sensorClient);
		} 
		
	}
	
	public void readPLC() {
		
		// GET PLC
		Request request = new Request(Code.GET);
					
		CoapResponse coapResponseGetPlc = this.coapClientPlc.advanced(request);
					
		Gson gsonPlc = new Gson();
		
		this.setPlc(gsonPlc.fromJson(coapResponseGetPlc.getResponseText(), PLC.class));
		
		System.out.println("Read: " + this.getPlc().state);
	
	}
	
    public void readEnergy() {
		
		// GET Energy
		Request request = new Request(Code.GET);
					
		CoapResponse coapResponseGetEnergy = this.coapClientEnergy.advanced(request);
					
		Gson gsonPlc = new Gson();
		
		this.setPm3200(gsonPlc.fromJson(coapResponseGetEnergy.getResponseText(), SchneiderPM3200.class));
	
	}
    
    public void readReject() {
		
		// GET Reject
		Request request = new Request(Code.GET);
					
		CoapResponse coapResponseGetReject = this.coapClientReject.advanced(request);
					
		Gson gsonReject = new Gson();
		
		this.setWeightSystem(gsonReject.fromJson(coapResponseGetReject.getResponseText(), WeightSystem.class));
		
		// System.out.println("Risposta readReject: " + coapResponseGetReject.getResponseText());
	
	}
    
    
    public void readDrives() {
    	
    	Iterator<CoapClient> coapIteratorDrive = coapCLientDrives.iterator();
    		
    	ArrayList<Drive> drives = new ArrayList<>();
    	
    	while(coapIteratorDrive.hasNext()) {
    		
    		CoapClient drive = coapIteratorDrive.next();
    		
    		Request request = new Request(Code.GET);
			
    		CoapResponse coapResponseGetDrive = drive.advanced(request);
    					
    		Gson gsonDrive = new Gson();
    		
    		Drive driveSer = gsonDrive.fromJson(coapResponseGetDrive.getResponseText(), Drive.class);   		
    		
    		drives.add(driveSer);
    		
    	}
    	
    	this.setDrives(drives);
    }
    
    public void readVibrations() {
    	
    	Iterator<CoapClient> coapIteratorSensor = coapCLientVibrations.iterator();
    		
    	ArrayList<BannerQm42vt2> sensors = new ArrayList<>();
    	
    	while(coapIteratorSensor.hasNext()) {
    		
    		CoapClient sensor = coapIteratorSensor.next();
    		
    		Request request = new Request(Code.GET);
			
    		CoapResponse coapResponseGetSensor = sensor.advanced(request);
    					
    		Gson gsonSensor = new Gson();
    		
    		BannerQm42vt2 sensorSer = gsonSensor.fromJson(coapResponseGetSensor.getResponseText(), BannerQm42vt2.class);   		
    		
    		sensors.add(sensorSer);
    		
    	}
    	
    	this.setSensors(sensors);
    }
    
    public void writeVelocity(String velocitySetpoint, int driveID) {
    	
    	JsonRequest frequencyRequest = new JsonRequest();
    	
    	// PUT drive velocity
	    frequencyRequest.setField("frequencySetpoint");
	    frequencyRequest.setValue(velocitySetpoint);
	    
	    int index = (driveID % 10) - 1;
	    
	    Gson gsonDrive = new Gson();
	    String driveSerialize = gsonDrive.toJson(frequencyRequest);
	    
	    CoapResponse coapResponseDrivePost = this.getCoapCLientDrives().get(index).post(driveSerialize, MediaTypeRegistry.APPLICATION_JSON);
	    	
    }
    
    
    public void writePlc(String commandType) {
    	
    	// Command reset
    	this.plc.reset = false;
		this.plc.startCommand = false;
		this.plc.stopCommand = false;
    	
    	
    	// START machine command
    	if(commandType.equals("0")) {
    		
			this.plc.reset = false;
			this.plc.startCommand = true;
			this.plc.stopCommand = false;
			
			Gson gson = new Gson();
			String plcSerialize = gson.toJson(plc);
			
			CoapResponse coapResponsePost = this.coapClientPlc.post(plcSerialize, MediaTypeRegistry.APPLICATION_JSON);
    		
    	}
    	
    	// STOP machine command
    	if(commandType.equals("1")) {
    		
			this.plc.reset = false;
			this.plc.startCommand = false;
			this.plc.stopCommand = true;
			
			Gson gson = new Gson();
			String plcSerialize = gson.toJson(plc);
			
			CoapResponse coapResponsePost = this.coapClientPlc.post(plcSerialize, MediaTypeRegistry.APPLICATION_JSON);
    		
    	}
    	
    	// STOP machine command
    	if(commandType.equals("2")) {
    		
			this.plc.reset = true;
			this.plc.startCommand = false;
			this.plc.stopCommand = false;
			
			Gson gson = new Gson();
			String plcSerialize = gson.toJson(plc);
			
			CoapResponse coapResponsePost = this.coapClientPlc.post(plcSerialize, MediaTypeRegistry.APPLICATION_JSON);
    		
    	}
    	
    	
    }
    
}
