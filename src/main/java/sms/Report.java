package sms;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import getapi.dao.Dao;
import getapi.models.Station;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class Report {
    private final String LOG_FILENAME = "src/main/resources/test.txt";
    private final String TEMP_FILENAME = "src/main/resources/template.xlsx";
    private final String OUT_FILENAME = "src/main/resources/Bản tin thời tiết tự động";

    private List<Station> litStation;
    private FileInputStream file;
    private XSSFWorkbook workbook;
    private FileOutputStream outFile;

    public Report() {
        Dao query = new Dao();

        litStation = query.getEnalbeApiStation();

        try {
            file = new FileInputStream(new File(TEMP_FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < litStation.size(); i++) {
            Station station = litStation.get(i);
            String stationCode = station.getStation_code();

            List<HourlyWeather> listStationHourlyData = query.getDataHourly(stationCode, "darksky", 0);
            if (listStationHourlyData == null) {
                System.out.println("Trạm không có dữ liệu dự báo: " + station.getStation_name());
            } else {

                System.out.println("Trạm có dữ liệu dự báo: " + station.getStation_name() + " size: " + listStationHourlyData.size());

                SessionFormular formular = new SessionFormular(listStationHourlyData);
                XSSFSheet sheet = workbook.cloneSheet(0, station.getStation_name());
                String sms = null;
                Cell cell = null;
                int row = 3;


                //Update the value of cell
                Calendar calendar = Calendar.getInstance();
                String date = calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH);
                cell = sheet.getRow(row).getCell(0);
                cell.setCellValue(date);

                if (listStationHourlyData.size() != 0) {


                    float sumUV = formular.getTotalUV();
                    cell = sheet.getRow(row).getCell(1);
                    cell.setCellValue(sumUV);

                    String maxTem = formular.getMaxTemperature();
                    cell = sheet.getRow(row).getCell(16);
                    cell.setCellValue(maxTem);

                    String minTem = formular.getMinTemperature();
                    cell = sheet.getRow(row).getCell(17);
                    cell.setCellValue(minTem);

                    float humidity = formular.getAverageHumidity();
                    cell = sheet.getRow(row).getCell(18);
                    cell.setCellValue(humidity);

                    String maxWinddirect = formular.getMaxWindDirect();
                    cell = sheet.getRow(row).getCell(19);
                    cell.setCellValue(maxWinddirect);

                    float avgWindSpeed = formular.getAverageWindSpeed();
                    cell = sheet.getRow(row).getCell(20);
                    cell.setCellValue(avgWindSpeed);

                    Hashtable<String, Float> rain = formular.getRain();
                    Hashtable<String, Integer> percentRain = formular.getPercentRain();

                    if (!rain.isEmpty()) {
                        List<String> session = SMSRule.getListSMS(SMSRule.getSession());
                        for (int j = 0; j < session.size(); j++) {
                            if (rain.containsKey(session.get(j))) {
                                float mount = (float) (Math.round(rain.get(session.get(j)) * 10.0) / 10.0);
                                cell = sheet.getRow(row).getCell(2 + j);
                                cell.setCellValue(mount);
                                cell = sheet.getRow(row).getCell(3 + j);
                                cell.setCellValue(percentRain.get(session.get(j)));
                            }
                        }
                    }

                    sms = getSMSReport(station.getStation_name(), date, formular);
                    cell = sheet.getRow(row).getCell(21);
                    System.out.println("SMS:"+sms);
                    cell.setCellValue(sms);

                    try {
                        Files.write(Paths.get(LOG_FILENAME), sms.getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    sms = "Trạm không có dữ liệu";
                }

            }
        }


        try {
            outFile = new FileOutputStream(new File(OUT_FILENAME + ".xlsx"), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {

            file.close();
            workbook.removeSheetAt(0);
            workbook.write(outFile);
            outFile.flush();
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void convertReport() {
        try {
            Workbook workbook = new Workbook(OUT_FILENAME + ".xlsx");

            // save in different formats

            workbook.save(OUT_FILENAME + ".pdf", SaveFormat.PDF);
            workbook.save(OUT_FILENAME + ".xps", SaveFormat.XPS);
            workbook.save(OUT_FILENAME + ".html", SaveFormat.HTML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSMSReport(String station, String date, SessionFormular formular) {

        StringBuilder report = new StringBuilder();

        report.append("\n\nDự báo ngày: ").append(date);
        report.append("\nTrạm ").append(station).append(" dự báo cho 1 ngày tới: ");
        report.append("\n" + formular.getReport_MaxTemperature());
        report.append("\n" + formular.getReport_MinTemperature());
        report.append("\n" + formular.getReport_AverageHumidity());
        report.append("\n" + formular.getReport_MaxWindDirect());
        report.append("\n" + formular.getReport_RainAndPercentRain());
        report.append("\n" + formular.getReport_AverageWindSpeed());
        report.append("\n" + formular.getReport_NumberMaxSun());

        return report.toString();

    }
}
