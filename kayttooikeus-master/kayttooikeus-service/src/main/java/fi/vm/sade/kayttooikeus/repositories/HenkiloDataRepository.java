package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.model.Henkilo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface HenkiloDataRepository extends JpaRepository<Henkilo, Long>, HenkiloDataRepositoryCustom {
    Optional<Henkilo> findByOidHenkilo(String oidHenkilo);

    Long countByEtunimetCachedNotNull();

    @EntityGraph("henkiloperustietohaku")
    List<Henkilo> findByOidHenkiloIn(List<String> oidHenkilo);

    @EntityGraph("henkilohaku")
    List<Henkilo> readByOidHenkiloIn(List<String> oidHenkilo);

    Stream<Henkilo> findByAnomusilmoitusIsNotNull();

}
