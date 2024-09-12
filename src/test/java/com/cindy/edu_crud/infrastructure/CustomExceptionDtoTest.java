package com.cindy.edu_crud.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.AssertJUnit.assertEquals;

public class CustomExceptionDtoTest {
    // Unit test for CustomExceptionDto
    @Test
    void testCustomExceptionDtoBuilder() {
        LocalDateTime now = LocalDateTime.now();
        CustomExceptionDto dto = CustomExceptionDto.builder()
                .dateTime(now)
                .status(400)
                .messages(List.of("Error message"))
                .shortMessage("Short message")
                .build();

        assertEquals(now, dto.getDateTime());
        assertEquals(400, dto.getStatus());
        assertEquals(List.of("Error message"), dto.getMessages());
        assertEquals("Short message", dto.getShortMessage());
    }

    // Unit test for InvalidDatesException
    @Test
    void testInvalidDatesException() {
        String message = "Invalid date range";
        InvalidDatesException exception = new InvalidDatesException(message);
        assertEquals(message, exception.getMessage());
    }


}
