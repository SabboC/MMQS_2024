package fi.vm.sade.kayttooikeus.service;

import fi.vm.sade.kayttooikeus.DatabaseService;
import fi.vm.sade.kayttooikeus.dto.KayttoOikeudenTila;
import fi.vm.sade.kayttooikeus.model.Henkilo;
import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhmaTapahtumaHistoria;
import fi.vm.sade.kayttooikeus.model.OrganisaatioHenkilo;
import fi.vm.sade.kayttooikeus.repositories.HenkiloDataRepository;
import fi.vm.sade.kayttooikeus.repositories.OrganisaatioHenkiloRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static fi.vm.sade.kayttooikeus.repositories.populate.HenkiloPopulator.henkilo;
import static fi.vm.sade.kayttooikeus.repositories.populate.KayttoOikeusRyhmaPopulator.kayttoOikeusRyhma;
import static fi.vm.sade.kayttooikeus.repositories.populate.OrganisaatioHenkiloKayttoOikeusPopulator.myonnettyKayttoOikeus;
import static fi.vm.sade.kayttooikeus.repositories.populate.OrganisaatioHenkiloPopulator.organisaatioHenkilo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyonnettyKayttoOikeusServiceTest {

    @Autowired
    private MyonnettyKayttoOikeusService myonnettyKayttoOikeusService;

    @Autowired
    private HenkiloDataRepository henkiloDataRepository;

    @Autowired
    private OrganisaatioHenkiloRepository organisaatioHenkiloRepository;

    @Autowired
    private DatabaseService databaseService;

    @After
    public void cleanup() {
        databaseService.truncate();
    }

    @Test
    public void poistaVanhentuneet() {
        databaseService.populate(henkilo("kayttaja"));
        databaseService.populate(myonnettyKayttoOikeus(organisaatioHenkilo("henkilo1", "organisaatio1"),
                kayttoOikeusRyhma("ryhmä1")).voimassaPaattyen(LocalDate.now().minusDays(1)));
        databaseService.populate(myonnettyKayttoOikeus(organisaatioHenkilo("henkilo1", "organisaatio1"),
                kayttoOikeusRyhma("ryhmä2")).voimassaPaattyen(LocalDate.now().minusDays(1)));
        databaseService.populate(myonnettyKayttoOikeus(organisaatioHenkilo("henkilo1", "organisaatio2"),
                kayttoOikeusRyhma("ryhmä1")));
        databaseService.populate(myonnettyKayttoOikeus(organisaatioHenkilo("henkilo2", "organisaatio1"),
                kayttoOikeusRyhma("ryhmä1")));

        Henkilo kayttaja = henkiloDataRepository.findByOidHenkilo("kayttaja").get();
        myonnettyKayttoOikeusService.poistaVanhentuneet(new MyonnettyKayttoOikeusService.DeleteDetails(
                kayttaja, KayttoOikeudenTila.SULJETTU, "suljettu testissä"));

        databaseService.runInTransaction(() -> {
            OrganisaatioHenkilo henkilo1organisaatio1 = organisaatioHenkiloRepository
                    .findByHenkiloOidHenkiloAndOrganisaatioOid("henkilo1", "organisaatio1").get();
            assertThat(henkilo1organisaatio1.isPassivoitu()).isTrue();
            assertThat(henkilo1organisaatio1.getMyonnettyKayttoOikeusRyhmas()).isEmpty();
            assertThat(henkilo1organisaatio1.getKayttoOikeusRyhmaHistorias())
                    .extracting(historia -> historia.getKayttoOikeusRyhma().getTunniste(),
                            KayttoOikeusRyhmaTapahtumaHistoria::getTila, KayttoOikeusRyhmaTapahtumaHistoria::getSyy)
                    .containsExactlyInAnyOrder(
                            tuple("ryhmä1", KayttoOikeudenTila.SULJETTU, "suljettu testissä"),
                            tuple("ryhmä2", KayttoOikeudenTila.SULJETTU, "suljettu testissä"));
            OrganisaatioHenkilo henkilo1organisaatio2 = organisaatioHenkiloRepository
                    .findByHenkiloOidHenkiloAndOrganisaatioOid("henkilo1", "organisaatio2").get();
            assertThat(henkilo1organisaatio2.isPassivoitu()).isFalse();
            assertThat(henkilo1organisaatio2.getMyonnettyKayttoOikeusRyhmas())
                    .extracting(myonnetty -> myonnetty.getKayttoOikeusRyhma().getTunniste())
                    .containsExactlyInAnyOrder("ryhmä1");
            assertThat(henkilo1organisaatio2.getKayttoOikeusRyhmaHistorias()).isEmpty();
            OrganisaatioHenkilo henkilo2organisaatio1 = organisaatioHenkiloRepository
                    .findByHenkiloOidHenkiloAndOrganisaatioOid("henkilo2", "organisaatio1").get();
            assertThat(henkilo2organisaatio1.isPassivoitu()).isFalse();
            assertThat(henkilo2organisaatio1.getMyonnettyKayttoOikeusRyhmas())
                    .extracting(myonnetty -> myonnetty.getKayttoOikeusRyhma().getTunniste())
                    .containsExactlyInAnyOrder("ryhmä1");
            assertThat(henkilo2organisaatio1.getKayttoOikeusRyhmaHistorias()).isEmpty();
        });
    }

    @Test
    public void passivoi() {
        databaseService.populate(henkilo("kayttaja"));
        databaseService.populate(myonnettyKayttoOikeus(organisaatioHenkilo("henkilo1", "organisaatio1"),
                kayttoOikeusRyhma("ryhmä1")));
        databaseService.populate(myonnettyKayttoOikeus(organisaatioHenkilo("henkilo1", "organisaatio1"),
                kayttoOikeusRyhma("ryhmä2")));
        databaseService.populate(myonnettyKayttoOikeus(organisaatioHenkilo("henkilo1", "organisaatio2"),
                kayttoOikeusRyhma("ryhmä1")));
        databaseService.populate(myonnettyKayttoOikeus(organisaatioHenkilo("henkilo2", "organisaatio1"),
                kayttoOikeusRyhma("ryhmä1")));

        databaseService.runInTransaction(() -> {
            Henkilo kayttaja = henkiloDataRepository.findByOidHenkilo("kayttaja").get();
            OrganisaatioHenkilo organisaatioHenkilo = organisaatioHenkiloRepository
                    .findByHenkiloOidHenkiloAndOrganisaatioOid("henkilo1", "organisaatio1").get();
            myonnettyKayttoOikeusService.passivoi(organisaatioHenkilo, new MyonnettyKayttoOikeusService.DeleteDetails(
                    kayttaja, KayttoOikeudenTila.SULJETTU, "suljettu testissä"));
        });

        databaseService.runInTransaction(() -> {
            OrganisaatioHenkilo henkilo1organisaatio1 = organisaatioHenkiloRepository
                    .findByHenkiloOidHenkiloAndOrganisaatioOid("henkilo1", "organisaatio1").get();
            assertThat(henkilo1organisaatio1.isPassivoitu()).isTrue();
            assertThat(henkilo1organisaatio1.getMyonnettyKayttoOikeusRyhmas()).isEmpty();
            assertThat(henkilo1organisaatio1.getKayttoOikeusRyhmaHistorias())
                    .extracting(historia -> historia.getKayttoOikeusRyhma().getTunniste(),
                            KayttoOikeusRyhmaTapahtumaHistoria::getTila, KayttoOikeusRyhmaTapahtumaHistoria::getSyy)
                    .containsExactlyInAnyOrder(
                            tuple("ryhmä1", KayttoOikeudenTila.SULJETTU, "suljettu testissä"),
                            tuple("ryhmä2", KayttoOikeudenTila.SULJETTU, "suljettu testissä"));
            OrganisaatioHenkilo henkilo1organisaatio2 = organisaatioHenkiloRepository
                    .findByHenkiloOidHenkiloAndOrganisaatioOid("henkilo1", "organisaatio2").get();
            assertThat(henkilo1organisaatio2.isPassivoitu()).isFalse();
            assertThat(henkilo1organisaatio2.getMyonnettyKayttoOikeusRyhmas())
                    .extracting(myonnetty -> myonnetty.getKayttoOikeusRyhma().getTunniste())
                    .containsExactlyInAnyOrder("ryhmä1");
            assertThat(henkilo1organisaatio2.getKayttoOikeusRyhmaHistorias()).isEmpty();
            OrganisaatioHenkilo henkilo2organisaatio1 = organisaatioHenkiloRepository
                    .findByHenkiloOidHenkiloAndOrganisaatioOid("henkilo2", "organisaatio1").get();
            assertThat(henkilo2organisaatio1.isPassivoitu()).isFalse();
            assertThat(henkilo2organisaatio1.getMyonnettyKayttoOikeusRyhmas())
                    .extracting(myonnetty -> myonnetty.getKayttoOikeusRyhma().getTunniste())
                    .containsExactlyInAnyOrder("ryhmä1");
            assertThat(henkilo2organisaatio1.getKayttoOikeusRyhmaHistorias()).isEmpty();
        });
    }

}
