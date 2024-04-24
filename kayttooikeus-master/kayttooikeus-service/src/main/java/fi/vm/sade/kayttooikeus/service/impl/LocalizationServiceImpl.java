package fi.vm.sade.kayttooikeus.service.impl;

import fi.vm.sade.kayttooikeus.dto.LocalizableOrganisaatio;
import fi.vm.sade.kayttooikeus.dto.TextGroupMapDto;
import fi.vm.sade.kayttooikeus.repositories.TextGroupRepository;
import fi.vm.sade.kayttooikeus.service.LocalizationService;
import fi.vm.sade.kayttooikeus.dto.Localizable;
import fi.vm.sade.kayttooikeus.dto.LocalizableDto;
import fi.vm.sade.kayttooikeus.service.external.OrganisaatioClient;
import fi.vm.sade.kayttooikeus.util.UserDetailsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalizationServiceImpl.class);

    private final TextGroupRepository textGroupRepository;

    private final OrganisaatioClient organisaatioClient;
    
    @Override
    @Transactional(readOnly = true)
    public <T extends LocalizableDto, C extends Collection<T>> C localize(C list) {
        localize(list.stream().flatMap(LocalizableDto::localizableTexts));
        return list;
    }
    
    protected void localize(Stream<Localizable> localizable) {
        Map<Long,List<Localizable>> byId = localizable
                .filter(v -> v != null && v.getId() != null).collect(groupingBy(Localizable::getId));
        if (!byId.isEmpty()) {
            textGroupRepository.findTexts(byId.keySet())
                .forEach(fetched -> byId.get(fetched.getTextGroupId())
                    .forEach(text -> text.put(fetched.getLang(), fetched.getText())));
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public <T extends LocalizableDto> T localize(T dto) {
        if (dto != null) {
            localize(dto.localizableTexts());
        }
        return dto;
    }

    @Override
    public <T extends LocalizableOrganisaatio, C extends Collection<T>> C localizeOrgs(C list) {
        list.forEach(localizableOrganisaatio -> localizableOrganisaatio.setNimi(
                new TextGroupMapDto(null, this.organisaatioClient
                        .getOrganisaatioPerustiedotCached(localizableOrganisaatio.getOrganisaatioOid())
                        .orElseGet(() -> {
                            String organisaatioOid = localizableOrganisaatio.getOrganisaatioOid();
                            LOGGER.warn("Organisaatiota {} ei löytynyt", organisaatioOid);
                            return UserDetailsUtil.createUnknownOrganisation(organisaatioOid);
                        })
                        .getNimi())));
        return list;
    }
}
