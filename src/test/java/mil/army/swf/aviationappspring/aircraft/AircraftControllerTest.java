package mil.army.swf.aviationappspring.aircraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AircraftController.class)
class AircraftControllerTest {

    private static final List<Aircraft> mockedAircraftList = new ArrayList<>();
    private static Aircraft mockAircraft;
    private static Aircraft updatedMockAircraft;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AircraftService aircraftService;

    @BeforeAll
    static void setup() {
        mockAircraft = new Aircraft(3L, "F-16", "Pete");
        mockedAircraftList.add(new Aircraft(1L, "F-22", "Doug"));
        mockedAircraftList.add(new Aircraft(2L, "F-35", "Steve"));
        updatedMockAircraft = new Aircraft(mockAircraft.getId(), mockAircraft.getAirframe(),
                "UPDATED pete");
    }

    @BeforeEach
    void mockSetup() {

        when(aircraftService.createAircraft(any(Aircraft.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        when(aircraftService.getAircraftById(1L))
                .thenReturn(mockedAircraftList.getFirst());

        when(aircraftService.getAllAircraft())
                .thenReturn(mockedAircraftList);

        when(aircraftService.updateAircraft(any(Long.class), any(Aircraft.class)))
                .thenReturn(updatedMockAircraft);
    }

    @Test
    void shouldCreateAircraft() throws Exception {
        // stub the controller response
        // when createAircraft is called on the service with any Aircraft object, return the
        // object passed

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/aircraft")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockAircraft))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.airframe").value("F-16"))
                .andExpect(jsonPath("$.pilot").value("Pete"));

        Mockito.verify(aircraftService).createAircraft(any(Aircraft.class));

    }

    @Test
    void shouldGetAircraftById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/aircraft/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.airframe").value("F-22"))
                .andExpect(jsonPath("$.pilot").value("Doug"));


        Mockito.verify(aircraftService).getAircraftById(any(Long.class));
    }

    @Test
    void shouldGetAllAircraft() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/aircraft"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        Mockito.verify(aircraftService).getAllAircraft();
    }

    @Test
    void shouldUpdateAircraftPilot() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/api/aircraft/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMockAircraft))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.pilot").value("UPDATED pete"))
                .andExpect(jsonPath("$.airframe").value("F-16"));


        Mockito.verify(aircraftService).updateAircraft(any(Long.class), any(Aircraft.class));
    }

    @Test
    void shouldDeleteAircraft() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/aircraft/3"))
                .andExpect(status().isNoContent());

        Mockito.verify(aircraftService).deleteAircraft(3L);
    }

    @Test
    void shouldReturnCorrectErrorMessageWhenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/aircraft/99"))
                .andExpect(status().isNotFound());

        Mockito.verify(aircraftService).getAircraftById(any(Long.class));

    }





}