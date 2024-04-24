package fi.vm.sade.kayttooikeus.dto;


import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KayttoOikeusDto {
    private Long id;
    private String rooli;
    private TextGroupDto textGroup;
        private Set<KayttoOikeusRyhmaDto> kayttoOikeusRyhmas = new HashSet<>();
    private PalveluDto palvelu;
}
