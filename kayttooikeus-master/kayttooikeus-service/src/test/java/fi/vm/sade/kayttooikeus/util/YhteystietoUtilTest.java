package fi.vm.sade.kayttooikeus.util;

import fi.vm.sade.oppijanumerorekisteri.dto.YhteystiedotRyhmaDto;
import fi.vm.sade.oppijanumerorekisteri.dto.YhteystietoDto;
import fi.vm.sade.oppijanumerorekisteri.dto.YhteystietoTyyppi;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class YhteystietoUtilTest {

    protected static List<YhteystiedotRyhmaDto> getFixture() {
        return IntStream.rangeClosed(1, 13).boxed()
                .map(i -> createYhteystiedotRyhmaDto(i, "yhteystietotyyppi" + i))
                .collect(toList());
    }

    protected static List<YhteystiedotRyhmaDto> getFixtureWithEmailAddresses() {
        return getFixture()
                .stream()
                .map(yhteystiedot -> {
                    yhteystiedot.setYhteystieto(Collections.singleton(new YhteystietoDto(YhteystietoTyyppi.YHTEYSTIETO_SAHKOPOSTI, yhteystiedot.getRyhmaKuvaus())));
                    return yhteystiedot;
                })
                .collect(toList());
    }

    private static YhteystiedotRyhmaDto createYhteystiedotRyhmaDto(long id, String ryhmakuvaus) {
        YhteystiedotRyhmaDto dto = new YhteystiedotRyhmaDto();
        dto.setId(id);
        dto.setRyhmaKuvaus(ryhmakuvaus);
        return dto;
    }

    @Test
    public void handlesNullParameterGracefully() {
        Optional<String> result = YhteystietoUtil.getWorkEmail(null);
        assertFalse(result.isPresent());
    }

    @Test
    public void noEmailAddresses() {
        List<YhteystiedotRyhmaDto> fixture = getFixture();
        Collections.shuffle(fixture);

        Optional<String> result = YhteystietoUtil.getWorkEmail(fixture);

        assertFalse(result.isPresent());
    }

    @Test
    public void hasWorkAddress() {
        List<YhteystiedotRyhmaDto> fixture = getFixtureWithEmailAddresses();
        Collections.shuffle(fixture);

        Optional<String> result = YhteystietoUtil.getWorkEmail(fixture);

        assertTrue(result.isPresent());
        assertEquals(YhteystietoUtil.TYOOSOITE, result.get());
    }

    @Test
    public void noWorkAddress() {
        List<YhteystiedotRyhmaDto> fixture = getFixtureWithEmailAddresses();
        fixture.remove(1); // index 1 = yhteystietotyyppi2 = YhteystietoUtil.TYOOSOITE
        Collections.shuffle(fixture);

        Optional<String> result = YhteystietoUtil.getWorkEmail(fixture);

        assertFalse(result.isPresent());
    }
}

