package fi.vm.sade.kayttooikeus.service.validators;

import fi.vm.sade.kayttooikeus.config.KutsujaValidatorConfiguration;
import fi.vm.sade.kayttooikeus.service.external.OppijanumerorekisteriClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("yksilointiDisabled")
@ContextConfiguration(classes = {KutsujaValidatorConfiguration.class})
public class KutsujaValidatorDisabledTest {

    @MockBean
    private OppijanumerorekisteriClient oppijanumerorekisteriClient;

    @Autowired
    private KutsujaValidator kutsujaValidator;

    @Test
    public void hyvaksyyHetunJaYksiloinninTarkistamatta() {
        verify(oppijanumerorekisteriClient, never()).getHenkiloByOid(anyString());
        assertTrue(kutsujaValidator.isKutsujaYksiloity("1.23.456.7890"));
    }
}
