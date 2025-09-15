package mil.army.swf.aviationappspring.pilot;

import mil.army.swf.aviationappspring.pilot.dto.FlightHourRanking;
import mil.army.swf.aviationappspring.util.http.exceptions.BadRequestException;
import mil.army.swf.aviationappspring.util.http.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PilotServiceTest {

    private static List<Pilot> mockPilotList;

    private static List<FlightHourRanking> mockLeaderboard;

    @Mock
    PilotRepository pilotRepository;

    @InjectMocks
    PilotService pilotService;

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
    }

    @Test
    void shouldGetFlightHourRanking() {
        when(pilotRepository.getFlightHourRanking())
                .thenReturn(mockLeaderboard);

        List<FlightHourRanking> leaderboard = pilotService.getFlightHourRankings();

        assertEquals(3, leaderboard.size());
        assertEquals(1L, leaderboard.getFirst().rank());

        verify(pilotRepository).getFlightHourRanking();

    }


    @Test
    void shouldSavePilot() {
        when(pilotRepository.save(any(Pilot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pilot response = pilotService.savePilot(mockPilotList.getFirst());

        assertEquals("Pete", response.getFirstName());
        assertEquals(29, response.getAge());
        assertEquals(1L, response.getId());

        verify(pilotRepository).save(any(Pilot.class));
    }

    @Test
    void shouldGetAllPilots() {
        when(pilotRepository.findAll())
                .thenReturn(mockPilotList);

        List<Pilot> pilotList = pilotService.findAllPilots();

        assertEquals(3, pilotList.size());

        verify(pilotRepository).findAll();

    }

    @Test
    void shouldGetPilotById() {
        when(pilotRepository.findById(any(Long.class)))
                .thenAnswer(invocation -> Optional.of(mockPilotList.getFirst()));

        Pilot pilot = pilotService.findPilotById(1L);

        assertEquals("Pete", pilot.getFirstName());
        assertEquals(29, pilot.getAge());

        verify(pilotRepository).findById(any(Long.class));

    }

    @Test
    void shouldThrowExceptionIfPilotNotFound() {
        when(pilotRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            pilotService.findPilotById(99L);
        });

        verify(pilotRepository).findById(any(Long.class));
    }

    @Test
    void shouldUpdateFlightHours() {
        when(pilotRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(mockPilotList.getFirst()));

        // return whatever is saved to make sure service is applying update
        when(pilotRepository.save(any(Pilot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Pilot updated = pilotService.updateFlightHours(1L, 6.4);

        assertEquals(106.4, updated.getFlightHours());

        verify(pilotRepository).findById(any(Long.class));
        verify(pilotRepository).save(any(Pilot.class));

    }

    @Test
    void shouldThrowWhenFlightHoursNotProvided() throws Exception {

        assertThrows(BadRequestException.class, () -> pilotService.updateFlightHours(1L, null));

        verify(pilotRepository, never()).findById(any(Long.class));
        verify(pilotRepository, never()).save(any(Pilot.class));
    }

}