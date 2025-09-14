package mil.army.swf.aviationappspring.aircraft;

import mil.army.swf.aviationappspring.pilot.Pilot;
import mil.army.swf.aviationappspring.pilot.views.AircraftPopularity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {

    @Mock AircraftRepository aircraftRepository;

    @InjectMocks AircraftService aircraftService;


    private static final List<Aircraft> mockedAircraftList = new ArrayList<>();

    private static final List<AircraftPopularity> mockAircraftPopularity = new ArrayList<>();

    @BeforeAll
    static void setup() {
        mockedAircraftList.add(new Aircraft(1L, "DOUGCraft", new Pilot(2L, "Doug", "Last", 45,
                100d)));
        mockedAircraftList.add(new Aircraft(2L, "PETECraft", new Pilot(1L, "Pete", "Last", 29,
                304.9)));
        mockedAircraftList.add(new Aircraft(3L, "BOBCraft", new Pilot(3L, "Steve", "lastname", 30
                , 562.4)));

        mockAircraftPopularity.add(new AircraftPopularity("UH-60", 4L));
        mockAircraftPopularity.add(new AircraftPopularity("CH-47", 2L));
        mockAircraftPopularity.add(new AircraftPopularity("AH-64", 29L));

    }

    @Test
    void shouldSaveAircraft() {
        when(aircraftRepository.save(any(Aircraft.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Aircraft result = aircraftService.createAircraft(mockedAircraftList.getFirst());

        assertEquals("DOUGCraft", result.getAirframe());
        assertEquals("Doug", result.getPilot().getFirstName());
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
                .thenAnswer(invocation -> Optional.of(mockedAircraftList.get(1)));

        Aircraft target = aircraftService.getAircraftById(2L);

        assertEquals("Pete", target.getPilot().getFirstName());
        assertEquals("PETECraft", target.getAirframe());

        Mockito.verify(aircraftRepository).findById(any(Long.class));
    }

    @Test
    void shouldGetAllAircraftPilotFlies() {
        when(aircraftRepository.findAllByPilot_Id(eq(1L)))
                .thenReturn(mockedAircraftList.stream().filter(aircraft -> aircraft.getPilot().getId() == 1L ).toList());

        List<Aircraft> aircraftList = aircraftService.getAircraftByPilotId(1L);

        assertEquals(1, aircraftList.size());


    }

    @Test
    void shouldDeleteAircraft() {
        Long id = 99L;
        Aircraft mock = new Aircraft();
        mock.setId(id);

        when(aircraftRepository.findById(id))
                .thenReturn(Optional.of(mock));
        doNothing().when(aircraftRepository).delete(any(Aircraft.class));

        aircraftService.deleteAircraft(id);

        verify(aircraftRepository).findById(id);
        verify(aircraftRepository).delete(mock);
    }

    @Test
    void shouldThrowWhenDeletingNonExitingAircraft() {
        Long id = 999L;

        when(aircraftRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> aircraftService.deleteAircraft(id));

        verify(aircraftRepository).findById(id);
        // ensure delete is never called on the repo if an entity isn't found
        verify(aircraftRepository, never()).deleteById(any());
    }

    @Test
    void shouldGetAircraftPopularityDto() {
        when(aircraftRepository.getAircraftPopularity()).thenReturn(mockAircraftPopularity);

        List<AircraftPopularity> popularAircraft = aircraftService.getPopularAircraft();
        assertEquals(3, popularAircraft.size());

        verify(aircraftRepository).getAircraftPopularity();
    }

    @Test
    void shouldGetLimitedPopularityDto() {
        when(aircraftRepository.getAircraftPopularity(1L)).thenReturn(mockAircraftPopularity
                .stream()
                .filter(mock -> mock.airframe().equals("UH-60")).toList());

        List<AircraftPopularity> popularAircraft = aircraftService.getPopularAircraft(1L);
        assertEquals(1, popularAircraft.size());

        verify(aircraftRepository).getAircraftPopularity(1L);

    }





}