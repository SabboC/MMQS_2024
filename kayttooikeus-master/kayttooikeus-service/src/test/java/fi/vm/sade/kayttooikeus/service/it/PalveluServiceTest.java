package fi.vm.sade.kayttooikeus.service.it;


import fi.vm.sade.kayttooikeus.dto.PalveluDto;
import fi.vm.sade.kayttooikeus.model.Palvelu;
import fi.vm.sade.kayttooikeus.service.PalveluService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static fi.vm.sade.kayttooikeus.repositories.populate.PalveluPopulator.palvelu;
import static fi.vm.sade.kayttooikeus.repositories.populate.TextGroupPopulator.text;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class PalveluServiceTest extends AbstractServiceIntegrationTest {
    @Autowired
    private PalveluService palveluService;

    @Test
    public void listPalvelusTest() {
        Palvelu palvelu1 = populate(palvelu("HENKILOPALVELU").kuvaus(text("FI", "Henkilöpalvelu")
                                .put("EN", "Person service"))),
                palvelu2 = populate(palvelu("KOODISTO"));
        List<PalveluDto> palvelus = palveluService.listPalvelus();
        assertEquals(2, palvelus.size());
        assertEquals(palvelu1.getId(), palvelus.get(0).getId());
        assertEquals("Henkilöpalvelu", palvelus.get(0).getDescription().get("fi"));
        assertEquals("KOODISTO", palvelus.get(1).getName());
    }
}
