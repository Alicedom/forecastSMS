package sms;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {


    public static String getCurTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date(System.currentTimeMillis()));
    }

    public static String getForecastTime(String path) throws ParseException {

        String filename = getFile(path);
        Date date = new Date(Utils.getDirDate(path));
        Timestamp timeforecast = new Timestamp(date.getTime() + 3600 * 1000 * (Integer.parseInt(filename)));
        return timeforecast.toString();
    }

    public static long getDirDate(String path) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        long date = 0;

        String parentdir = getDir(path);
        String timedownload = parentdir.substring(4, parentdir.length());

        date = formatter.parse(timedownload).getTime() + 7 * 3600 * 1000; // +UTC

        return date;
    }

    public static String getDir(String path) {
        return new File(path).getParentFile().getName();
    }

    public static String getFile(String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }

    public static void checkAndCreateNewFile(String saveDir) throws IOException {
        File file = new File(saveDir);

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists())
            file.createNewFile();

    }

}
