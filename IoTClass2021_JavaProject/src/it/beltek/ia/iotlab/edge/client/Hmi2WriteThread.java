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
		System.out.println("Hmi2WriteThread start at " + new Date());
		System.out.println("Command list: ");
		System.out.println("0: LINE VELOCITY SETPOINT");
		System.out.println("1: THR UNIT WEIGHT SETPOINT");
		
		while(true) {
			
		  try {
			  
			  String insert = scanner.nextLine();
			  String[] split = insert.split("_");
			  
			  int lineID = Integer.parseInt(split[0]);
			  int machineID = Integer.parseInt(split[1]);
			  
			  System.out.println("Line ID: " + split[0]);
			  System.out.println("Machine ID: " + split[1]);
			  System.out.println("Request code: " + split[2]);
			  System.out.println("Value: " + split[3]);
			  
			  
			  // LINE VELOCITY SETPOINT
			  if(split[2].equals("0")) {
				  
				  System.out.println("Line velocity setpoint command");
				 
				  Iterator<HMIReject> hmiIterator = this.hmiProduction.getHmiRejectsList().iterator();
				  
				  while(hmiIterator.hasNext()) {
					  
					  HMIReject hmiReject = hmiIterator.next();
					  
					  String hmiRejectDeviceName = hmiReject.getRejectDevice().deviceName;
					  
					  String[] hmiRejectDeviceNameSplit = hmiRejectDeviceName.split("_");
					  
					  if(hmiRejectDeviceNameSplit[1].equals(split[0]) && hmiRejectDeviceNameSplit[2].equals(split[1])) {
						  
						  // Write line velocity setpoint on right reject machine
						  hmiReject.writeLineVelocitySetpoint(split[3]);
					  }
				  }
				  			 
			  }
			  
			  // THR UNIT WEIGHT SETPOINT
			  if(split[2].equals("1")) {
				  
				  System.out.println("Unit weight thr setpoint command");
					 
				  Iterator<HMIReject> hmiIterator = this.hmiProduction.getHmiRejectsList().iterator();
				  
				  while(hmiIterator.hasNext()) {
					  
					  HMIReject hmiReject = hmiIterator.next();
					  
					  String hmiRejectDeviceName = hmiReject.getRejectDevice().deviceName;
					  
					  String[] hmiRejectDeviceNameSplit = hmiRejectDeviceName.split("_");
					  
					  if(hmiRejectDeviceNameSplit[1].equals(split[0]) && hmiRejectDeviceNameSplit[2].equals(split[1])) {
						  
						  // Write line velocity setpoint on right reject machine
						  hmiReject.writeThrSetpoint(split[3]);
					  }
				  }
				  
			  }
			  
			
		  }catch (Exception e) {
			
			  e.printStackTrace();
		}
		
		}
		
	}
	
}
