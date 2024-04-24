package fi.vm.sade.kayttooikeus.service.exception;

import org.springframework.validation.Errors;

import static java.util.Objects.requireNonNull;

public class UnprocessableEntityException extends RuntimeException {

    private final Errors errors;

    public UnprocessableEntityException(Errors errors) {
        this.errors = requireNonNull(errors);
    }

    public Errors getErrors() {
        return errors;
    }

}
