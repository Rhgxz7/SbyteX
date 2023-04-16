package util;

public class LogMessage {

    private String message;
    private int severity;

    LogMessage(String message, int severity) {
        this.message = message;
        this.severity = severity;
    }

    String getMessage(){
        return this.message;
    }

    int getSeverity() {
        return this.severity;
    }

}
