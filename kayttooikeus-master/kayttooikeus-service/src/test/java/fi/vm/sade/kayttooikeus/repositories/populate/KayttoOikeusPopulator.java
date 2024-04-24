package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.model.KayttoOikeus;
import fi.vm.sade.kayttooikeus.model.Palvelu;
import fi.vm.sade.kayttooikeus.model.TextGroup;

import javax.persistence.EntityManager;

import static fi.vm.sade.kayttooikeus.repositories.populate.PalveluPopulator.palvelu;

public class KayttoOikeusPopulator implements Populator<KayttoOikeus> {
    private final Populator<Palvelu> palvelu;
    private final String rooli;
    private Populator<TextGroup> kuvaus = Populator.constant(new TextGroup());

    public KayttoOikeusPopulator(Populator<Palvelu> palvelu, String rooli) {
        this.palvelu = palvelu;
        this.rooli = rooli;
    }

    public KayttoOikeusPopulator(String palveluName, String rooli) {
        this.palvelu = palvelu(palveluName);
        this.rooli = rooli;
    }
    
    public static KayttoOikeusPopulator oikeus(Populator<Palvelu> palvelu, String rooli) {
        return new KayttoOikeusPopulator(palvelu, rooli);
    }

    public static KayttoOikeusPopulator oikeus(String palveluName, String rooli) {
        return new KayttoOikeusPopulator(palveluName, rooli);
    }
    
    public KayttoOikeusPopulator kuvaus(Populator<TextGroup> textGroup) {
        this.kuvaus = textGroup;
        return this;
    }

    @Override
    public KayttoOikeus apply(EntityManager entityManager) {
        Palvelu palvelu = this.palvelu.apply(entityManager);
        return Populator.<KayttoOikeus>firstOptional(entityManager.createQuery("select ko from KayttoOikeus ko " +
                "where ko.palvelu.name = :palveluName and ko.rooli = :rooli")
                    .setParameter("palveluName", palvelu.getName())
                    .setParameter("rooli", rooli)).orElseGet(() -> {
            KayttoOikeus oikeus = new KayttoOikeus(rooli, palvelu);
            oikeus.setRooli(rooli);
            oikeus.setPalvelu(palvelu);
            palvelu.addKayttooikeus(oikeus);
            oikeus.setTextGroup(kuvaus.apply(entityManager));
            entityManager.persist(oikeus);
            return oikeus;
        });
    }
}
