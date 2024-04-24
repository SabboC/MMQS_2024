package fi.vm.sade.kayttooikeus.service.external;

import fi.vm.sade.kayttooikeus.util.FunctionalUtils.RuntimeIOExceptionWrapper;

import java.util.function.Function;

public class ExternalServiceException extends RuntimeException {
    private final String resource;
    
    public ExternalServiceException(String resource, String message, Throwable cause) {
        super(message, cause);
        this.resource = resource;
    }

    public ExternalServiceException(String resource, Throwable cause) {
        super(cause);
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
    
    public static <Ex extends Exception>  Function<Ex,ExternalServiceException> mapper(String resource) {
        return from -> {
            Throwable source = from;
            if (source instanceof RuntimeIOExceptionWrapper) {
                source = source.getCause();
            }
            return new ExternalServiceException(resource, source.getMessage(), source);
        };
    }
}
