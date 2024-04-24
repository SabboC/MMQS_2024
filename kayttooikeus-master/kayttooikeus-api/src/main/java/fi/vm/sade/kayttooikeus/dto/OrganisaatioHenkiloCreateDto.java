package fi.vm.sade.kayttooikeus.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrganisaatioHenkiloCreateDto {
    @NotNull @Size(min = 1)
    private String organisaatioOid;
    private OrganisaatioHenkiloTyyppi organisaatioHenkiloTyyppi;
    private LocalDate voimassaAlkuPvm;
    private LocalDate voimassaLoppuPvm;
    private String tehtavanimike;

}
