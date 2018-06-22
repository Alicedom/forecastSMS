CREATE TABLE `accuweather_hourly` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `station_code` char(8) NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `time` datetime NOT NULL,
  `temperature` double unsigned NOT NULL,
  `realfile_temperature` double unsigned NOT NULL,
  `wetbulb_temperature` double unsigned NOT NULL,
  `dew_point` double unsigned NOT NULL,
  `humidity` tinyint(4) NOT NULL,
  `total_liquid` double unsigned NOT NULL,
  `probability` tinyint(4) unsigned NOT NULL,
  `liquid_type` char(4) NOT NULL,
  `rain` double unsigned NOT NULL,
  `rain_probability` tinyint(4) NOT NULL,
  `ice` double NOT NULL,
  `ice_probability` tinyint(4) unsigned NOT NULL,
  `snow` double NOT NULL,
  `snow_probability` tinyint(4) unsigned NOT NULL,
  `wind_speed` double unsigned NOT NULL,
  `wind_edge` smallint(6) NOT NULL,
  `wind_direction` char(3) NOT NULL,
  `windgust_speed` double unsigned NOT NULL,
  `visibility` double unsigned NOT NULL,
  `ceiling` double unsigned NOT NULL,
  `uv_index` tinyint(4) NOT NULL,
  `cloud_cover` tinyint(4) NOT NULL,
  `icon` varchar(30) NOT NULL,
  `is_day_light` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`station_code`,`time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;



CREATE TABLE `darksky_hourly` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `station_code` char(8) NOT NULL,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `time` datetime NOT NULL,
  `temperature` double unsigned NOT NULL,
  `realfile_temperature` double unsigned NOT NULL,
  `dew_point` double unsigned NOT NULL,
  `humidity` tinyint(4) NOT NULL,
  `total_liquid` double unsigned NOT NULL,
  `probability` tinyint(4) NOT NULL,
  `liquid_type` char(4) NOT NULL,
  `wind_speed` double unsigned NOT NULL,
  `wind_edge` smallint(6) NOT NULL,
  `wind_direction` char(4) NOT NULL,
  `windgust_speed` double unsigned NOT NULL,
  `visibility` double unsigned NOT NULL,
  `uv_index` tinyint(4) NOT NULL,
  `cloud_cover` tinyint(4) NOT NULL,
  `ozone` double unsigned NOT NULL,
  `pressure` double unsigned NOT NULL,
  `icon` varchar(30) NOT NULL,
  `summary` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `name` (`station_code`,`time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2662 DEFAULT CHARSET=utf8;



CREATE TABLE `apikey` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `website` varchar(10) NOT NULL,
  `apikey` varchar(255) NOT NULL,
  `account` varchar(30) DEFAULT NULL,
  `called_inday` int(11) unsigned DEFAULT NULL,
  `max_number` int(11) unsigned NOT NULL,
  `last_called` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `dead` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `website` (`website`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

CREATE TABLE `stations` (
  `station_code` char(8) NOT NULL,
  `station_name` varchar(30) NOT NULL,
  `station_name_vi` varchar(30) DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lon` double DEFAULT NULL,
  `accuweather_key` char(8) DEFAULT NULL,
  `api_enable` bit(1) DEFAULT b'1',
  PRIMARY KEY (`station_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- test save report

CREATE TABLE `forecast_daily` (
  `website` varchar(30) NOT NULL,
  `update_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `max_temperature` double unsigned DEFAULT NULL,
  `min_temperature` double(255,0) DEFAULT NULL,
  `hour_uv200` varchar(255) DEFAULT NULL,
  `avg_humidity` double(255,0) unsigned DEFAULT NULL,
  `max_wind_direct` char(4) DEFAULT NULL,
  `number_max_wind_direct` int(11) unsigned DEFAULT NULL,
  `avg_wind_speed` double unsigned DEFAULT NULL,
  `rain_0_7` double unsigned DEFAULT NULL,
  `rain_0_7_probalility` double unsigned DEFAULT NULL,
  `rain_7_10` double unsigned DEFAULT NULL,
  `rain_7_10_probalility` double(4,1) unsigned DEFAULT NULL,
  `rain_10_13` double unsigned DEFAULT NULL,
  `rain_10_13_probability` double(4,1) unsigned DEFAULT NULL,
  `rain_13_16` double unsigned DEFAULT NULL,
  `rain_13_16_probability` double(4,1) unsigned DEFAULT NULL,
  `rain_16_19` double unsigned DEFAULT NULL,
  `rain_16_19_probability` double(4,1) unsigned DEFAULT NULL,
  `rain_19_22` double unsigned DEFAULT NULL,
  `rain_19_22_probability` double(4,1) unsigned DEFAULT NULL,
  `rain_22_24` double unsigned DEFAULT NULL,
  `rain_22_24_probability` double(4,1) unsigned DEFAULT NULL,
  PRIMARY KEY (`website`,`update_time`),
  UNIQUE KEY `website` (`website`,`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
