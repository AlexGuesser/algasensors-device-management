package dev.alexguesser.devicemanagement.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dev.alexguesser.devicemanagement.domain.model.Sensor;
import io.hypersistence.tsid.TSID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SensorOutput(
        TSID id,
        String name,
        String ip,
        String location,
        String protocol,
        String model,
        boolean enabled
) {

    public static SensorOutput from(Sensor sensor) {
        return new SensorOutput(
                sensor.getId().getValue(),
                sensor.getName(),
                sensor.getIp(),
                sensor.getLocation(),
                sensor.getProtocol(),
                sensor.getModel(),
                sensor.isEnabled()
        );
    }

}
