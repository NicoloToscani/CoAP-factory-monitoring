package it.beltek.ia.iotlab.edge.gateway.device.components;

import java.time.LocalDateTime;

public class Alarm {
	
	int value;

	int id;
	
	String description;
	
	String timestamp;
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


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
	

	/**
     * Two alarms are equal if their Id are same.
     */
   @Override
   public boolean equals(Object obj) {

	   Alarm alarm = (Alarm)obj;
	   
	   return (this.id == alarm.id);
	   
    }

}
