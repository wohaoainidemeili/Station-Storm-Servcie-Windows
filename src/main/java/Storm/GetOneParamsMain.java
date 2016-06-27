package Storm;

import Storm.DataBaseOperation.SensorGetAndInsert;
import Storm.SensorObservationService.SpoutParams;

/**
 * Created by root on 16-5-11.
 */
public class GetOneParamsMain {
    public static void main(String[] args){

        SensorConfigReader.reader();
        String stationID = "urn:liesmars:insitusensor:platform:BaoxieHydrologicalStation";
        SpoutParams spoutParams= SensorGetAndInsert.getSpoutParams(stationID);
        int x=0;

    }
}
