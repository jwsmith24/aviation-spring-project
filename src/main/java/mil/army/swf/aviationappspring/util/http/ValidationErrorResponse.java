package mil.army.swf.aviationappspring.util.http;

import java.util.Map;

public record ValidationErrorResponse(
        String errorCode,
        String message,
        Map<String, String> errors
) {

}
