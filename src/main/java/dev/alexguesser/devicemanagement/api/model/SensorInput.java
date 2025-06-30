package dev.alexguesser.devicemanagement.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SensorInput(
        String name,
        String ip,
        String location,
        String protocol,
        String model
) {
}
