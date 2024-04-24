package fi.vm.sade.kayttooikeus.config.scheduling;

import fi.vm.sade.kayttooikeus.model.Anomus;
import fi.vm.sade.kayttooikeus.service.EmailService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscardExpiredApplicationsTask extends AbstractExpiringEntitiesTask<Anomus> {
    private final EmailService emailService;

    @Override
    public void sendNotification(Anomus application) {
        emailService.sendDiscardNotification(application);
    }
}


