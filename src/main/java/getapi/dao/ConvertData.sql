

-- insert location from field_climate
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



