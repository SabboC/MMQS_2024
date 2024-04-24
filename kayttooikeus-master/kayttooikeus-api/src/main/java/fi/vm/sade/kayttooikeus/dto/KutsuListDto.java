package fi.vm.sade.kayttooikeus.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class KutsuListDto implements Serializable {
    private Long id;
    private KutsunTila tila;
    private String etunimi;
    private String sukunimi;
    private String sahkoposti;
    private LocalDateTime aikaleima;
    private List<KutsuOrganisaatioListDto> organisaatiot = new ArrayList<>();
}
