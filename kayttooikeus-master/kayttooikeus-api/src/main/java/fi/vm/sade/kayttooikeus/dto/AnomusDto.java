package fi.vm.sade.kayttooikeus.dto;

import fi.vm.sade.kayttooikeus.dto.types.AnomusTyyppi;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnomusDto {

    private String organisaatioOid;

    private LocalDateTime anottuPvm;

    private Date anomusTilaTapahtumaPvm;

    private AnomusTyyppi anomusTyyppi;

    private HenkiloNimiDto henkilo;

    private String perustelut;

}
