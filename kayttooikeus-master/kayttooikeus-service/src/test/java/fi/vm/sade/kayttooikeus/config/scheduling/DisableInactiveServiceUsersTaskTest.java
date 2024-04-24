package fi.vm.sade.kayttooikeus.config.scheduling;

import fi.vm.sade.kayttooikeus.config.properties.CommonProperties;
import fi.vm.sade.kayttooikeus.service.it.AbstractServiceIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Map;

import static fi.vm.sade.kayttooikeus.repositories.populate.HenkiloPopulator.palvelukayttaja;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DisableInactiveServiceUsersTaskTest extends AbstractServiceIntegrationTest {

    @Autowired
    private DisableInactiveServiceUsersTask task;

    @Autowired
    private CommonProperties commonProperties;

    @Test
    public void testSuccess() {
        populate(palvelukayttaja(commonProperties.getAdminOid()));
        populate(palvelukayttaja("foo").withUsername("bar").withLoginCounter());

        Map<Boolean, Integer> result = task.passivateUnusedServiceUsers(LocalDateTime.now().plusDays(1));

        assertThat(result).hasSize(1).containsKey(true);
    }

    @Test
    public void testFailure() {
        populate(palvelukayttaja("foo").withUsername("bar").withLoginCounter());

        Map<Boolean, Integer> result = task.passivateUnusedServiceUsers(LocalDateTime.now().plusDays(1));

        assertThat(result).hasSize(1).containsKey(false);
    }
}
