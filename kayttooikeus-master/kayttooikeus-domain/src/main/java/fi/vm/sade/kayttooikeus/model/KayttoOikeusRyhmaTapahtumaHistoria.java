package fi.vm.sade.kayttooikeus.model;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeudenTila;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kayttooikeusryhma_tapahtuma_historia")
public class KayttoOikeusRyhmaTapahtumaHistoria extends IdentifiableAndVersionedEntity {

    private static final long serialVersionUID = 6182609506916140406L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kayttooikeusryhma_id", nullable = false)
    private KayttoOikeusRyhma kayttoOikeusRyhma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisaatiohenkilo_id", nullable = false)
    private OrganisaatioHenkilo organisaatioHenkilo;

    @Column(name = "syy")
    private String syy;

    @Enumerated(EnumType.STRING)
    @Column(name = "tila", nullable = false)
    private KayttoOikeudenTila tila;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kasittelija_henkilo_id")
    private Henkilo kasittelija;

    @Column(name = "aikaleima", nullable = false)
    private LocalDateTime aikaleima;
}
