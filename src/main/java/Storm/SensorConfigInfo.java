package Storm;

import java.util.Properties;

/**
 * Created by Yuan on 2016/5/10.
 */
public class SensorConfigInfo {
    //basic properties
    String URL="url";
    String HOST="host";
    String DRIVER="driver";
    String USER="user";
    String PASSWORD="password";

    String DBNAME="dbName";

    //station table and attributes
    String STATIONTABLENAME="stationTableName";
    String SAT_ID="stationID";
    String SAT_IP="ip";
    String SAT_PORT="port";
    String SAT_STARTINGADDRESS="startingAddress";
    String SAT_SLAVEADDRESS="slaveAddress";
    String SAT_SOCKETOUTOFTIME="socketOutTime";
    String SAT_SLEEPTIME="sleepTime";
    String SAT_LOGFILE="logFile";
    String SAT_PROTOCOL="protocol";

    //sensor table and attributes
    String SENSORTABLENAME="sensorTableName";
    String SET_ID="sensorID";
    String SET_LAT="lat";
    String SET_LON="lon";

    //observation property table and attributes
    String PROPERTYTABLENAME="propertyTableName";
    String PT_ID="id";
    String PT_PROPERTYID="propertyID";
    String PT_PROPERTYNAME="propertyName";
    String PT_PROPERTYUNIT="propertyUnit";
    String PT_PROPERTYSTARTPOS="propertyStartPos";
    String PT_PROPERTYSTARTLEN="propertyLen";

    //the connection of sensor ¡¢station and property
    String SETSATTABLENAME="sensorofstationTableName";
    String PTSATTABLENAME="propertyofstationTableName";
    String PTSETTABLENAME="propertyofSensorTableName";

    private static String url;
    private static String host;
    private static String driver;
    private static String user;
    private static String password;

    private static String dbName;

    private static String stationtableName;
    private static String sat_id;
    private static String sat_ip;
    private static String sat_port;
    private static String sat_startingAddress;
    private static String sat_slaveAddress;
    private static String sat_socketOutofTime;
    private static String sat_sleepTime;
    private static String sat_logFile;
    private static String sat_protocal;

    private static String sensorTableName;
    private static String set_id;
    private static String set_lon;
    private static String set_lat;

    private static String propertyTableName;
    private static String pt_id;
    private static String pt_propertyID;
    private static String pt_propertyName;
    private static String pt_propertyUnit;
    private static String pt_propertyStartPos;
    private static String pt_propertyLen;

    private static String setsatTableName;
    private static String ptsatTableName;
    private static String ptsetTableName;

    public SensorConfigInfo(Properties properties){
        setUrl(properties.getProperty(URL));
        setDriver(properties.getProperty(DRIVER));
        setHost(properties.getProperty(HOST));
        setUser(properties.getProperty(USER));
        setPassword(properties.getProperty(PASSWORD));

        setDbName(properties.getProperty(DBNAME));
        setStationtableName(properties.getProperty(STATIONTABLENAME));
        setSat_id(properties.getProperty(SAT_ID));
        setSat_ip(properties.getProperty(SAT_IP));
        setSat_port(properties.getProperty(SAT_PORT));
        setSat_startingAddress(properties.getProperty(SAT_STARTINGADDRESS));
        setSat_slaveAddress(properties.getProperty(SAT_SLAVEADDRESS));
        setSat_socketOutofTime(properties.getProperty(SAT_SOCKETOUTOFTIME));
        setSat_sleepTime(properties.getProperty(SAT_SLEEPTIME));
        setSat_logFile(properties.getProperty(SAT_LOGFILE));
        setSat_protocal(properties.getProperty(SAT_PROTOCOL));

        setSensorTableName(properties.getProperty(SENSORTABLENAME));
        setSet_id(properties.getProperty(SET_ID));
        setSet_lat(properties.getProperty(SET_LAT));
        setSet_lon(properties.getProperty(SET_LON));

        setPropertyTableName(properties.getProperty(PROPERTYTABLENAME));
        setPt_id(properties.getProperty(PT_ID));
        setPt_propertyID(properties.getProperty(PT_PROPERTYID));
        setPt_propertyName(properties.getProperty(PT_PROPERTYNAME));
        setPt_propertyUnit(properties.getProperty(PT_PROPERTYUNIT));
        setPt_propertyStartPos(properties.getProperty(PT_PROPERTYSTARTPOS));
        setPt_propertyLen(properties.getProperty(PT_PROPERTYSTARTLEN));

        setSetsatTableName(properties.getProperty(SETSATTABLENAME));
        setPtsatTableName(properties.getProperty(PTSATTABLENAME));
        setPtsetTableName(properties.getProperty(PTSETTABLENAME));
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        SensorConfigInfo.url = url;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        SensorConfigInfo.host = host;
    }

    public static String getDriver() {
        return driver;
    }

    public static void setDriver(String driver) {
        SensorConfigInfo.driver = driver;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        SensorConfigInfo.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SensorConfigInfo.password = password;
    }

