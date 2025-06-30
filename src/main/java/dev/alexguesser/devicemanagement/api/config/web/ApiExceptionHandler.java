package dev.alexguesser.devicemanagement.api.config.web;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.alexguesser.devicemanagement.api.client.SensorMonitoringClientException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({
            SocketTimeoutException.class,
            ConnectException.class,
            ClosedChannelException.class
    })
    public ProblemDetail handle(IOException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);
        problemDetail.setTitle("Gateway Timeout");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("errors/gateway-timeout"));
        return problemDetail;
    }

    @ExceptionHandler(SensorMonitoringClientException.class)
    public ProblemDetail handleSensorMonitoringClientException(SensorMonitoringClientException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problemDetail.setTitle("Bad Gateway");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("errors/bad-gateway"));
        return problemDetail;
    }

}
