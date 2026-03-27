package exceptions;

public class InvalidComputerStatusException extends RuntimeException {
    public InvalidComputerStatusException(String message) {
        super(message);
    }
}
