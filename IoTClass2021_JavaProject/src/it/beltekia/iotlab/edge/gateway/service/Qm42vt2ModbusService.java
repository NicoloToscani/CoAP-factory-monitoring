package it.beltekia.iotlab.edge.gateway.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import de.re.easymodbus.modbusclient.ModbusClient;
import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.Device;
import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

/**
 * The class {@code Qm42vt2ModbusService} manages communication with a device that implement Modbus TCP-IP protocol. 
 * 
 * @author Nicolò Toscani
 * @version 1.0
 *
 */
public class Qm42vt2ModbusService implements Device {
	
    private ModbusClient modbusClient;
	
	private String IPAddress;
	private int Port;
	private int UnitId;
	private ConnectionState connectionState;

	private BannerQm42vt2 bannerQm42vt2 = new BannerQm42vt2();
	
	private int[] readValues;
	
    public Qm42vt2ModbusService(String IPAddress, int Port) {
		
		this.modbusClient = new ModbusClient();
		
		this.IPAddress = IPAddress;
		
		this.Port = Port;
		
		this.UnitId = UnitId; // if we use one gateway with Modbus RTU devices
		
		this.connectionState = ConnectionState.Offline;
		
		initMeasures();
		
	}

    @Override
	public void Connect() {
		
		this.connectionState = ConnectionState.Connecting;
		
		try {
			
			this.modbusClient.Connect(IPAddress, Port);
			
			if(this.modbusClient.isConnected() == true) {
				
				this.connectionState = ConnectionState.Online;
				
				System.out.println("Qm42vt2: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
			}
			
			else {
				
				this.connectionState = ConnectionState.Offline;
				
				System.out.println("Qm42vt2: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
				
			}
			
		
		} catch (Exception e) {
			
           this.connectionState = ConnectionState.Offline;
			
			System.out.println("Qm42vt2: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
			
			e.printStackTrace();
		}
		
	}

	@Override
	public void Disconnect() {
		
		try {
			
			this.modbusClient.Disconnect();
			
			if(! (this.modbusClient.isConnected())) {
				
				this.connectionState = ConnectionState.Offline;
				
				System.out.println("Qm42vt2: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

	@Override
	public boolean IsAlive() {
		// TODO Ping device for connection management
		return false;
	}
	
	// Read values
		public void Read() {
			
			LocalDateTime currentDateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			this.bannerQm42vt2.timestamp = currentDateTime.format(formatter);
			
			this.bannerQm42vt2.connectionState = this.connectionState;
			
			readHoldingRegisters_1();
			
		}
		
		// Read Modbus registers in a single packet
		private void readHoldingRegisters_1() {
			
			try {
			    // Leggerli tutti in un colpo solo
				readValues = this.modbusClient.ReadHoldingRegisters(2999, 12);
				
			
			} catch (Exception e) {
				
				e.printStackTrace();
			} finally {
				
				int[] value_1 = new int[2];
				value_1[0] = readValues[0];
				value_1[1] = readValues[1];
				this.bannerQm42vt2.Z_Axis_RMS_Velocity_In_Sec = ModbusClient.ConvertRegistersToFloat(value_1);
				
				int[] value_2 = new int[2];
				value_2[0] = readValues[2];
				value_2[1] = readValues[3];
				this.bannerQm42vt2.Z_Axis_RMS_Velocity_Mm_Sec = ModbusClient.ConvertRegistersToFloat(value_2);
				
				int[] value_3 = new int[2];
				value_3[0] = readValues[4];
				value_3[1] = readValues[5];
				this.bannerQm42vt2.Temperature_F = ModbusClient.ConvertRegistersToFloat(value_3);
				
				int[] value_4 = new int[2];
				value_4[0] = readValues[6];
				value_4[1] = readValues[7];
				this.bannerQm42vt2.Temperature_C = ModbusClient.ConvertRegistersToFloat(value_4);
				
				int[] value_5 = new int[2];
				value_5[0] = readValues[8];
				value_5[1] = readValues[9];
				this.bannerQm42vt2.X_Axis_RMS_Velocity_In_Sec = ModbusClient.ConvertRegistersToFloat(value_5);
				
				int[] value_6 = new int[2];
				value_6[0] = readValues[10];
				value_6[1] = readValues[11];
				this.bannerQm42vt2.X_Axis_RMS_Velocity_Mn_Sec = ModbusClient.ConvertRegistersToFloat(value_6);
				
				int[] value_7 = new int[2];
				value_7[0] = readValues[12];
				value_7[1] = readValues[13];
				this.bannerQm42vt2.Z_Axis_Peak_Acceleration_G = ModbusClient.ConvertRegistersToFloat(value_7);
				
				int[] value_8 = new int[2];
				value_8[0] = readValues[14];
				value_8[1] = readValues[15];
				this.bannerQm42vt2.X_Axis_Peak_Acceleration_G = ModbusClient.ConvertRegistersToFloat(value_8);
				
				int[] value_9 = new int[2];
				value_9[0] = readValues[16];
				value_9[1] = readValues[17];
				this.bannerQm42vt2.Z_Axis_Peak_Velocity_Component_Frequency = ModbusClient.ConvertRegistersToFloat(value_9);
				
				int[] value_10 = new int[2];
				value_10[0] = readValues[18];
				value_10[1] = readValues[19];
				this.bannerQm42vt2.X_Axis_Peak_Velocity_Component_Frequency = ModbusClient.ConvertRegistersToFloat(value_10);
				
				
				
				
				
				
				
			}
			
		}
	
    private void initMeasures() {
		
    	// TODO Measure initialization
		
	}

}
