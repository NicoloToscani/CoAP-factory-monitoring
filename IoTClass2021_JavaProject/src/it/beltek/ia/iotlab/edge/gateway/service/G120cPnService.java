/**
 * 
 */
package it.beltek.ia.iotlab.edge.gateway.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.Device;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.driver.s7.S7;
import it.beltek.ia.iotlab.edge.gateway.device.driver.s7.S7Client;

/**
 * The class {@code G120cPnService} manages communication with a drive trough PLC that implement Siemens S7 protocol. 
 * 
 * @author Nicolò Toscani
 * @version 1.0
 *
 */
public class G120cPnService implements Device {
	
	private S7Client s7Client;
	
	private String IPAddress;
	private int Rack;
	private int Slot;
	private ConnectionState connectionState;
	
	private Drive drive;
	
	private int ID;
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public G120cPnService(String IPAddress, int Rack, int Slot, int driveID) {
		
		this.s7Client = new S7Client();
		
		this.IPAddress = IPAddress;
		
		this.Rack = Rack;
		
		this.Slot = Slot;
		
		this.drive = new Drive();
		
		this.connectionState = ConnectionState.Offline;
		
		this.ID = driveID;
		
	}

	public Drive getDrive() {
		
		return drive;
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
		
		byte[] buffer = new byte[34]; 
		
		this.s7Client.ReadArea(S7.S7AreaDB, this.ID, 0, buffer.length, buffer);
		
		this.drive.speedDeviationInTol = S7.GetBitAt(buffer, 12, 0);
		this.drive.masterControlRequest = S7.GetBitAt(buffer, 12, 1);
		this.drive.compSpeedReached = S7.GetBitAt(buffer, 12, 2);
		this.drive.impLimitReached = S7.GetBitAt(buffer, 12, 3);
		this.drive.holdingBrakeOpen = S7.GetBitAt(buffer, 12, 4);
		this.drive.alarmMotorOvertemp = S7.GetBitAt(buffer, 12, 5);
		this.drive.motorRotateClockwise = S7.GetBitAt(buffer, 12, 6);
		this.drive.alarmInverterThermalOverload = S7.GetBitAt(buffer, 12, 7);
		this.drive.readyToStart = S7.GetBitAt(buffer, 13, 0);
		this.drive.ready = S7.GetBitAt(buffer, 13, 1);
		this.drive.operationEnabled = S7.GetBitAt(buffer, 13, 2);
		this.drive.faultActive = S7.GetBitAt(buffer, 13, 3);
		this.drive.off2Inactive = S7.GetBitAt(buffer, 13, 4);
		this.drive.off3Inactive = S7.GetBitAt(buffer, 13, 5);
		this.drive.closingLookoutActive = S7.GetBitAt(buffer, 13, 6);
		this.drive.alarmActive = S7.GetBitAt(buffer, 13, 7);
		
		this.drive.actualSpeed = S7.GetWordAt(buffer, 24);
		this.drive.actualCurrent = S7.GetWordAt(buffer, 26);
		this.drive.actualTorque = S7.GetWordAt(buffer, 28);
		this.drive.warnCode = S7.GetWordAt(buffer, 30);
		this.drive.faultCode = S7.GetWordAt(buffer, 32);
		
		System.out.println("Lettura eseguita");
	
	
	}
	
	
	// Write start command
    public void writeData() {
    	
    	byte[] buffer = new byte[4]; 
    	
    	// Setpoint command frequency Hz 
    	S7.SetFloatAt(buffer, 0, this.drive.setpoint);
    	
    	this.s7Client.WriteArea(S7.S7AreaDB, this.ID, 34, buffer.length, buffer);    
    	
    	System.out.println("Scrittura eseguita");

    	
	}

		
	public ConnectionState getConnectionState() {
		return connectionState;
	}

	public void setConnectionState(ConnectionState connectionState) {
		this.connectionState = connectionState;
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
		this.drive.timestamp = currentDateTime.format(formatter);
				
		this.drive.connectionState = this.connectionState;
				
		readData();
				
		}
	
	// Start cmd
	public void Write() {
		
		this.drive.connectionState = this.connectionState;
		
		writeData();
		
		
	}
			
}
