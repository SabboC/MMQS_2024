package fi.vm.sade.kayttooikeus.service;

import fi.vm.sade.kayttooikeus.dto.OrganisaatioCriteriaDto;
import fi.vm.sade.kayttooikeus.dto.OrganisaatioDto;
import fi.vm.sade.kayttooikeus.dto.OrganisaatioWithChildrenDto;

import java.util.Collection;
import java.util.Map;

public interface OrganisaatioService {

    void updateOrganisaatioCache();

    Long getClientCacheState();

    Collection<OrganisaatioDto> listBy(OrganisaatioCriteriaDto criteria);

    OrganisaatioWithChildrenDto getRootWithChildrenBy(OrganisaatioCriteriaDto criteria);

    OrganisaatioWithChildrenDto getByOid(String oid);

    Map<String, Map<String, String>> getOrganisationNames();
}
