package fi.vm.sade.kayttooikeus.utiltest;


import fi.vm.sade.kayttooikeus.util.UserDetailsUtil;
import fi.vm.sade.oppijanumerorekisteri.dto.HenkiloDto;
import fi.vm.sade.oppijanumerorekisteri.dto.HenkiloPerustietoDto;
import fi.vm.sade.oppijanumerorekisteri.dto.KielisyysDto;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsUtilTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {

    }

    @Test
    public void getName() {
        HenkiloDto henkiloDto = new HenkiloDto();
        henkiloDto.setEtunimet("arpa noppa");
        henkiloDto.setKutsumanimi("arpa");
        henkiloDto.setSukunimi("kuutio");
        String name = UserDetailsUtil.getName(henkiloDto);
        assertThat(name).isEqualTo("arpa kuutio");
    }

    @Test
    public void getCurrentUserOidNotFound() {
        this.expectedException.expect(NullPointerException.class);
        this.expectedException.expectMessage("No user name available from SecurityContext!");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserDetailsUtil.getCurrentUserOid();
    }

    @Test
    public void getCurrentUserOid() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1.2.3.4.5");
        SecurityContextHolder.setContext(securityContext);

        String username = UserDetailsUtil.getCurrentUserOid();
        assertThat(username).isEqualTo("1.2.3.4.5");
    }

    @Test
    public void getLanguageCode() {
        HenkiloDto henkiloDto = new HenkiloDto();
        henkiloDto.setAsiointiKieli(new KielisyysDto("sv", "svenska"));

        String kielikoodi = UserDetailsUtil.getLanguageCode(henkiloDto);
        assertThat(kielikoodi).isEqualTo("sv");
    }

    @Test
    public void getLanguageCodeDefault() {
        HenkiloDto henkiloDto = new HenkiloDto();
        henkiloDto.setAsiointiKieli(new KielisyysDto());

        String kielikoodi = UserDetailsUtil.getLanguageCode(henkiloDto);
        assertThat(kielikoodi).isEqualTo("fi");
    }

    @Test
    public void getLanguageCodePerustieto() {
        HenkiloPerustietoDto henkiloDto = new HenkiloPerustietoDto();
        henkiloDto.setAsiointiKieli(new KielisyysDto("sv", "svenska"));

        String kielikoodi = UserDetailsUtil.getLanguageCode(henkiloDto);
        assertThat(kielikoodi).isEqualTo("sv");
    }

    @Test
    public void getLanguageCodePerustietoDefault() {
        HenkiloPerustietoDto henkiloDto = new HenkiloPerustietoDto();
        henkiloDto.setAsiointiKieli(new KielisyysDto());

        String kielikoodi = UserDetailsUtil.getLanguageCode(henkiloDto);
        assertThat(kielikoodi).isEqualTo("fi");
    }
}
