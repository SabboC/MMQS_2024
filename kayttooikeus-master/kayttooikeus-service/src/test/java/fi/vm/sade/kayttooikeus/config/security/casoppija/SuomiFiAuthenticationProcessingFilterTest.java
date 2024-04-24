package fi.vm.sade.kayttooikeus.config.security.casoppija;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fi.vm.sade.kayttooikeus.config.security.casoppija.SuomiFiAuthenticationProcessingFilter.ETUNIMET_ALT_ATTRIBUTE;
import static fi.vm.sade.kayttooikeus.config.security.casoppija.SuomiFiAuthenticationProcessingFilter.ETUNIMET_ATTRIBUTE;
import static fi.vm.sade.kayttooikeus.config.security.casoppija.SuomiFiAuthenticationProcessingFilter.HETU_ATTRIBUTE;
import static fi.vm.sade.kayttooikeus.config.security.casoppija.SuomiFiAuthenticationProcessingFilter.SUKUNIMI_ATTRIBUTE;
import static fi.vm.sade.kayttooikeus.config.security.casoppija.SuomiFiAuthenticationProcessingFilter.OPPIJA_TICKET_PARAM_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuomiFiAuthenticationProcessingFilterTest {

    private final TicketValidator validator = mock(TicketValidator.class);
    private final SuomiFiAuthenticationProcessingFilter filter =
            new SuomiFiAuthenticationProcessingFilter(validator);

    @Test
    public void extractServiceParameters_yhdistaaParametrit() {
        Map<String, String[]> parameterMap = new ParameterBuilder()
                .parameter("param1", "value1")
                .parameter("param2", "value2")
                .build();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameterMap()).thenReturn(parameterMap);
        String result = filter.extractServiceParameters(request);
        assertEquals("param1=value1&param2=value2", result);
    }

    @Test
    public void extractServiceParameters_jattaaTicketinPois() {
        Map<String, String[]> parameterMap = new ParameterBuilder()
                .parameter("param1", "value1")
                .parameter("ticket", "shouldnotbeincluded")
                .build();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameterMap()).thenReturn(parameterMap);
        String result = filter.extractServiceParameters(request);
        assertEquals("param1=value1", result);
    }

    @Test
    public void extractServiceParameters_hanskaaUseammanSamannimisen() {
        Map<String, String[]> parameterMap = new ParameterBuilder()
                .parameter("param1", "value1_1")
                .parameter("param1", "value1_2")
                .parameter("param2", "value2")
                .build();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameterMap()).thenReturn(parameterMap);
        String result = filter.extractServiceParameters(request);
        assertEquals("param1=value1_1&param1=value1_2&param2=value2", result);
    }

    @Test
    public void extractUserDetails_palauttaaHetunJaNimen() {
        Assertion assertion = mock(Assertion.class);
        AttributePrincipal principal = mock(AttributePrincipal.class);
        Map<String, Object> attributes = Map.of(
                HETU_ATTRIBUTE, "123456-7890",
                SUKUNIMI_ATTRIBUTE, "Testi",
                ETUNIMET_ATTRIBUTE, "Testi-Petteri Einari"
        );
        when(assertion.isValid()).thenReturn(true);
        when(assertion.getPrincipal()).thenReturn(principal);
        when(principal.getAttributes()).thenReturn(attributes);
        SuomiFiUserDetails details = filter.extractUserDetails(assertion);
        assertEquals("123456-7890", details.hetu);
        assertEquals("Testi", details.sukunimi);
        assertEquals("Testi-Petteri Einari", details.etunimet);
    }

    @Test
    public void extractUserDetails_palauttaaEtunimetVaihtoehtoisellaAvaimella() {
        Assertion assertion = mock(Assertion.class);
        AttributePrincipal principal = mock(AttributePrincipal.class);
        Map<String, Object> attributes = Map.of(
                ETUNIMET_ALT_ATTRIBUTE, "Testi-Petteri Einari"
        );
        when(assertion.isValid()).thenReturn(true);
        when(assertion.getPrincipal()).thenReturn(principal);
        when(principal.getAttributes()).thenReturn(attributes);
        SuomiFiUserDetails details = filter.extractUserDetails(assertion);
        assertEquals("Testi-Petteri Einari", details.etunimet);
    }

    @Test
    public void getPreAuthenticatedPrincipal_palauttaaNullJosEiTickettia() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Object principal = filter.getPreAuthenticatedPrincipal(request);
        assertNull(principal);
    }

    @Test
    public void getPreAuthenticatedPrincipal_palauttaaNullJosEpavalidiTicket() throws TicketValidationException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String ticket = "epävaliditiksu";
        when(request.getParameterMap()).thenReturn(Map.of(OPPIJA_TICKET_PARAM_NAME, new String[] { ticket }));
        Assertion assertion = mock(Assertion.class);
        when(assertion.isValid()).thenReturn(false);
        when(validator.validate(anyString(), anyString())).thenReturn(assertion);
        Object principal = filter.getPreAuthenticatedPrincipal(request);
        assertNull(principal);
    }

    private static class ParameterBuilder {

        private final Map<String, List<String>> parameters = new HashMap<>();

        private ParameterBuilder parameter(String name, String ...values) {
            List<String> resultValues = parameters.getOrDefault(name, new ArrayList<>());
            resultValues.addAll(Arrays.asList(values));
            if (!parameters.containsKey(name)) {
                parameters.put(name, resultValues);
            }
            return this;
        }

        private Map<String, String[]> build() {
            return parameters.entrySet().stream().collect(
                    Collectors.toMap(
                            Map.Entry::getKey,
                            (e) -> e.getValue().toArray(new String[0]))
            );
        }
    }
}
