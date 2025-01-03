package com.finapp.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class BackendApplicationTest {
    @Test
    public void contextLoads() {
        assertThat(true).isTrue();
    }
}