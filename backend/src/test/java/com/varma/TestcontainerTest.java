package com.varma;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainerTest extends AbstractTestContainersUnitTest {


    @Test
    void canStartPostgresDB() {

        assertThat(postgreSQLContainer.isRunning()).isTrue();
        //assertThat(postgreSQLContainer.isHealthy()).isTrue();1
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        //given
        //when
        //then
    }


}
