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

public class Hmi2WriteThread implements Runnable {
	
	HmiProduction hmiProduction;
	
	BufferedReader bufferedReader;
	
	Scanner scanner;
	
	public Hmi2WriteThread(HmiProduction hmiProduction) {
		
		this.hmiProduction = hmiProduction;
		
		this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		this.scanner = new Scanner(System.in);
		
	}

	@Override
	public void run() {
		
		// Command request example: machineID_code_value_deviceID
		System.out.println("Hmi1ReadThread start at " + new Date());
		System.out.println("----- Line " + this.hmiProduction.getLineNumber() + " -----");
		System.out.println("Command list: ");
		System.out.println("0: START");
		System.out.println("1: STOP");
		System.out.println("2: RESET");
		System.out.println("10_VELOCITY_MOTOR");
		System.out.println("20_THRESOLD_MOTOR");
		
		while(true) {
			
		  try {
			  
			  String insert = scanner.nextLine();
			  String[] split = insert.split("_");
			  
			  int machineID = Integer.parseInt(split[0]);
			  
			  System.out.println("Machine ID: " + split[0] );
			  System.out.println("Request code: " + split[1] );
			  
			  System.out.println("Prova volori lettura");
			  System.out.println("0: " + split[0]);
			  System.out.println("1: " + split[1]);
			  
			  
			  /*
			  // START
			  if(split[1].equals("0")) {
				  
				  System.out.println("START command");
				  
				  HMIMachine hmiMachine = this.hmiProduction.getHmiMachineMap().get(machineID);
				  
				  hmiMachine.writePlc(split[1]);
				 			 
			  }
			  
			  // STOP
			  if(split[1].equals("1")) {
				  
				  System.out.println("STOP command");
				  
                  HMIMachine hmiMachine = this.hmiProduction.getHmiMachineMap().get(machineID);
				  
				  hmiMachine.writePlc(split[1]);
			  }
			  
			 // RESET
			  if(split[1].equals("2")) {
				  
				  System.out.println("RESET command");
				  
                  HMIMachine hmiMachine = this.hmiProduction.getHmiMachineMap().get(machineID);
				  
				  hmiMachine.writePlc(split[1]);
			  }
			    
			  // 10_VELOCITY_MOTOR or 20_THRESOLD_MOTOR
			  if(split[1].equals("10") || split[1].equals("20")) {
				 
				  // 10_VELOCITY_MOTOR
				  if(split[1].equals("10")) {
					  
					  String motorVelocity = split[2];	
					  String deviceNumber = split[3];
					  
					  System.out.println("VELOCITY: " + motorVelocity + " MOTOR: " + deviceNumber);
					  
					  HMIMachine hmiMachine = this.hmiProduction.getHmiMachineMap().get(machineID);
					  
					  hmiMachine.writeVelocity(motorVelocity, Integer.parseInt(deviceNumber));
					  
					  
				  }
				  
				  // 20_THRESOLD_MOTOR
				  else if(split[1].equals("20")) {
					  
					  String motorThreshold = split[2];	
					  String deviceNumber = split[3];
					  
					  System.out.println("THRESHOLD: " + motorThreshold+ " MOTOR: " + deviceNumber);
					  
					  // Devo inviare la scrittura al relativo drive ancora da modellare
					  
					  
					  // Provo scrittura verso PLC
					 // CoapResponse coapResponsePost2 = this.hmi1Maintenance.getCoapClientPlc().post("Ciao", MediaTypeRegistry.TEXT_PLAIN);
					  
				  }
				  
			  }
			  
			  */
		
			
		  }catch (Exception e) {
			
			  e.printStackTrace();
		}
		
		}
		
	}
	
}
