package it.beltek.ia.iotlab.edge.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;

import org.eclipse.californium.core.coap.CoAP.Code;


public class Hmi1WriteThread implements Runnable {
	
	HmiMaintenance hmi1Maintenance;
	
	BufferedReader bufferedReader;
	
	Scanner scanner;
	
	public Hmi1WriteThread(HmiMaintenance hmi1Maintenance) {
		
		this.hmi1Maintenance = hmi1Maintenance;
		
		this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		this.scanner = new Scanner(System.in);
		
	}

	@Override
	public void run() {
		
		System.out.println("Hmi1ReadThread start at " + new Date());
		System.out.println("----- Line " + this.hmi1Maintenance.getLineNumber() + " -----");
		System.out.println("Command list: ");
		System.out.println("0: START");
		System.out.println("1: STOP");
		System.out.println("2: RESET");
		System.out.println("10_VELOCITY_MOTOR");
		System.out.println("20_THRESOLD_MOTOR");
		
		while(true) {
			
		  try {
			  
			 // String insert = this.bufferedReader.readLine();
			  String insert = scanner.nextLine();
			  String[] split = insert.split("_");
			  System.out.println("Request code: " + split[0] );
			  
			  if(split[0].equals("0")) {
				  
				  System.out.println("START command");
				  
				  CoapResponse coapResponsePost = this.hmi1Maintenance.getCoapClientPlc().post("15", MediaTypeRegistry.TEXT_PLAIN);
				  
				  
				  
			  }
			    
			  if(split[0].equals("10") || split[0].equals("20")) {
				  
				  if(split[0].equals("10")) {
					  
					  String motorVelocity = split[1];	
					  String deviceNumber = split[2];
					  
					  System.out.println("VELOCITY: " + motorVelocity+ " MOTOR: " + deviceNumber);
					  
					  // Devo inviare la scrittura al relativo drive ancora da modellare
					  CoapResponse coapResponsePost = this.hmi1Maintenance.getCoapClientDrive().post(motorVelocity, MediaTypeRegistry.TEXT_PLAIN);
					  
					  // Provo scrittura verso PLC
					 // CoapResponse coapResponsePost2 = this.hmi1Maintenance.getCoapClientPlc().post("Ciao", MediaTypeRegistry.TEXT_PLAIN);
					  
				  }
				  
				  else if(split[0].equals("20")) {
					  
					  String motorThreshold = split[1];	
					  String deviceNumber = split[2];
					  
					  System.out.println("THRESHOLD: " + motorThreshold+ " MOTOR: " + deviceNumber);
					  
					  // Devo inviare la scrittura al relativo drive ancora da modellare
					  
					  
					  // Provo scrittura verso PLC
					 // CoapResponse coapResponsePost2 = this.hmi1Maintenance.getCoapClientPlc().post("Ciao", MediaTypeRegistry.TEXT_PLAIN);
					  
				  }
				  
			  }
		
			
		  }catch (Exception e) {
			
			  e.printStackTrace();
		}
		
		}
		
	}
	

}
