package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.dto.PalveluDto;
import fi.vm.sade.kayttooikeus.model.Palvelu;

import java.util.List;
import java.util.Optional;

public interface PalveluRepository extends BaseRepository<Palvelu> {
    List<PalveluDto> findAll();

    Optional<Palvelu> findByName(String name);
}
