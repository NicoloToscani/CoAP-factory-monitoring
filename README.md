# CoAP-factory-monitoring
Industrial object modelling using CoAP protocol and Californium framework

## Description
CoAP-factory-monitoring implements a possible [IIoT](https://en.wikipedia.org/wiki/Industrial_internet_of_things) scenario for factory data exchange in an [Industry 4.0](https://en.wikipedia.org/wiki/Fourth_Industrial_Revolution) perspective with [CoAP](https://en.wikipedia.org/wiki/Fourth_Industrial_Revolution) protocol using [Californium](https://www.eclipse.org/californium/) framework.

The mains goals of this project are:
- Production monitoring in a manufacturing plant from different company levels
- Data distribution inside the plant using CoAP protocol for future data analysis and predictive maintenance
- Model different data acquisition devices for hiding low level field communication protocol implementation details

### Factory scenario
![factory-scenario](Factory_Scenario.png)

### Factory modelling
![factory-modelling](Factory_Modelling.png)

## Usage
The possible uses of this application are shown below:

### Run Master Repository
* Run MasterRepository.java in `it.beltek.ia.iotlab.edge.database`
### Run Device Gateway
Compose different machines and lines running a process contained in `it.beltek.ia.iotlab.edge.gateway`
* PlcGateway.java
* Pm3200Gateway.java
* RejectGateway.java
* DriveGateway.java: run a process for each motor drive and specify also the drive numbers (Drive ID)
* Qm42vt2Gateway.java: run a process for each motor sensor and specify also the sensor numbers (Drive ID)


