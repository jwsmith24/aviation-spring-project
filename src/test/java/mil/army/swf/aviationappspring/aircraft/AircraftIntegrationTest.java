package mil.army.swf.aviationappspring.aircraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mil.army.swf.aviationappspring.pilot.Pilot;
import mil.army.swf.aviationappspring.pilot.PilotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AircraftIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.5")
            .withDatabaseName("aviation_test_db")
            .withUsername("user")
            .withPassword("root");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AircraftRepository aircraftRepository;

    @Autowired
    PilotRepository pilotRepository;

    Pilot mockPilot = new Pilot();
    Aircraft mockAircraft = new Aircraft();


    @BeforeEach
    void setup() {
        mockPilot.setFirstName("Test");
        mockPilot.setLastName("Pilot");
        mockPilot.setFlightHours(400.0);

        mockAircraft.setAirframe("C-130");
        mockAircraft.setPilot(mockPilot);

    }

    @BeforeEach
    void cleanDatabase() {
        aircraftRepository.deleteAll();
        pilotRepository.deleteAll();
    }

    @Test
    void shouldCreateAircraft() throws Exception {

        // post aircraft
        MvcResult result = mockMvc.perform(post("/api/aircraft")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAircraft))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.pilot.firstName").value("Test"))
                .andExpect(jsonPath("$.airframe").value("C-130"))
                .andReturn();

        // parse id
        String responseBody = result.getResponse().getContentAsString();
        Aircraft created = objectMapper.readValue(responseBody, Aircraft.class);
        Long createdId = created.getId();

        // try fetching the new aircraft

        mockMvc.perform(MockMvcRequestBuilders.get("/api/aircraft/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airframe").value("C-130"))
                .andExpect(jsonPath("$.id").value(createdId));

    }

    @Test
    void shouldGetAllAircraft() throws Exception {

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/aircraft")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockAircraft)));
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/aircraft"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }


}
