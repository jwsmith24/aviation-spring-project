package mil.army.swf.aviationappspring.pilot.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateFlightHoursRequest(
        @NotNull @Positive Double flightHours
) {
}
