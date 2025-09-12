package mil.army.swf.aviationappspring.pilot;

import com.fasterxml.jackson.databind.ObjectMapper;
import mil.army.swf.aviationappspring.aircraft.AircraftService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PilotController.class)
class PilotControllerTest {

    private static List<Pilot> mockPilotList;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PilotService pilotService;


    @BeforeAll
    static void setup() {
        mockPilotList = new ArrayList<>();

        mockPilotList.add(new Pilot(1L, "Pete", "Last", 29));
        mockPilotList.add(new Pilot(2L, "Doug", "Last", 45));
        mockPilotList.add(new Pilot(3L, "Steve", "lastname", 30));
    }

    @Test
    void shouldCreatePilot() throws Exception {

        when(pilotService.savePilot(any(Pilot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pilot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPilotList.getFirst()))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Pete"))
                .andExpect(jsonPath("$.age").value(29));

        verify(pilotService).savePilot(any(Pilot.class));
    }

    @Test
    void shouldGetPilots() throws Exception {
        when (pilotService.findAllPilots())
                .thenReturn(mockPilotList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pilot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

        verify(pilotService).findAllPilots();
    }

    @Test
    void shouldGetPilotById() throws Exception {
        when (pilotService.findPilotById(any(Long.class)))
                .thenReturn(mockPilotList.getFirst());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pilot/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Pete"))
                .andExpect(jsonPath("$.age").value(29));

        verify(pilotService).findPilotById(eq(1L));
    }

    @Test
    void shouldThrowWhenPilotNotFound() throws Exception {
        when (pilotService.findPilotById(any(Long.class)))
                .thenThrow(new NoSuchElementException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pilot/999"))
                .andExpect(status().isNotFound());

        verify(pilotService).findPilotById(eq(999L));

    }

}