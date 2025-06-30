package dev.alexguesser.devicemanagement;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import dev.alexguesser.devicemanagement.common.IdGenerator;
import dev.alexguesser.devicemanagement.common.UUIDv7Utils;

class UUIDv7Test {

    @Test
    void shouldGenerateUUIDv7() {
        UUID uuid = IdGenerator.generateTimeBasedUUID();
        OffsetDateTime uuidDateTime = UUIDv7Utils.extractOffsetDateTime(uuid).truncatedTo(ChronoUnit.MINUTES);
        OffsetDateTime currentDateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        assertThat(uuidDateTime).isEqualTo(currentDateTime);
    }

}
