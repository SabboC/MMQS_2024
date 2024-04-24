package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.model.ScheduleTimestamps;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface ScheduleTimestampsDataRepository extends CrudRepository<ScheduleTimestamps, Long> {
    Optional<ScheduleTimestamps> findFirstByIdentifier(String identifier);
}
