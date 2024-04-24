package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhmaMyontoViite;

import javax.persistence.EntityManager;
public class KayttoOikeusRyhmaMyontoViitePopulator implements Populator<KayttoOikeusRyhmaMyontoViite> {
    private final Long masterId;
    private final Long slaveId;

    public KayttoOikeusRyhmaMyontoViitePopulator(Long masterId, Long slaveId) {
        this.masterId = masterId;
        this.slaveId = slaveId;
    }

    public static KayttoOikeusRyhmaMyontoViitePopulator kayttoOikeusRyhmaMyontoViite(Long masterId, Long slaveId) {
        return new KayttoOikeusRyhmaMyontoViitePopulator(masterId, slaveId);
    }

    @Override
    public KayttoOikeusRyhmaMyontoViite apply(EntityManager entityManager) {
        KayttoOikeusRyhmaMyontoViite kayttoOikeusRyhmaMyontoViite = new KayttoOikeusRyhmaMyontoViite();

        kayttoOikeusRyhmaMyontoViite.setMasterId(this.masterId);
        kayttoOikeusRyhmaMyontoViite.setSlaveId(this.slaveId);

        entityManager.persist(kayttoOikeusRyhmaMyontoViite);
        return kayttoOikeusRyhmaMyontoViite;
    }
}
