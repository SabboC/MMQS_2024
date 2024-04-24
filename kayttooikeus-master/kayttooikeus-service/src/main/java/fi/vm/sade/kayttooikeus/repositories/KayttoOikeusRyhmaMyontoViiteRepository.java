package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhmaMyontoViite;
import fi.vm.sade.kayttooikeus.repositories.criteria.MyontooikeusCriteria;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface KayttoOikeusRyhmaMyontoViiteRepository extends BaseRepository<KayttoOikeusRyhmaMyontoViite> {

    Set<Long> getMasterIdsBySlaveIds(Set<Long> slaveIds);

    List<Long> getSlaveIdsByMasterIds(List<Long> masterIds);

    /**
     * Palauttaa käyttöoikeusryhmien id:t organisaatioittain joihin annetulla
     * henkilöllä on myöntöoikeudet (henkilön myönnettyjen käyttöoikeuksien ja
     * käyttöoikeusryhmien myöntöviitteiden perusteella).
     *
     * @param henkiloOid henkilön oid
     * @param criteria hakukriteerit
     * @return käyttöoikeusryhmä id:t organisaatioittain
     */
    Map<String, Set<Long>> getSlaveIdsByMasterHenkiloOid(String henkiloOid, MyontooikeusCriteria criteria);

    boolean isCyclicMyontoViite(Long id, List<Long> slaveIds);

    List<KayttoOikeusRyhmaMyontoViite> getMyontoViites(Long id);
}
