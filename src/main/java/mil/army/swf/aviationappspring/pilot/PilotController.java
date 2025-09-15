package mil.army.swf.aviationappspring.pilot;

import jakarta.validation.Valid;
import mil.army.swf.aviationappspring.pilot.dto.FlightHourRanking;
import mil.army.swf.aviationappspring.pilot.dto.UpdateFlightHoursRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pilot")
public class PilotController {


    private final PilotService pilotService;

    public PilotController(PilotService pilotService) {
        this.pilotService = pilotService;
    }

    @PostMapping
    public ResponseEntity<Pilot> createPilot(@RequestBody Pilot pilot) {
        Pilot saved = pilotService.savePilot(pilot);

        return ResponseEntity.created(URI.create("/api/pilot")).body(saved);

    }

    @GetMapping
    public ResponseEntity<List<Pilot>> getPilots() {
        return ResponseEntity.ok(pilotService.findAllPilots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPilotById(@PathVariable Long id) {

        Pilot target = pilotService.findPilotById(id);
        return ResponseEntity.ok(target);

    }

    @GetMapping("/rankings")
    public ResponseEntity<List<FlightHourRanking>> getFlightHourRanking() {
        List<FlightHourRanking> leaderboard = pilotService.getFlightHourRankings();

        return ResponseEntity.ok(leaderboard);

    }

    @PatchMapping("/{id}/hours")
    public ResponseEntity<Pilot> updateFlightHours(@PathVariable("id") Long pilotId,
                                                   @Valid @RequestBody UpdateFlightHoursRequest request) {
        Pilot updated = pilotService.updateFlightHours(pilotId, request.flightHours());
        return ResponseEntity.ok(updated);
    }


}
