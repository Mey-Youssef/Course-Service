package tn.esprit.courseservice.Exception;

public class UnauthorizedAccessException extends RuntimeException  {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

}
