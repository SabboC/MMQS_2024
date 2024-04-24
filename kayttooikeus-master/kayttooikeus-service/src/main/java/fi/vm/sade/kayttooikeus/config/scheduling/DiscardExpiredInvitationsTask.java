package fi.vm.sade.kayttooikeus.config.scheduling;

import fi.vm.sade.kayttooikeus.model.Kutsu;
import fi.vm.sade.kayttooikeus.service.EmailService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class DiscardExpiredInvitationsTask extends AbstractExpiringEntitiesTask<Kutsu> {
    private final EmailService emailService;

    @Override
    public void sendNotification(Kutsu invitation) {
        emailService.sendDiscardNotification(invitation);
    }
}
