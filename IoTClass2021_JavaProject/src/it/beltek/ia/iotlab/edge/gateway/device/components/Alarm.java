package it.beltek.ia.iotlab.edge.gateway.device.components;

public class Alarm {
	
	int value;

	int id;
	
	String description;
	
	public int getValue() {
		return value;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
