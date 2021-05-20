package it.beltekia.iotlab.edge.gateway.service;

import java.io.IOException;

import de.re.easymodbus.modbusclient.ModbusClient;
import de.re.easymodbus.modbusclient.ModbusClient.RegisterOrder;
import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.Device;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

/**
 * The class {@code Pm3200ModbusService} manages communication with a device that implement Modbus TCP-IP protocol. 
 * 
 * @author Nicolò Toscani
 * @version 1.0
 *
 */
public class Pm3200ModbusService implements Device {
	
    private ModbusClient modbusClient;
	
	private String IPAddress;
	private int Port;
	private int UnitId;
	private ConnectionState connectionState;
	private SchneiderPM3200 schneiderPM3200 = new SchneiderPM3200();
	
	private int[] readCurrent;
	private int[] readVoltage;
	private int[] readPower;
    private int[] readPF;
    private int[] readCurrentUnbalance; 
    private int[] readVoltageUnbalance;
    private int[] readTangentPhi; 
    private int[] readFrequency;
    private int[] readTemperature; 
    private float tangentPhi; 
    private float frequency;
    private float temperature; 
    private int[] readActivePowerImpTotal; 
    private String loadType;
	
	
	public Pm3200ModbusService(String IPAddress, int Port) {
		
		this.modbusClient = new ModbusClient();
		
		this.IPAddress = IPAddress;
		
		this.Port = Port;
		
		this.UnitId = UnitId;
		
	}
	

