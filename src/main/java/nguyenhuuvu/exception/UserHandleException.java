package nguyenhuuvu.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UserHandleException extends Exception{
    private UserHandleException() {
    }
    private HttpStatus status;
    public UserHandleException(String message) {
        super(message);
    }

    public UserHandleException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
