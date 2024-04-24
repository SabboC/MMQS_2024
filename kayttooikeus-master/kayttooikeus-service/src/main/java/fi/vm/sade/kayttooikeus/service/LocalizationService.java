package fi.vm.sade.kayttooikeus.service;

import fi.vm.sade.kayttooikeus.dto.LocalizableDto;
import fi.vm.sade.kayttooikeus.dto.LocalizableOrganisaatio;

import java.util.Collection;

public interface LocalizationService {
    <T extends LocalizableDto, C extends Collection<T>> C localize(C list);

    <T extends LocalizableDto> T localize(T dto);

    <T extends LocalizableOrganisaatio, C extends Collection<T>> C localizeOrgs(C list);
}
