package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeusHistoriaDto;
import fi.vm.sade.kayttooikeus.dto.KayttoOikeusTyyppi;
import fi.vm.sade.kayttooikeus.dto.PalveluKayttoOikeusDto;
import fi.vm.sade.kayttooikeus.dto.PalveluRooliDto;
import fi.vm.sade.kayttooikeus.model.KayttoOikeus;
import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhma;
import fi.vm.sade.kayttooikeus.model.MyonnettyKayttoOikeusRyhmaTapahtuma;
import fi.vm.sade.kayttooikeus.repositories.dto.ExpiringKayttoOikeusDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

import static fi.vm.sade.kayttooikeus.repositories.populate.HenkiloPopulator.henkilo;
import static fi.vm.sade.kayttooikeus.repositories.populate.KayttoOikeusPopulator.oikeus;
import static fi.vm.sade.kayttooikeus.repositories.populate.KayttoOikeusRyhmaPopulator.kayttoOikeusRyhma;
import static fi.vm.sade.kayttooikeus.repositories.populate.OrganisaatioHenkiloKayttoOikeusPopulator.myonnettyKayttoOikeus;
import static fi.vm.sade.kayttooikeus.repositories.populate.OrganisaatioHenkiloPopulator.organisaatioHenkilo;
import static fi.vm.sade.kayttooikeus.repositories.populate.PalveluPopulator.palvelu;
import static fi.vm.sade.kayttooikeus.repositories.populate.TextGroupPopulator.text;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class KayttoOikeusRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private KayttoOikeusRepository kayttoOikeusRepository;
    
    @Test
    public void isHenkiloMyonnettyKayttoOikeusToPalveluInRoleTest() {
        MyonnettyKayttoOikeusRyhmaTapahtuma tapahtuma = populate(myonnettyKayttoOikeus(
            organisaatioHenkilo(henkilo("1.2.3.4.5"), "3.4.5.6.7"),
            kayttoOikeusRyhma("RYHMA")
                    .withOikeus(oikeus("HENKILOHALLINTA", "CRUD"))
                    .withOikeus(oikeus("KOODISTO", "READ"))
        ));
        
        assertTrue(kayttoOikeusRepository.isHenkiloMyonnettyKayttoOikeusToPalveluInRole("1.2.3.4.5", "HENKILOHALLINTA", "CRUD"));
        assertTrue(kayttoOikeusRepository.isHenkiloMyonnettyKayttoOikeusToPalveluInRole("1.2.3.4.5", "KOODISTO", "READ"));
        assertFalse(kayttoOikeusRepository.isHenkiloMyonnettyKayttoOikeusToPalveluInRole("1.2.3.4.5", "KOODISTO", "CRUD"));
        assertFalse(kayttoOikeusRepository.isHenkiloMyonnettyKayttoOikeusToPalveluInRole("1.2.3.4.5", "PALVELU", "UPDATE"));
        assertFalse(kayttoOikeusRepository.isHenkiloMyonnettyKayttoOikeusToPalveluInRole("1.2.3.4.6", "HENKILOHALLINTA", "CRUD"));
        
        tapahtuma.setVoimassaAlkuPvm(LocalDate.now().plusDays(1));
        em.merge(tapahtuma);

        assertFalse(kayttoOikeusRepository.isHenkiloMyonnettyKayttoOikeusToPalveluInRole("1.2.3.4.5", "HENKILOHALLINTA", "CRUD"));
        
        populate(myonnettyKayttoOikeus(
            organisaatioHenkilo(henkilo("1.2.3.4.6"), "3.4.5.6.7").voimassaAlkaen(LocalDate.now().plusMonths(1)),
            kayttoOikeusRyhma("RYHMA").withOikeus(oikeus("HENKILOHALLINTA", "CRUD"))
        ));
        assertFalse(kayttoOikeusRepository.isHenkiloMyonnettyKayttoOikeusToPalveluInRole("1.2.3.4.6", "HENKILOHALLINTA", "CRUD"));
    }

    @Test
    public void findSoonToBeExpiredTapahtumasTest() {
        MyonnettyKayttoOikeusRyhmaTapahtuma tapahtuma = populate(myonnettyKayttoOikeus(
                organisaatioHenkilo(henkilo("1.2.3.4.5"), "3.4.5.6.7"),
                kayttoOikeusRyhma("RYHMA").withNimi(text("FI", "Kuvaus"))
                        .withOikeus(oikeus("HENKILOHALLINTA", "CRUD"))
                        .withOikeus(oikeus("KOODISTO", "READ"))
        ).voimassaPaattyen(LocalDate.now().plusMonths(3)));

        List<ExpiringKayttoOikeusDto> expiring = kayttoOikeusRepository.findSoonToBeExpiredTapahtumas(LocalDate.now(), Period.ofWeeks(1));
        assertTrue(expiring.isEmpty());

        expiring = kayttoOikeusRepository.findSoonToBeExpiredTapahtumas(LocalDate.now());
        assertTrue(expiring.isEmpty());

        expiring = kayttoOikeusRepository.findSoonToBeExpiredTapahtumas(LocalDate.now(), Period.ofMonths(4), Period.ofMonths(2));
        assertTrue(expiring.isEmpty());
        
        expiring = kayttoOikeusRepository.findSoonToBeExpiredTapahtumas(LocalDate.now(), Period.ofMonths(3), Period.ofMonths(1));
        assertFalse(expiring.isEmpty());
        assertEquals(1, expiring.size());
        assertEquals(tapahtuma.getVoimassaLoppuPvm(), expiring.get(0).getVoimassaLoppuPvm());
        assertEquals(tapahtuma.getId(), expiring.get(0).getMyonnettyTapahtumaId());
        assertEquals("1.2.3.4.5", expiring.get(0).getHenkiloOid());
        assertEquals("RYHMA", expiring.get(0).getRyhmaName());
        assertEquals(tapahtuma.getKayttoOikeusRyhma().getNimi().getId(), expiring.get(0).getRyhmaDescription().getId());
    }

    @Test
    public void listKayttoOikeusByPalveluTest() {
        populate(kayttoOikeusRyhma("RYHMA").withNimi(text("FI", "Kuvaus"))
                        .withOikeus(oikeus("HENKILOHALLINTA", "CRUD").kuvaus(text("FI", "Kuvaus")
                                .put("EN", "Desc")))
                        .withOikeus(oikeus("HENKILOHALLINTA", "READ"))
                        .withOikeus(oikeus("KOODISTO", "READ")));
        List<PalveluKayttoOikeusDto> results = kayttoOikeusRepository.listKayttoOikeusByPalvelu("PALVELU2");
        assertTrue(results.isEmpty());

        results = kayttoOikeusRepository.listKayttoOikeusByPalvelu("HENKILOHALLINTA");
        assertEquals(2, results.size());
        assertEquals("CRUD", results.get(0).getRooli());
        assertEquals("READ", results.get(1).getRooli());
        assertNotNull(results.get(0).getOikeusLangs());
    }

    @Test
    public void listMyonnettyKayttoOikeusHistoriaForHenkiloTest() {
        MyonnettyKayttoOikeusRyhmaTapahtuma tapahtuma1 = populate(myonnettyKayttoOikeus(
                    organisaatioHenkilo(henkilo("1.2.3.4.5"), "3.4.5.6.7"),
                    kayttoOikeusRyhma("RYHMA").withNimi(text("FI", "Kuvaus"))
                            .withOikeus(oikeus("HENKILOHALLINTA", "CRUD").kuvaus(text("FI", "Luku-muokkaus")))
                            .withOikeus(oikeus(palvelu("KOODISTO").kuvaus(text("FI", "Palvelukuvaus")), "READ")))
                        .voimassaPaattyen(LocalDate.now())),
            tapahtuma2 = populate(myonnettyKayttoOikeus(
                    organisaatioHenkilo(henkilo("1.2.3.4.5"), "4.5.6.7.8").tehtavanimike("testaaja"),
                    kayttoOikeusRyhma("RYHMA2").withNimi(text("FI", "Ryhmäkuvaus"))
                            .withOikeus(oikeus("KOODISTO", "WRITE").kuvaus(text("FI", "Kirjoitusoikeus"))))
                    .voimassaPaattyen(LocalDate.now().plusMonths(3)));
        
        List<KayttoOikeusHistoriaDto> historia = kayttoOikeusRepository.listMyonnettyKayttoOikeusHistoriaForHenkilo("1.2.3.4.5");
        assertEquals(3, historia.size());
        assertEquals(tapahtuma2.getAikaleima(), historia.get(0).getAikaleima());
        assertEquals(tapahtuma2.getKayttoOikeusRyhma().getKayttoOikeus().iterator().next().getId().longValue(),
                historia.get(0).getKayttoOikeusId());
        assertEquals(tapahtuma2.getTila(), historia.get(0).getTila());
        assertEquals(KayttoOikeusTyyppi.KOOSTEROOLI, historia.get(0).getTyyppi());
        assertEquals("4.5.6.7.8", historia.get(0).getOrganisaatioOid());
        assertEquals("testaaja", historia.get(0).getTehtavanimike());
        assertEquals(LocalDate.now().plusMonths(3), historia.get(0).getVoimassaLoppuPvm());
        assertEquals(LocalDate.now(), historia.get(0).getVoimassaAlkuPvm());
        assertEquals(tapahtuma2.getKasittelija().getOidHenkilo(), historia.get(0).getKasittelija());
        assertEquals(tapahtuma2.getKayttoOikeusRyhma().getNimi().getId(), historia.get(0).getKuvaus().getId());
        assertEquals("KOODISTO", historia.get(0).getPalvelu());
        assertEquals("WRITE", historia.get(0).getRooli());
        assertNotNull(historia.get(0).getKuvaus());
        assertNotNull(historia.get(0).getKuvaus().getId());
        assertNotNull(historia.get(0).getPalveluKuvaus());
        assertNotNull(historia.get(0).getPalveluKuvaus().getId());
        assertNotNull(historia.get(0).getKayttoOikeusKuvaus());
        assertNotNull(historia.get(0).getKayttoOikeusKuvaus().getId());
    }

    @Test
    public void findPalveluRoolitByKayttoOikeusRyhmaIdTest() throws Exception {
        KayttoOikeusRyhma kayttoOikeusRyhma = populate(kayttoOikeusRyhma("RYHMA")
                .withOikeus(oikeus("HENKILOHALLINTA", "CRUD")
                        .kuvaus(text("FI", "Kuvaus").put("SV", "på svenska").put("EN", "desc")))
                .withOikeus(oikeus("KOODISTO", "READ")
                        .kuvaus(text("FI", "Kuvaus henkilöhallinta").put("SV", "på svenska").put("EN", "desc"))));

        List<PalveluRooliDto> palveluRoolis = kayttoOikeusRepository.findPalveluRoolitByKayttoOikeusRyhmaId(kayttoOikeusRyhma.getId());
        assertEquals(2, palveluRoolis.size());

        assertTrue(palveluRoolis.stream()
                .map(PalveluRooliDto::getPalveluName)
                .collect(toList())
                .containsAll(Arrays.asList("HENKILOHALLINTA", "KOODISTO")));
    }

    @Test
    public void findByRooliAndPalveluTest() throws Exception {
        KayttoOikeus kayttoOikeus = kayttoOikeusRepository.findByRooliAndPalvelu("rooli", "palvelu");
        assertNull(kayttoOikeus);

        populate(kayttoOikeusRyhma("RYHMA")
                .withOikeus(oikeus("HENKILOHALLINTA", "CRUD"))
                .withOikeus(oikeus("HENKILOHALLINTA", "READ"))
                .withOikeus(oikeus("KOODISTO", "READ")));

        kayttoOikeus = kayttoOikeusRepository.findByRooliAndPalvelu("READ", "KOODISTO");
        assertNotNull(kayttoOikeus);
        assertEquals("READ", kayttoOikeus.getRooli());
        assertEquals("KOODISTO", kayttoOikeus.getPalvelu().getName());

        kayttoOikeus = kayttoOikeusRepository.findByRooliAndPalvelu("READ", "HENKILOHALLINTA");
        assertNotNull(kayttoOikeus);
        assertEquals("READ", kayttoOikeus.getRooli());
        assertEquals("HENKILOHALLINTA", kayttoOikeus.getPalvelu().getName());

        kayttoOikeus = kayttoOikeusRepository.findByRooliAndPalvelu("CRUD", "HENKILOHALLINTA");
        assertNotNull(kayttoOikeus);
        assertEquals("CRUD", kayttoOikeus.getRooli());
        assertEquals("HENKILOHALLINTA", kayttoOikeus.getPalvelu().getName());

        kayttoOikeus = kayttoOikeusRepository.findByRooliAndPalvelu("CRUD", "KOODISTO");
        assertNull(kayttoOikeus);

    }

}
