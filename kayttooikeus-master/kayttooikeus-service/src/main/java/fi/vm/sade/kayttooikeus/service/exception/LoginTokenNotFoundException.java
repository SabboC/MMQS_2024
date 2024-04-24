package fi.vm.sade.kayttooikeus.service.exception;

public class LoginTokenNotFoundException extends RuntimeException {
    public LoginTokenNotFoundException(String message) {
        super(message);
    }

    public LoginTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginTokenNotFoundException(Throwable cause) {
        super(cause);
    }

    public LoginTokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
