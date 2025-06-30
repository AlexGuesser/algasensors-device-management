package dev.alexguesser.devicemanagement.api.client.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import dev.alexguesser.devicemanagement.api.client.RestClientFactory;
import dev.alexguesser.devicemanagement.api.client.SensorMonitoringClient;
import dev.alexguesser.devicemanagement.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;


//@Component
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient restClient;

    public SensorMonitoringClientImpl(RestClientFactory restClientFactory) {
        this.restClient = restClientFactory.forTemperatureMonitoring();
    }


    @Override
    public void enableSensorMonitoring(TSID sensorId) {
        String url = String.format("/api/sensors/%s/monitoring/enable", sensorId.toString());
        restClient.put()
                .uri(url)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void disableSensorMonitoring(TSID sensorId) {
        String url = String.format("/api/sensors/%s/monitoring/disable", sensorId.toString());
        restClient.delete()
                .uri(url)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public SensorMonitoringOutput getSensorMonitoring(TSID sensorId) {
        return restClient.get()
                .uri(String.format("/api/sensors/%s/monitoring", sensorId.toString()))
                .retrieve()
                .body(SensorMonitoringOutput.class);
    }
}
