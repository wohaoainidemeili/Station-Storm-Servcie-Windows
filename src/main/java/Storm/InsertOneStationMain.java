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
        String stationID = "urn:liesmars:insitusensor:platform:BaoxieHydrologicalStation";
        SpoutParams spoutParams = SensorGetAndInsert.fromXMLToParams(stationID);
        spoutParams.ipAddress="202.114.118.60";
        spoutParams.port=9006;
        spoutParams.startingAddress=0;
        spoutParams.slaveAddress=1;
        spoutParams.socketTimeOut=200000;
        spoutParams.logFile="C:\\test";
        spoutParams.protocol="modbus";
        //0 ph 1 temperature 2 water level
        spoutParams.sensors.get(0).properties.get(0).setStartpos(29);
        spoutParams.sensors.get(1).properties.get(0).setStartpos(23);
        spoutParams.sensors.get(2).properties.get(0).setStartpos(33);

        spoutParams.sensors.get(0).properties.get(0).setLen(2);
        spoutParams.sensors.get(1).properties.get(0).setLen(2);
        spoutParams.sensors.get(2).properties.get(0).setLen(2);

        SensorGetAndInsert.insertSpoutParams(spoutParams);


    }

}
