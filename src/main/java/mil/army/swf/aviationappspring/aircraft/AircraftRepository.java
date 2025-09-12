package mil.army.swf.aviationappspring.aircraft;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    List<Aircraft> findAllByPilot_Id(Long pilotId);

}
