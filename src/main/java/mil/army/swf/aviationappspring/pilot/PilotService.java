package mil.army.swf.aviationappspring.pilot;

import mil.army.swf.aviationappspring.pilot.dto.FlightHourRanking;
import mil.army.swf.aviationappspring.util.http.exceptions.BadRequestException;
import mil.army.swf.aviationappspring.util.http.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PilotService {

    private final PilotRepository pilotRepository;


    public PilotService(PilotRepository pilotRepository) {
        this.pilotRepository = pilotRepository;
    }

    public Pilot savePilot(Pilot pilot) {

        return pilotRepository.save(pilot);

    }

    public List<Pilot> findAllPilots() {
        return pilotRepository.findAll();
    }

    public Pilot findPilotById(Long id) {

        return pilotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Pilot with id: " + id + " not found"));

    }

    public List<FlightHourRanking> getFlightHourRankings() {
        return pilotRepository.getFlightHourRanking();
    }

    public Pilot updateFlightHours(Long pilotId, Double flightHours) {
        if (flightHours == null) {
            throw new BadRequestException("No flight hours were provided to update pilot with id:" +
                    " " + pilotId);
        }

        if (flightHours < 0 ) {
            throw new BadRequestException("Flight hours cannot be negative for pilot with id: " + pilotId);
        }

        Pilot pilot =
                pilotRepository.findById(pilotId)
                        .orElseThrow(() -> new ResourceNotFoundException("Pilot with ID: " + pilotId + " not found.."));

        double existingFlightHours = pilot.getFlightHours() != null ?
                pilot.getFlightHours()
                : 0d;

        pilot.setFlightHours(existingFlightHours + flightHours);

        return pilotRepository.save(pilot);
    }


}
