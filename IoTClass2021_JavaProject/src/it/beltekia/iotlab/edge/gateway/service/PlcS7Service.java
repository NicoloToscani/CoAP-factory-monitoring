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
	
	private int deviceAlarms = 10;

	
	public PlcS7Service(String IPAddress, int Rack, int Slot) {
		
		this.s7Client = new S7Client();
		
		this.IPAddress = IPAddress;
		
		this.Rack = Rack;
		
		this.Slot = Slot;
		
		this.siemensPLC = new PLC();
		
		this.connectionState = ConnectionState.Offline;
		
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
		
		this.siemensPLC.alarmPresence = S7.GetBitAt(buffer, 2, 0);
		
		this.siemensPLC.alarms = new boolean[deviceAlarms];
		
		// Alarms
		this.siemensPLC.alarms[0] = S7.GetBitAt(buffer, 4, 0);
		this.siemensPLC.alarms[1] = S7.GetBitAt(buffer, 4, 1);
		this.siemensPLC.alarms[2] = S7.GetBitAt(buffer, 4, 2);
		this.siemensPLC.alarms[3] = S7.GetBitAt(buffer, 4, 3);
		this.siemensPLC.alarms[4] = S7.GetBitAt(buffer, 4, 4);
		this.siemensPLC.alarms[5] = S7.GetBitAt(buffer, 4, 5);
		this.siemensPLC.alarms[6] = S7.GetBitAt(buffer, 4, 6);
		this.siemensPLC.alarms[7] = S7.GetBitAt(buffer, 4, 7);
		this.siemensPLC.alarms[8] = S7.GetBitAt(buffer, 5, 0);
		this.siemensPLC.alarms[9] = S7.GetBitAt(buffer, 5, 1);
	
	}
	
	
	// Write start command
    public void writeData() {
    	
    	byte[] buffer = new byte[2]; 
    	
    	// Start cmd 
    	S7.SetBitAt(buffer, 0, 0, this.siemensPLC.stopCommand);
    	
    	// Stop cmd
    	S7.SetBitAt(buffer, 0, 1, this.siemensPLC.stopCommand);
    	
    	// Reset cmd
    	S7.SetBitAt(buffer, 0, 2, this.siemensPLC.reset);
    	
    	this.s7Client.WriteArea(S7.S7AreaDB, 2, 0, buffer.length, buffer);    	

    	
	}

		
	public ConnectionState getConnectionState() {
		return connectionState;
	}

	public void setConnectionState(ConnectionState connectionState) {
		this.connectionState = connectionState;
	}

	public void setSiemensPLC(PLC siemensPLC) {
		this.siemensPLC = siemensPLC;
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
	
	// Start cmd
	public void Write() {
		
		this.siemensPLC.connectionState = this.connectionState;
		
		writeData();
		
		
	}
			
}
