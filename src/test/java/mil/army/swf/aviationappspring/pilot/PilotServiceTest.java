package mil.army.swf.aviationappspring.pilot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PilotServiceTest {

    private static List<Pilot> mockPilotList;

    @Mock
    PilotRepository pilotRepository;

    @InjectMocks
    PilotService pilotService;

    @BeforeAll
    static void setup() {
        mockPilotList = new ArrayList<>();

        mockPilotList.add(new Pilot(1L, "Pete", "Last", 29));
        mockPilotList.add(new Pilot(2L, "Doug", "Last", 45));
        mockPilotList.add(new Pilot(3L, "Steve", "lastname", 30));
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

        assertThrows(NoSuchElementException.class, () -> {
            pilotService.findPilotById(99L);
        });

        verify(pilotRepository).findById(any(Long.class));
    }

}