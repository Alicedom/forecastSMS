package sms;

import getapi.dao.Dao;
import getapi.models.Station;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class GenerateReport {
    private final String LOG_FILENAME = "src/main/resources/test.txt";
    private final String TEMP_FILENAME = "src/main/resources/template.xlsx";
    private final String OUT_FILENAME = "src/main/resources/Bản tin thời tiết tự động";
    private XSSFWorkbook workbook;
    private FileInputStream file;

    private Dao query;

    public GenerateReport() {
        try {
            file= new FileInputStream(new File(TEMP_FILENAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (query == null)
            query = new Dao();

        List<Station> litStation = query.getEnalbeApiStation();

        for (int i = 0; i < litStation.size(); i++) {
            Station station = litStation.get(i);
            String stationCode = station.getStation_code();

            List<HourlyWeather> listStationHourlyData = query.getDataHourly(stationCode, "darksky", 1);
            if (listStationHourlyData == null) {
                System.out.println("Trạm không có dữ liệu dự báo: " + station);
            } else {
                SessionFormular formular = new SessionFormular(listStationHourlyData);
                SaveReport(i, station, formular);
//                SaveLog(stationCode, formular);
            }
        }

    }

    private void SaveReport(int i, Station station, SessionFormular formular) {
        try {

            XSSFSheet sheet;

            System.out.println("i = " + i);

//                sheet = workbook.createSheet();
            sheet = workbook.cloneSheet(0, station.getStation_name());
//            workbook.createSheet(station.getStation_name());
//            sheet = workbook.getSheet(station.getStation_name());

            System.out.println("station = " + station.getStation_name());

            Cell cell = null;
            int row = 3;

            //Update the value of cell
            Calendar calendar = Calendar.getInstance();
            String date = calendar.get(Calendar.DATE)+"/"+calendar.get(Calendar.MONTH);
            cell = sheet.getRow(row).getCell(0);
            cell.setCellValue(date);

            float sumUV = formular.getTotalUV();
            cell = sheet.getRow(row).getCell(1);
            cell.setCellValue(sumUV);

            float maxTem = formular.getMaxTemperature();
            cell = sheet.getRow(row).getCell(16);
            cell.setCellValue(maxTem);

            float minTem = formular.getMinTemperature();
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
                    if (rain.containsKey(session.get(i))) {
                        cell = sheet.getRow(row).getCell(2 + i);
                        cell.setCellValue(rain.get(session.get(i)));
                        cell = sheet.getRow(row).getCell(3 + i);
                        cell.setCellValue(percentRain.get(session.get(i)));
                    }
                }
            }
            file.close();

            FileOutputStream outFile = new FileOutputStream(new File(OUT_FILENAME+i+".xlsx"), true);
            workbook.write(outFile);
            outFile.flush();
            outFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void SaveLog(String station, SessionFormular formular) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(LOG_FILENAME, true);
            bw = new BufferedWriter(fw);

            StringBuilder str = new StringBuilder();

            str.append("\nDự báo hồi: ").append((new Date()).toString());
            str.append("\nTrạm ").append(station).append(" dự báo cho 1 ngày tới: ");
            str.append("\n" + formular.getReport_MaxTemperature());
            str.append("\n" + formular.getReport_MinTemperature());
            str.append("\n" + formular.getReport_AverageHumidity());
            str.append("\n" + formular.getReport_MaxWindDirect());
            str.append("\n" + formular.getReport_RainAndPercentRain());
            str.append("\n" + formular.getReport_AverageWindSpeed());
            str.append(formular.getReport_NumberMaxSun());

            bw.write(str.toString());
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
