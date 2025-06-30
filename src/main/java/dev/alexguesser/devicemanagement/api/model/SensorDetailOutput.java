package dev.alexguesser.devicemanagement.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dev.alexguesser.devicemanagement.domain.model.Sensor;
import io.hypersistence.tsid.TSID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SensorDetailOutput(
        SensorOutput sensor,
        SensorMonitoringOutput monitoring
) {

    public static SensorDetailOutput from(Sensor sensor, SensorMonitoringOutput sensorMonitoring) {
        return new SensorDetailOutput(
                SensorOutput.from(sensor),
                sensorMonitoring
        );
    }
}
