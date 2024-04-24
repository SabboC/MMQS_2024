package fi.vm.sade.kayttooikeus.config.scheduling;

import fi.vm.sade.kayttooikeus.model.Identifiable;
import fi.vm.sade.kayttooikeus.service.ExpiringEntitiesService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Slf4j
public abstract class AbstractExpiringEntitiesTask<T extends Identifiable> {
    @Transactional
    public void expire(String name, ExpiringEntitiesService<T> service, Period threshold) {
        Collection<T> entities = service.findExpired(threshold);
        log.info("Discarding {} expired {}s", entities.size(), name);
        Map<Boolean, Integer> result = entities.stream()
                .map(entity -> {
                    try {
                        service.discard(entity);
                        sendNotification(entity);
                    } catch (Exception e) {
                        log.warn("Error while discarding {} id: {}", name, entity.getId(), e);
                        return false;
                    }
                    return true;
                })
                .collect(groupingBy(Boolean::booleanValue, summingInt(success -> 1)));
        if (!result.isEmpty()) {
            log.info("Sent discarded {}s notifications. {} success, {} failures",
                    name,
                    result.getOrDefault(true, 0),
                    result.getOrDefault(false, 0));
        }
        if (result.containsKey(false)) {
            log.error("There were errors while discarding {}s, please check the logs", name);
        }
    }

    public abstract void sendNotification(T invitation);
}
