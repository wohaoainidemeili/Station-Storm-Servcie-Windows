package Storm.ServerSocketOperation.Protocol;


import Storm.SensorConfigInfo;
import Storm.SensorObservationService.ObsProperty;
import Storm.SensorObservationService.SOSWrapper;
import Storm.SensorObservationService.SensorParams;
import Storm.SensorObservationService.SpoutParams;

import java.util.ArrayList;
import java.util.List;

/**
 * the modbus protocol to solve the recieved data
 * Created by Yuan on 2016/4/19.
 */
public class Moudus {
    /**
     *  create the query data command
     * @param spoutParams the station parameters
     * @return the query command
     */
    public static byte[] getSendData(SpoutParams spoutParams){
        byte[] sendData=new byte[8];
        sendData[0]= (byte)(spoutParams.slaveAddress);//the address of the equipment of sensors

        sendData[1]=3;//the read code
        sendData[2]=(byte)(spoutParams.startingAddress>>>8);//the high code of the first cache in socket
        sendData[3]=(byte)(spoutParams.startingAddress);// the low code of the first cache in socket

        sendData[4] = 0x00;//the high code of the last cache in socket
        sendData[5]=0x10;//the low code of the last cache in socket

        sendData[6] = 0x00;//CRC high code
        sendData[7] = 0x00;//CRC low code

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
