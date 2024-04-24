package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.dto.OrganisaatioViiteDto;
import fi.vm.sade.kayttooikeus.model.OrganisaatioViite;

import java.util.List;
import java.util.Set;

public interface OrganisaatioViiteRepository extends BaseRepository<OrganisaatioViite>{
    List<OrganisaatioViiteDto> findByKayttoOikeusRyhmaIds(Set<Long> ids);
}
