package fi.vm.sade.kayttooikeus.model;

import fi.vm.sade.kayttooikeus.dto.types.AnomusTyyppi;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Käyttöoikeusanomus.
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anomus", schema = "public")
public class Anomus extends IdentifiableAndVersionedEntity {

    /**
     * Anoja.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "henkilo_id")
    private Henkilo henkilo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kasittelija_henkilo_id")
    private Henkilo kasittelija;

    @Column(name = "organisaatiooid")
    private String organisaatioOid;

    @Column(name = "tehtavanimike")
    private String tehtavanimike;

    @Column(name = "anomustyyppi")
    @Enumerated(EnumType.STRING)
    private AnomusTyyppi anomusTyyppi;

    @Column(name = "anomuksentila")
    @Enumerated(EnumType.STRING)
    private AnomuksenTila anomuksenTila;

    @Column(name = "anottupvm")
    private LocalDateTime anottuPvm;

    @Column(name = "anomustilatapahtumapvm")
    private LocalDateTime anomusTilaTapahtumaPvm;

    @Column(name = "perustelut")
    private String perustelut;

    @Column(name = "sahkopostiosoite", nullable = false)
    private String sahkopostiosoite;

    @Column(name = "puhelinnumero")
    private String puhelinnumero;

    @Column(name = "matkapuhelinnumero")
    private String matkapuhelinnumero;

    @Column(name = "hylkaamisperuste")
    private String hylkaamisperuste;

    @OneToMany(mappedBy = "anomus", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private Set<HaettuKayttoOikeusRyhma> haettuKayttoOikeusRyhmas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "anomus_myonnettykayttooikeusryhmas", joinColumns = @JoinColumn(name = "anomus_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "myonnettykayttooikeusryhma_id", referencedColumnName = "id"))
    private Set<MyonnettyKayttoOikeusRyhmaTapahtuma> myonnettyKayttooikeusRyhmas = new HashSet<>();

    public void addHaettuKayttoOikeusRyhma(HaettuKayttoOikeusRyhma haettuKayttoOikeusRyhma) {
        this.haettuKayttoOikeusRyhmas.add(haettuKayttoOikeusRyhma);
        haettuKayttoOikeusRyhma.setAnomus(this);
    }

    public void addMyonnettyKayttooikeusRyhma(MyonnettyKayttoOikeusRyhmaTapahtuma myonnettyKayttoOikeusRyhmaTapahtuma) {
        if (this.myonnettyKayttooikeusRyhmas == null) {
            this.myonnettyKayttooikeusRyhmas = new HashSet<>();
        }
        this.myonnettyKayttooikeusRyhmas.add(myonnettyKayttoOikeusRyhmaTapahtuma);
        myonnettyKayttoOikeusRyhmaTapahtuma.addAnomus(this);
    }
}
