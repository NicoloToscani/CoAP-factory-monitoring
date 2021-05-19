/**
 * 
 */
package it.beltek.ia.iotlab.edge.gateway;

/**
 * The interface {@code Device} defines the behavior of the physical device defining main methods used to interact with the edge device.
 * This interface can be implemented by a class that using a specific communication protocol (e.g. Siemens S7Protocol, Ethernet/IP, Modbus TCP-IP, ...). 
 * 
 * @author Nicolò Toscani
 * @version 1.0
 * 
 */
public interface Device {
	
	public void Connect();
	
	public void Disconnect();
	
	public boolean IsAlive();
	

}
