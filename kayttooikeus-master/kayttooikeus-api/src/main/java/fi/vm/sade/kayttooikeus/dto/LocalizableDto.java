package fi.vm.sade.kayttooikeus.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

@FunctionalInterface
public interface LocalizableDto {
    @JsonIgnore
    Stream<Localizable> localizableTexts();

    static LocalizableDto of(LocalizableDto localizable) {
        return () -> localizable == null ? Stream.of() : localizable.localizableTexts();
    }
    static LocalizableDto of(Localizable... tekstit) {
        return () -> notNull(Stream.of(tekstit));
    }
    static LocalizableDto of(Collection<? extends LocalizableDto> of) {
        return () -> notNull(of.stream()).flatMap(LocalizableDto::localizableTexts);
    }
    default LocalizableDto and(Localizable... teksti) {
        return and(notNull(Stream.of(teksti)));
    }
    default LocalizableDto and(LocalizableDto... and) {
        return and(notNull(Stream.of(and)).flatMap(LocalizableDto::localizableTexts));
    }
    default LocalizableDto and(Collection<? extends LocalizableDto> and) {
        return and(notNull(and.stream()).flatMap(LocalizableDto::localizableTexts));
    }
    static<T> Stream<T> notNull(Stream<T> stream) {
        return stream.filter(Objects::nonNull);
    }
    default LocalizableDto and(Stream<Localizable> stream) {
        return () -> Stream.concat(localizableTexts(), stream);
    }
}
