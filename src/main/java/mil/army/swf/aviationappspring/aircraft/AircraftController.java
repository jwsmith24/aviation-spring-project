package mil.army.swf.aviationappspring.aircraft;

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
        return ResponseEntity.ok(service.getAircraftById(id));
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

}
