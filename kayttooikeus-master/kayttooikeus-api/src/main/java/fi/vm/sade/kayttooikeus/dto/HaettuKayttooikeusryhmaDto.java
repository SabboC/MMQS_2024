package fi.vm.sade.kayttooikeus.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HaettuKayttooikeusryhmaDto {

    private Long id;

    private AnomusDto anomus;

    private KayttoOikeusRyhmaDto kayttoOikeusRyhma;

    private LocalDateTime kasittelyPvm;

    private KayttoOikeudenTila tyyppi;
}
