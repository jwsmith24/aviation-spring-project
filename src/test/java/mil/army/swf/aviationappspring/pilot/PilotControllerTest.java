package mil.army.swf.aviationappspring.pilot;

import com.fasterxml.jackson.databind.ObjectMapper;
import mil.army.swf.aviationappspring.pilot.dto.FlightHourRanking;
import mil.army.swf.aviationappspring.pilot.dto.UpdateFlightHoursRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PilotController.class)
class PilotControllerTest {

    private static List<Pilot> mockPilotList;
    private static List<FlightHourRanking> mockLeaderboard;
    private static ArgumentCaptor<Pilot> captor;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PilotService pilotService;


    @BeforeAll
    static void setup() {
        mockPilotList = new ArrayList<>();
        mockLeaderboard = new ArrayList<>();

        mockPilotList.add(new Pilot(1L, "Pete", "Last", 29, 100d));
        mockPilotList.add(new Pilot(2L, "Doug", "Last", 45, 304.9));
        mockPilotList.add(new Pilot(3L, "Steve", "lastname", 30, 562.4));

        mockLeaderboard.add(new FlightHourRanking(3L, "Steve", "lastname", 562.4, 1L));
        mockLeaderboard.add(new FlightHourRanking(2L, "Doug", "Last", 304.9, 2L));
        mockLeaderboard.add(new FlightHourRanking(1L, "Pete", "Last", 100d, 3L));

        captor =  ArgumentCaptor.forClass(Pilot.class);
    }

    @Test
    void shouldCreatePilot() throws Exception {

        when(pilotService.savePilot(any(Pilot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pilot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPilotList.getFirst()))
                )
                .andExpect(status().isCreated());



        verify(pilotService).savePilot(captor.capture());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(mockPilotList.getFirst());
    }

    @Test
    void shouldGetPilots() throws Exception {
        when(pilotService.findAllPilots())
                .thenReturn(mockPilotList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pilot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

        verify(pilotService).findAllPilots();
    }

    @Test
    void shouldGetPilotById() throws Exception {
        when(pilotService.findPilotById(any(Long.class)))
                .thenReturn(mockPilotList.getFirst());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pilot/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Pete"))
                .andExpect(jsonPath("$.age").value(29));

        verify(pilotService).findPilotById(eq(1L));
    }


    @Test
    void shouldGetFlightHourRanking() throws Exception {
        when(pilotService.getFlightHourRankings())
                .thenReturn(mockLeaderboard);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pilot/rankings"))
                .andExpect(status().isOk());

        verify(pilotService).getFlightHourRankings();
    }

    @Test
    void shouldUpdatePilotFlightHours() throws Exception {
        Pilot updatedPilot = mockPilotList.getFirst();
        updatedPilot.setFlightHours(updatedPilot.getFlightHours() + 6.4);

        when(pilotService.updateFlightHours(any(Long.class), any(Double.class)))
                .thenReturn(updatedPilot);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/pilot/1/hours")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateFlightHoursRequest(6.4)))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldReturnErrorMessageForMissingFlightHours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/pilot/1" +
                        "/hours")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateFlightHoursRequest(null))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Validation failed for incoming request"))
                .andExpect(jsonPath("$.errors.flightHours").value("must not be null"));

        verify(pilotService, never()).updateFlightHours(any(Long.class), any(Double.class));

    }

    @Test
    void shouldReturnErrorMessageForNegativeFlightHours() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/pilot/1/hours")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateFlightHoursRequest(-23.4)))

        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed for incoming request"))
                .andExpect(jsonPath("$.errors.flightHours").value("must be greater than 0"));
    }



}