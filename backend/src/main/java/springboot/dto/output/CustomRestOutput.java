package springboot.dto.output;

import java.util.Date;

public class CustomRestOutput {
    public long timestamp;
    public String message;

    public CustomRestOutput(Date timestamp, String message) {
        this.timestamp = timestamp.getTime();
        this.message = message;
    }

    public CustomRestOutput(Exception ex) {
        this.timestamp = new Date().getTime();
        this.message = ex.getMessage();
    }
}