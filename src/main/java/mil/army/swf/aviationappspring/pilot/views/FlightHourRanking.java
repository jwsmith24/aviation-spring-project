package mil.army.swf.aviationappspring.pilot.views;

public record FlightHourRanking(
        Long id,
        String firstName,
        String lastName,
        Double flightHours,
        Long rank
) {
}
