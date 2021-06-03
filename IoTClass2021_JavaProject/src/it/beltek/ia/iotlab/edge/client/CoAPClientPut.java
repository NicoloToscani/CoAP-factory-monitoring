package it.beltek.ia.iotlab.edge.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;

import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoAPClientPut extends CoapClient {
	
	private PLC plc;
	
	private Drive drive;
	
	private CoapClient coapClient;
	
	private CoapClient coapClient2;
	
	
	// String url = "coap://localhost::5683/.well-known/core";
	private String url = "coap://localhost:5683/plc_1_1";
    private String url2 = "coap://localhost:5690/drive_1_1_10";
	//private String url2 = "coap://localhost:5690/.well-known/core";
	
	public CoAPClientPut() {
		
		this.plc = new PLC();
		this.drive = new Drive();
		
		this.coapClient = new CoapClient(url);
		this.coapClient2 = new CoapClient(url2);
		
	}
	
	/**
	 * Client code
	**/
	private void run(){
		
		while(true) {
			
			// GET PLC
			Request request = new Request(Code.GET);
			
			CoapResponse coapResponseGet = this.coapClient.advanced(request);
			
			System.out.println("GET: " + coapResponseGet.getResponseText());
			
			// GET plc
			Request requestGetDrive = new Request(Code.GET);
						
			CoapResponse coapResponseGetDrive = this.coapClient2.advanced(requestGetDrive);
						
		    System.out.println("GET: " + coapResponseGetDrive.getResponseText());
			
			// PUT drive
			this.drive.setpoint = 25.6f;
			Gson gsonDrive = new Gson();
			String driveSerialize = gsonDrive.toJson(drive);
			CoapResponse coapResponseDrivePut = this.coapClient2.post(driveSerialize, MediaTypeRegistry.APPLICATION_JSON);
			
			
			// Serializzo il PLC da inviare , invio comandi al PLC
			this.plc.reset = false;
			this.plc.startCommand = false;
			this.plc.stopCommand = false;
			
			
			Gson gson = new Gson();
			String plcSerialize = gson.toJson(plc);
			
			//CoapResponse coapResponsePost = this.coapClient.post("Sto scrivendo POST", MediaTypeRegistry.TEXT_PLAIN);
			CoapResponse coapResponsePut = this.coapClient.post(plcSerialize, MediaTypeRegistry.APPLICATION_JSON);
			
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	
	public static void main(String[] args) {
		
		new CoAPClientPut().run();
	}

}
