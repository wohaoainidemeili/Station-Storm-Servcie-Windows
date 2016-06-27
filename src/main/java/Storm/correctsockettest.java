package Storm;

import Storm.DataBaseOperation.DBInitial;
import Storm.DataBaseOperation.SensorGetAndInsert;
import Storm.SensorObservationService.SOSWrapper;
import Storm.SensorObservationService.SpoutParams;
import Storm.ServerSocketOperation.RecieveData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by Administrator on 2016/5/15.
 */
public class correctsockettest {
    public static void main(String[] args) throws InterruptedException {
        SensorConfigReader.reader();
        String stationID="urn:liesmars:insitusensor:platform:BaoxieWeatherSoilStation1";

        SpoutParams spoutParams= SensorGetAndInsert.getSpoutParams(stationID);
        ServerSocket serverSocket=null;
        try {
            serverSocket=new ServerSocket();
            serverSocket.bind(new InetSocketAddress(spoutParams.ipAddress,spoutParams.port));
        } catch (IOException e) {
            System.out.println("ServerSocket Initial failed!" + e.getMessage());
        }
        Socket socket=null;
        System.out.println("spout run again!");
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("socket accept error!" + e.getMessage());
        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for (int i=0;i>-1;i++) {
            RecieveData recieveData = new RecieveData(spoutParams);
            List<SOSWrapper> sosWrappers = recieveData.RecieveMessage(socket);

           if (sosWrappers!=null) {
               for (SOSWrapper sosWrapper : sosWrappers) {
                   sosWrapper.Insert();
               }
               Thread.sleep(10000);
           }
//            try {
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

}
