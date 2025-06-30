package dev.alexguesser.devicemanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import dev.alexguesser.devicemanagement.common.IdGenerator;
import io.hypersistence.tsid.TSID;

class TSIDTest {


    @Test
    void shouldGenerateTSID() {
        TSID tsid = IdGenerator.generateTSID();

        System.out.println(tsid);
        System.out.println(tsid.toLong());
        System.out.println(tsid.getInstant());

        assertThat(tsid.getInstant()).isCloseTo(
                Instant.now(),
                within(1, ChronoUnit.SECONDS));
    }

}
