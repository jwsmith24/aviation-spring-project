package mil.army.swf.aviationappspring.aircraft;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AircraftServiceTest {

    @Mock AircraftRepository aircraftRepository;

    @InjectMocks AircraftService aircraftService;

    Aircraft mockAircraft = new Aircraft(1L, "DOUGCraft", "Doug");

    @Test
    void shouldSaveAircraft() {
        when(aircraftRepository.save(mockAircraft)).thenReturn(mockAircraft);

        Aircraft result = aircraftService.createAircraft(mockAircraft);

        assertEquals("DOUGCraft", result.getAirframe());
        assertEquals("Doug", result.getPilot());
        assertEquals(1L, result.getId());

        Mockito.verify(aircraftRepository).save(any(Aircraft.class));
    }




}