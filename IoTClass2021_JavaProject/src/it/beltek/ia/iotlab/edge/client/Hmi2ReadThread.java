package it.beltek.ia.iotlab.edge.client;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;

import com.google.gson.Gson;

import it.beltek.ia.iotlab.edge.gateway.device.BannerQm42vt2;
import it.beltek.ia.iotlab.edge.gateway.device.Drive;
import it.beltek.ia.iotlab.edge.gateway.device.PLC;
import it.beltek.ia.iotlab.edge.gateway.device.SchneiderPM3200;
import it.beltek.ia.iotlab.edge.gateway.device.components.Alarm;

import org.eclipse.californium.core.coap.CoAP.Code;


public class Hmi2ReadThread implements Runnable {
	
	HmiProduction hmiProduction;
	
	Timer requestTimer;

	
	public Hmi2ReadThread(HmiProduction hmiProduction) {
		
		this.hmiProduction = hmiProduction;
		
		this.requestTimer = new Timer();
		
	}

	@Override
	public void run() {
		
		System.out.println("Hmi1ReadThread start at " + new Date());
		
		requestTimer.schedule(new RequestTimerTask(this), 0, 5000);  // 5 s
		
	}
	
	// Leggo i dati dai diversi server
	private class RequestTimerTask extends TimerTask{
		
		Hmi2ReadThread hmi1ReadThread;
		
		public RequestTimerTask(Hmi2ReadThread hmi1ReadThread) {
			
			this.hmi1ReadThread = hmi1ReadThread;
		}

