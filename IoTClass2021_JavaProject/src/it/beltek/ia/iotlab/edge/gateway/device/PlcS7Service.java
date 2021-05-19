/**
 * 
 */
package it.beltek.ia.iotlab.edge.gateway.device;

import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.Device;
import it.beltek.ia.iotlab.edge.gateway.device.driver.s7.S7;
import it.beltek.ia.iotlab.edge.gateway.device.driver.s7.S7Client;

/**
 * The class {@code S7Service} manages communication with a device that implement Siemens S7 protocol. 
 * 
 * @author Nicolò Toscani
 * @version 1.0
 *
 */
public class PlcS7Service implements Device {
	
	private S7Client s7Client;
	
	private String IPAddress;
	private int Rack;
	private int Slot;
	private ConnectionState connectionState;

	
	public PlcS7Service(String IPAddress, int Rack, int Slot) {
		
		this.s7Client = new S7Client();
		
		this.IPAddress = IPAddress;
		
		this.Rack = Rack;
		
		this.Slot = Slot;
		
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public int getRack() {
		return Rack;
	}


	public void setRack(int rack) {
		Rack = rack;
	}


	public int getSlot() {
		return Slot;
	}

	public void setSlot(int slot) {
		Slot = slot;
	}
	
	public byte[] readData() {
		
		byte[] buffer = new byte[5]; 
		
		this.s7Client.ReadArea(S7.S7AreaDB, 1, 0, buffer.length, buffer);
		
		return buffer;
	}

		
	@Override
	public void Connect() {
		
		this.connectionState = ConnectionState.Connecting;
		
		try {
			
		this.s7Client.ConnectTo(IPAddress, Rack, Slot);
		
		if(this.s7Client.Connected == true) {
			
			this.connectionState = ConnectionState.Online;
			
			System.out.println("Online");
			
		}
		
		else {
			
			this.connectionState = ConnectionState.Offline;
			
			System.out.println("Offline");
			
		}
		
		}catch(Exception e) {
			
			this.connectionState = ConnectionState.Offline;
			
			e.printStackTrace();
			
			System.out.println("Disconnesso");
			
		}
		
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

	

}