	@Override
	public void Connect() {
		
		this.connectionState = ConnectionState.Connecting;
		
		try {
			
			this.modbusClient.Connect(IPAddress, Port);
			
			if(this.modbusClient.isConnected() == true) {
				
				this.connectionState = ConnectionState.Online;
				
				System.out.println("PM3200: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
			}
			
			else {
				
				this.connectionState = ConnectionState.Offline;
				
				System.out.println("PM3200: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
				
			}
			
		
		} catch (Exception e) {
			
           this.connectionState = ConnectionState.Offline;
			
			System.out.println("PM3200: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
			
			e.printStackTrace();
		}
		
	}

	@Override
	public void Disconnect() {
		
		try {
			
			this.modbusClient.Disconnect();
			
			if(! (this.modbusClient.isConnected())) {
				
				this.connectionState = ConnectionState.Offline;
				
				System.out.println("PM3200: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean IsAlive() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void Read() {
		
		readHoldingRegisters_1();
		readHoldingRegisters_2();
		readHoldingRegisters_3();
		readHoldingRegisters_4();
		readHoldingRegisters_5();
		readHoldingRegisters_6();
		readHoldingRegisters_7();
		readHoldingRegisters_8();
		readHoldingRegisters_9();
		readHoldingRegisters_10();
		
	}
	
	// Read current values
	private void readHoldingRegisters_1() {
		
		try {
			
			readCurrent = this.modbusClient.ReadHoldingRegisters(2999, 12);
			
		
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readCurrent[0];
			value_1[1] = readCurrent[1];
			this.schneiderPM3200.I1 = ModbusClient.ConvertRegistersToFloat(value_1);
			
			int[] value_2 = new int[2];
			value_2[0] = readCurrent[2];
			value_2[1] = readCurrent[3];
			this.schneiderPM3200.I2 = ModbusClient.ConvertRegistersToFloat(value_2);
			
			int[] value_3 = new int[2];
			value_3[0] = readCurrent[4];
			value_3[1] = readCurrent[5];
			this.schneiderPM3200.I3 = ModbusClient.ConvertRegistersToFloat(value_3);
			
			int[] value_4 = new int[2];
			value_4[0] = readCurrent[6];
			value_4[1] = readCurrent[7];
			this.schneiderPM3200.I_Avg = ModbusClient.ConvertRegistersToFloat(value_4);
			
		}
		
	}
	
	// Read voltage values
	private void readHoldingRegisters_2() {
		
         try {
			
			readVoltage = this.modbusClient.ReadHoldingRegisters(3019, 18);
			
		
		 } catch (Exception e) {
			
			e.printStackTrace();
		 } finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readVoltage[0];
			value_1[1] = readVoltage[1];
			this.schneiderPM3200.L1_L2 = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
			
			int[] value_2 = new int[2];
			value_2[0] = readVoltage[2];
			value_2[1] = readVoltage[3];
			this.schneiderPM3200.L2_L3 = ModbusClient.ConvertRegistersToFloat(value_2, RegisterOrder.HighLow);
			
			int[] value_3 = new int[2];
			value_3[0] = readVoltage[4];
			value_3[1] = readVoltage[5];
			this.schneiderPM3200.L3_L1 = ModbusClient.ConvertRegistersToFloat(value_3, RegisterOrder.HighLow);
			
			int[] value_4 = new int[2];
			value_4[0] = readVoltage[6];
			value_4[1] = readVoltage[7];
			this.schneiderPM3200.LL_Avg = ModbusClient.ConvertRegistersToFloat(value_4, RegisterOrder.HighLow);
			
			int[] value_5 = new int[2];
			value_5[0] = readVoltage[8];
			value_5[1] = readVoltage[9];
			this.schneiderPM3200.L1_N = ModbusClient.ConvertRegistersToFloat(value_5, RegisterOrder.HighLow);
			
			int[] value_6 = new int[2];
			value_6[0] = readVoltage[10];
			value_6[1] = readVoltage[11];
			this.schneiderPM3200.L2_N = ModbusClient.ConvertRegistersToFloat(value_6, RegisterOrder.HighLow);
			
			int[] value_7 = new int[2];
			value_7[0] = readVoltage[12];
			value_7[1] = readVoltage[13];
			this.schneiderPM3200.L3_N = ModbusClient.ConvertRegistersToFloat(value_7, RegisterOrder.HighLow);
			
			// Not used - Free register
			int[] value_8 = new int[2];
			value_8[0] = readVoltage[14];
			value_8[1] = readVoltage[15];
			
			int[] value_9 = new int[2];
			value_9[0] = readVoltage[16];
			value_9[1] = readVoltage[17];
			this.schneiderPM3200.LN_Avg = ModbusClient.ConvertRegistersToFloat(value_9, RegisterOrder.HighLow);
			
		}
		
	}
	
	// Read power values
	private void readHoldingRegisters_3() {
		
         try {
			
			 readPower = this.modbusClient.ReadHoldingRegisters(3053, 24);
		
		 } catch (Exception e) {
			
			e.printStackTrace();
			
		 } finally {
			 
			 int[] value_1 = new int[2];
			 value_1[0] = readPower[0];
			 value_1[1] = readPower[1];
			 this.schneiderPM3200.active_power_P1 = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
			 
			 int[] value_2 = new int[2];
			 value_2[0] = readPower[2];
			 value_2[1] = readPower[3];
			 this.schneiderPM3200.active_power_P2 = ModbusClient.ConvertRegistersToFloat(value_2, RegisterOrder.HighLow);
			 
			 int[] value_3 = new int[2];
			 value_3[0] = readPower[4];
			 value_3[1] = readPower[5];
			 this.schneiderPM3200.active_power_P3 = ModbusClient.ConvertRegistersToFloat(value_3, RegisterOrder.HighLow);
			 
			 int[] value_4 = new int[2];
			 value_4[0] = readPower[6];
			 value_4[1] = readPower[7];
			 this.schneiderPM3200.active_power_T = ModbusClient.ConvertRegistersToFloat(value_4, RegisterOrder.HighLow);
			 
			 int[] value_5 = new int[2];
			 value_5[0] = readPower[8];
			 value_5[1] = readPower[9];
			 this.schneiderPM3200.reactive_power_P1 = ModbusClient.ConvertRegistersToFloat(value_5, RegisterOrder.HighLow);
			 
			 int[] value_6 = new int[2];
			 value_6[0] = readPower[10];
			 value_6[1] = readPower[11];
			 this.schneiderPM3200.reactive_power_P2 = ModbusClient.ConvertRegistersToFloat(value_6, RegisterOrder.HighLow);
			 
			 int[] value_7 = new int[2];
			 value_7[0] = readPower[12];
			 value_7[1] = readPower[13];
			 this.schneiderPM3200.reactive_power_P3 = ModbusClient.ConvertRegistersToFloat(value_7, RegisterOrder.HighLow);
			 
			 int[] value_8 = new int[2];
			 value_8[0] = readPower[14];
			 value_8[1] = readPower[15];
			 this.schneiderPM3200.reactive_power_T = ModbusClient.ConvertRegistersToFloat(value_8, RegisterOrder.HighLow);
			 
			 int[] value_9 = new int[2];
			 value_9[0] = readPower[16];
			 value_9[1] = readPower[17];
			 this.schneiderPM3200.apparent_power_P1 = ModbusClient.ConvertRegistersToFloat(value_9, RegisterOrder.HighLow);
			 
			 int[] value_10 = new int[2];
			 value_10[0] = readPower[18];
			 value_10[1] = readPower[19];
			 this.schneiderPM3200.apparent_power_P2 = ModbusClient.ConvertRegistersToFloat(value_10, RegisterOrder.HighLow);
			 
			 int[] value_11 = new int[2];
			 value_11[0] = readPower[20];
			 value_11[1] = readPower[21];
			 this.schneiderPM3200.apparent_power_P3 = ModbusClient.ConvertRegistersToFloat(value_11, RegisterOrder.HighLow);
			 
			 int[] value_12 = new int[2];
			 value_12[0] = readPower[22];
			 value_12[1] = readPower[23];
			 this.schneiderPM3200.apparent_power_T = ModbusClient.ConvertRegistersToFloat(value_12, RegisterOrder.HighLow);
			 
		 }
		 
	}
	
	// Power factor values
	private void readHoldingRegisters_4() {
		
		try {
			
			readPF = this.modbusClient.ReadHoldingRegisters(3077, 8);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readPF[0];
			value_1[1] = readPF[1];
			float pf_P1_temp = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
			
			
			int[] value_2 = new int[2];
			value_2[0] = readPF[2];
			value_2[1] = readPF[3];
			float pf_P2_temp = ModbusClient.ConvertRegistersToFloat(value_2, RegisterOrder.HighLow);
			
			
			int[] value_3 = new int[2];
			value_3[0] = readPF[4];
			value_3[1] = readPF[5];
			float pf_P3_temp = ModbusClient.ConvertRegistersToFloat(value_3, RegisterOrder.HighLow);
			
			int[] value_4 = new int[2];
			value_4[0] = readPF[6];
			value_4[1] = readPF[7];
			float pf_T_temp = ModbusClient.ConvertRegistersToFloat(value_4, RegisterOrder.HighLow);
			
			// Quadrant calculation
			float pf_quadrant_T = 0;
            float pf_quadrant_P1 = 0;
            float pf_quadrant_P2 = 0;
            float pf_quadrant_P3 = 0;
            
            // Quadrant 1
            if(pf_T_temp >= 0 && pf_T_temp <= 1)
            {
                pf_quadrant_P1 = pf_P1_temp;
                pf_quadrant_P2 = pf_P2_temp;
                pf_quadrant_P3 = pf_P3_temp;
                pf_quadrant_T = pf_T_temp;

                this.loadType = "Inductive";
            }

            // Quadrant 2
            if (pf_T_temp >= -1 && pf_T_temp <=0 )
            {
                pf_quadrant_P1 = pf_P1_temp;
                pf_quadrant_P2 = pf_P2_temp;
                pf_quadrant_P3 = pf_P3_temp;
                pf_quadrant_T = pf_T_temp;

                this.loadType = "Capacitive";
            }

            // Quadrant 3
            if (pf_T_temp >= -2 && pf_T_temp <= -1)
            {
                pf_quadrant_P1 = (-2) - pf_P1_temp;
                pf_quadrant_P2 = (-2) - pf_P2_temp;
                pf_quadrant_P3 = (-2) - pf_P3_temp;
                pf_quadrant_T = (-2) - pf_T_temp;

                this.loadType = "Inductive";
            }

            // Quadrant 4
            if (pf_T_temp >= 1 && pf_T_temp <= 2)
            {
                pf_quadrant_P1 = (2) - pf_P1_temp;
                pf_quadrant_P2 = (2) - pf_P2_temp;
                pf_quadrant_P3 = (2) - pf_P3_temp;
                pf_quadrant_T = (2) - pf_T_temp;

                this.loadType = "Capacitive";
            }
            
            this.schneiderPM3200.pf_P1 = pf_quadrant_P1;
            this.schneiderPM3200.pf_P2 = pf_quadrant_P2;
            this.schneiderPM3200.pf_P3 = pf_quadrant_P3;
            this.schneiderPM3200.pf_T = pf_quadrant_T;
			
			
		}
		
	}
	
	// Current Unbalanced values
	private void readHoldingRegisters_5() {
		
		try {
			
			readCurrentUnbalance = this.modbusClient.ReadHoldingRegisters(3011, 8);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readCurrentUnbalance[0];
			value_1[1] = readCurrentUnbalance[1];
			this.schneiderPM3200.current_unbalance_I1 = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
			
			int[] value_2 = new int[2];
			value_2[0] = readCurrentUnbalance[2];
			value_2[1] = readCurrentUnbalance[3];
			this.schneiderPM3200.current_unbalance_I2 = ModbusClient.ConvertRegistersToFloat(value_2, RegisterOrder.HighLow);
			
			int[] value_3 = new int[2];
			value_3[0] = readCurrentUnbalance[4];
			value_3[1] = readCurrentUnbalance[5];
			this.schneiderPM3200.current_unbalance_I3 = ModbusClient.ConvertRegistersToFloat(value_3, RegisterOrder.HighLow);
			
			int[] value_4 = new int[2];
			value_4[0] = readCurrentUnbalance[6];
			value_4[1] = readCurrentUnbalance[7];
			this.schneiderPM3200.current_unbalance_worst = ModbusClient.ConvertRegistersToFloat(value_4, RegisterOrder.HighLow);
		}
		
	}
	
	// Voltage Unbalanced values
	private void readHoldingRegisters_6() {
		
		try {
			
			readVoltageUnbalance = this.modbusClient.ReadHoldingRegisters(3037, 16);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readVoltageUnbalance[0];
			value_1[1] = readVoltageUnbalance[1];
			this.schneiderPM3200.voltage_unbalance_L1L2 = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
			
			int[] value_2 = new int[2];
			value_2[0] = readVoltageUnbalance[2];
			value_2[1] = readVoltageUnbalance[3];
			this.schneiderPM3200.voltage_unbalance_L2L3 = ModbusClient.ConvertRegistersToFloat(value_2, RegisterOrder.HighLow);
			
			int[] value_3 = new int[2];
			value_3[0] = readVoltageUnbalance[4];
			value_3[1] = readVoltageUnbalance[5];
			this.schneiderPM3200.voltage_unbalance_L3L1 = ModbusClient.ConvertRegistersToFloat(value_3, RegisterOrder.HighLow);
			
			int[] value_4 = new int[2];
			value_4[0] = readVoltageUnbalance[6];
			value_4[1] = readVoltageUnbalance[7];
			this.schneiderPM3200.voltage_unbalance_LL_worst = ModbusClient.ConvertRegistersToFloat(value_4, RegisterOrder.HighLow);
			
			int[] value_5 = new int[2];
			value_1[0] = readVoltageUnbalance[8];
			value_1[1] = readVoltageUnbalance[9];
			this.schneiderPM3200.voltage_unbalance_L1N = ModbusClient.ConvertRegistersToFloat(value_5, RegisterOrder.HighLow);
			
			int[] value_6 = new int[2];
			value_6[0] = readVoltageUnbalance[10];
			value_6[1] = readVoltageUnbalance[11];
			this.schneiderPM3200.voltage_unbalance_L2N = ModbusClient.ConvertRegistersToFloat(value_6, RegisterOrder.HighLow);
			
			int[] value_7 = new int[2];
			value_7[0] = readVoltageUnbalance[12];
			value_7[1] = readVoltageUnbalance[13];
			this.schneiderPM3200.voltage_unbalance_L3N = ModbusClient.ConvertRegistersToFloat(value_7, RegisterOrder.HighLow);
			
			int[] value_8 = new int[2];
			value_8[0] = readVoltageUnbalance[14];
			value_8[1] = readVoltageUnbalance[15];
			this.schneiderPM3200.voltage_unbalance_LN_worst = ModbusClient.ConvertRegistersToFloat(value_8, RegisterOrder.HighLow);
		}
	}
	
	// Tangent Phi value (Reactive factor)
	private void readHoldingRegisters_7() {
		
		try {
			
			readTangentPhi = this.modbusClient.ReadHoldingRegisters(3107, 2);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readTangentPhi[0];
			value_1[1] = readTangentPhi[1];
			this.schneiderPM3200.tangent_phi = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
		}
	}
	
	// Frequency value
	private void readHoldingRegisters_8() {
		
		try {
			
			readFrequency = this.modbusClient.ReadHoldingRegisters(3109, 2);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readFrequency[0];
			value_1[1] = readFrequency[1];
			this.schneiderPM3200.Frequency = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
			
			
		}
	}
	
	// Temperature value
	private void readHoldingRegisters_9() {
		
		try {
			
			readTemperature = this.modbusClient.ReadHoldingRegisters(3131, 2);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readTemperature[0];
			value_1[1] = readTemperature[1];
			this.schneiderPM3200.Temperature = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
			
		}
	}
	
	// Total imported active power value (32 bit)
	private void readHoldingRegisters_10() {
		
		try {
			
			readActivePowerImpTotal = this.modbusClient.ReadHoldingRegisters(45165, 2);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			
			int[] value_1 = new int[2];
			value_1[0] = readActivePowerImpTotal[0];
			value_1[1] = readActivePowerImpTotal[1];
			this.schneiderPM3200.Active_power_imp_total = ModbusClient.ConvertRegistersToFloat(value_1, RegisterOrder.HighLow);
		}
	}

		


	public String getIPAddress() {
		return IPAddress;
	}


	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}


	public int getPort() {
		return Port;
	}


	public void setPort(int port) {
		Port = port;
	}


	public int getUnitId() {
		return UnitId;
	}


	public void setUnitId(int unitId) {
		UnitId = unitId;
	}
	
	public SchneiderPM3200 getSchneiderPM3200() {
		return schneiderPM3200;
	}


	public void setSchneiderPM3200(SchneiderPM3200 schneiderPM3200) {
		this.schneiderPM3200 = schneiderPM3200;
	}


}
