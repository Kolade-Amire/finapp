package com.finapp.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:.env")
class BackendApplicationTest {
    @Test
    public void contextLoads() {
        assertThat(true).isTrue();
    }
}