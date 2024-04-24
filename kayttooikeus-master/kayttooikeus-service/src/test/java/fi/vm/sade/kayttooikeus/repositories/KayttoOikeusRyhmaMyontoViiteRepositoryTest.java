package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhmaMyontoViite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static fi.vm.sade.kayttooikeus.repositories.populate.KayttoOikeusPopulator.oikeus;
import static fi.vm.sade.kayttooikeus.repositories.populate.KayttoOikeusRyhmaMyontoViitePopulator.kayttoOikeusRyhmaMyontoViite;
import static fi.vm.sade.kayttooikeus.repositories.populate.KayttoOikeusRyhmaPopulator.kayttoOikeusRyhma;
import static fi.vm.sade.kayttooikeus.repositories.populate.TextGroupPopulator.text;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class KayttoOikeusRyhmaMyontoViiteRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private KayttoOikeusRyhmaMyontoViiteRepository kayttoOikeusRyhmaMyontoViiteRepository;

    @Test
    public void getSlaveIdsByMasterIdsTest() throws Exception {
        Long id = populate(kayttoOikeusRyhma("RYHMA").withNimi(text("FI", "Käyttäjähallinta")
                .put("EN", "User management"))
                .withOikeus(oikeus("HENKILOHALLINTA", "CRUD"))
                .withOikeus(oikeus("KOODISTO", "READ"))).getId();

        Long id2 = populate(kayttoOikeusRyhma("RYHMA2")
                .withOikeus(oikeus("PALVELU1", "CRUD"))
                .withOikeus(oikeus("PALVELU2", "READ"))).getId();

        Long id3 = populate(kayttoOikeusRyhma("RYHMA3")
                .withOikeus(oikeus("PALVELU3", "CRUD"))
                .withOikeus(oikeus("PALVELU4", "READ"))).getId();

        populate(kayttoOikeusRyhmaMyontoViite(1000L, id));
        populate(kayttoOikeusRyhmaMyontoViite(1000L, id2));
        populate(kayttoOikeusRyhmaMyontoViite(2000L, id3));

        List<Long> ids = kayttoOikeusRyhmaMyontoViiteRepository.getSlaveIdsByMasterIds(Collections.singletonList(1000L));
        assertEquals(2, ids.size());
        assertTrue(ids.containsAll(Arrays.asList(id, id2)));
        ids = kayttoOikeusRyhmaMyontoViiteRepository.getSlaveIdsByMasterIds(Collections.singletonList(2000L));
        assertEquals(1, ids.size());
        assertTrue(ids.containsAll(Collections.singletonList(id3)));
    }

    @Test
    public void getSlaveIdsByMasterIdsEmpty() {
        List<Long> ids = this.kayttoOikeusRyhmaMyontoViiteRepository.getSlaveIdsByMasterIds(Collections.singletonList(1000L));
        assertThat(ids).isNotNull().isEmpty();
    }

    @Test
    public void isCyclicMyontoViiteTest() throws Exception {
        Long id = populate(kayttoOikeusRyhma("RYHMA").withNimi(text("FI", "Käyttäjähallinta")
                .put("EN", "User management"))
                .withOikeus(oikeus("HENKILOHALLINTA", "CRUD"))
                .withOikeus(oikeus("KOODISTO", "READ"))).getId();

        Long id2 = populate(kayttoOikeusRyhma("RYHMA2")
                .withOikeus(oikeus("PALVELU1", "CRUD"))
                .withOikeus(oikeus("PALVELU2", "READ"))).getId();

        populate(kayttoOikeusRyhmaMyontoViite(1000L, id));
        populate(kayttoOikeusRyhmaMyontoViite(1000L, id2));

        boolean isCyclic = kayttoOikeusRyhmaMyontoViiteRepository.isCyclicMyontoViite(id, Collections.singletonList(1000L));
        assertTrue(isCyclic);
        isCyclic = kayttoOikeusRyhmaMyontoViiteRepository.isCyclicMyontoViite(id, Arrays.asList(1000L, 2000L));
        assertTrue(isCyclic);
        isCyclic = kayttoOikeusRyhmaMyontoViiteRepository.isCyclicMyontoViite(id, Arrays.asList(3000L, 7000L));
        assertFalse(isCyclic);
        isCyclic = kayttoOikeusRyhmaMyontoViiteRepository.isCyclicMyontoViite(5555L, Arrays.asList(3000L, 1000L));
        assertFalse(isCyclic);
    }

    @Test
    public void getMyontoViitesTest() throws Exception {
        Long id = populate(kayttoOikeusRyhma("RYHMA").withNimi(text("FI", "Käyttäjähallinta")
                .put("EN", "User management"))
                .withOikeus(oikeus("HENKILOHALLINTA", "CRUD"))
                .withOikeus(oikeus("KOODISTO", "READ"))).getId();

        Long id2 = populate(kayttoOikeusRyhma("RYHMA2")
                .withOikeus(oikeus("PALVELU1", "CRUD"))
                .withOikeus(oikeus("PALVELU2", "READ"))).getId();

        populate(kayttoOikeusRyhmaMyontoViite(1000L, id));
        populate(kayttoOikeusRyhmaMyontoViite(1001L, id));

        List<KayttoOikeusRyhmaMyontoViite> viites = kayttoOikeusRyhmaMyontoViiteRepository.getMyontoViites(1000L);
        assertEquals(1, viites.size());
        assertEquals(1000L, viites.get(0).getMasterId().longValue());
        assertEquals(id, viites.get(0).getSlaveId());

        populate(kayttoOikeusRyhmaMyontoViite(1000L, id2));
        viites = kayttoOikeusRyhmaMyontoViiteRepository.getMyontoViites(1000L);
        assertEquals(2, viites.size());
    }

}
