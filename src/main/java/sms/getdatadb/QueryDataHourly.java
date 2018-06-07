package sms.getdatadb;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/*
    model to get data form DB and convert to list Object
 */
public class QueryDataHourly {

    private Connection conn = null;
    private Statement stmt = null;

    public QueryDataHourly() {
        String hostName = "localhost";
        String dbName = "climate";
        String userName = "root";
        String password = "";

        String dbURL = "jdbc:mysql://" + hostName + ":3306/" + dbName
                + "?useSSL=false&useUnicode=true&characterEncoding=utf8";

        if (conn == null) {
            try {
                conn = DriverManager.getConnection(dbURL, userName, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stmt == null) {
            try {
                stmt = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<HourlyWeather> getDataHourly(String station_code, String website, int after_day) throws SQLException, NumberFormatException {
        List<HourlyWeather> list = new LinkedList<HourlyWeather>();

        String tableHourly = website+"_hourly";

        String SQL = "SELECT a.time,a.station_code,a.total_liquid,a.probability,a.temperature,a.uv_index,a.humidity,a.wind_speed,a.wind_edge\n" +
                " FROM "+tableHourly+" a\n" +
                " WHERE a.id IN (SELECT\tMAX(id) AS id FROM "+tableHourly+" a \n" +
                " WHERE\tDATE(a.time) = (CURDATE() + INTERVAL "+after_day+" DAY) AND a.station_code = '" +station_code +"' AND a.liquid_type = 'rain'"+
                " GROUP BY a.station_code,a.time) ";

        ResultSet rs = stmt.executeQuery(SQL);

        while (rs.next()){
            HourlyWeather dataWeather = new HourlyWeather();
            dataWeather.setTime(rs.getString("time"));
            dataWeather.setStation_code(rs.getString("station_code"));
            dataWeather.setRain(rs.getFloat("total_liquid"));
            dataWeather.setRain(rs.getInt("probability"));
            dataWeather.setTemperature(rs.getFloat("temperature"));
            dataWeather.setUv_index(rs.getInt("uv_index"));
            dataWeather.setHumidity(rs.getFloat("humidity"));
            dataWeather.setWin_speed(rs.getFloat("wind_speed"));
            dataWeather.setWind_edge(rs.getString("wind_edge"));

            list.add(dataWeather);
        }
        return list;
    }

}
