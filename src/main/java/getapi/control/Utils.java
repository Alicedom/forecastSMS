package getapi.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void log(String fileLog, String sms) {
        checkAndCreateNewFile(fileLog);

        String sdt = df.format(new Date(System.currentTimeMillis()));
        try {
            Files.write(Paths.get(fileLog), (sdt + " : " + sms + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sdt + " : " + fileLog + " : " + sms);
    }

    public static void checkAndCreateNewFile(String saveDir) {
        File log = new File(saveDir);

        if (log.getParentFile() != null && !log.getParentFile().exists()) {
            log.getParentFile().mkdirs();
        }
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
