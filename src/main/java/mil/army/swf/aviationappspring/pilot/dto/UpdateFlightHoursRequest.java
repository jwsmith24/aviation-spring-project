package mil.army.swf.aviationappspring.pilot.dto;


import jakarta.validation.constraints.NotNull;

public record UpdateFlightHoursRequest(
        @NotNull Double flightHours
) {
}
