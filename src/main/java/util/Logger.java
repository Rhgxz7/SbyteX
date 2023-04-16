package util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {

    static private String logFpath = "log.txt";
    public static <T> void log(T msg, int level) {
        SimpleDateFormat formatter = new SimpleDateFormat("[HH:mm:ss]");
        Date date = new Date();
        String timeStamp = formatter.format(date);

        String severity = "(Log)";
        switch (level) {
            case 0: //trace
                severity = " Log:";
            case 1: //info
                severity = " Info:";
            case 2: //war
                severity = " WARNING:";
            case 3: //err
                severity = " ERROR:";
            default:
                severity = " Log:";
        }

        System.out.println(timeStamp + severity + " " + msg);

        SimpleDateFormat fileTimeFormatter = new SimpleDateFormat("HH-mm-ss");
        Date fileDate = new Date();
        String fileTimeStamp = fileTimeFormatter.format(date);

        logFpath = ("SbyteX_log.txt " + fileTimeStamp);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFpath, true));

            writer.write(timeStamp +  severity + " " + msg);
            writer.newLine();
            writer.close();

        } catch (java.io.IOException e) {

            System.out.println("(ERROR) Error Creating Logfile");
            assert false : "(ERROR) Error Creating Logfile";
            e.printStackTrace();
        }


    }

}
