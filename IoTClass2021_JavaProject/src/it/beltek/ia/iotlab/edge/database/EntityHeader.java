package it.beltek.ia.iotlab.edge.database;

public class EntityHeader {
	
	private String deviceType;
	private int lineID;
	private int machineID;
	private int deviceID; // Drive and Motor
	private int coapPortNumber;
	private String coapIpAddress; // If we run in a real factory environment
	
	public EntityHeader(int CoapPortNumber, String deviceType, int lineID, int machineID, int deviceID) {
		
		this.coapPortNumber = CoapPortNumber;
		
		this.deviceType = deviceType;
		
		this.lineID = lineID;
		
		this.machineID = machineID;
		
		this.deviceID = deviceID;
		
	}
	
	public String getDeviceType() {
		return deviceType;
	}

	public int getLineID() {
		return lineID;
	}

	public int getMachineID() {
		return machineID;
	}

	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void setLineID(int lineID) {
		this.lineID = lineID;
	}

	public void setMachineID(int machineID) {
		this.machineID = machineID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}
	
	public int getCoapPortNumber() {
		return coapPortNumber;
	}

	public void setCoapPortNumber(int coapPortNumber) {
		this.coapPortNumber = coapPortNumber;
	}

}
