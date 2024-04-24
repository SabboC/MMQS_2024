package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.dto.PalveluTyyppi;
import fi.vm.sade.kayttooikeus.model.KayttoOikeus;
import fi.vm.sade.kayttooikeus.model.Palvelu;
import fi.vm.sade.kayttooikeus.model.TextGroup;

import javax.persistence.EntityManager;

import static fi.vm.sade.kayttooikeus.repositories.populate.Populator.first;

public class PalveluPopulator implements Populator<Palvelu> {
    private final String name;
    private Populator<TextGroup> kuvaus = Populator.constant(new TextGroup());
    private Populator<KayttoOikeus> kayttoOikeus = Populator.constant(new KayttoOikeus());

    public PalveluPopulator(String name) {
        this.name = name;
    }
    
    public static PalveluPopulator palvelu(String name) {
        return new PalveluPopulator(name);
    }
    
    public PalveluPopulator kuvaus(Populator<TextGroup> kuvaus) {
        this.kuvaus = kuvaus;
        return this;
    }

    public PalveluPopulator kayttoOikeus(Populator<KayttoOikeus> kayttoOikeus){
        this.kayttoOikeus = kayttoOikeus;
        return this;
    }

    @Override
    public Palvelu apply(EntityManager entityManager) {
        Palvelu existing = first(entityManager.createQuery("select p from Palvelu p where p.name = :name")
                .setParameter("name", name));
        if (existing != null) {
            return existing;
        }
        
        Palvelu palvelu = new Palvelu();
        palvelu.setName(name);
        palvelu.setDescription(this.kuvaus.apply(entityManager));
        palvelu.setPalveluTyyppi(PalveluTyyppi.YKSITTAINEN);
        entityManager.persist(palvelu);
        
        return palvelu;
    }
}
