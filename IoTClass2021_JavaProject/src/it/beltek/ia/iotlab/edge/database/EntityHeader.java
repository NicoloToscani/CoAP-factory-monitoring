package it.beltek.ia.iotlab.edge.database;

public class EntityHeader {
	
	private String deviceType;
	private int lineID;
	private int machineID;
	private int deviceID; // Drive and Motor
	
	public EntityHeader(String deviceType, int lineID, int machineID, int deviceID) {
		
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

}
