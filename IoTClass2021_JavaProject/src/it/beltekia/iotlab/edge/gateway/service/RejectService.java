package it.beltekia.iotlab.edge.gateway.service;

import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.Device;
import it.beltek.ia.iotlab.edge.gateway.device.WeightSystem;

public class RejectService implements Device {
	
	private String IPAddress;
	private int Port;
	private int UnitId;
	private ConnectionState connectionState;

	private WeightSystem weightSystem;

	@Override
	public void Connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean IsAlive() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
    public void Simulate() {
		
		System.out.println("Simulation");
		
		
		
    }

}
