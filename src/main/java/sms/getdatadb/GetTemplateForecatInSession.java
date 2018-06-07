package sms.getdatadb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetTemplateForecatInSession {

    private static final String FILENAME = "src/main/resources/test.txt";

    public static void main(String[] args) {

        String[] stationList = { "002040B1", "00203827", "002040D7", "000021C4"};


        QueryDataHourly query = new QueryDataHourly();
        for (String station : stationList) {
            try {
                List<HourlyWeather> listStationHourlyData = query.getDataHourly(station, "darksky", 1);

                SessionFormular formular = new SessionFormular(listStationHourlyData);


                BufferedWriter bw = null;
                FileWriter fw = null;

                try {

                    String content = null;

                    fw = new FileWriter(FILENAME, true);
                    bw = new BufferedWriter(fw);

                    content = "Trạm " + station + " dự báo cho 1 ngày tới: ";
                    System.out.println(content);
                    bw.write(content);
                    bw.newLine();

                    content = "Nhiệt độ tối đa: " + formular.getMaxTemperature();
                    System.out.println(content);
                    bw.write(content);
                    bw.newLine();

                    content = "Nhiệt độ tối thiểu: " + formular.getMinTemperature();
                    System.out.println(content);
                    bw.write(content);
                    bw.newLine();

                    content = "Độ ẩm trung bình: " + formular.getAverageHumidity();
                    System.out.println(content);
                    bw.write(content);
                    bw.newLine();

                    content = "Hướng gió nhiều nhất: " + formular.getMaxWindDirect();
                    System.out.println(content);
                    bw.write(content);
                    bw.newLine();

                    content = "Dự báo buổi mưa: " + formular.getRainAndPercentRain();
                    System.out.println(content);
                    bw.write(content);
                    bw.newLine();

                    bw.flush();

                    System.out.println("Done");

                } catch (IOException e) {

                    e.printStackTrace();

                } finally {

                    try {

                        if (bw != null)
                            bw.close();

                        if (fw != null)
                            fw.close();

                    } catch (IOException ex) {

                        ex.printStackTrace();

                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
