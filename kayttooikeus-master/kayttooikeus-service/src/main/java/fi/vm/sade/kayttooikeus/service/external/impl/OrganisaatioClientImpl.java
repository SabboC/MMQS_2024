package fi.vm.sade.kayttooikeus.service.external.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.sade.javautils.http.OphHttpClient;
import fi.vm.sade.javautils.http.OphHttpRequest;
import fi.vm.sade.kayttooikeus.config.OrikaBeanMapper;
import fi.vm.sade.kayttooikeus.config.properties.CommonProperties;
import fi.vm.sade.kayttooikeus.config.properties.UrlConfiguration;
import fi.vm.sade.kayttooikeus.dto.enumeration.OrganisaatioStatus;
import fi.vm.sade.kayttooikeus.service.external.ExternalServiceException;
import fi.vm.sade.kayttooikeus.service.external.OrganisaatioClient;
import fi.vm.sade.kayttooikeus.service.external.OrganisaatioHakutulos;
import fi.vm.sade.kayttooikeus.service.external.OrganisaatioPerustieto;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.carrotsearch.sizeof.RamUsageEstimator.humanReadableUnits;
import static com.carrotsearch.sizeof.RamUsageEstimator.sizeOf;
import static fi.vm.sade.kayttooikeus.config.HttpClientConfiguration.HTTP_CLIENT_ORGANISAATIO;
import static fi.vm.sade.kayttooikeus.service.external.ExternalServiceException.mapper;
import static fi.vm.sade.kayttooikeus.service.external.impl.HttpClientUtil.noContentOrNotFoundException;
import static fi.vm.sade.kayttooikeus.util.FunctionalUtils.io;
import static fi.vm.sade.kayttooikeus.util.FunctionalUtils.retrying;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
public class OrganisaatioClientImpl implements OrganisaatioClient {

    private final OphHttpClient httpClient;
    private final UrlConfiguration urlConfiguration;
    private final String rootOrganizationOid;
    private final ObjectMapper objectMapper;
    private final OrikaBeanMapper orikaBeanMapper;

    private OrganisaatioCache cache;
    
    
    public OrganisaatioClientImpl(@Qualifier(HTTP_CLIENT_ORGANISAATIO) OphHttpClient httpClient,
                                  UrlConfiguration urlConfiguration,
                                  ObjectMapper objectMapper,
                                  CommonProperties commonProperties,
                                  OrikaBeanMapper orikaBeanMapper) {
        this.httpClient = httpClient;
        this.urlConfiguration = urlConfiguration;
        this.objectMapper = objectMapper;
        this.rootOrganizationOid = commonProperties.getRootOrganizationOid();
        this.orikaBeanMapper = orikaBeanMapper;
    }

    @Override
    public synchronized long refreshCache() {
        String haeHierarchyUrl = this.urlConfiguration.url(
                "organisaatio-service.organisaatio.v4.jalkelaiset", rootOrganizationOid);
        // Add organisations to cache (active, incoming and passive)
        List<OrganisaatioPerustieto> organisaatiosWithoutRootOrg =
                retrying(io(() -> get(haeHierarchyUrl, OrganisaatioHakutulos.class)), 2)
                        .get().orFail(mapper(haeHierarchyUrl)).getOrganisaatiot();
        // Add ryhmas to cache
        Map<String, String> queryParamsRyhmat = new HashMap<String, String>() {{
            put("aktiiviset", "true");
            put("lakkautetut", "true");
        }};
        String haeRyhmasUrl = this.urlConfiguration.url("organisaatio-service.organisaatio.ryhmat", queryParamsRyhmat);
        organisaatiosWithoutRootOrg.addAll(Arrays.stream(retrying(io(() ->
                get(haeRyhmasUrl, OrganisaatioPerustieto[].class)), 2)
                .get().<ExternalServiceException>orFail(mapper(haeRyhmasUrl)))
                // Make ryhma parentoidpath format same as on normal organisations.
                .map(ryhma -> {
                    ryhma.setParentOidPath(ryhma.getOid() + "/"
                            + ryhma.getParentOidPath().replaceAll("^\\||\\|$", "").replace("|", "/"));
                    return ryhma;
                }).collect(Collectors.toSet()));
        this.cache = new OrganisaatioCache(this.fetchPerustiedot(this.rootOrganizationOid), organisaatiosWithoutRootOrg);
        log.info("Organisation client cache refreshed. Cache size " + humanReadableUnits(sizeOf(this.cache)));
        return cache.getCacheCount();
    }

    private OrganisaatioPerustieto fetchPerustiedot(String oid) {
        String url = urlConfiguration.url("organisaatio-service.organisaatio.perustiedot", oid);
        return this.orikaBeanMapper.map(retrying(io(() -> get(url, OrganisaatioRDTO.class)), 2).get()
                .orFail(mapper(url)), OrganisaatioPerustieto.class);
    }

    private <T> T get(String url, Class<T> type) {
        return httpClient.<T>execute(OphHttpRequest.Builder.get(url).build())
                .expectedStatus(200)
                .mapWith(json -> io(() -> objectMapper.readValue(json, type)).get())
                .orElseThrow(() -> noContentOrNotFoundException(url));
    }

    @Override
    public Long getCacheOrganisationCount() {
        return this.cache.getCacheCount();
    }
    @Override
    public Optional<OrganisaatioPerustieto> getOrganisaatioPerustiedotCached(String oid) {
        return this.cache.getByOid(oid);
    }

    @Override
    public OrganisaatioPerustieto getRoot() {
        return this.cache.getRoot();
    }

    @Override
    public Stream<OrganisaatioPerustieto> stream() {
        return this.cache.getAllOrganisaatios();
    }

    @Override
    public List<OrganisaatioPerustieto> listWithParentsAndChildren(String organisaatioOid, Predicate<OrganisaatioPerustieto> filter) {
        return this.cache.flatWithParentsAndChildren(organisaatioOid)
                // the resource never returns the root
                .filter(org -> !rootOrganizationOid.equals(org.getOid()))
                .filter(filter)
                .collect(toList());
    }

    @Override
    public List<String> getParentOids(String organisaatioOid) {
        return this.cache.flatWithParentsByOid(organisaatioOid).map(OrganisaatioPerustieto::getOid).collect(toList());
    }

    @Override
    public List<String> getActiveParentOids(String organisaatioOid) {
        return this.cache.flatWithParentsByOid(organisaatioOid)
                .filter(organisaatioPerustieto -> OrganisaatioStatus.AKTIIVINEN.equals(organisaatioPerustieto.getStatus()))
                .map(OrganisaatioPerustieto::getOid)
                .collect(toList());
    }

    @Override
    public List<String> getChildOids(String organisaatioOid) {
        return this.cache.flatWithChildrenByOid(organisaatioOid)
                        .map(OrganisaatioPerustieto::getOid)
                        .collect(toList());
    }

    @Override
    public Set<String> listWithChildOids(String organisaatioOid, Predicate<OrganisaatioPerustieto> filter) {
        return this.cache.flatWithChildrenByOid(organisaatioOid)
                .filter(filter)
                .map(OrganisaatioPerustieto::getOid)
                .collect(toSet());
    }

    @Override
    public Set<String> getLakkautetutOids() {
        return this.cache.getAllOrganisaatios()
                .filter(organisaatioPerustieto -> OrganisaatioStatus.PASSIIVINEN.equals(organisaatioPerustieto.getStatus()))
                .map(OrganisaatioPerustieto::getOid)
                .distinct()
                .collect(Collectors.toSet());
    }

}
