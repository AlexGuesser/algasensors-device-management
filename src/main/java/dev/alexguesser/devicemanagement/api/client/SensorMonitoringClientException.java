package dev.alexguesser.devicemanagement.api.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class SensorMonitoringClientException extends RuntimeException {
    public SensorMonitoringClientException(String message) {
        super(message);
    }
}
