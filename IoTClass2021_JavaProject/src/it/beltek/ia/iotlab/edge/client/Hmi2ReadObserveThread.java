package it.beltek.ia.iotlab.edge.client;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;

import org.eclipse.californium.core.coap.CoAP.Code;


public class Hmi2ReadObserveThread implements Runnable {
	
	HmiProduction hmiProduction;
	
	HMIReject hmiReject;
	
	String obsResource;
	
	
	public Hmi2ReadObserveThread(HmiProduction hmiProduction, HMIReject hmiReject) {
		
		this.hmiProduction = hmiProduction;
		
		this.hmiReject = hmiReject;
		
		this.obsResource = this.hmiReject.getObserveResourceReject() + "_Velocity";
		
		CoapClient coapClient = new CoapClient(this.obsResource);
		
		this.hmiReject.setCoapClientReject(coapClient);
	}

	@Override
	public void run() {
		
		System.out.println("Hmi2ReadObserveThread reject:" + this.hmiReject.getRejectDevice().deviceName + " start at " + new Date());
		
		this.hmiReject.getCoapClientReject().observe(new CoapHandler() {
			
			@Override
			public void onLoad(CoapResponse response) {
				System.out.println("--- Resource update ---");
				System.out.println("Line velocity " + hmiReject.getRejectDevice().deviceName + " : " + response.getResponseText() + " unit/min" );
				
			}
			
			@Override
			public void onError() {
				System.out.print("Line velocity erro " + hmiReject.getRejectDevice().deviceName);
				
			}
		});
		
		while(true) {
			
			try {
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
