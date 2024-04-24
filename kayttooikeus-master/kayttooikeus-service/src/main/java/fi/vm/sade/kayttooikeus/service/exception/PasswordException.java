package fi.vm.sade.kayttooikeus.service.exception;

import fi.vm.sade.generic.service.exception.SadeBusinessException;

public class PasswordException extends SadeBusinessException{
    public PasswordException() {

    }

    public PasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordException(String message) {
        super(message);
    }

    public PasswordException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getErrorKey() {
        return PasswordException.class.getCanonicalName();
    }
}
