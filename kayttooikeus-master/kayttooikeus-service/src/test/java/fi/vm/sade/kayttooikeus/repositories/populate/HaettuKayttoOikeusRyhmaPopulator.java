package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeudenTila;
import fi.vm.sade.kayttooikeus.model.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class HaettuKayttoOikeusRyhmaPopulator implements Populator<HaettuKayttoOikeusRyhma> {
    private KayttoOikeudenTila tyyppi;
    private Populator<KayttoOikeusRyhma> kayttoOikeusRyhma;

    private HaettuKayttoOikeusRyhmaPopulator(KayttoOikeudenTila tyyppi) {
        this.tyyppi = tyyppi;
    }
    
    public static HaettuKayttoOikeusRyhmaPopulator haettuKayttooikeusryhma(KayttoOikeudenTila tyyppi) {
        return new HaettuKayttoOikeusRyhmaPopulator(tyyppi);
    }
    
    public HaettuKayttoOikeusRyhmaPopulator withRyhma(Populator<KayttoOikeusRyhma> oikeus) {
        this.kayttoOikeusRyhma = oikeus;
        return this;
    }
    
    @Override
    public HaettuKayttoOikeusRyhma apply(EntityManager entityManager) {
        HaettuKayttoOikeusRyhma ryhma = new HaettuKayttoOikeusRyhma();
        ryhma.setTyyppi(this.tyyppi);
        ryhma.setKayttoOikeusRyhma(this.kayttoOikeusRyhma.apply(entityManager));
        entityManager.persist(ryhma);

        return ryhma;
    }
}
