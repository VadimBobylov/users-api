package band.proxy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private static final String USER_NOT_FOUND_BY_ID = "User ID(\"%s\") not found";

    public UserNotFoundException(String id) {
        super(String.format(USER_NOT_FOUND_BY_ID, id));
    }

}
