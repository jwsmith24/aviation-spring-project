package mil.army.swf.aviationappspring.aircraft;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {

    @Mock AircraftRepository aircraftRepository;

    @InjectMocks AircraftService aircraftService;


    private static final List<Aircraft> mockedAircraftList = new ArrayList<>();

    @BeforeAll
    static void setup() {
        mockedAircraftList.add(new Aircraft(1L, "DOUGCraft", "Doug"));
        mockedAircraftList.add(new Aircraft(2L, "PETECraft", "Pete"));
        mockedAircraftList.add(new Aircraft(3L, "BOBCraft", "Bob"));

    }

    @Test
    void shouldSaveAircraft() {
        when(aircraftRepository.save(any(Aircraft.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Aircraft result = aircraftService.createAircraft(mockedAircraftList.getFirst());

        assertEquals("DOUGCraft", result.getAirframe());
        assertEquals("Doug", result.getPilot());
        assertEquals(1L, result.getId());

        Mockito.verify(aircraftRepository).save(any(Aircraft.class));
    }

    @Test
    void shouldGetAllAircraft() {
        when(aircraftRepository.findAll())
                .thenReturn(mockedAircraftList);

        List<Aircraft> aircraftList = aircraftService.getAllAircraft();

        assertEquals(3, aircraftList.size());

        Mockito.verify(aircraftRepository).findAll();
    }

    @Test
    void shouldGetAircraftById() {
        when(aircraftRepository.findById(any(Long.class)))
                .thenAnswer(invocation -> mockedAircraftList.get(invocation.getArgument(0)));

        Aircraft target = aircraftService.getAircraftById(2L);

        assertEquals("Pete", target.getPilot());
        assertEquals("PETECraft", target.getAirframe());

        Mockito.verify(aircraftRepository).findById(any(Long.class));
    }



}