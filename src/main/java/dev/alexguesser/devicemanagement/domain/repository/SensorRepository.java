package dev.alexguesser.devicemanagement.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.alexguesser.devicemanagement.domain.model.Sensor;
import dev.alexguesser.devicemanagement.domain.model.SensorId;

public interface SensorRepository extends JpaRepository<Sensor, SensorId> {
}
