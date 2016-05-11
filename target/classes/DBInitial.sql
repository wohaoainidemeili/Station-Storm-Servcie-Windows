CREATE TABLE IF NOT EXISTS Station
(
  station_id VARCHAR(255) NOT NULL PRIMARY KEY ,
  ip VARCHAR(255) NOT NULL ,
  port INT NOT NULL ,
  starting_address INT NOT NULL ,
  slave_address INT NOT NULL ,
  socket_out_time INT NOT NULL ,
  sleep_time INT NOT NULL ,
  log_file VARCHAR(2000) ,
  protocol VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS Sensor
(
  sensor_id VARCHAR(255) NOT NULL PRIMARY KEY,
  lon DOUBLE ,
  lat DOUBLE
);
CREATE TABLE IF NOT EXISTS Property
(
  property_id VARCHAR(255) NOT NULL,
  property_name VARCHAR(255) charset utf8 NOT NULL,
  property_unit VARCHAR(255) NOT NULL ,
  property_start_pos INT NOT NULL ,
  property_len INT,
  PRIMARY KEY (property_id,property_start_pos,property_len)
);
CREATE TABLE  IF NOT EXISTS SensorOfStation
(
  station_id VARCHAR(255) NOT NULL ,
  sensor_id  VARCHAR(255) NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS PropertyOfStation
(
  station_id VARCHAR(255) NOT NULL ,
  property_id VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS PropertyOfSensor
(
  sensor_id  VARCHAR(255) NOT NULL  ,
  property_id VARCHAR(255) NOT NULL
);