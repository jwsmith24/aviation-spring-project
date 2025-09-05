package mil.army.swf.aviationappspring.aircraft;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftService {

    private final AircraftRepository repository;

    public AircraftService(AircraftRepository repository) {
        this.repository = repository;
    }

    public Aircraft createAircraft(Aircraft aircraft) {
       return  repository.save(aircraft);
    }

    public List<Aircraft> getAllAircraft() {
        return repository.findAll();
    }

    public Aircraft getAircraftById(Long id) {
        return repository.findById(id).orElseThrow();
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

        Aircraft target = repository.findById(id).orElseThrow();
        repository.delete(target);
    }
}
