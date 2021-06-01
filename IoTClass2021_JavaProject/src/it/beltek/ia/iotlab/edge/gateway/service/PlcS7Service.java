/**
 * 
 */
package it.beltek.ia.iotlab.edge.gateway.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.Device;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;

import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;
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
		
		this.connectionState = ConnectionState.Offline;
		
		this.siemensPLC.alarmList = new ArrayList<>();

		
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
		
		byte[] buffer = new byte[6]; 
		
		this.s7Client.ReadArea(S7.S7AreaDB, 1, 0, buffer.length, buffer);
		
		int state = S7.GetShortAt(buffer, 0);
		
		System.out.print("Valore stato: " + state);
		
		if(state == 0) {
			
			this.siemensPLC.state = "STOPPED";
		}
		else if (state == 1) {
			
			this.siemensPLC.state = "SUSPENDED";
			
		}
		
		else if(state == 2) {
			
			this.siemensPLC.state = "EXECUTE";
			
		}
		
		this.siemensPLC.alarmPresence = S7.GetBitAt(buffer, 2, 0);
			
		checkAlarm(buffer);
	
	}
	
	
	// Write start command
    public void writeData() {
    	
    	byte[] buffer = new byte[2]; 
    	
    	// Start command
    	S7.SetBitAt(buffer, 0, 0, this.siemensPLC.startCommand);
    	
    	// Stop command
    	S7.SetBitAt(buffer, 0, 1, this.siemensPLC.stopCommand);
    	
    	// Reset command
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
	
	private void checkAlarm(byte[] buffer) {
		
		//this.siemensPLC.alarmList.clear();
		
		        // Alarm 0: Safeties not restored
				if(S7.GetBitAt(buffer, 4, 0) == true) {
					
					Alarm alarm = new Alarm();
					alarm.setId(0);
					alarm.setValue(1);
					alarm.setDescription("Safeties not restored");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
					
					System.out.println("Alarm_0 active");					
				
				}else if (S7.GetBitAt(buffer, 4, 0) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(0);
					alarm.setValue(0);
					alarm.setDescription("Safeties not restored");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				
				// Alarm 1: Compressed air failure
				if(S7.GetBitAt(buffer, 4, 1) == true) {
							
					Alarm alarm = new Alarm();
					alarm.setId(1);
					alarm.setValue(1);
					alarm.setDescription("Compressed air failure");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
							
					
					System.out.print("Alarm_1 active");
							
				}else if (S7.GetBitAt(buffer, 4, 1) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(1);
					alarm.setValue(0);
					alarm.setDescription("Compressed air failure");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				
				
				// Alarm 2: Inappropriate temperature
				if(S7.GetBitAt(buffer, 4, 2) == true) {
						
					Alarm alarm = new Alarm();
					alarm.setId(2);
					alarm.setValue(1);
					alarm.setDescription("Inappropriate temperature");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
							
					System.out.print("Alarm_2 active");
									
				}else if (S7.GetBitAt(buffer, 4, 2) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(2);
					alarm.setValue(0);
					alarm.setDescription("Inappropriate temperature");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				// Alarm 3: Lack of external consent 
				if(S7.GetBitAt(buffer, 4, 3) == true) {
						
					Alarm alarm = new Alarm();
					alarm.setId(3);
					alarm.setValue(1);
					alarm.setDescription("Lack of external consent");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
											
					
					System.out.print("Alarm_3 active");
											
				}else if (S7.GetBitAt(buffer, 4, 3) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(3);
					alarm.setValue(0);
					alarm.setDescription("Lack of external consent");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				// Alarm 4: Timeout feedback encoder encoder
				if(S7.GetBitAt(buffer, 4, 4) == true) {
						
					Alarm alarm = new Alarm();
					alarm.setId(4);
					alarm.setValue(1);
					alarm.setDescription("Timeout feedback encoder encoder");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
													
											
					System.out.print("Alarm_4 active");
				}else if (S7.GetBitAt(buffer, 4, 4) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(4);
					alarm.setValue(0);
					alarm.setDescription("Timeout feedback encoder encoder");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				// Alarm 5: Timeout feedback sensor
				if(S7.GetBitAt(buffer, 4, 5) == true) {
					
					Alarm alarm = new Alarm();
					alarm.setId(5);
					alarm.setValue(1);
					alarm.setDescription("Timeout feedback sensor");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
															
											
					System.out.print("Alarm_5 active");
					
				}else if (S7.GetBitAt(buffer, 4, 5) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(5);
					alarm.setValue(0);
					alarm.setDescription("Timeout feedback sensor");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				// Alarm 6:  Inappropriate weight
				if(S7.GetBitAt(buffer, 4, 6) == true) {
					
					Alarm alarm = new Alarm();
					alarm.setId(6);
					alarm.setValue(1);
					alarm.setDescription("Inappropriate weight");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
																	
											
					System.out.print("Alarm_6 active");
				}else if (S7.GetBitAt(buffer, 4, 6) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(6);
					alarm.setValue(0);
					alarm.setDescription("Inappropriate weight");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				// Alarm 7: Labelling machine warning
				if(S7.GetBitAt(buffer, 4, 7) == true) {
					
					Alarm alarm = new Alarm();
					alarm.setId(7);
					alarm.setValue(1);
					alarm.setDescription("Labelling machine warning");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
																			
											
					System.out.print("Alarm_7 active");
				}else if (S7.GetBitAt(buffer, 4, 7) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(7);
					alarm.setValue(0);
					alarm.setDescription("Labelling machine warning");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				// Alarm 8: Pallet missing
				if(S7.GetBitAt(buffer, 5, 0) == true) {
						
					Alarm alarm = new Alarm();
					alarm.setId(8);
					alarm.setValue(1);
					alarm.setDescription("Pallet missing");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
																					
											
					System.out.print("Alarm_8 active");
				}else if (S7.GetBitAt(buffer, 5, 0) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(8);
					alarm.setValue(0);
					alarm.setDescription("Pallet missing");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
				
				// Alarm 9: Maintenance request
				if(S7.GetBitAt(buffer, 5, 1) == true) {
						
					Alarm alarm = new Alarm();
					alarm.setId(9);
					alarm.setValue(1);
					alarm.setDescription("Maintenance rquired");
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					currentDateTime.format(formatter);
					
					String timestamp = currentDateTime.toString();
					alarm.setTimestamp(timestamp);
					
					if(!this.siemensPLC.alarmList.contains(alarm)) {
					
						this.siemensPLC.alarmList.add(alarm);
					
					}
																							
											
					System.out.print("Alarm_9 active");
				}else if (S7.GetBitAt(buffer, 5, 1) == false) {
					
					Alarm alarm = new Alarm();
					alarm.setId(9);
					alarm.setValue(0);
					alarm.setDescription("Maintenance required");
					
					if(this.siemensPLC.alarmList.contains(alarm) == true) {
					
					this.siemensPLC.alarmList.remove(alarm);
					
					}
					
				}
		
	}
			
}
