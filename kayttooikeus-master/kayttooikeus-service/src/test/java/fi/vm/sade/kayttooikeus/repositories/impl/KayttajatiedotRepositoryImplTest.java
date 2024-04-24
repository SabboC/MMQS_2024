package fi.vm.sade.kayttooikeus.repositories.impl;

import fi.vm.sade.kayttooikeus.dto.KayttajatiedotReadDto;
import fi.vm.sade.kayttooikeus.model.Identification;
import fi.vm.sade.kayttooikeus.repositories.KayttajatiedotRepository;
import fi.vm.sade.kayttooikeus.service.PermissionCheckerService;
import fi.vm.sade.kayttooikeus.service.external.OrganisaatioClient;
import fi.vm.sade.kayttooikeus.service.it.AbstractServiceIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static fi.vm.sade.kayttooikeus.repositories.populate.HenkiloPopulator.*;
import static fi.vm.sade.kayttooikeus.repositories.populate.IdentificationPopulator.identification;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class KayttajatiedotRepositoryImplTest extends AbstractServiceIntegrationTest {

    @MockBean
    PermissionCheckerService permissionCheckerService;
    @MockBean
    OrganisaatioClient organisaatioClient;
    @Autowired
    private KayttajatiedotRepository repository;

    @Test
    public void testQueryExecutes() {
        String oid = "oid1";

        Optional<KayttajatiedotReadDto> kayttajatiedot = repository.findByHenkiloOid(oid);

        assertThat(kayttajatiedot).isEmpty();
    }

    @Test
    public void cleanObsoletedIdentifications() {
        assertThat(repository.cleanObsoletedIdentifications()).isZero();
    }

    @Test
    public void cleanObsoletedIdentificationsReportsCorrectly() {
        populate(identification(Identification.CAS_AUTHENTICATION_IDP, "foo", henkilo("oid").withUsername("bar")));
        assertThat(repository.cleanObsoletedIdentifications()).isEqualTo(1);
    }

    @Test
    public void cleanObsoletedIdentificationsSkipsProtectedIDPs() {
        populate(identification(Identification.HAKA_AUTHENTICATION_IDP, "foo", henkilo("oid").withUsername("bar")));
        assertThat(repository.cleanObsoletedIdentifications()).isZero();
    }

    @Test
    public void cleanObsoletedIdentificationsSkipCorrectlyMatching() {
        populate(identification(Identification.CAS_AUTHENTICATION_IDP, "foo", henkilo("oid").withUsername("foo")));
        assertThat(repository.cleanObsoletedIdentifications()).isZero();
    }

    @Test
    public void cleanObsoletedIdentificationsHandlesNullUsernames() {
        populate(identification(Identification.CAS_AUTHENTICATION_IDP, "foo", henkilo("oid")));
        assertThat(repository.cleanObsoletedIdentifications()).isEqualTo(1);
    }

    @Test
    public void findPassiveServiceUsersSkipIncorrectUserType() {
        populate(virkailija("foo").withUsername("bar"));
        populate(virkailija("qux").withUsername("xyz").withLoginCounter());
        assertThat(repository.findPassiveServiceUsers(LocalDateTime.now().minusDays(1))).isEmpty();
        assertThat(repository.findPassiveServiceUsers(LocalDateTime.now().plusDays(1))).isEmpty();
    }

    @Test
    public void findPassiveServiceUsersHavingLoginCounter() {
        populate(palvelukayttaja("foo").withUsername("bar").withLoginCounter());
        assertThat(repository.findPassiveServiceUsers(LocalDateTime.now().minusDays(1))).isEmpty();
        assertThat(repository.findPassiveServiceUsers(LocalDateTime.now().plusDays(1))).hasSize(1);
    }

    @Test
    public void findPassiveServiceUsersNotHavingLoginCounter() {
        populate(palvelukayttaja("foo").withUsername("bar"));
        assertThat(repository.findPassiveServiceUsers(LocalDateTime.now().minusDays(1))).isEmpty();
        assertThat(repository.findPassiveServiceUsers(LocalDateTime.now().plusDays(1))).hasSize(1);
    }
}
