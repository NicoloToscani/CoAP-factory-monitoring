package it.beltek.ia.iotlab.edge.client;

import java.awt.List;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP.Code;

import it.beltek.ia.iotlab.edge.gateway.RejectFieldbusThread;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

public class HmiMachine{
	
	private ThreadPoolExecutor pool;
	 
	private static final int COREPOOL = 10;
    private static final int MAXPOOL = 10;
    private static final int IDLETIME = 5000;
    private static final int SLEEPTIME = 1000;
	
	private int driveNumber;
	
    private SchneiderPM3200 schneiderPM3200;
    public SchneiderPM3200 getSchneiderPM3200() {
		return schneiderPM3200;
	}


	public void setSchneiderPM3200(SchneiderPM3200 schneiderPM3200) {
		this.schneiderPM3200 = schneiderPM3200;
	}




	private PLC plc;

	private CoapClient coapClientEnergy;
	private CoapClient coapClientPlc;
	private ArrayList<CoapClient> coAPCLientDrives;
	private ArrayList<CoapClient> coAPCLientVibrations;
	private CoapClient coapClientDrive;
	private CoapClient coapClientVibration;
	
	//String url = "coap://localhost:5687/.well-known/core";
	private String url1 = "coap://localhost:5683/plc";
	private String url2 = "coap://localhost:5684/powerEnergyMeter";
	private ArrayList<String> driveUrls;
	private ArrayList<String> vibrationUrls;
	
	// Da cambiare
	String driveUrl1 = "coap://localhost:5686/drive";
	String vibrationUrl1 = "coap://localhost:5685/vibrationSensor";
	
	
	

	public HmiMachine() {
		
		this.schneiderPM3200 = new SchneiderPM3200();
		
		this.plc = new PLC();
		
		this.coapClientPlc = new CoapClient(url1);
		
		this.coapClientEnergy = new CoapClient(url2);
		
		this.coapClientDrive = new CoapClient(driveUrl1);
		
		this.coapClientDrive = new CoapClient(vibrationUrl1);
		
		
		this.driveNumber = 3; // Da caricare a runtime
		
		this.coAPCLientDrives = new ArrayList<>();
		
		this.coAPCLientVibrations = new ArrayList<>();
		
		this.pool = new ThreadPoolExecutor(COREPOOL, MAXPOOL, IDLETIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		
		
	}
	
	
	/**
	 * HMI_1: Maintenance supervisor
	**/
	private void run(){
		
		this.pool.execute(new Hmi1ReadThread(this, this.driveNumber));
		
	}
	
	public int getDriveNumber() {
		return driveNumber;
	}

	public CoapClient getCoapClientEnergy() {
		return coapClientEnergy;
	}


	public ArrayList<CoapClient> getCoAPCLientDrives() {
		return coAPCLientDrives;
	}

	public ArrayList<CoapClient> getCoAPCLientVibrations() {
		return coAPCLientVibrations;
	}
	
	public CoapClient getCoapClientPlc() {
		return coapClientPlc;
	}
	
	public PLC getPlc() {
		return plc;
	}
	

	public void setPlc(PLC plc) {
		this.plc = plc;
	}


	

	public static void main(String[] args) {
		
		new HmiMachine().run();

	}

}

