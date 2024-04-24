package fi.vm.sade.kayttooikeus.dto;

import java.util.Comparator;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public interface Localizable {
    Long getId();

    Localizable put(String lang, String value);
    
    String get(String lang);
    
    Optional<String> getOrAny(String lang);

    static<T extends Localizable> Comparator<T> comparingPrimarlyBy(String lang) {
        return (a,b) -> comparePrimarlyByLang(a, b, lang);
    }

    static int comparePrimarlyByLang(Localizable a, Localizable b, String lang) {
        return nullSafeCompare(a.getOrAny(lang).orElse(null), b.getOrAny(lang).orElse(null));
    }
    
    static int compareLangs(Localizable a, Localizable b, String lang) {
        return nullSafeCompare(ofNullable(a).map(l -> l.get(lang)).orElse(null), ofNullable(b).map(l -> l.get(lang)).orElse(null)); 
    }

    static int nullSafeCompare(String at, String bt) {
        if (at == null && bt == null) {
            return 0;
        }
        if (at == null) {
            return 1;
        }
        if (bt == null) {
            return -1;
        }
        return at.compareTo(bt);
    }
}
