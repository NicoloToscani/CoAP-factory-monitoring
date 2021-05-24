/**
 * 
 */
package it.beltekia.iotlab.edge.gateway.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.re.easymodbus.modbusclient.ModbusClient;
import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.Device;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
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
	
	private PLC siemensPLC;

	
	public PlcS7Service(String IPAddress, int Rack, int Slot) {
		
		this.s7Client = new S7Client();
		
		this.IPAddress = IPAddress;
		
		this.Rack = Rack;
		
		this.Slot = Slot;
		
		this.siemensPLC = new PLC();
		
	}

	public PLC getSiemensPLC() {
		return siemensPLC;
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
	
	public void readData() {
		
		byte[] buffer = new byte[8]; 
		
		this.s7Client.ReadArea(S7.S7AreaDB, 1, 0, buffer.length, buffer);
		
		this.siemensPLC.state = S7.GetShortAt(buffer, 0);
		
	}

		
	@Override
	public void Connect() {
		
		this.connectionState = ConnectionState.Connecting;
		
		try {
			
		this.s7Client.ConnectTo(IPAddress, Rack, Slot);
		
		if(this.s7Client.Connected == true) {
			
			this.connectionState = ConnectionState.Online;
			
			System.out.println("PLC: " + this.connectionState + " IPAddress: " + this.IPAddress);
			
		}
		
		else {
			
			this.connectionState = ConnectionState.Offline;
			
			System.out.println("PLC: " + this.connectionState + " IPAddress: " + this.IPAddress);
			
		}
		
		}catch(Exception e) {
			
			this.connectionState = ConnectionState.Offline;
			
			e.printStackTrace();
			
			System.out.println("PLC: " + this.connectionState + " IPAddress: " + this.IPAddress);
			
		}
		
	}

	@Override
	public void Disconnect() {
		
		this.s7Client.Disconnect();
		
		if(this.s7Client.Connected == false) {
			
            this.connectionState = ConnectionState.Offline;
			
			System.out.println("PLC: " + this.connectionState + " IPAddress: " + this.IPAddress);
			
		}
		
	}

	@Override
	public boolean IsAlive() {
		// TODO Auto-generated method stub
		return false;
	}
	
	// Read values
	public void Read() {
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.siemensPLC.timestamp = currentDateTime.format(formatter);
				
		this.siemensPLC.connectionState = this.connectionState;
				
		readData();
				
		}
			
			
			
	

}
