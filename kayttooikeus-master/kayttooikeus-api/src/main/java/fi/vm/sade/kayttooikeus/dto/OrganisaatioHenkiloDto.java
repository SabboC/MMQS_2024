package fi.vm.sade.kayttooikeus.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganisaatioHenkiloDto implements Serializable {
    private long id;
    private String organisaatioOid;
    private OrganisaatioHenkiloTyyppi organisaatioHenkiloTyyppi;
    private String tehtavanimike;
    private boolean passivoitu;
    private LocalDate voimassaAlkuPvm;
    private LocalDate voimassaLoppuPvm;
}
