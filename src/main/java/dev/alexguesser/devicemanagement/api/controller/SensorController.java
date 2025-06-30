package dev.alexguesser.devicemanagement.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.alexguesser.devicemanagement.api.client.SensorMonitoringClient;
import dev.alexguesser.devicemanagement.api.model.SensorDetailOutput;
import dev.alexguesser.devicemanagement.api.model.SensorInput;
import dev.alexguesser.devicemanagement.api.model.SensorMonitoringOutput;
import dev.alexguesser.devicemanagement.api.model.SensorOutput;
import dev.alexguesser.devicemanagement.common.IdGenerator;
import dev.alexguesser.devicemanagement.domain.model.Sensor;
import dev.alexguesser.devicemanagement.domain.model.SensorId;
import dev.alexguesser.devicemanagement.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;
    private final SensorMonitoringClient sensorMonitoringClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput input) {
        Sensor sensor = Sensor.builder()
                                .id(new SensorId(IdGenerator.generateTSID()))
                                .name(input.name())
                                .ip(input.ip())
                                .location(input.location())
                                .protocol(input.protocol())
                                .model(input.model())
                                .build();

        sensorRepository.saveAndFlush(sensor);

        return SensorOutput.from(sensor);
    }

    @GetMapping({"{sensorId}"})
    public SensorOutput getById(@PathVariable TSID sensorId) {
        return sensorRepository.findById(new SensorId(sensorId))
                       .map(SensorOutput::from)
                       .orElseThrow(() -> new ResponseStatusException(
                               HttpStatus.NOT_FOUND,
                               String.format("Sensor with id %s not found", sensorId)
                       ));
    }

    @GetMapping
    public Page<SensorOutput> search(@PageableDefault Pageable pageable) {
        return sensorRepository.findAll(pageable)
                       .map(SensorOutput::from);
    }

    @PutMapping("{sensorId}")
    public SensorOutput update(@PathVariable TSID sensorId, @RequestBody SensorInput input) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Sensor with id %s not found", sensorId)
                                ));

        sensor.setName(input.name());
        sensor.setIp(input.ip());
        sensor.setLocation(input.location());
        sensor.setProtocol(input.protocol());
        sensor.setModel(input.model());

        sensorRepository.saveAndFlush(sensor);

        return SensorOutput.from(sensor);
    }

    @DeleteMapping("{sensorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TSID sensorId) {
        SensorId id = new SensorId(sensorId);
        if (!sensorRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Sensor with id %s not found", sensorId)
            );
        }
        sensorRepository.deleteById(id);
        sensorMonitoringClient.disableSensorMonitoring(sensorId);
    }

    @PutMapping("{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableSensor(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Sensor with id %s not found", sensorId)
                                ));
        sensor.setEnabled(true);
        sensorRepository.saveAndFlush(sensor);
        sensorMonitoringClient.enableSensorMonitoring(sensorId);
    }

    @PutMapping("{sensorId}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableSensor(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Sensor with id %s not found", sensorId)
                                ));
        sensor.setEnabled(false);
        sensorRepository.saveAndFlush(sensor);
        sensorMonitoringClient.disableSensorMonitoring(sensorId);
    }

    @GetMapping("{sensorId}/detail")
    public SensorDetailOutput getSensorDetail(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Sensor with id %s not found", sensorId)
                                ));

        // Fetch additional details from the sensor monitoring service
        SensorMonitoringOutput sensorMonitoring = sensorMonitoringClient.getSensorMonitoring(sensorId);
        return SensorDetailOutput.from(sensor, sensorMonitoring);
    }

}
