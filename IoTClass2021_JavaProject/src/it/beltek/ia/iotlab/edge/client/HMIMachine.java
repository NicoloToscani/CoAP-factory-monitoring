package it.beltek.ia.iotlab.edge.client;

import java.util.ArrayList;

public class HMIMachine {
	
	private DeviceStruct energyDevice;
	private DeviceStruct plcDevice;
	private DeviceStruct dischargeDevice;
	private ArrayList<DeviceStruct> driveDevices;
	private ArrayList<DeviceStruct> sensorDevices;
	
	public HMIMachine() {
		
		this.energyDevice = new DeviceStruct();
		
		this.plcDevice = new DeviceStruct();
		
		this.dischargeDevice = new DeviceStruct();
		
		this.driveDevices = new ArrayList<>();
		
		this.sensorDevices = new ArrayList<>();
			
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
	
}
