package Storm.Topology;

import Storm.SensorObservationService.SOSWrapper;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by Yuan on 2016/5/10.
 */
public class InsertBolt extends BaseRichBolt {
    private OutputCollector collector;
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector=collector;
    }

    public void execute(Tuple input) {
        SOSWrapper sosWrapper=(SOSWrapper)input.getValueByField("SOS");
        sosWrapper.Insert();
        System.out.println("Insert SOS Success!");
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
