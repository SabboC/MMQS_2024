package fi.vm.sade.kayttooikeus.util;

import fi.vm.sade.oppijanumerorekisteri.dto.YhteystiedotRyhmaDto;
import fi.vm.sade.oppijanumerorekisteri.dto.YhteystietoTyyppi;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class YhteystietoUtil {

    public static final String TYOOSOITE = "yhteystietotyyppi2";

    public static Optional<String> getWorkEmail(Collection<YhteystiedotRyhmaDto> contactInformation) {
        return getStream(contactInformation)
                .filter(yhteystiedot -> TYOOSOITE.equals(yhteystiedot.getRyhmaKuvaus()))
                .sorted(Comparator.comparing(YhteystiedotRyhmaDto::getId, Comparator.reverseOrder()))
                .map(getEmailAddressFromYhteystietoryhma())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private static Stream<YhteystiedotRyhmaDto> getStream(Collection<YhteystiedotRyhmaDto> contactInformation) {
        return Optional
                .ofNullable(contactInformation)
                .orElse(Collections.EMPTY_SET)
                .stream();
    }

    private static Function<YhteystiedotRyhmaDto, Optional<String>> getEmailAddressFromYhteystietoryhma() {
        return yhteystiedot -> yhteystiedot.getYhteystieto().stream()
                .filter(yhteystieto -> yhteystieto.getYhteystietoTyyppi() == YhteystietoTyyppi.YHTEYSTIETO_SAHKOPOSTI)
                .filter(yhteystieto -> yhteystieto.getYhteystietoArvo() != null && !yhteystieto.getYhteystietoArvo().isEmpty())
                .map(yhteystieto -> yhteystieto.getYhteystietoArvo())
                .findFirst();
    }
}