		@Override
		public void run() {
			
			// Read 
			Iterator <Integer> it = this.hmi1ReadThread.hmiProduction.getHmiMachineMap().keySet().iterator();
	        while(it.hasNext()) {
	        	
	        	int key = it.next();
	        
	        	HMIMachine hmiMachine = this.hmi1ReadThread.hmiProduction.getHmiMachineMap().get(key);
	        	
	        	hmiMachine.readPLC();
	        	hmiMachine.readEnergy();
	        	hmiMachine.readReject();
	        	hmiMachine.readDrives();
	        	hmiMachine.readVibrations();
	        	
	        }
	        
	        // Display values
	     	Iterator <Integer> itMeasure = this.hmi1ReadThread.hmiProduction.getHmiMachineMap().keySet().iterator();
	     	while(itMeasure.hasNext()) {
	     	        	
	     	    int key = itMeasure.next();
	     	        
	     	    HMIMachine hmiMachine = this.hmi1ReadThread.hmiProduction.getHmiMachineMap().get(key);
	     	    
	     	    System.out.println("-------------- Machine " + key + " --------------------");
	     	    
	     	    System.out.println("---------- PLC ----------");
	     	    System.out.println("Machine state: " + hmiMachine.getPlc().state);
		        
		        System.out.println("---------- ENERGY ----------");
		        System.out.println("I1: " + hmiMachine.getPm3200().I1 + " " + hmiMachine.getPm3200().currentUnitMeasure);
				System.out.println("I2: " + hmiMachine.getPm3200().I2 + " " + hmiMachine.getPm3200().currentUnitMeasure);
				System.out.println("I3: " + hmiMachine.getPm3200().I3 + " " + hmiMachine.getPm3200().currentUnitMeasure);			
				System.out.println("I_Avg: " + hmiMachine.getPm3200().I_Avg + " " + hmiMachine.getPm3200().currentUnitMeasure);
				System.out.println("L1_L2: " + hmiMachine.getPm3200().L1_L2 + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("L2_L3: " + hmiMachine.getPm3200().L2_L3 + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("L3_L1: " + hmiMachine.getPm3200().L3_L1 + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("LL_Avg: " + hmiMachine.getPm3200().LL_Avg + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("L1_N: " + hmiMachine.getPm3200().L1_N + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("L1_N: " + hmiMachine.getPm3200().L1_N + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("L2_N: " + hmiMachine.getPm3200().L2_N + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("L3_N: " + hmiMachine.getPm3200().L3_N + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("LN_Avg: " + hmiMachine.getPm3200().LN_Avg + " " + hmiMachine.getPm3200().voltageUnitMeasure);
				System.out.println("ActivePower_P1: " + hmiMachine.getPm3200().active_power_P1 + " " + hmiMachine.getPm3200().activePowerUnitMeasure);
				System.out.println("ActivePower_P2: " + hmiMachine.getPm3200().active_power_P2 + " " + hmiMachine.getPm3200().activePowerUnitMeasure);
				System.out.println("ActivePower_P3: " + hmiMachine.getPm3200().active_power_P3 + " " + hmiMachine.getPm3200().activePowerUnitMeasure);
				System.out.println("ActivePower_T: " + hmiMachine.getPm3200().active_power_T + " " + hmiMachine.getPm3200().activePowerUnitMeasure);
				System.out.println("ReactivePower_P1: " + hmiMachine.getPm3200().reactive_power_P1 + " " + hmiMachine.getPm3200().reactivePowerUnitMeasure);
				System.out.println("ReactivePower_P2: " + hmiMachine.getPm3200().reactive_power_P2 + " " + hmiMachine.getPm3200().reactivePowerUnitMeasure);
				System.out.println("ReactivePower_P3: " + hmiMachine.getPm3200().reactive_power_P3 + " " + hmiMachine.getPm3200().reactivePowerUnitMeasure);
				System.out.println("ReactivePower_T: " + hmiMachine.getPm3200().reactive_power_T + " " + hmiMachine.getPm3200().reactivePowerUnitMeasure);
				System.out.println("ApparentPower_P1: " + hmiMachine.getPm3200().apparent_power_P1 + " " + hmiMachine.getPm3200().apparentPowerUnitMeasure);
				System.out.println("ApparentPower_P2: " + hmiMachine.getPm3200().apparent_power_P2 + " " + hmiMachine.getPm3200().apparentPowerUnitMeasure);
				System.out.println("ApparentPower_P3: " + hmiMachine.getPm3200().apparent_power_P3 + " " + hmiMachine.getPm3200().apparentPowerUnitMeasure);
				System.out.println("ApparentPower_T: " + hmiMachine.getPm3200().apparent_power_T + " " + hmiMachine.getPm3200().apparentPowerUnitMeasure);
				System.out.println("PF_1: " + hmiMachine.getPm3200().pf_P1);
				System.out.println("PF_2: " + hmiMachine.getPm3200().pf_P2);
				System.out.println("PF_3: " + hmiMachine.getPm3200().pf_P3);
				System.out.println("PF_T: " + hmiMachine.getPm3200().pf_T);
				System.out.println("Frequency: " + hmiMachine.getPm3200().Frequency + " " + hmiMachine.getPm3200().frequencyUnitMeasure);
				System.out.println("Temperature: " + hmiMachine.getPm3200().Temperature + " " + hmiMachine.getPm3200().teperatureUnitMeasure);
				System.out.println("Active_power_Import_Total: " + hmiMachine.getPm3200().Active_power_imp_total + " " + hmiMachine.getPm3200().totalActivePowerUnitMeasure);
				
				System.out.println("---------- VIBRATION ANALYSIS ----------");
		
				Iterator<BannerQm42vt2> sensorsIter = hmiMachine.getSensors().iterator();
				
				while(sensorsIter.hasNext()) {
					
					int sensorID = 10;
					
					BannerQm42vt2 sensor = sensorsIter.next();
					
					System.out.println("--- Sensor " + sensorID + " ---" );
					
			        System.out.println("Z-Axis RMS Velocity: " + sensor.Z_Axis_RMS_Velocity_In_Sec + " in/sec");
			        System.out.println("Z-Axis RMS Velocity: " + sensor.Z_Axis_RMS_Velocity_Mm_Sec + " mm/sec");
			        System.out.println("Temperature: " + sensor.Temperature_F + " °F");
			        System.out.println("Temperature: " + sensor.Temperature_C + " °C");
			        System.out.println("X-Axis RMS Velocity : " + sensor.X_Axis_RMS_Velocity_In_Sec + " in/sec");
			        System.out.println("X-Axis RMS Velocity : " + sensor.X_Axis_RMS_Velocity_Mn_Sec + " mm/sec");
			        System.out.println("Z-Axis Peak Acceleration : " + sensor.Z_Axis_Peak_Acceleration_G + " G");
			        System.out.println("X-Axis Peak Acceleration : " + sensor.X_Axis_Peak_Acceleration_G + " G");
			        System.out.println("Z-Axis Peak Velocity Component Frequency : " + sensor.Z_Axis_Peak_Velocity_Component_Frequency + " Hz");
			        System.out.println("X-Axis Peak Velocity Component Frequency : " + sensor.X_Axis_Peak_Velocity_Component_Frequency + " Hz");
			        System.out.println("Z-Axis RMS Acceleration : " + sensor.Z_Axis_RMS_Acceleration_G + " G");
			        System.out.println("X-Axis RMS Acceleration : " + sensor.X_Axis_RMS_Acceleration_G + " G");
			        System.out.println("Z-Axis Kurtosis : " + sensor.Z_Axis_Kurtosis);
			        System.out.println("X-Axis Kurtosis : " + sensor.X_Axis_Kurtosis);
			        System.out.println("Z-Axis Crest Factor : " + sensor.Z_Axis_Crest_Factor);
			        System.out.println("X-Axis Crest Factor : " + sensor.X_Axis_Crest_Factor);
			        System.out.println("Z-Axis Peak Velocity : " + sensor.Z_Axis_Peak_Velocity_In_Sec + " in/sec");
			        System.out.println("Z-Axis Peak Velocity : " + sensor.Z_Axis_Peak_Velocity_Mm_Sec + " mm/sec");
			        System.out.println("X-Axis Peak Velocity : " + sensor.X_Axis_Peak_Velocity_In_Sec + " in/sec");
			        System.out.println("X-Axis Peak Velocity : " + sensor.X_Axis_Peak_Velocity_Mm_Sec + " mm/sec");
			        System.out.println("Z-Axis High-Frequency RMS Acceleration : " + sensor.Z_Axis_High_Frequency_RMS_Acceleration_G + " G");
			        System.out.println("X-Axis High-Frequency RMS Acceleration : " + sensor.X_Axis_High_Frequency_RMS_Acceleration_G + " G");
			        
			        sensorID = sensorID + 10; // Aggiungere un indice identificativo del sensore sul motore alla classe
					
				}
				
				System.out.println("---------- DRIVE  ----------");
				
				Iterator<Drive> drivesIter = hmiMachine.getDrives().iterator();
				
				int driveID = 10;
				
				while(drivesIter.hasNext()) {
					
					Drive drive = drivesIter.next();
					
					System.out.println("--- Drive " + driveID + " ---" );
					
					System.out.println("Actual speed: " + drive.actualSpeed + " Hz");
					System.out.println("Actual current: " + drive.actualCurrent + " A");
					System.out.println("Actual torque: " + drive.actualSpeed + " %");
					System.out.println("Fault code: " + drive.faultCode);
					System.out.println("Actual speed: " + drive.warnCode);
					System.out.println("Thermal overload: " + drive.alarmInverterThermalOverload);
					System.out.println("Motor overtemp: " + drive.alarmMotorOvertemp);
					System.out.println("Closing lookout: " + drive.closingLookoutActive);
					System.out.println("Speed reached: " + drive.compSpeedReached);
					System.out.println("Holding breake open: " + drive.holdingBrakeOpen);
					System.out.println("Limit reached: " + drive.impLimitReached);
					System.out.println("Master control request: " + drive.masterControlRequest);
					System.out.println("Rotation clockwise: " + drive.motorRotateClockwise);
					System.out.println("OFF2: " + drive.off2Inactive);
					System.out.println("OFF3: " + drive.off3Inactive);
					System.out.println("Operation enabled: " + drive.operationEnabled);
					System.out.println("Ready: " + drive.ready);
					System.out.println("Ready to start: " + drive.readyToStart);
					System.out.println("Speed deviation: " + drive.speedDeviationInTol);
					
					driveID = driveID + 10;
				}
				
				
				List<Alarm> alarmList = hmiMachine.getPlc().alarmList; 
				
				if(alarmList.size() != 0) {
				
					Iterator<Alarm> iterator = alarmList.iterator();
					
					System.out.println("---------- ALARMS LIST ----------");
					while(iterator.hasNext()) {
						
						Alarm alarm = iterator.next();
						
						System.out.println("Valore: " + alarm.getValue() + " ID: " + alarm.getId() + " Desc: " + alarm.getDescription() + " Timestamp: " + alarm.getTimestamp());
						
					}
					
				}
	
	     }
	       
		}
		
	}
	
	
	private class ObsTimerTask extends TimerTask{
		
		Hmi2ReadThread rejectFieldbusThread;
		
		public ObsTimerTask(Hmi2ReadThread rejectFieldbusThread) {
			
			this.rejectFieldbusThread = rejectFieldbusThread;
		}

		@Override
		public void run() {
			
			
		}
		
	}

}
