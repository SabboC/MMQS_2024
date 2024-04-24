package fi.vm.sade.kayttooikeus.repositories.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

public class BaseCriteria {
    protected static <E> boolean used(Collection<E> param) {
        return param != null && !param.isEmpty();
    }
    protected static <E> boolean used(Object param) {
        return param != null;
    }

    protected static <E> List<E> params(E...values) {
        if (values == null || values.length < 1) {
            return new ArrayList<>();
        }
        return new ArrayList<>(asList(values));
    }
}
