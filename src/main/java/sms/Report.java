package sms;

import getapi.dao.Dao;
import getapi.models.Station;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

public class Report {
    private final static Logger logger = LoggerFactory.getLogger(Report.class);
    private final String TEMP_FILENAME = "src/main/resources/template.xlsx";
    private final String OUT_FILENAME = "data/report/Bản tin thời tiết tự động.xlsx";

    private final int START_ROW = 3;
    private final int NUMBER_FORECAST = 5;


    private Dao query;

    public Report() {

        if (query == null) {
            query = new Dao();
        }
    }

    public void fromWebsite(String website) {
        FileInputStream templateFile = null;
        XSSFWorkbook workbook = null;

        try {
            templateFile = new FileInputStream(new File(TEMP_FILENAME));
            workbook = new XSSFWorkbook(templateFile);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        List<Station> listStation = query.getStationEnalbeApi();

        for (int i = 0; i < listStation.size(); i++) {
            Station station = listStation.get(i);

            XSSFSheet sheet = workbook.cloneSheet(0, station.getStation_name_vi());

            for (int forecastDay = 0; forecastDay < NUMBER_FORECAST; forecastDay++) {
                int row = START_ROW + forecastDay;
                List<HourlyWeather> listStationHourlyData = query.getDataHourly(station.getStation_code(), website, forecastDay);

                Cell cell = null;
                //Update the value of cell

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, forecastDay);
                String date = calendar.get(Calendar.DATE) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
                cell = sheet.getRow(row).getCell(0);
                cell.setCellValue(date);

                String sms = null;
                if (listStationHourlyData == null || listStationHourlyData.isEmpty()) {
                    sms = "Trạm " + station.getStation_name_vi() + " không có dữ liệu dự báo\n";
                } else {
                    SessionFormular formular = new SessionFormular(listStationHourlyData);

                    sms = "Trạm " + station.getStation_name_vi() + getSMSReport(date, formular);

                    if (listStationHourlyData.size() < 24) {
                        sms = "Trạm " + station.getStation_name_vi() + " tính trên số giờ dữ liệu " + listStationHourlyData.size() + getSMSReport(date, formular);
                    }

                    double sumUV = formular.getTotalUV();
                    cell = sheet.getRow(row).getCell(1);
                    cell.setCellValue(sumUV);

                    String maxTem = formular.getMaxTemperature();
                    cell = sheet.getRow(row).getCell(16);
                    cell.setCellValue(maxTem);

                    String minTem = formular.getMinTemperature();
                    cell = sheet.getRow(row).getCell(17);
                    cell.setCellValue(minTem);

                    double humidity = formular.getAverageHumidity();
                    cell = sheet.getRow(row).getCell(18);
                    cell.setCellValue(humidity);

                    String maxWinddirect = formular.getMaxWindDirect();
                    cell = sheet.getRow(row).getCell(19);
                    cell.setCellValue(maxWinddirect);

                    double avgWindSpeed = formular.getAverageWindSpeed();
                    cell = sheet.getRow(row).getCell(20);
                    cell.setCellValue(avgWindSpeed);

                    Hashtable<String, Double> rain = formular.getRain();
                    Hashtable<String, Double> percentRain = formular.getPercentRain();

                    if (!rain.isEmpty()) {
                        List<String> session = SMSRule.getListSMS(SMSRule.getSession());
                        for (int j = 0; j < session.size(); j++) {
                            if (rain.containsKey(session.get(j))) {
                                double mount = (double) (Math.round(rain.get(session.get(j)) * 10.0) / 10.0);
                                cell = sheet.getRow(row).getCell(2 + j);
                                cell.setCellValue(mount);
                                cell = sheet.getRow(row).getCell(3 + j);
                                cell.setCellValue(percentRain.get(session.get(j)));
                            }
                        }
                    }
                }

                cell = sheet.getRow(row).getCell(21);
                cell.setCellValue(sms);

                cell = sheet.getRow(row).getCell(22);
                cell.setCellValue(getNonSign(sms));
                //log
                logger.info(sms);
            }
        }

        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream(new File(OUT_FILENAME), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {

            templateFile.close();
            workbook.removeSheetAt(0);
            workbook.write(outFile);
            outFile.flush();
            outFile.close();

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getSMSReport(String date, SessionFormular formular) {

        StringBuilder report = new StringBuilder();

        report.append(".\nDự báo ngày: ").append(date);
        report.append(".\n" + formular.getReport_MaxTemperature());
        report.append(".\n" + formular.getReport_MinTemperature());
        report.append(".\n" + formular.getReport_AverageHumidity());
        report.append(".\n" + formular.getReport_MaxWindDirect());
        report.append(" " + formular.getReport_AverageWindSpeed());
        report.append(".\n" + formular.getReport_NumberMaxSun());
        report.append(". " + formular.getReport_RainAndPercentRain());
        report.append(".\n");

        return report.toString();

    }

    // ham tra ve sms khong dau
    private String getNonSign(String s) {
        String nfdNormalizedString = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
