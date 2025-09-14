package mil.army.swf.aviationappspring.aircraft;

import mil.army.swf.aviationappspring.pilot.views.AircraftPopularity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    List<Aircraft> findAllByPilot_Id(Long pilotId);

    @Query(value = "SELECT * FROM aircraft_popularity", nativeQuery = true)
    List<AircraftPopularity> getAircraftPopularity();


    @Query(value = "SELECT * FROM aircraft_popularity ORDER BY total_pilots DESC LIMIT :limit",
            nativeQuery =
            true)
    List<AircraftPopularity> getAircraftPopularity(@Param("limit") Long limit);

}
