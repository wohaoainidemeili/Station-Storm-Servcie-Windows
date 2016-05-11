package Storm.Topology;

import Storm.DataBaseOperation.SensorGetAndInsert;
import Storm.SensorObservationService.SpoutParams;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

/**
 * Created by Yuan on 2016/5/10.
 */
public class ObservationRecieveTopology {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        String stationID="";
        String spoutID="Senor_Spout";
        String blotID="Blot_ID";
        String topologyName="Station";

        SpoutParams spoutParams= SensorGetAndInsert.getSpoutParams(stationID);
        SensorSpout sensorSpout=new SensorSpout();
        sensorSpout.getSpoutParams(spoutParams);
        InsertBolt insertBolt=new InsertBolt();

        TopologyBuilder builder=new TopologyBuilder();
        builder.setSpout(spoutID, sensorSpout);
        builder.setBolt(blotID,insertBolt).fieldsGrouping(spoutID, new Fields("SOS"));

        Config config=new Config();
        if (args.length==0){
            LocalCluster localCluster=new LocalCluster();
            localCluster.submitTopology(topologyName,config,builder.createTopology());
            Utils.sleep(100000);
            localCluster.killTopology(topologyName);
            localCluster.shutdown();
        }else{
            StormSubmitter.submitTopology(topologyName,config,builder.createTopology());
        }
    }
}
