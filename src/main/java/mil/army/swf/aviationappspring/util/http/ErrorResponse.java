package mil.army.swf.aviationappspring.util.http;

public record ErrorResponse(
        String errorCode,
        String message
) {
}
