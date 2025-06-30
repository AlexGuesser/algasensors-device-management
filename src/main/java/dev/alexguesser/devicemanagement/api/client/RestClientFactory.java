package dev.alexguesser.devicemanagement.api.client;

import java.time.Duration;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestClientFactory {

    private final RestClient.Builder builder;

    public RestClient forTemperatureMonitoring() {
        return builder.baseUrl("http://localhost:8081/")
                       .requestFactory(generateClientHttpRequestFactory())
                       .defaultStatusHandler(
                               HttpStatusCode::isError,
                               (request, response) -> {
                                   throw new SensorMonitoringClientException("Failed to communicate with sensor monitoring service");
                               }
                       ).build();
    }

    private ClientHttpRequestFactory generateClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        return requestFactory;
    }


}
