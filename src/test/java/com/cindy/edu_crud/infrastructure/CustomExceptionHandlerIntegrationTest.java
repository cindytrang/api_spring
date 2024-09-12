package com.cindy.edu_crud.infrastructure;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomExceptionHandlerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        void testHandleInvalidDatesException() throws Exception {
            mockMvc.perform(get("/api/test-exception"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.messages[0]").value("Invalid date range"))
                    .andExpect(jsonPath("$.shortMessage").value("Invalid date range"));
        }

        @RestController
        static class TestController {
            @GetMapping("/api/test-exception")
            public void throwException() {
                throw new InvalidDatesException("Invalid date range");
            }
        }
}