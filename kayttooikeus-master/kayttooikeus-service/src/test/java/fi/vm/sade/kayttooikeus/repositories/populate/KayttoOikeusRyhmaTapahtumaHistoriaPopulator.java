package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeudenTila;
import fi.vm.sade.kayttooikeus.model.Henkilo;
import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhma;
import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhmaTapahtumaHistoria;
import fi.vm.sade.kayttooikeus.model.OrganisaatioHenkilo;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

public class KayttoOikeusRyhmaTapahtumaHistoriaPopulator implements Populator<KayttoOikeusRyhmaTapahtumaHistoria> {
    private LocalDateTime aikaleima = LocalDateTime.now();
    private Populator<Henkilo> henkilo = Populator.constant(new Henkilo());
    private Populator<KayttoOikeusRyhma> kayttoOikeusRyhma = Populator.constant(new KayttoOikeusRyhma());
    private Populator<OrganisaatioHenkilo> organisaatioHenkilo = Populator.constant(new OrganisaatioHenkilo());
    private KayttoOikeudenTila tila = KayttoOikeudenTila.MYONNETTY;
    private String syy;

    public KayttoOikeusRyhmaTapahtumaHistoriaPopulator() {}

    public static KayttoOikeusRyhmaTapahtumaHistoriaPopulator historia() {
        return new KayttoOikeusRyhmaTapahtumaHistoriaPopulator();
    }

    public KayttoOikeusRyhmaTapahtumaHistoriaPopulator tila(KayttoOikeudenTila tila){
        this.tila = tila;
        return this;
    }

    public KayttoOikeusRyhmaTapahtumaHistoriaPopulator syy(String syy){
        this.syy = syy;
        return this;
    }

    public KayttoOikeusRyhmaTapahtumaHistoriaPopulator kayttoOikeusRyhma(Populator<KayttoOikeusRyhma> kayttoOikeusRyhma){
        this.kayttoOikeusRyhma = kayttoOikeusRyhma;
        return this;
    }

    public KayttoOikeusRyhmaTapahtumaHistoriaPopulator kasittelija(Populator<Henkilo> henkilo){
        this.henkilo = henkilo;
        return this;
    }

    public KayttoOikeusRyhmaTapahtumaHistoriaPopulator organisaatioHenkilo(Populator<OrganisaatioHenkilo> organisaatioHenkilo){
        this.organisaatioHenkilo = organisaatioHenkilo;
        return this;
    }

    @Override
    public KayttoOikeusRyhmaTapahtumaHistoria apply(EntityManager entityManager) {
        KayttoOikeusRyhmaTapahtumaHistoria historia = new KayttoOikeusRyhmaTapahtumaHistoria();
        historia.setAikaleima(this.aikaleima);
        historia.setKasittelija(this.henkilo.apply(entityManager));
        historia.setKayttoOikeusRyhma(this.kayttoOikeusRyhma.apply(entityManager));
        historia.setOrganisaatioHenkilo(this.organisaatioHenkilo.apply(entityManager));
        historia.setTila(this.tila);
        historia.setSyy(this.syy);
        entityManager.persist(historia);
        return historia;
    }
}
