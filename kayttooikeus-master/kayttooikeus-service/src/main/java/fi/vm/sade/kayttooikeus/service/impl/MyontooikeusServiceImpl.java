package fi.vm.sade.kayttooikeus.service.impl;

import fi.vm.sade.kayttooikeus.repositories.KayttoOikeusRyhmaMyontoViiteRepository;
import fi.vm.sade.kayttooikeus.repositories.criteria.MyontooikeusCriteria;
import fi.vm.sade.kayttooikeus.service.MyontooikeusService;
import fi.vm.sade.kayttooikeus.service.external.OrganisaatioClient;
import fi.vm.sade.kayttooikeus.util.OrganisaatioMyontoPredicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static fi.vm.sade.kayttooikeus.util.FunctionalUtils.appending;
import static java.util.stream.Collectors.toMap;

@Service
@Transactional
@RequiredArgsConstructor
public class MyontooikeusServiceImpl implements MyontooikeusService {

    private final KayttoOikeusRyhmaMyontoViiteRepository kayttoOikeusRyhmaMyontoViiteRepository;
    private final OrganisaatioClient organisaatioClient;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Set<Long>> getMyontooikeudet(String myontajaOid,
                                                    MyontooikeusCriteria myontooikeusCriteria,
                                                    OrganisaatioMyontoPredicate organisaatioMyontoPredicate) {
        return kayttoOikeusRyhmaMyontoViiteRepository.getSlaveIdsByMasterHenkiloOid(myontajaOid, myontooikeusCriteria)
                .entrySet().stream()
                .flatMap(entry -> Stream.concat(Stream.of(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue())),
                        organisaatioClient.listWithChildOids(entry.getKey(), organisaatioMyontoPredicate)
                                .stream()
                                .map(aliorganisaatioOid -> new AbstractMap.SimpleEntry<>(aliorganisaatioOid, entry.getValue()))))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, appending(), HashMap::new));
    }

}
