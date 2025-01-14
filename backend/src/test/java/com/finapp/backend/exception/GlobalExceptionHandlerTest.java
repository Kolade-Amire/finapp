package com.finapp.backend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finapp.backend.dto.RegistrationRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:.env.test")
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenInvalidRegistrationInput_thenReturnsCustomErrorMessages() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .email("invalid-email")
                .firstname("")
                .lastname("")
                .phoneNumber("invalid-number")
                .password("bad")
                .confirmPassword("even-worse")
                .build();


        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Request Validation Failed"))
                .andExpect(jsonPath("$.detail").value("One or more validation errors occurred."))
                .andExpect(jsonPath("$.properties.errors").isArray())
                .andExpect(jsonPath("$.properties.errors[*]").value(Matchers.hasItem(Matchers.containsString("Invalid email format."))))
                .andExpect(jsonPath("$.properties.errors[*]").value(Matchers.hasItem(Matchers.containsString("Invalid phone number format."))))
                .andExpect(jsonPath("$.properties.errors[*]").value(Matchers.hasItem(Matchers.containsString("Password must be at least 8 characters long, include an uppercase letter, a number, and a special character."))))
                .andExpect(jsonPath("$.properties.errors[*]").value(Matchers.hasItem(Matchers.containsString("Lastname is required."))))
                .andExpect(jsonPath("$.properties.errors[*]").value(Matchers.hasItem(Matchers.containsString("Firstname is required."))))
                .andDo(result -> LOGGER.info("Response: {}", result.getResponse().getContentAsString()));


    }
}
