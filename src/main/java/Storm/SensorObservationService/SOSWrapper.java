package Storm.SensorObservationService;

import Storm.SensorConfigInfo;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the sensor information for insert into database by sos
 * Created by Yuan on 2016/5/10.
 */
public class SOSWrapper implements Serializable {
    //the sensorID
    String sensorID;
    //the properties of the sensor
    public List<ObsProperty> properties=new ArrayList<ObsProperty>();
    String simpleTime;//the observation time of the observation
    Double lon;//sensor longtitude
    Double lat;//sensor latitude
    String sosAddress;//sos url
    public SOSWrapper(){}
    public SOSWrapper(String sensorID,String simpleTime,Double lon,Double lat,String sosAddress,List<ObsProperty>properties){
        this.sensorID=sensorID;
        this.simpleTime=simpleTime;
        this.lon=lon;
        this.lat=lat;
        this.sosAddress=sosAddress;
        this.properties=properties;
    }

    /**
     * Dom4j to create xml to insert observation
     * @return changed xml
     */
    public String ChangeInsertXML(){
        String result="";
        //use dom4j to create the observation XML
        SAXReader saxReader=new SAXReader();//SAXReader to load the xml document of the given model in the resources
        Map nameSpace=new HashMap();
        nameSpace.put("sos", "http://www.opengis.net/sos/1.0");
        nameSpace.put("ows", "http://www.opengis.net/ows/1.1");
        nameSpace.put("ogc", "http://www.opengis.net/ogc");
        nameSpace.put("om", "http://www.opengis.net/om/1.0");
        nameSpace.put("sa", "http://www.opengis.net/sampling/1.0");
        nameSpace.put("gml", "http://www.opengis.net/gml");
        nameSpace.put("swe", "http://www.opengis.net/swe/1.0.1");
        nameSpace.put("xlink", "http://www.w3.org/1999/xlink");
        nameSpace.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(nameSpace);//Set the namespace of the xml
        try {
            org.dom4j.Document document=saxReader.read(ClassLoader.getSystemResource("InsertObservation.xml"));

            //get sensor ID node and change the sensorID
            Node sensorIDNode=document.selectSingleNode("/sos:InsertObservation/sos:AssignedSensorId");
            sensorIDNode.setText(sensorID);

            //get time node and change the observation time
            Node simpleTimeNode=document.selectSingleNode("/sos:InsertObservation/om:Observation/om:samplingTime/gml:TimeInstant/gml:timePosition");
            simpleTimeNode.setText(simpleTime);

            //get the procedure and change the procedure
            Node procedureNode=document.selectSingleNode("/sos:InsertObservation/om:Observation/om:procedure/@xlink:href");
            procedureNode.setText(sensorID);

            //get the positionID Node and change the position ID
            Node spIDNode=document.selectSingleNode("/sos:InsertObservation/om:Observation/om:featureOfInterest/sa:SamplingPoint/@gml:id");
            spIDNode.setText(lat+" "+lon);

            //get the positionName Node and change the name of it
            Node spNameNode=document.selectSingleNode("/sos:InsertObservation/om:Observation/om:featureOfInterest/sa:SamplingPoint/gml:name");
            spNameNode.setText(lat+" "+lon);

            //get the position node and change the position value
            Node spPosNode=document.selectSingleNode("/sos:InsertObservation/om:Observation/om:featureOfInterest/sa:SamplingPoint/sa:position/gml:Point/gml:pos");
            spPosNode.setText(lat+" "+lon);

            //get the property Node
            Node propertyNode=document.selectSingleNode("/sos:InsertObservation/om:Observation/om:observedProperty/swe:CompositePhenomenon");

            //get the observation record Node
            Node resultDataNode=document.selectSingleNode("/sos:InsertObservation/om:Observation/om:result/swe:DataArray/swe:elementType/swe:DataRecord");

            String resultValue="";
            int valueIndex=0;
            //change the property Node and observation record Node
            for(ObsProperty property:properties){
                //add <swe:component> Node under <swe:CompositePhenomenon> Node
                Element propertyEle=(Element)propertyNode;//change propertyNode to element type
                Element forAddProperty= DocumentHelper.createElement("swe:component");//create new <swe:component> element
                forAddProperty.addAttribute("xlink:href", property.getId());
                propertyEle.add(forAddProperty);//add the <swe:component> Node under <swe:CompositePhenomenon> Node

                //create<swe:field>under<swe:DataRecord>Node
                Element resultDataEle=(Element)resultDataNode;
                //create <swe:field> Node under <swe:DataRecord>Node
                Element forAddFiled=DocumentHelper.createElement("swe:field");//
                forAddFiled.addAttribute("name",property.getName());
                //create  <swe:Quantity> Node  under <swe:field> node
                Element forAddQuantity=DocumentHelper.createElement("swe:Quantity");
                forAddQuantity.addAttribute("definition", property.getId());
                //create <swe:uom>node under<swe:quantity>node
                Element forAddUom=DocumentHelper.createElement("swe:uom");
                forAddUom.addAttribute("code",property.getUnit());
                //add the nodes
                forAddQuantity.add(forAddUom);
                forAddFiled.add(forAddQuantity);
                resultDataEle.add(forAddFiled);

                //
                if (valueIndex==0){
                    resultValue=String.valueOf(property.getValue());
                }else {
                    resultValue = resultValue +","+ property.getValue();
                }
                valueIndex++;
            }
            //change record value
            Node resultValueNode=document.selectSingleNode("/sos:InsertObservation/om:Observation/om:result/swe:DataArray/swe:values");
            resultValueNode.setText(simpleTime+","+resultValue+";");

            //transmit document to string
            result= document.asXML();

        } catch (DocumentException e) {
            System.out.println("open InsertObservation.xml doucunment error!"+e.getMessage());
        }
        System.out.println(result);

        return result;
    }

    /**
     * insert sos operation
            * @return the insert result success or failed
            */
    public String Insert(){
        String postReturnStr=null;
        HttpRequestAndPost httpRequestAndPost=new HttpRequestAndPost();
        postReturnStr=httpRequestAndPost.sendPost(SensorConfigInfo.getUrl(),ChangeInsertXML());//get result
        System.out.println(postReturnStr);
        return postReturnStr;
    }
    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public List<ObsProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ObsProperty> properties) {
        this.properties.addAll( properties);
    }

    public String getSimpleTime() {
        return simpleTime;
    }

    public void setSimpleTime(String simpleTime) {
        this.simpleTime = simpleTime;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getSosAddress() {
        return sosAddress;
    }

    public void setSosAddress(String sosAddress) {
        this.sosAddress = sosAddress;
    }

}
