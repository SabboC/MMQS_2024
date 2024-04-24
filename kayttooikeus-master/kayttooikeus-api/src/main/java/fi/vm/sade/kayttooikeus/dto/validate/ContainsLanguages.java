package fi.vm.sade.kayttooikeus.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = ContainedLanguagesValidator.class)
@Documented
public @interface ContainsLanguages {
    String message() default "{fi.vm.sade.kayttooikeus.containsLanguages}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] languages() default {"FI", "EN", "SV"};
}
