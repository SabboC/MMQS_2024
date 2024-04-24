package fi.vm.sade.kayttooikeus.service;

import fi.vm.sade.kayttooikeus.repositories.criteria.MyontooikeusCriteria;
import fi.vm.sade.kayttooikeus.util.OrganisaatioMyontoPredicate;

import java.util.Map;
import java.util.Set;

public interface MyontooikeusService {

    Map<String, Set<Long>> getMyontooikeudet(String myontajaOid,
                                             MyontooikeusCriteria myontooikeusCriteria,
                                             OrganisaatioMyontoPredicate organisaatioMyontoPredicate);

}
