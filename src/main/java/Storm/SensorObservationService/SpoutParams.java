package Storm.SensorObservationService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Parameters of the Station
 * in order to pass the params in storm
 * the class must implements java.io.Serializable class
 * Created by Yuan on 2016/5/10.
 */
public class SpoutParams implements Serializable{
    public String stationID;// station ID
    // the station contained sensors
    public List<SensorParams> sensors=new ArrayList<SensorParams>();
    public int sleepTime;// the sleep time of one spout tuple the data
    public  String ipAddress;// the socket ip
    public  int port; //the socket port
    public  int startingAddress;//the start positon where start to get by socket
    public  int slaveAddress;//the slave address of the socket
    public  int socketTimeOut;// how long can we wait if the socket don't response
    public  String logFile;// the log file path
    public  String protocol;//the protocol the sensor used
}
