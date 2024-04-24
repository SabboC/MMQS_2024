package fi.vm.sade.kayttooikeus.util;

import fi.vm.sade.kayttooikeus.dto.enumeration.LogInRedirectType;
import fi.vm.sade.kayttooikeus.model.Henkilo;

import java.time.LocalDateTime;

public final class HenkiloUtils {

    private HenkiloUtils() {
    }

    public static LogInRedirectType getLoginRedirectType(Henkilo henkilo, boolean isVahvastiTunnistettu, LocalDateTime now) {
        if (henkilo.isPalvelu()) {
            return null;
        }

        if(Boolean.FALSE.equals(isVahvastiTunnistettu)) {
            return LogInRedirectType.STRONG_IDENTIFICATION;
        }

        LocalDateTime sixMonthsAgo = now.minusMonths(6);
        if(henkilo.getSahkopostivarmennusAikaleima() == null || henkilo.getSahkopostivarmennusAikaleima().isBefore(sixMonthsAgo)) {
            return LogInRedirectType.EMAIL_VERIFICATION;
        }

        LocalDateTime yearAgo = now.minusMonths(12);
        if (henkilo.getKayttajatiedot() != null
                && henkilo.getKayttajatiedot().getPassword() != null
                && henkilo.getKayttajatiedot().getPasswordChange() != null 
                && henkilo.getKayttajatiedot().getPasswordChange().isBefore(yearAgo)) {
            return LogInRedirectType.PASSWORD_CHANGE;
        }

        return null;
    }

}
