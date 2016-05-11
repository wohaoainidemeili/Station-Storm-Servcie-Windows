package Storm.DataBaseOperation;

import Storm.SensorConfigInfo;
import Storm.SensorObservationService.HttpRequestAndPost;
import Storm.SensorObservationService.ObsProperty;
import Storm.SensorObservationService.SensorParams;
import Storm.SensorObservationService.SpoutParams;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class is used to get and insert the needed parameters of sensors
 * Created by Yuan on 2016/5/9.
 */
public class SensorGetAndInsert {
    /**
     * get the parameters needed in the storm program by given StationID
     * @param StationID the station ID of Sensors
     * @return the parametes of the station to recieve data by socket
     */
    public static SpoutParams getSpoutParams(String StationID){
        SpoutParams spoutParams=new SpoutParams();
        spoutParams.stationID=StationID;
        //initial the mysql database connection configure
        String host= SensorConfigInfo.getHost();
        String user=SensorConfigInfo.getUser();
        String passWord=SensorConfigInfo.getPassword();
        String dbname= SensorConfigInfo.getDbName();
        String driver=SensorConfigInfo.getDriver();

        //get the connection pool of the database
        SqlConnectionPool connectionPool=new SqlConnectionPool(host,dbname,user,passWord,driver);
        Connection con= connectionPool.getConnection();

        //get station info
        String stationInfoQuery="Select * from " +SensorConfigInfo.getStationtableName()+" where "+SensorConfigInfo.getSat_id()+"='"+StationID+"'";
        //get the sensors of the station
        String sensorInfoQuery="select sensor.* from "+SensorConfigInfo.getSensorTableName()+" sensor,"
                +SensorConfigInfo.getSetsatTableName()+" setsat where sensor."+SensorConfigInfo.getSet_id()+"=setsat."+SensorConfigInfo.getSet_id()
                +" and setsat."+SensorConfigInfo.getSat_id()+"='"+StationID+"'";
        //get properties of echo sensor
       // String propertyInfoQuery="select * from "+SensorConfigInfo.getPropertyTableName()+"property,"+SensorConfigInfo.getPtsetTableName()+
      //  "ptset where "+SensorConfigInfo.getPt_propertyID()+"='"+SensorID+"'";

        Statement state=null;
        ResultSet resultSet=null;
        try {
            state =con.createStatement();
            resultSet=state.executeQuery(stationInfoQuery);
            while (resultSet.next()) {
                String ip = resultSet.getString(SensorConfigInfo.getSat_ip());
                int port = resultSet.getInt(SensorConfigInfo.getSat_port());
                int startingAddress = resultSet.getInt(SensorConfigInfo.getSat_startingAddress());
                int slaveAddress = resultSet.getInt(SensorConfigInfo.getSat_slaveAddress());
                int socketOutOfTime = resultSet.getInt(SensorConfigInfo.getSat_socketOutofTime());
                int sleepTime = resultSet.getInt(SensorConfigInfo.getSat_sleepTime());
                String logFile = resultSet.getString(SensorConfigInfo.getSat_logFile());
//                double lat = resultSet.getDouble(SensorConfigInfo.getSt_lat());
//                double lon = resultSet.getDouble(SensorConfigInfo.getSt_lon());
                String protocol = resultSet.getString(SensorConfigInfo.getSat_protocal());

                spoutParams.ipAddress = ip;
                spoutParams.port = port;
                spoutParams.startingAddress = startingAddress;
                spoutParams.slaveAddress = slaveAddress;
                spoutParams.socketTimeOut = socketOutOfTime;
                spoutParams.sleepTime = sleepTime;
                spoutParams.logFile = logFile;
//                spoutParams.lat = lat;
//                spoutParams.lon = lon;
                spoutParams.protocol = protocol;

                //get the sensors of the station
                Statement sensorState=con.createStatement();
                ResultSet sensorResultSet = sensorState.executeQuery(sensorInfoQuery);
                while (sensorResultSet.next()) {
                    String sensorID=sensorResultSet.getString(SensorConfigInfo.getSet_id());
                    double lat=sensorResultSet.getDouble(SensorConfigInfo.getSet_lat());
                    double lon=sensorResultSet.getDouble(SensorConfigInfo.getSet_lon());

                    //create sensor container
                    SensorParams sensorParams=new SensorParams();
                    sensorParams.sensorID=sensorID;
                    sensorParams.lat=lat;
                    sensorParams.lon=lon;
                    //get properties of the sensor
                    String propertyInfoQuery="select property.* from "+SensorConfigInfo.getPropertyTableName()+" property,"+SensorConfigInfo.getPtsetTableName()+
                            " ptset where property."+SensorConfigInfo.getPt_propertyID()+"=ptset."+SensorConfigInfo.getPt_propertyID()
                            +" and ptset."+SensorConfigInfo.getSet_id()+"='"+sensorID+"'";
                    Statement propertyState=con.createStatement();
                    ResultSet propertyResult=propertyState.executeQuery(propertyInfoQuery);
                    while (propertyResult.next()) {
                        String propertyID = propertyResult.getString(SensorConfigInfo.getPt_propertyID());
                        String propertyName = propertyResult.getString(SensorConfigInfo.getPt_propertyName());
                        String propertyUnit = propertyResult.getString(SensorConfigInfo.getPt_propertyUnit());
                        int propertyStartPos = propertyResult.getInt(SensorConfigInfo.getPt_propertyStartPos());
                        int propertyLen = propertyResult.getInt(SensorConfigInfo.getPt_propertyLen());

                        //get observation property and create obsproperty
                        ObsProperty obsProperty=new ObsProperty(propertyID,propertyName,propertyUnit,propertyStartPos,propertyLen);
                        sensorParams.properties.add(obsProperty);
                    }
                    //get sensor information and submmit to the SpoutParams
                    spoutParams.sensors.add(sensorParams);
                }
            }

        } catch (SQLException e) {
            System.out.println("SQLException:"+e.getMessage());
        }
        //release the statement and connection
        finally {
            if (state!=null){
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return spoutParams;
    }

    /**
     * insert the parameters of one station into the database
     * @param spoutParams
     */
    public static void insertSpoutParams(SpoutParams spoutParams){
        String host=SensorConfigInfo.getHost();
        String user=SensorConfigInfo.getUser();
        String passWord=SensorConfigInfo.getPassword();
        String dbname= SensorConfigInfo.getDbName();
        String driver=SensorConfigInfo.getDriver();

        //create sql connection pool
        SqlConnectionPool connectionPool=new SqlConnectionPool(host,dbname,user,passWord,driver);
        Connection con= connectionPool.getConnection();
        Statement statement=null;

        try {
            statement=con.createStatement();
            //insert station table
            String InsertStation="insert into "+SensorConfigInfo.getStationtableName()+" values('"
                    +spoutParams.stationID+"','"+spoutParams.ipAddress+"',"+spoutParams.port+","
                    +spoutParams.startingAddress+","+spoutParams.slaveAddress+","
                    +spoutParams.socketTimeOut+","+spoutParams.sleepTime+",'"+spoutParams.logFile+"','"
                    +spoutParams.protocol+"')";
            statement.execute(InsertStation);
            for (SensorParams sensorParams:spoutParams.sensors){
                //insert sensor table
                String InsertSensor="insert into "+SensorConfigInfo.getSensorTableName()+" values('"
                        +sensorParams.sensorID+"',"+sensorParams.lon+","+sensorParams.lat+")";

                //create the relationship between sensor and station
                String InsertSensorOfStation="insert into "+SensorConfigInfo.getSetsatTableName()+" values ('"
                        +spoutParams.stationID+"','"+sensorParams.sensorID+"')";
                statement.execute(InsertSensor);
                statement.execute(InsertSensorOfStation);
                for (ObsProperty obsProperty:sensorParams.properties){
                    //insert property if the property is not all same as any of the properties in property table
                    String InsertProperty="insert ignore into "+SensorConfigInfo.getPropertyTableName()
                            +"(" +SensorConfigInfo.getPt_propertyID()+","+SensorConfigInfo.getPt_propertyName()+","
                            +SensorConfigInfo.getPt_propertyUnit()+","+SensorConfigInfo.getPt_propertyStartPos()+","
                            +SensorConfigInfo.getPt_propertyLen()+") values('"+obsProperty.getId()+"','"
                            +obsProperty.getName()+"','"+obsProperty.getUnit()+"',"+obsProperty.getStartpos()+","
                            +obsProperty.getLen()+")";
                    //create the relationship between property and sensor
                    String InsertPropertyOfSensor="insert into "+SensorConfigInfo.getPtsetTableName()+" values('"
                            +sensorParams.sensorID+"','"+obsProperty.getId()+"')";
                    statement.execute(InsertProperty);
                    statement.execute(InsertPropertyOfSensor);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Execute Error!"+e.getMessage());
        }


    }

    /**
     * get some sensor parameters from the sos
     * @param StationID
     * @return
     */
    public static SpoutParams fromXMLToParams(String StationID){
        SpoutParams spoutParams=new SpoutParams();
        spoutParams.stationID=StationID;

        //read the describeSensor.xml and create a new station describe sos
        SAXReader saxReader=new SAXReader();
        Map nameSpace=new HashMap();
        nameSpace.put("sos","http://www.opengis.net/sos/1.0");
        nameSpace.put("xsi","http://www.w3.org/2001/XMLSchema-instance");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(nameSpace);
        Document document=null;
        try {
            document = saxReader.read(ClassLoader.getSystemResourceAsStream("DescribeSensor.xml"));
            //change the procedure ID to Station ID
            Node propertyNode= document.selectSingleNode("/sos:DescribeSensor/sos:procedure");
            propertyNode.setText(StationID);
        } catch (DocumentException e) {
            System.out.println("Construct Station Desrcibe XML Error!"+e.getMessage());
        }
        //get the DescirbeSensor.xml String
        String sendMessage=document.asXML();

        //post the station describe xml to SOS url like http://gsw.whu.edu.cn:8080/SOSv3.5.0/sos
        HttpRequestAndPost httpRequestAndPost=new HttpRequestAndPost();
        String stationDataXML=httpRequestAndPost.sendPost(SensorConfigInfo.getUrl(), sendMessage);

        //solve the returned station describe xml string
        SAXReader saxReader1=new SAXReader();
        //this is needed in windows system but must be deleted in Linux system
        //due to the different encoding
        //saxReader1.setEncoding("GB2312");

        Map nameSpace1=new HashMap();//xml NameSpace
        nameSpace1.put("sml","http://www.opengis.net/sensorML/1.0.1");
        nameSpace1.put("gml","http://www.opengis.net/gml");
        nameSpace1.put("swe","http://www.opengis.net/swe/1.0.1");
        nameSpace1.put("xlink","http://www.w3.org/1999/xlink");
        nameSpace1.put("xsi","http://www.w3.org/2001/XMLSchema-instance");
        nameSpace1.put("ows","http://www.opengeospatial.net/ows");
        nameSpace1.put("ogc","http://www.opengis.net/ogc");
        nameSpace1.put("om", "http://www.opengis.net/om/1.0");
        saxReader1.getDocumentFactory().setXPathNamespaceURIs(nameSpace1);//set NameSpace

        Document document1=null;
        InputStream inputStream=new ByteArrayInputStream(stationDataXML.getBytes());
        try {

            document1=saxReader1.read(inputStream);
            //get the sensor nodes of the station
            List<Node> sensorNodes=document1.selectNodes("/sml:SensorML/sml:member/sml:System/sml:components/sml:ComponentList/sml:component");
            for (Node sensor:sensorNodes){
                SensorParams sensorParams=new SensorParams();
                //get the sensorID
                Element sensorEle=(Element)sensor;
                String sensorID= sensorEle.attribute("href").getText();
                sensorParams.sensorID=sensorID;
                //create describe sensor xml
                saxReader.getDocumentFactory().setXPathNamespaceURIs(nameSpace);
                Document sensorDescribeQueryXMLDoucment= saxReader.read(ClassLoader.getSystemResourceAsStream("DescribeSensor.xml"));
                Node sensorIDNode=sensorDescribeQueryXMLDoucment.selectSingleNode("/sos:DescribeSensor/sos:procedure");
                sensorIDNode.setText(sensorID);
                String sendSensorXML=sensorDescribeQueryXMLDoucment.asXML();

                //post the describe sensor xml and get the returned xml
                String sensorDescribeXML= httpRequestAndPost.sendPost(SensorConfigInfo.getUrl(), sendSensorXML);
                InputStream sensorInputStream=new ByteArrayInputStream(sensorDescribeXML.getBytes());
                //set the namespace again or it will be throw an exception
                saxReader1.getDocumentFactory().setXPathNamespaceURIs(nameSpace1);
                Document sensorDocument=saxReader1.read(sensorInputStream);

                //get the latitude and longtitude
                List<Node> posNodes= sensorDocument.selectNodes("/sml:SensorML/sml:member/sml:System/sml:position/swe:Position/swe:location/swe:Vector/swe:coordinate/swe:Quantity");
                for (Node node:posNodes) {
                    Element ele = (Element) node;
                    //use axisID attribute to judge the current node is latitude or longtitude
                    if (ele.attribute("axisID").getText().equals("y")){
                        Node valueNode= ele.selectSingleNode("swe:value");
                        sensorParams.lat=Double.valueOf(valueNode.getText());
                    }else if(ele.attribute("axisID").getText().equals("x")){
                        Node valueNode=ele.selectSingleNode("swe:value");
                        sensorParams.lon=Double.valueOf(valueNode.getText());
                    }
                }

                //property
                List<Node> propertyNodes=sensorDocument.selectNodes("/sml:SensorML/sml:member/sml:System/sml:outputs/sml:OutputList/sml:output");
                for (Node node:propertyNodes){
                    ObsProperty property=new ObsProperty();//new Property
                    Element ele=(Element)node;
                    //get the property name
                    property.setName(ele.attribute("name").getText());
                    //swe:Quantity get the property id
                    Element quanEle=(Element)ele.selectSingleNode("swe:Quantity");
                    property.setId(quanEle.attribute("definition").getText());
                    //swe:uom
                    Element uomEle=(Element)quanEle.selectSingleNode("swe:uom");
                    property.setUnit(uomEle.attribute("code").getText());
                    sensorParams.properties.add(property);
                }
                //add the current sensor
                spoutParams.sensors.add(sensorParams);
            }

        } catch (DocumentException e) {
            System.out.println("Open returned station describe xml error!"+e.getMessage());
        }

        return spoutParams;
    }
}
