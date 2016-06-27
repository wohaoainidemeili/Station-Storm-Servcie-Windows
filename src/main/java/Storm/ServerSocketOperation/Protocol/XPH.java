package Storm.ServerSocketOperation.Protocol;

import Storm.SensorConfigInfo;
import Storm.SensorObservationService.ObsProperty;
import Storm.SensorObservationService.SOSWrapper;
import Storm.SensorObservationService.SensorParams;
import Storm.SensorObservationService.SpoutParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuan on 2016/5/17.
 */
public class XPH {

    public static byte[] getSendData(SpoutParams spoutParams){
        byte[] sendData=new byte[6];
        sendData[0]= (byte)(spoutParams.slaveAddress);//the address of the equipment of sensors

        sendData[1]=3;//the read code
        sendData[2]=0x00;//in xph protocol this byte must be 0x00
        sendData[3]=0x00;// represent reading the real-time observation data

        sendData[4] = 0x00;//the high code of the last cache in socket
        sendData[5]=0x00;//the low code of the last cache in socket

        return CRC16.CRCCaculate(sendData);//caculate the CRC16 code (the 6 and 7 of sendData)
    }
    /**
     * use SOSWrapper to wrapper the sos information
     * @param spoutParams
     * @param recievedData
     * @return
     */
    public static List<SOSWrapper> solveRecievedData(SpoutParams spoutParams,byte[] recievedData){
        List<SOSWrapper> sosWrappers=new ArrayList<SOSWrapper>();
        List<SensorParams> sensors=spoutParams.sensors;//get all sensors in the station
        for (SensorParams sensor:sensors){
            SOSWrapper sosWrapper=new SOSWrapper();
            sosWrapper.setSensorID(sensor.sensorID);
            sosWrapper.setLat(sensor.lat);
            sosWrapper.setLon(sensor.lon);
            sosWrapper.setSosAddress(SensorConfigInfo.getUrl());//SOS URL
            for (ObsProperty obsProperty:sensor.properties){
                obsProperty.setValue(GetValueFromRecieveData(recievedData,obsProperty.getStartpos()));
            }
            sosWrapper.setProperties(sensor.properties);
        }

        return sosWrappers;
    }

    /**
     * change the recieved byte data into double
     * @param recieveData all the recieved data in type of byte
     * @param startPos the start position of the sensor
     * @return the double value of the property record
     */
    public static double GetValueFromRecieveData(byte[] recieveData,int startPos){
        //change the low code and high code
        short shortRes= (short)((recieveData[startPos]<<8)|(recieveData[startPos+1]&0xff));
        return shortRes*1.0/10.0;
    }
}
