package it.beltek.ia.iotlab.edge.gateway.device;

import it.beltek.ia.iotlab.edge.gateway.ConnectionState;

public class Drive {
	
	// Measure timestamp
    public String timestamp;
    
    // Connection value (measure quality)
    public ConnectionState connectionState;
	
	// Machine state
	public int actualSpeed;
	
	// Actual current
	public int actualCurrent;
	
	// Actual torque
	public int actualTorque;

	// Warn code
	public int warnCode;
	
	// Fault code
	public int faultCode;
	
	// Speed_deviation_in_tol
	public boolean speedDeviationInTol;
	
	// Master_control_request
	public boolean masterControlRequest;
	
	// Comp_speed_reached
	public boolean compSpeedReached;
	
	// I_M_P_limit_reached
	public boolean impLimitReached;
	
	// Holding_brake_open
	public boolean holdingBrakeOpen;
	
	// Alarm_motor_overtemp
	public boolean alarmMotorOvertemp;
	
	// Motor_rotates_clockwise
	public boolean motorRotateClockwise;
	
	// Alarm_inverter_thermal_overload
	public boolean alarmInverterThermalOverload;
	
	// Ready_to_start
	public boolean readyToStart;
	
	// Ready
	public boolean ready;
	
	// Operation_enabled
	public boolean operationEnabled;
	
	// Fault_active
	public boolean faultActive;
	
	// OFF2_inactive
	public boolean off2Inactive;
	
	// OFF3_inactive
	public boolean off3Inactive;
	
	// Closing_lookout_active
	public boolean closingLookoutActive;
	
	// Alarm active
	public boolean alarmActive;
	
	// Setpoint
	public float setpoint;
	
}
