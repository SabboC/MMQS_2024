package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.model.GoogleAuthToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface GoogleAuthTokenRepository extends CrudRepository<GoogleAuthToken, Long> {
}
