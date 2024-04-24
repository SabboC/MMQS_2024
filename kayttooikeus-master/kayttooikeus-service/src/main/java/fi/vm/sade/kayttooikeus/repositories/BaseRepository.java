package fi.vm.sade.kayttooikeus.repositories;

import java.util.Optional;

public interface BaseRepository<T> {
    Optional<T> findById(long id);

    T persist(T entity);

    void remove(T entity);
}
