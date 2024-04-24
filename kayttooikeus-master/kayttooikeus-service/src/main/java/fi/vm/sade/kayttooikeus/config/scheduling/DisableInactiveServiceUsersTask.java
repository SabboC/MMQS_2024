package fi.vm.sade.kayttooikeus.config.scheduling;

import fi.vm.sade.kayttooikeus.config.properties.CommonProperties;
import fi.vm.sade.kayttooikeus.config.properties.KayttooikeusProperties;
import fi.vm.sade.kayttooikeus.service.HenkiloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Slf4j
@Component
@RequiredArgsConstructor
public class DisableInactiveServiceUsersTask {
    private final HenkiloService henkiloService;
    private final KayttooikeusProperties kayttooikeusProperties;
    private final CommonProperties commonProperties;

    public void execute() {
        LocalDateTime passiveSince = LocalDateTime.now().minus(Period.parse(kayttooikeusProperties.getScheduling().getConfiguration().getDisableInactiveServiceUsersThreshold()));
        summarize(passivateUnusedServiceUsers(passiveSince));
    }

    protected Map<Boolean, Integer> passivateUnusedServiceUsers(LocalDateTime passiveSince) {
        return henkiloService.findPassiveServiceUsers(passiveSince).stream()
                .map(henkilo -> {
                    try {
                        log.info("Inactive service found. Passivating {}", henkilo.getOidHenkilo());
                        henkiloService.poistaOikeudet(henkilo, commonProperties.getAdminOid(), "Inactive service user");
                        return true;
                    } catch (Exception e) {
                        log.error("Error during service user passivation", e);
                        return false;
                    }
                }).collect(groupingBy(Boolean::booleanValue, summingInt(success -> 1)));
    }

    private void summarize(Map<Boolean, Integer> result) {
        if (!result.isEmpty()) {
            log.info("Passivated inactive service users. {} success, {} failures",
                    result.getOrDefault(true, 0),
                    result.getOrDefault(false, 0));
        }
    }
}
