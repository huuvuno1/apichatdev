package nguyenhuuvu.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message) {
        super(message);
    }
}
