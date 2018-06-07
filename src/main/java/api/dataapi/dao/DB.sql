CREATE TABLE `accuweather_hourly` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `station_code` char(8) DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `temperature` float unsigned DEFAULT NULL,
  `realfile_temperature` float unsigned DEFAULT NULL,
  `wetbulb_temperature` float unsigned DEFAULT NULL,
  `dew_point` float unsigned DEFAULT NULL,
  `humidity` tinyint(4) DEFAULT NULL,
  `total_liquid` float unsigned DEFAULT NULL,
  `probability` tinyint(4) unsigned DEFAULT NULL,
  `liquid_type` char(4) DEFAULT NULL,
  `rain` float unsigned DEFAULT NULL,
  `rain_probability` tinyint(4) DEFAULT NULL,
  `ice` float DEFAULT NULL,
  `ice_probability` tinyint(4) unsigned DEFAULT NULL,
  `snow` float DEFAULT NULL,
  `snow_probability` tinyint(4) unsigned DEFAULT NULL,
  `wind_speed` float unsigned DEFAULT NULL,
  `wind_edge` smallint(6) DEFAULT NULL,
  `wind_direction` char(3) DEFAULT NULL,
  `windgust_speed` float unsigned DEFAULT NULL,
  `visibility` float unsigned DEFAULT NULL,
  `ceiling` float unsigned DEFAULT NULL,
  `uv_index` tinyint(4) DEFAULT NULL,
  `cloud_cover` tinyint(4) DEFAULT NULL,
  `icon` varchar(30) DEFAULT NULL,
  `is_day_light` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`station_code`,`time`,`updated_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;

CREATE TABLE `darksky_hourly` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `station_code` char(8) DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `temperature` float unsigned DEFAULT NULL,
  `realfile_temperature` float unsigned DEFAULT NULL,
  `dew_point` float unsigned DEFAULT NULL,
  `humidity` tinyint(4) DEFAULT NULL,
  `total_liquid` float unsigned DEFAULT NULL,
  `probability` tinyint(4) DEFAULT NULL,
  `liquid_type` char(4) DEFAULT NULL,
  `wind_speed` float unsigned DEFAULT NULL,
  `wind_edge` smallint(6) DEFAULT NULL,
  `wind_direction` char(4) DEFAULT NULL,
  `windgust_speed` float unsigned DEFAULT NULL,
  `visibility` float unsigned DEFAULT NULL,
  `uv_index` tinyint(4) DEFAULT NULL,
  `cloud_cover` tinyint(4) DEFAULT NULL,
  `ozone` float unsigned DEFAULT NULL,
  `pressure` float unsigned DEFAULT NULL,
  `icon` varchar(30) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`station_code`,`time`,`updated_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=849 DEFAULT CHARSET=utf8;

CREATE TABLE `apikey` (
  `website` varchar(30) NOT NULL,
  `apikey` varchar(255) NOT NULL,
  `account` varchar(30) DEFAULT NULL,
  `called_inday` int(11) unsigned DEFAULT NULL,
  `max_number` int(11) unsigned NOT NULL,
  KEY `website` (`website`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stations` (
  `station_code` char(8) NOT NULL,
  `station_name` varchar(30) NOT NULL,
  `lat` float DEFAULT NULL,
  `lon` float DEFAULT NULL,
  `accuweather_key` char(8) DEFAULT NULL,
  `api_enable` bit(1) DEFAULT b'1',
  PRIMARY KEY (`station_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--insert location from field_climate
INSERT INTO stations (stations.station_code, stations.station_name, stations.lat, stations.lon,stations.accuweather_key)
SELECT s.station_code, s.station_name, s.lat, s.lng, s.accuweather_key
FROM fieldclimate.stations s
WHERE s.accuweather_key  IS NOT NULL;

-- update wind direct darksky
UPDATE climate.darksky_hourly
SET wind_direction = CASE
WHEN darksky_hourly.wind_edge < 11.5 THEN
	"N"
WHEN darksky_hourly.wind_edge < 33.5 THEN
	"NNE"
WHEN darksky_hourly.wind_edge < 56.5 THEN
	"NE"
WHEN darksky_hourly.wind_edge < 78.5 THEN
	"ENE"
WHEN darksky_hourly.wind_edge < 101.5 THEN
	"E"
WHEN darksky_hourly.wind_edge < 123.5 THEN
	"ESE"
WHEN darksky_hourly.wind_edge < 146.5 THEN
	"SE"
WHEN darksky_hourly.wind_edge < 168.5 THEN
	"SSE"
WHEN darksky_hourly.wind_edge < 191.5 THEN
	"S"
WHEN darksky_hourly.wind_edge < 213.5 THEN
	"SSW"
WHEN darksky_hourly.wind_edge < 236.5 THEN
	"SW"
WHEN darksky_hourly.wind_edge < 258.5 THEN
	"WSW"
WHEN darksky_hourly.wind_edge < 281.5 THEN
	"W"
WHEN darksky_hourly.wind_edge < 303.5 THEN
	"WNW"
WHEN darksky_hourly.wind_edge < 326.5 THEN
	"NW"
WHEN darksky_hourly.wind_edge < 348.5 THEN
	"NNW"
ELSE
	"N"
END

-- update liquid type  accuweather
UPDATE climate.accuweather_hourly a
SET liquid_type = CASE
WHEN a.rain > 0
AND a.rain_probability > 20 THEN
	"rain"
WHEN a.snow > 0
AND a.snow_probability > 20 THEN
	"snow"
WHEN a.ice > 0
AND a.ice_probability > 20 THEN
	"ice"
ELSE
	NULL
END

-- test save report

CREATE TABLE `forecast_daily` (
  `website` varchar(30) NOT NULL,
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `max_temperature` float unsigned DEFAULT NULL,
  `min_temperature` float(255,0) DEFAULT NULL,
  `hour_uv200` varchar(255) DEFAULT NULL,
  `avg_humidity` float(255,0) unsigned DEFAULT NULL,
  `max_wind_direct` char(4) DEFAULT NULL,
  `number_max_wind_direct` int(11) unsigned DEFAULT NULL,
  `avg_wind_speed` double unsigned DEFAULT NULL,
  `rain_0_7` float unsigned DEFAULT NULL,
  `rain_0_7_probalility` float unsigned DEFAULT NULL,
  `rain_7_10` float unsigned DEFAULT NULL,
  `rain_7_10_probalility` float(4,1) unsigned DEFAULT NULL,
  `rain_10_13` float unsigned DEFAULT NULL,
  `rain_10_13_probability` float(4,1) unsigned DEFAULT NULL,
  `rain_13_16` float unsigned DEFAULT NULL,
  `rain_13_16_probability` float(4,1) unsigned DEFAULT NULL,
  `rain_16_19` float unsigned DEFAULT NULL,
  `rain_16_19_probability` float(4,1) unsigned DEFAULT NULL,
  `rain_19_22` float unsigned DEFAULT NULL,
  `rain_19_22_probability` float(4,1) unsigned DEFAULT NULL,
  `rain_22_24` float unsigned DEFAULT NULL,
  `rain_22_24_probability` float(4,1) unsigned DEFAULT NULL,
  PRIMARY KEY (`website`,`update_time`),
  UNIQUE KEY `website` (`website`,`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


