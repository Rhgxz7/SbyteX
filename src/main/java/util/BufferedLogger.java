package util;

import java.util.List;

public class BufferedLogger {

    static private List<LogMessage> logBuffer;

    static void push(LogMessage logMsg) {
        logBuffer.add(logMsg);
    }

    static void loop() {

        //read buffer

        //log from buffer

    }

}
