package mil.army.swf.aviationappspring.aircraft;

import mil.army.swf.aviationappspring.pilot.views.AircraftPopularity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
public class AircraftController {

    private final AircraftService service;

    public AircraftController(AircraftService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft newAircraft) {
        Aircraft created = service.createAircraft(newAircraft);

        return ResponseEntity.created(URI.create("/api/aircraft"))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Aircraft>> getAllAircraft() {
        return ResponseEntity.ok(service.getAllAircraft());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {

        Aircraft targetAircraft = service.getAircraftById(id);
        if (targetAircraft == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(targetAircraft);
    }

    @GetMapping(params = "pilotId")
    public ResponseEntity<List<Aircraft>> getAircraftByPilotId(@RequestParam("pilotId") Long pilotId) {
        return ResponseEntity.ok(service.getAircraftByPilotId(pilotId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id,
                                                   @RequestBody Aircraft updated) {
        return ResponseEntity.ok(service.updateAircraft(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {

        service.deleteAircraft(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<AircraftPopularity>> getPopularAircraft() {
        return ResponseEntity.ok(service.getPopularAircraft());
    }

    @GetMapping(value = "/popular", params = "limit")
    public ResponseEntity<List<AircraftPopularity>> getLimitedPopularityList(@RequestParam("limit") Long limit) {
        return ResponseEntity.ok(service.getPopularAircraft(limit));
    }

}
