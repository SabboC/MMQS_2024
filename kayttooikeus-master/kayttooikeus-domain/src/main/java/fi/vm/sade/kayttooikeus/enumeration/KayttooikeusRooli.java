package fi.vm.sade.kayttooikeus.enumeration;

import lombok.Getter;

@Getter
public enum KayttooikeusRooli {
    VASTUUKAYTTAJAT("VASTUUKAYTTAJAT"),
    ;

    String name;

    KayttooikeusRooli(String name) {
        this.name = name;
    }

}
