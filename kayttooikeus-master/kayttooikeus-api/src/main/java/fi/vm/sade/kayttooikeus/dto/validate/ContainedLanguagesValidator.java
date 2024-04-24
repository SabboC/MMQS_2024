package fi.vm.sade.kayttooikeus.dto.validate;

import fi.vm.sade.kayttooikeus.dto.TextGroupDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

/**
 * Created by autio on 31.10.2016.
 */
public class ContainedLanguagesValidator implements ConstraintValidator<ContainsLanguages, TextGroupDto> {
   private String[] required;

   public void initialize(ContainsLanguages constraint) {
      this.required = constraint.languages();
   }

   public boolean isValid(TextGroupDto dto, ConstraintValidatorContext context) {
      return dto == null || Stream.of(required).allMatch(lang -> isEmpty(dto.get(lang)));
   }

   private static boolean isEmpty(String str) {
      return str != null && !str.isEmpty();
   }
}
