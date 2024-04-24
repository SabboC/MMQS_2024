package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeudenTila;
import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhmaTapahtumaHistoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface KayttoOikeusRyhmaTapahtumaHistoriaDataRepository extends CrudRepository<KayttoOikeusRyhmaTapahtumaHistoria, Long> {
    List<KayttoOikeusRyhmaTapahtumaHistoria> findByOrganisaatioHenkiloHenkiloOidHenkiloAndTila(String oidHenkilo, KayttoOikeudenTila kayttoOikeudenTila);
}
