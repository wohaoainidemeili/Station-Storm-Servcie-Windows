package Storm.SensorObservationService;

import java.io.Serializable;

/**
 * the property information of an observation property
 * Created by Yuan on 2016/5/10.
 */
public class ObsProperty implements Serializable{
    String id;//propertyID
    String name;//propertyName
    String unit;//propertyUnit
    int startpos;//the start position in recieved data by socket
    int len;// the length of the property in recieved data by socket
    double value;//?the result we get from the recieved data by socket
    public ObsProperty(){
    }
    public ObsProperty(String id,String name,String unit,int startpos,int len){
        this.id=id;
        this.name=name;
        this.unit=unit;
        this.startpos=startpos;
        this.len=len;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStartpos() {
        return startpos;
    }

    public void setStartpos(int startpos) {
        this.startpos = startpos;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
