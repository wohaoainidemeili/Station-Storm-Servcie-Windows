package Storm.ServerSocketOperation;

import Storm.SensorObservationService.SOSWrapper;
import Storm.SensorObservationService.SpoutParams;
import Storm.ServerSocketOperation.Protocol.CRC16;
import Storm.ServerSocketOperation.Protocol.Moudus;
import Storm.ServerSocketOperation.Protocol.XPH;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Yuan on 2016/5/10.
 */
public class RecieveData {
    private SpoutParams spoutParams;
    public RecieveData(SpoutParams spoutParams){
        this.spoutParams=spoutParams;
    }
    public List<SOSWrapper> RecieveMessage(Socket socketClient)
    {
        try {
            socketClient.setSoTimeout(spoutParams.socketTimeOut);
        } catch (SocketException e) {
            System.out.println("Socket set out time error!");
        }
        List<SOSWrapper> sosWrappers=null;
        try {

            System.out.print("get start!");
            OutputStream outputStream=socketClient.getOutputStream();//send the command to sensors by the socket outputstream
            byte[] sendData=null;
            //use different protocol to sendData
            if(spoutParams.protocol.equals("modbus")){
                sendData= Moudus.getSendData(spoutParams);
            }else if(spoutParams.protocol.equals("xph")){
                sendData= XPH.getSendData(spoutParams);
            }
            else {
                System.out.println("Unkown Protocol!");
                Thread.interrupted();
            }
            outputStream.write(sendData);//use outputstream to send data
            outputStream.flush();

            InputStream inputStream=socketClient.getInputStream();//get the returned result by the socket
            //recieved data to store in recievedData bytes
            byte[] recievedData=new byte[1024];

            int recieveDataLen= inputStream.read(recievedData);//get the length of the recieved data
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");//get the recieved data time
            String recieveDataTime=df.format(new Date());

            // use flag to present the CRC16 check is pass or not,pass is true
            boolean crcflag=false;
            //use modbus to analysis the reuslt to get the property value
            if(spoutParams.protocol.equals("modbus")) {
                //For CRC check
                byte[] recieveBuffer=new byte[recieveDataLen];
                for (int i=0;i<recieveDataLen;i++){
                    recieveBuffer[i]=recievedData[i];
                }
                //use CRC16 check the result
                if (crcflag=CRC16.CRCcheck(recieveBuffer)) {
                    System.out.println("CRC check passed!");
                    sosWrappers = Moudus.solveRecievedData(spoutParams, recieveBuffer);
                }
            }else if (spoutParams.protocol.equals("xph")){
                byte[] recieveBuffer=new byte[recieveDataLen];
                for (int i=0;i<recieveDataLen;i++){
                    recieveBuffer[i]=recievedData[i];
                }
                //use CRC16 check the result
                if (crcflag=CRC16.CRCcheck(recieveBuffer)) {
                    System.out.println("CRC check passed!");
                    sosWrappers = XPH.solveRecievedData(spoutParams, recieveBuffer);
                }
            }else {
                System.out.println("Unkown protocol!");
                Thread.interrupted();
            }
            //change the simpleTime
            if (crcflag) {
                for (SOSWrapper sosWrapper : sosWrappers) {
                    sosWrapper.setSimpleTime(recieveDataTime);
                }
            }

        } catch (IOException e) {
            System.out.println("socket analysis error: "+e.getMessage());
        }
        return sosWrappers;

    }
}
