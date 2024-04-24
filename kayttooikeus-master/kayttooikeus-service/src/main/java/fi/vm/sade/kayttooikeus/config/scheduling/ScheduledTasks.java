package fi.vm.sade.kayttooikeus.config.scheduling;

import fi.vm.sade.kayttooikeus.service.OrganisaatioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Ajastusten konfigurointi.
 *
 * @see SchedulingConfiguration ajastuksen aktivointi
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "kayttooikeus.scheduling.enabled", matchIfMissing = true)
public class ScheduledTasks {
    private final OrganisaatioService organisaatioService;

    @Scheduled(fixedDelayString = "${kayttooikeus.scheduling.configuration.organisaatiocache:600000}",
            initialDelayString = "${kayttooikeus.scheduling.configuration.organisaatiocache:600000}")
    public void updateOrganisaatioCache() {
        this.organisaatioService.updateOrganisaatioCache();
    }
}
