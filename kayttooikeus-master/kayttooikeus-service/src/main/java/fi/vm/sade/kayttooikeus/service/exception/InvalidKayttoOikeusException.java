package fi.vm.sade.kayttooikeus.service.exception;

import fi.vm.sade.generic.service.exception.SadeBusinessException;

public class InvalidKayttoOikeusException extends SadeBusinessException{
    public InvalidKayttoOikeusException() {

    }

    public InvalidKayttoOikeusException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidKayttoOikeusException(String message) {
        super(message);
    }

    public InvalidKayttoOikeusException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getErrorKey() {
        return InvalidKayttoOikeusException.class.getCanonicalName();
    }
}
