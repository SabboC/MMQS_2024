package fi.vm.sade.kayttooikeus.repositories;

import java.util.Set;

public interface RyhmaRepositoryCustom {

    Set<String> findNimiByKayttaja(String kayttajaDn);

}
