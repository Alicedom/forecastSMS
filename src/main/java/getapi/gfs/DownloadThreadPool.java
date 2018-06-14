package getapi.gfs;

import getapi.control.Utils;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static org.jsoup.Jsoup.connect;

public class DownloadThreadPool {
    private final String LOG = "data/log/forecastSMS/DownloadThreadPool.txt";
    private final String ERR_LOG = "data/log/forecastSMS/errDownloadThreadPool.txt";
    private final String SAVE_FOLDER = "data/forecastSMS/file/";
    private final int INDEX_DIR = 0;
    private final int NUMBER_LINK = 20;
    private final int THREAD_SIZE = 10;
    private final int TIME_OUT = 5000;


    public DownloadThreadPool() {

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_SIZE);
        CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);

        // bo file dau tien khong download????
        String dirFromURL = getDirFromURL(INDEX_DIR);
        if (dirFromURL.isEmpty()) {
            Utils.log(LOG, "Not found dir on url");
        } else if (new File(SAVE_FOLDER + dirFromURL).exists() && (new File(SAVE_FOLDER + dirFromURL).listFiles().length > 0)) {
            Utils.log(LOG, dirFromURL + " have data!!!");
        } else {

            List<String> list = getFileFromURL(NUMBER_LINK, dirFromURL);
            for (int i = 0; i < list.size(); i++) {
                Callable<String> r = new DownloadFileCallable(list.get(i), SAVE_FOLDER + dirFromURL + "/" + String.valueOf(i), TIME_OUT);
                Future<String> future = pool.submit(r);

                try {
                    Utils.log(LOG, future.get());
                } catch (InterruptedException e) {
                    Utils.log(ERR_LOG, e.getMessage());
                } catch (ExecutionException e) {
                    Utils.log(ERR_LOG, e.getMessage());
                }
            }
        }

        threadPool.shutdown();
    }

    public static void main(String[] args) {
        new DownloadThreadPool();
    }

    /*
    choose second:  take last url using jsoup
    Đơn giản, chính xác, chậm: toàn bộ link mất 3000ms
     */
    public String getDirFromURL(int indexDir) {
        final String urlDir = "http://nomads.ncep.noaa.gov/cgi-bin/filter_gfs_0p25.pl";

        String lastDir = null;
        try {
            Document docDir;
            docDir = connect(urlDir).timeout(10000).get();
            lastDir = docDir.select("table > tbody > tr > td > a").get(indexDir).text().trim(); // back to second link

        } catch (IOException e) {
            Utils.log(ERR_LOG, e.getMessage());
        }

        return lastDir;
    }

    public List<String> getFileFromURL(int numberFilePerDir, String dir) {
        List<String> downloadLink = new LinkedList<String>();

        final String urlPartFile = "http://nomads.ncep.noaa.gov/cgi-bin/filter_gfs_0p25.pl?dir=%2F";
        String fileURL = "http://nomads.ncep.noaa.gov/cgi-bin/filter_gfs_0p25.pl?file=YYY" +
                "&lev_10_m_above_ground=on&lev_2_m_above_ground=on&lev_surface=on&var_APCP=on&var_RH=on&var_TMP=on&var_UGRD=on&var_VGRD=on" +
                "leftlon=102&rightlon=109&toplat=23&bottomlat=8&dir=%2FXXX";

        Elements listFile = null;
        try {
            fileURL = fileURL.replace("XXX", dir); // set dir
            Document fileDir = connect(urlPartFile + dir).timeout(10000).get();
            listFile = fileDir.select("body > form > p:nth-child(2) > select > option");

        } catch (IOException e) {
            Utils.log(ERR_LOG, e.getMessage());
        }

        if (listFile.size() < numberFilePerDir) {
            Utils.log(LOG, "Link do not enough file: " + listFile.size());
        } else {
            for (int i = 1; i <= numberFilePerDir; i++) {
                String file = listFile.get(i).attr("value");
                String urlDownload = fileURL.replace("YYY", file); // set file
                downloadLink.add(urlDownload);
            }
        }

        return downloadLink;
    }


    private class DownloadFileCallable implements Callable<String> {

        private String link;
        private String fileName;
        private int timeout;

        public DownloadFileCallable(String link, String file, int timeout) {
            this.link = link;
            this.fileName = file;
            this.timeout = timeout;
        }

        public String call() {

            long start = System.currentTimeMillis();
            try {
                FileUtils.copyURLToFile(new URL(link), new File(fileName), timeout, timeout);

            } catch (IOException e) {
                Utils.log(ERR_LOG, e.getMessage());
            }
            long stop = System.currentTimeMillis();

            String result = (stop - start) + " : " + fileName + " : " + link + "\n";
            return result;

        }
    }
}
