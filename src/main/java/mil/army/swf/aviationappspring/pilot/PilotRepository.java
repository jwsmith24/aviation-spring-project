package mil.army.swf.aviationappspring.pilot;

import mil.army.swf.aviationappspring.pilot.views.FlightHourRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PilotRepository extends JpaRepository<Pilot, Long> {

    @Query(value =
            "SELECT id, first_name as firstName, last_name AS lastName, " +
                    "flight_hours AS flightHours, rank " +
                    "FROM pilot_hours_ranked",
            nativeQuery = true)
    List<FlightHourRanking> getFlightHourRanking();

}
