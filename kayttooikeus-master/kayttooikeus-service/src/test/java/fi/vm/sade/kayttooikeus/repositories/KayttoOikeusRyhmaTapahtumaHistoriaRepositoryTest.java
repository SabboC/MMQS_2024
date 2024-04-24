package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeudenTila;
import fi.vm.sade.kayttooikeus.dto.MyonnettyKayttoOikeusDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static fi.vm.sade.kayttooikeus.repositories.populate.HenkiloPopulator.henkilo;
import static fi.vm.sade.kayttooikeus.repositories.populate.KayttoOikeusRyhmaPopulator.kayttoOikeusRyhma;
import static fi.vm.sade.kayttooikeus.repositories.populate.KayttoOikeusRyhmaTapahtumaHistoriaPopulator.historia;
import static fi.vm.sade.kayttooikeus.repositories.populate.OrganisaatioHenkiloPopulator.organisaatioHenkilo;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class KayttoOikeusRyhmaTapahtumaHistoriaRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private KayttoOikeusRyhmaTapahtumaHistoriaRepository kayttoOikeusRyhmaTapahtumaHistoriaRepository;

    @Test
    public void findByHenkiloInOrganisaatioTest() throws Exception {
        populate(historia()
                .kasittelija(henkilo("1.2.3.4.5"))
                .kayttoOikeusRyhma(kayttoOikeusRyhma("RYHMA"))
                .tila(KayttoOikeudenTila.HYLATTY)
                .syy("joku syy")
                .organisaatioHenkilo(organisaatioHenkilo("5.6.7.8.9", "3.4.5.6.7")
                        .tehtavanimike("tehtävä")));

        populate(historia()
                .kasittelija(henkilo("1.2.3.4.5"))
                .kayttoOikeusRyhma(kayttoOikeusRyhma("RYHMA"))
                .tila(KayttoOikeudenTila.ANOTTU)
                .syy("joku syy")
                .organisaatioHenkilo(organisaatioHenkilo("5.6.7.8.9", "3.4.5.6.7")
                        .tehtavanimike("tehtävä")));

        populate(historia()
                .kasittelija(henkilo("1.2.3.4.5"))
                .kayttoOikeusRyhma(kayttoOikeusRyhma("RYHMA"))
                .tila(KayttoOikeudenTila.PERUUTETTU)
                .syy("joku syy")
                .organisaatioHenkilo(organisaatioHenkilo("5.6.7.8.9", "3.4.5.6.8")
                        .tehtavanimike("tehtävä")));

        List<MyonnettyKayttoOikeusDto> kayttoOikeusList = kayttoOikeusRyhmaTapahtumaHistoriaRepository.findByHenkiloInOrganisaatio("5.6.7.8.9", "");
        assertEquals(2, kayttoOikeusList.size());
        kayttoOikeusList = kayttoOikeusRyhmaTapahtumaHistoriaRepository.findByHenkiloInOrganisaatio("5.6.7.8.9", "3.4.5.6.7");
        assertEquals(1, kayttoOikeusList.size());
        assertEquals("3.4.5.6.7", kayttoOikeusList.get(0).getOrganisaatioOid());
        assertEquals("tehtävä", kayttoOikeusList.get(0).getTehtavanimike());
        assertEquals("1.2.3.4.5", kayttoOikeusList.get(0).getKasittelijaOid());
        assertEquals("joku syy", kayttoOikeusList.get(0).getMuutosSyy());

    }
}
