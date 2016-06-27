package Storm;

import Storm.DataBaseOperation.DBInitial;
import Storm.DataBaseOperation.SensorGetAndInsert;
import Storm.SensorObservationService.SpoutParams;

/**
 * the unit not consider into the recieved program
 * Created by Yuan on 2016/5/11.
 */
public class InsertOneStationMain {
    public static void main(String[] args) {
        SensorConfigReader.reader();
        DBInitial.initial();
        String stationID = "urn:liesmars:insitusensor:platform:BaoxieWeatherSoilStation1";
        SpoutParams spoutParams = SensorGetAndInsert.fromXMLToParams(stationID);
        spoutParams.ipAddress="202.114.118.60";
        spoutParams.port=9007;
        spoutParams.startingAddress=0;
        spoutParams.slaveAddress=1;
        spoutParams.sleepTime=60*60;
        spoutParams.socketTimeOut=200000;
        spoutParams.logFile="C:\\test";
        spoutParams.protocol="modbus";
        //0 ph 1 temperature 2 water level
        spoutParams.sensors.get(0).properties.get(0).setStartpos(3);
        spoutParams.sensors.get(1).properties.get(0).setStartpos(5);
        spoutParams.sensors.get(2).properties.get(0).setStartpos(7);
        spoutParams.sensors.get(3).properties.get(0).setStartpos(19);
        spoutParams.sensors.get(4).properties.get(0).setStartpos(21);
        spoutParams.sensors.get(5).properties.get(0).setStartpos(25);
        spoutParams.sensors.get(6).properties.get(0).setStartpos(27);
        spoutParams.sensors.get(7).properties.get(0).setStartpos(31);

        spoutParams.sensors.get(0).properties.get(0).setLen(2);
        spoutParams.sensors.get(1).properties.get(0).setLen(2);
        spoutParams.sensors.get(2).properties.get(0).setLen(2);
        spoutParams.sensors.get(3).properties.get(0).setLen(2);
        spoutParams.sensors.get(4).properties.get(0).setLen(2);
        spoutParams.sensors.get(5).properties.get(0).setLen(2);
        spoutParams.sensors.get(6).properties.get(0).setLen(2);
        spoutParams.sensors.get(7).properties.get(0).setLen(2);

        SensorGetAndInsert.insertSpoutParams(spoutParams);


    }

}
