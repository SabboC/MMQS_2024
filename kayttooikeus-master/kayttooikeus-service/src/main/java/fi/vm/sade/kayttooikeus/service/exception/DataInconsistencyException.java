package fi.vm.sade.kayttooikeus.service.exception;

public class DataInconsistencyException extends RuntimeException {

    public DataInconsistencyException() {
    }

    public DataInconsistencyException(String message) {
        super(message);
    }

    public DataInconsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataInconsistencyException(Throwable cause) {
        super(cause);
    }

}
