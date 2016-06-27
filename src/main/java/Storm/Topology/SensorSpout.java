package Storm.Topology;

import Storm.SensorObservationService.SOSWrapper;
import Storm.SensorObservationService.SpoutParams;
import Storm.ServerSocketOperation.RecieveData;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * send sos information by spout
 * Created by Yuan on 2016/5/10.
 */
public class SensorSpout extends BaseRichSpout {
    SpoutOutputCollector collector;
    SpoutParams spoutParams;
    ServerSocket serverSocket;
    Socket socket;

    public void getSpoutParams(SpoutParams spoutParams){
        this.spoutParams=spoutParams;
    }
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("SOS"));
    }

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector=collector;
        try {
            serverSocket=new ServerSocket();
            serverSocket.bind(new InetSocketAddress(spoutParams.ipAddress,spoutParams.port));
            socket=serverSocket.accept();
        } catch (IOException e) {
            System.out.println("ServerSocket Initial failed!" + e.getMessage());
        }
    }

    public void nextTuple() {
        System.out.println("spout run again!");
        RecieveData recieveData=new RecieveData(spoutParams);
        List<SOSWrapper> sosWrappers= recieveData.RecieveMessage(socket);
        //if get the sos value ,soswrappers will not be null
        if (sosWrappers!=null) {
            for (SOSWrapper sosWrapper : sosWrappers) {
                this.collector.emit(new Values(sosWrapper));
            }
            Utils.sleep(spoutParams.sleepTime);
        }

    }
}
