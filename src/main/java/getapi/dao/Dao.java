package getapi.dao;

import getapi.models.AccuweatherHourly;
import getapi.models.DarkskyHourly;
import getapi.models.Station;
import sms.HourlyWeather;
import sms.SMSRule;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Dao {
    // connect DB
    private Connection conn = null;

    public Dao() {
        String hostName = "localhost";
        String dbName = "climate";
        String userName = "root";
        String password = "";
        String dbURL = "jdbc:mysql://" + hostName + ":3306/" + dbName
                + "?useSSL=false&useUnicode=true&characterEncoding=utf8";

        if (conn == null) {
            try {
                conn = DriverManager.getConnection(dbURL, userName, password);
                System.out.println("Connecting...");
            } catch (SQLException ex) {
                System.err.println("Cannot connect database " + ex);
            }
        }
    }

    public String getApiKey(String source) {
        String apiKey = null;
        String id = null;

        String checkToResetCalledSQL = "UPDATE apikey a SET a.called_inday = 0 WHERE DATE(a.last_called) < CURDATE() AND a.dead = 0";
        String getApiKeySQL = "SELECT a.id, a.apikey FROM `apikey` a WHERE a.website = '" + source + "' AND a.called_inday < a.max_number AND a.dead = 0 LIMIT 1;";
        String updateApiSQL = "UPDATE apikey SET called_inday = called_inday + 1, apikey.last_called = NOW() WHERE apikey.id = 'APIKeyID'";

        Statement statement = null;

        try {
            statement = conn.createStatement();

            //reset all api
            int resetAPI = statement.executeUpdate(checkToResetCalledSQL);
            if (resetAPI > 0) {
                System.out.println("Reset: " + resetAPI);
            } else {
                System.out.println("No API reset");
            }

            //get api
            ResultSet rs = statement.executeQuery(getApiKeySQL);
            while (rs.next()) {
                apiKey = rs.getString("apikey");
                id = rs.getString("id");
                System.out.println("Get API Key: " + apiKey + " for website: " + source);
            }


            //update api was get
            if (id != null) {
                statement.executeUpdate(updateApiSQL.replace("APIKeyID", id));
                System.out.println("Update API");
            } else {
                System.out.println("API have no ID to Update");
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return apiKey;
    }

    // get station
    public ArrayList<Station> getStationEnalbeApi() {
        ArrayList<Station> listStation = new ArrayList<Station>();

        String sql = "SELECT * FROM stations s WHERE s.api_enable = TRUE";

        Statement statement;

        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                String station_code = rs.getString("station_code");
                String station_name = rs.getString("station_name");
                String station_name_vi = rs.getString("station_name_vi");
                float lat = rs.getFloat("lat");
                float lon = rs.getFloat("lon");
                String accuweather_key = rs.getString("accuweather_key");

                Station station = new Station(station_code, station_name, station_name_vi,lat, lon, accuweather_key);
                listStation.add(station);
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listStation;
    }

    // save darksky hourly forecast data
    public void saveDarkskyHourlyData(List<DarkskyHourly> listForecast, String station_code, String updated_time) {
        String sql = "INSERT INTO darksky_hourly(station_code, time, summary, total_liquid, probability, "
                + "liquid_type, temperature, realfile_temperature, dew_point, humidity, pressure, wind_speed, windgust_speed, "
                + "wind_edge, cloud_cover, uv_index, ozone, updated_time,icon, wind_direction, visibility)"
                + "\nVALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";


        PreparedStatement statement = null;

        for (DarkskyHourly forecast : listForecast) {
            try {
                statement = conn.prepareStatement(sql);
                statement.setString(1, station_code);
                statement.setString(2, forecast.getTime());
                statement.setString(3, forecast.getSummary());
                statement.setFloat(4, forecast.getPrecipIntensity());
                statement.setFloat(5, forecast.getPrecipProbability());
                statement.setString(6, forecast.getPrecipType());
                statement.setFloat(7, forecast.getTemperature());
                statement.setFloat(8, forecast.getApparentTemperature());
                statement.setFloat(9, forecast.getDewPoint());
                statement.setFloat(10, forecast.getHumidity());
                statement.setFloat(11, forecast.getPressure());
                statement.setFloat(12, forecast.getWindSpeed());
                statement.setFloat(13, forecast.getWindGust());
                statement.setFloat(14, forecast.getWindBearing());
                statement.setFloat(15, forecast.getCloudCover());
                statement.setFloat(16, forecast.getUvIndex());
                statement.setFloat(17, forecast.getOzone());
                statement.setString(18, updated_time);
                statement.setString(19, forecast.getIcon());
                String windDirect = SMSRule.smsGenerateElement(forecast.getWindBearing(), SMSRule.getGetWindDirectELRule());
                statement.setString(20, windDirect);
                statement.setFloat(21, forecast.getVisibility());

                statement.addBatch();
                statement.executeBatch();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // save accuweather hourly data
    public void saveAccuweatherHourlyData(List<AccuweatherHourly> listForecast, String station_code, String updated_time) {

        String sql = "INSERT INTO accuweather_hourly(station_code, updated_time, time, is_day_light, temperature, "
                + "realfile_temperature, wetbulb_temperature, dew_point, wind_speed, wind_direction, wind_edge, windgust_speed, "
                + "humidity, visibility, ceiling, uv_index, probability, rain_probability, snow_probability, "
                + "ice_probability, total_liquid, rain, snow, ice, cloud_cover, icon )"
                + "\nVALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        PreparedStatement statement = null;
        for (AccuweatherHourly forecast : listForecast) {
            try {
                statement = conn.prepareStatement(sql);
                statement.setString(1, station_code);
                statement.setString(2, updated_time);
                statement.setString(3, forecast.getTime());
                statement.setInt(4, forecast.isIsDaylight());
                statement.setString(5, forecast.getTemperature().getValue().toString());

                statement.setString(6, forecast.getRealFeelTemperature().getValue().toString());
                statement.setString(7, forecast.getWetBulbTemperature().getValue().toString());
                statement.setString(8, forecast.getDewPoint().getValue().toString());
                statement.setString(9, String.valueOf(forecast.getWind().getSpeed().getValue()));
                statement.setString(10, forecast.getWind().getDirection().getLocalized().toString());
                statement.setString(11, String.valueOf(forecast.getWind().getDirection().getDegrees()));
                statement.setString(12, String.valueOf(forecast.getWindGust().getSpeed().getValue()));

                statement.setInt(13, forecast.getRelativeHumidity());
                statement.setString(14, forecast.getVisibility().getValue().toString());
                statement.setString(15, forecast.getCeiling().getValue().toString());
                statement.setInt(16, forecast.getUVIndex());
                statement.setInt(17, forecast.getPrecipitationProbability());
                statement.setInt(18, forecast.getRainProbability());
                statement.setInt(19, forecast.getSnowProbability());

                statement.setInt(20, forecast.getIceProbability());
                statement.setString(21, forecast.getTotalLiquid().getValue().toString());
                statement.setString(22, forecast.getRain().getValue().toString());
                statement.setString(23, forecast.getSnow().getValue().toString());
                statement.setString(24, forecast.getIce().getValue().toString());
                statement.setInt(25, forecast.getCloudCover());
                statement.setString(26, forecast.getIconPhrase());

                statement.addBatch();
                statement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update accuweather_key
    public void updateKey(ArrayList<Station> listStation) {

        String sql = "update stations set accuweather_key = ? WHERE station_code = ?;";

        PreparedStatement statement = null;


        for (Station station : listStation) {
            try {
                statement = conn.prepareStatement(sql);
                statement.setString(1, station.getAccuweather_key());
                statement.setString(2, station.getStation_code());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    //getDataHourly
    public List<HourlyWeather> getDataHourly(String station_code, String website, int after_day) {
        List<HourlyWeather> list = new LinkedList<HourlyWeather>();

        String tableHourly = website + "_hourly";

        String SQL = "SELECT a.time,a.station_code,a.total_liquid,a.probability,a.temperature,a.uv_index,a.humidity,a.wind_speed,a.wind_edge\n" +
                " FROM " + tableHourly + " a\n" +
                " WHERE a.id IN (SELECT\tMAX(id) AS id FROM " + tableHourly + " a \n" +
                " WHERE DATE(a.time) = (CURDATE() + INTERVAL " + after_day + " DAY) AND a.station_code = '" + station_code +"'"+
                " GROUP BY a.station_code,a.time) ";

        //check SQL
        System.out.println("get data: "+ station_code + " website: "+website+ " = " +SQL);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                HourlyWeather dataWeather = new HourlyWeather();
                dataWeather.setTime(rs.getString("time"));
                dataWeather.setStation_code(rs.getString("station_code"));
                dataWeather.setRain(rs.getFloat("total_liquid"));
                dataWeather.setProbability(rs.getInt("probability"));
                dataWeather.setTemperature(rs.getFloat("temperature"));
                dataWeather.setUv_index(rs.getInt("uv_index"));
                dataWeather.setHumidity(rs.getFloat("humidity"));
                dataWeather.setWin_speed(rs.getFloat("wind_speed"));
                dataWeather.setWind_edge(rs.getString("wind_edge"));

                list.add(dataWeather);
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void close() {

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
