package fi.vm.sade.kayttooikeus.repositories.dto;

import fi.vm.sade.kayttooikeus.dto.Constants;
import fi.vm.sade.oppijanumerorekisteri.dto.KielisyysDto;
import fi.vm.sade.oppijanumerorekisteri.validation.ValidateAsiointikieli;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HenkiloCreateByKutsuDto {
    @NotNull
    @Size(min = 1)
    private String kutsumanimi;

    @ValidateAsiointikieli
    private KielisyysDto asiointiKieli;

    private String kayttajanimi;

    private String password;
}
