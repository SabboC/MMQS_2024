package fi.vm.sade.kayttooikeus.model;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeudenTila;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "haettu_kayttooikeusryhma")
public class HaettuKayttoOikeusRyhma extends IdentifiableAndVersionedEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anomus_id")
    private Anomus anomus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kayttooikeusryhma_id")
    private KayttoOikeusRyhma kayttoOikeusRyhma;

    @Column(name = "kasittelypvm")
    private LocalDateTime kasittelyPvm;

    @Enumerated(EnumType.STRING)
    @Column(name = "tyyppi")
    private KayttoOikeudenTila tyyppi;
}
