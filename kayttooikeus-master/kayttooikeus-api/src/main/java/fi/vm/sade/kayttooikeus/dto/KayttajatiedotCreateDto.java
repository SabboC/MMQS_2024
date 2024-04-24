package fi.vm.sade.kayttooikeus.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KayttajatiedotCreateDto {

    @NotNull
    @Pattern(regexp = Constants.USERNAME_REGEXP)
    private String username;

}
