package mil.army.swf.aviationappspring.aircraft;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import mil.army.swf.aviationappspring.pilot.Pilot;
import mil.army.swf.aviationappspring.pilot.views.AircraftPopularity;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AircraftController.class)
class AircraftControllerTest {

    private static final List<Aircraft> mockedAircraftList = new ArrayList<>();
    private static final List<AircraftPopularity> mockAircraftPopularity = new ArrayList<>();

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
        mockAircraft = new Aircraft(3L, "F-16", new Pilot(1L, "Pete", "Last", 29, 100d));
        mockedAircraftList.add(new Aircraft(1L, "F-22", new Pilot(2L, "Doug", "Last", 45, 304.9)));
        mockedAircraftList.add(new Aircraft(2L, "F-35", new Pilot(3L, "Steve", "lastname", 30,
                562.4)));
        updatedMockAircraft = new Aircraft(mockAircraft.getId(), mockAircraft.getAirframe(),
                new Pilot(1L, "UPDATEDPete", "Last", 29, 100d));

        mockAircraftPopularity.add(new AircraftPopularity("UH-60", 4L));
        mockAircraftPopularity.add(new AircraftPopularity("CH-47", 2L));
        mockAircraftPopularity.add(new AircraftPopularity("AH-64", 29L));

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

        when(aircraftService.getPopularAircraft()).thenReturn(mockAircraftPopularity);
        when(aircraftService.getPopularAircraft(1L)).thenReturn(mockAircraftPopularity
                .stream()
                .filter(mock -> mock.airframe().equals("UH-60")).toList());
    }

    @Test
    void shouldCreateAircraft() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/aircraft")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockAircraft))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.airframe").value("F-16"))
                .andExpect(jsonPath("$.pilot.firstName").value("Pete"));

        Mockito.verify(aircraftService).createAircraft(any(Aircraft.class));

    }

    @Test
    void shouldGetAircraftById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/aircraft/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.airframe").value("F-22"))
                .andExpect(jsonPath("$.pilot.firstName").value("Doug"));


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
                .andExpect(jsonPath("$.pilot.firstName").value("UPDATEDPete"))
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

    @Test
    void shouldGetPopularAircraftList() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/aircraft/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].totalPilots").value(4));

        verify(aircraftService).getPopularAircraft();
    }

    @Test
    void shouldGetLimitedPopularAircraftList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/aircraft/popular?limit=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].totalPilots").value(4));

        verify(aircraftService).getPopularAircraft(1L);
    }





}