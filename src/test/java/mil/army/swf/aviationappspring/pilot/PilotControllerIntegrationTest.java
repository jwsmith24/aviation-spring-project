package mil.army.swf.aviationappspring.pilot;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mil.army.swf.aviationappspring.aircraft.Aircraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PilotControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Pilot mockPilot = new Pilot();

    @BeforeEach
    void setup() {
        mockPilot.setFirstName("Test");
        mockPilot.setLastName("Pilot");
        mockPilot.setFlightHours(400.0);
    }

    @Test
    void shouldCreatePilot() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/pilot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockPilot))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Pilot created = objectMapper.readValue(responseBody, Pilot.class);
        Long createdId = created.getId();

        mockMvc.perform(get("/api/pilot/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("Pilot"))
                .andExpect(jsonPath("$.flightHours").value(400.0))
                .andExpect(jsonPath("$.id").value(createdId));
    }


}
