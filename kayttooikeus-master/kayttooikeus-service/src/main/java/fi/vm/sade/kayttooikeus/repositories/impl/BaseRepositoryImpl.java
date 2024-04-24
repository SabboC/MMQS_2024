package fi.vm.sade.kayttooikeus.repositories.impl;

import fi.vm.sade.kayttooikeus.model.Identifiable;
import fi.vm.sade.kayttooikeus.repositories.BaseRepository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class BaseRepositoryImpl<E extends Identifiable> extends AbstractRepository implements BaseRepository<E> {
    private final Class<E> clz;

    protected BaseRepositoryImpl() {
        Class clz = classifyGenericTypeParameters(getClass().getGenericSuperclass())[0];
        this.clz = clz;
    }

    @Override
    public Optional<E> findById(long id) {
        return ofNullable(em.find(clz, id));
    }

    @Override
    public E persist(E entity){
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Override
    public void remove(E entity){
        em.remove(entity);
    }

    private static Class<?>[] classifyGenericTypeParameters(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            Class<?>[] classes = new Class<?>[actualTypes.length];
            for (int i = 0; i < actualTypes.length; ++i) {
                classes[i] = (actualTypes[i] instanceof Class) ? (Class<?>) actualTypes[i] : null;
            }
            return classes;
        }
        return null;
    }
}
