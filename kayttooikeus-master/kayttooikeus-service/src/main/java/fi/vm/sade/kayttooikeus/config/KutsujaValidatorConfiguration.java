package fi.vm.sade.kayttooikeus.config;

import fi.vm.sade.kayttooikeus.service.external.OppijanumerorekisteriClient;
import fi.vm.sade.kayttooikeus.service.validators.KutsujaValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class KutsujaValidatorConfiguration {

    @Bean
    @Profile("!(yksilointiDisabled | dev)")
    public KutsujaValidator kutsujaValidatorEnabled(OppijanumerorekisteriClient oppijanumerorekisteriClient) {
        return new KutsujaValidator.Enabled(oppijanumerorekisteriClient);
    }

    @Bean
    @Profile("yksilointiDisabled | dev")
    public KutsujaValidator kutsujaValidatorDisabled() {
        return new KutsujaValidator.Disabled();
    }

}
