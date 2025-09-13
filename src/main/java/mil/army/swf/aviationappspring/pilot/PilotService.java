package mil.army.swf.aviationappspring.pilot;

import mil.army.swf.aviationappspring.pilot.views.FlightHourRanking;
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

        return pilotRepository.findById(id).orElseThrow();

    }

    public List<FlightHourRanking> getFlightHourRankings() {
        return pilotRepository.getFlightHourRanking();
    }
}