    public static String getDbName() {
        return dbName;
    }

    public static void setDbName(String dbName) {
        SensorConfigInfo.dbName = dbName;
    }

    public static String getStationtableName() {
        return stationtableName;
    }

    public static void setStationtableName(String stationtableName) {
        SensorConfigInfo.stationtableName = stationtableName;
    }

    public static String getSat_id() {
        return sat_id;
    }

    public static void setSat_id(String sat_id) {
        SensorConfigInfo.sat_id = sat_id;
    }

    public static String getSat_ip() {
        return sat_ip;
    }

    public static void setSat_ip(String sat_ip) {
        SensorConfigInfo.sat_ip = sat_ip;
    }

    public static String getSat_port() {
        return sat_port;
    }

    public static void setSat_port(String sat_port) {
        SensorConfigInfo.sat_port = sat_port;
    }

    public static String getSat_startingAddress() {
        return sat_startingAddress;
    }

    public static void setSat_startingAddress(String sat_startingAddress) {
        SensorConfigInfo.sat_startingAddress = sat_startingAddress;
    }

    public static String getSat_slaveAddress() {
        return sat_slaveAddress;
    }

    public static void setSat_slaveAddress(String sat_slaveAddress) {
        SensorConfigInfo.sat_slaveAddress = sat_slaveAddress;
    }

    public static String getSat_socketOutofTime() {
        return sat_socketOutofTime;
    }

    public static void setSat_socketOutofTime(String sat_socketOutofTime) {
        SensorConfigInfo.sat_socketOutofTime = sat_socketOutofTime;
    }

    public static String getSat_sleepTime() {
        return sat_sleepTime;
    }

    public static void setSat_sleepTime(String sat_sleepTime) {
        SensorConfigInfo.sat_sleepTime = sat_sleepTime;
    }

    public static String getSat_logFile() {
        return sat_logFile;
    }

    public static void setSat_logFile(String sat_logFile) {
        SensorConfigInfo.sat_logFile = sat_logFile;
    }

    public static String getSat_protocal() {
        return sat_protocal;
    }

    public static void setSat_protocal(String sat_protocal) {
        SensorConfigInfo.sat_protocal = sat_protocal;
    }

    public static String getSensorTableName() {
        return sensorTableName;
    }

    public static void setSensorTableName(String sensorTableName) {
        SensorConfigInfo.sensorTableName = sensorTableName;
    }

    public static String getSet_id() {
        return set_id;
    }

    public static void setSet_id(String set_id) {
        SensorConfigInfo.set_id = set_id;
    }

    public static String getSet_lon() {
        return set_lon;
    }

    public static void setSet_lon(String set_lon) {
        SensorConfigInfo.set_lon = set_lon;
    }

    public static String getSet_lat() {
        return set_lat;
    }

    public static void setSet_lat(String set_lat) {
        SensorConfigInfo.set_lat = set_lat;
    }

    public static String getPropertyTableName() {
        return propertyTableName;
    }

    public static void setPropertyTableName(String propertyTableName) {
        SensorConfigInfo.propertyTableName = propertyTableName;
    }

    public static String getPt_id() {
        return pt_id;
    }

    public static void setPt_id(String pt_id) {
        SensorConfigInfo.pt_id = pt_id;
    }

    public static String getPt_propertyID() {
        return pt_propertyID;
    }

    public static void setPt_propertyID(String pt_propertyID) {
        SensorConfigInfo.pt_propertyID = pt_propertyID;
    }

    public static String getPt_propertyName() {
        return pt_propertyName;
    }

    public static void setPt_propertyName(String pt_propertyName) {
        SensorConfigInfo.pt_propertyName = pt_propertyName;
    }

    public static String getPt_propertyUnit() {
        return pt_propertyUnit;
    }

    public static void setPt_propertyUnit(String pt_propertyUnit) {
        SensorConfigInfo.pt_propertyUnit = pt_propertyUnit;
    }

    public static String getPt_propertyStartPos() {
        return pt_propertyStartPos;
    }

    public static void setPt_propertyStartPos(String pt_propertyStartPos) {
        SensorConfigInfo.pt_propertyStartPos = pt_propertyStartPos;
    }

    public static String getPt_propertyLen() {
        return pt_propertyLen;
    }

    public static void setPt_propertyLen(String pt_propertyLen) {
        SensorConfigInfo.pt_propertyLen = pt_propertyLen;
    }

    public static String getSetsatTableName() {
        return setsatTableName;
    }

    public static void setSetsatTableName(String setsatTableName) {
        SensorConfigInfo.setsatTableName = setsatTableName;
    }

    public static String getPtsatTableName() {
        return ptsatTableName;
    }

    public static void setPtsatTableName(String ptsatTableName) {
        SensorConfigInfo.ptsatTableName = ptsatTableName;
    }

    public static String getPtsetTableName() {
        return ptsetTableName;
    }

    public static void setPtsetTableName(String ptsetTableName) {
        SensorConfigInfo.ptsetTableName = ptsetTableName;
    }



}
