package mil.army.swf.aviationappspring.aircraft;

import mil.army.swf.aviationappspring.aircraft.views.AircraftPopularity;
import mil.army.swf.aviationappspring.util.http.exceptions.BadRequestException;
import mil.army.swf.aviationappspring.util.http.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftService {

    private final AircraftRepository repository;

    public AircraftService(AircraftRepository repository) {
        this.repository = repository;
    }

    public Aircraft createAircraft(Aircraft aircraft) {

        if (aircraft.getAirframe() == null || aircraft.getAirframe().isBlank()) {
            throw new BadRequestException("Airframe must be present");
        }

        return repository.save(aircraft);
    }

    public List<Aircraft> getAllAircraft() {
        return repository.findAll();
    }

    public Aircraft getAircraftById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft with id: " + id + " not" +
                        " found"));

    }

    public List<Aircraft> getAircraftByPilotId(Long pilotId) {
        return repository.findAllByPilot_Id(pilotId);
    }

    public Aircraft updateAircraft(Long id, Aircraft updatedAircraft) {

        return repository.findById(id)
                .map(existing -> {
                    // update everything we need besides the id
                    existing.setAirframe(updatedAircraft.getAirframe());
                    existing.setPilot(updatedAircraft.getPilot());
                    return repository.save(existing);
                })
                .orElseThrow();
    }

    public void deleteAircraft(Long id) {

        Aircraft target =
                repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Aircraft" +
                        " with id: " + id + " not found"));
        repository.delete(target);
    }

    public List<AircraftPopularity> getPopularAircraft() {
        return repository.getAircraftPopularity();
    }

    public List<AircraftPopularity> getPopularAircraft(Long limit) {
        return repository.getAircraftPopularity(limit);
    }
}
