package mil.army.swf.aviationappspring.pilot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

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

        try {
            Pilot target = pilotService.findPilotById(id);
            return ResponseEntity.ok(target);

        } catch(NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }


    }


}
