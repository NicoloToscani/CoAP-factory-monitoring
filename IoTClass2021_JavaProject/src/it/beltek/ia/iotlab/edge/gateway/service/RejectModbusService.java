package it.beltek.ia.iotlab.edge.gateway.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.re.easymodbus.modbusclient.ModbusClient;
import it.beltek.ia.iotlab.edge.gateway.ConnectionState;
import it.beltek.ia.iotlab.edge.gateway.Device;
import it.beltek.ia.iotlab.edge.gateway.DriveFieldbusThread;
import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.WeightSystem;
import it.beltek.ia.iotlab.edge.gateway.device.driver.s7.S7;
import it.beltek.ia.iotlab.edge.gateway.service.simulation.RejectFieldbusThread;

/**
 * The class {@code RejectModbusService} manages communication with a device that implement Modbus TCP-IP protocol. 
 * 
 * @author Nicolò Toscani
 * @version 1.0
 *
 */
public class RejectModbusService implements Device {
	
    private ModbusClient modbusClient;
	
	private String IPAddress;
	private int Port;
	private int UnitId;
	private ConnectionState connectionState;

	private WeightSystem weightSystem;
	private WeightSystem weightSystemSimulate;
	
	
	private ThreadPoolExecutor pool;
	
	private static final int COREPOOL = 1;
    private static final int MAXPOOL = 1;
    private static final int IDLETIME = 5000;
    private static final int SLEEPTIME = 1000;
	
    public RejectModbusService(String IPAddress, int Port) {
		
		this.modbusClient = new ModbusClient();
		
		this.IPAddress = IPAddress;
		
		this.Port = Port;
		
		this.UnitId = UnitId; // if we use one gateway with multiple Modbus RTU devices
		
		// this.connectionState = ConnectionState.Offline;
		this.connectionState = ConnectionState.Online; // Simulation
		
		this.weightSystem = new WeightSystem();
		
		this.weightSystemSimulate = new WeightSystem();
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		initMeasures();
		
		Simulate(); // Start simulation
		
	}

    @Override
	public void Connect() {
		
		this.connectionState = ConnectionState.Connecting;
		
		try {
			
			this.modbusClient.Connect(IPAddress, Port);
			
			if(this.modbusClient.isConnected() == true) {
				
				this.connectionState = ConnectionState.Online;
				
				System.out.println("WeightSystem: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
			}
			
			else {
				
				this.connectionState = ConnectionState.Offline;
				
				System.out.println("WeightSystem: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
				
			}
			
		
		} catch (Exception e) {
			
           this.connectionState = ConnectionState.Offline;
			
			System.out.println("WeightSystem: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
			
			e.printStackTrace();
		}
		
	}

	@Override
	public void Disconnect() {
		
		try {
			
			this.modbusClient.Disconnect();
			
			if(! (this.modbusClient.isConnected())) {
				
				this.connectionState = ConnectionState.Offline;
				
				System.out.println("WeightSystem: " + this.connectionState + " IPAddress: " + this.IPAddress + " Port: " + this.Port );
				
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
			this.weightSystem.timestamp = currentDateTime.format(formatter);
			
			this.weightSystem.connectionState = this.connectionState;
			
			this.weightSystem = this.weightSystemSimulate;
			
			
		}
		
		// Write values
		public void Write() {
			
			this.weightSystem.connectionState = this.connectionState;
			
			writeData();
			
		}
		
		// Write start command
	    public void writeData() {
	    	
	    	this.weightSystemSimulate.lineVelocitySetpoint = this.weightSystem.lineVelocitySetpoint;
	    	this.weightSystemSimulate.setpoint = this.weightSystem.setpoint;
	  	
		}
		
    public String getIPAddress() {
			return IPAddress;
		}

		public int getPort() {
			return Port;
		}

		public int getUnitId() {
			return UnitId;
		}

		public ConnectionState getConnectionState() {
			return connectionState;
		}

		public WeightSystem getWeightSystem() {
			return weightSystem;
		}

		public void setIPAddress(String iPAddress) {
			IPAddress = iPAddress;
		}

		public void setPort(int port) {
			Port = port;
		}

		public void setUnitId(int unitId) {
			UnitId = unitId;
		}

		public void setConnectionState(ConnectionState connectionState) {
			this.connectionState = connectionState;
		}
		
		public WeightSystem getWeightSystemSimulate() {
			return weightSystemSimulate;
		}


	private void initMeasures() {
		
    	// TODO Measure initialization
		
	}

	public void Simulate() {
		
		System.out.println("Simulation");
		
		// Avvio un thread che ogni 10 secondi mi genera un peso che confrontato con il setpoint incrementa o no il contatore dei pezzi prodotti e il totale
		this.pool.execute(new RejectFieldbusThread(this));
		
		
	}

}
