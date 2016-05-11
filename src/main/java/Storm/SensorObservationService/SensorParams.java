package Storm.SensorObservationService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * One sensor information is recorded as follows
 * Created by Yuan on 2016/5/10.
 */
public class SensorParams implements Serializable {
    public String sensorID;//the sensor ID
    public double lat;// the latitude of the sensor's location
    public double lon;// the longtitude of the senor's location
    public List<ObsProperty> properties=new ArrayList<ObsProperty>();//the properties of the sensor
}
