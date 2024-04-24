package fi.vm.sade.kayttooikeus.repositories;


import fi.vm.sade.kayttooikeus.model.TextGroup;
import fi.vm.sade.kayttooikeus.repositories.dto.TextGroupTextDto;

import java.util.Collection;
import java.util.List;

public interface TextGroupRepository {
    List<TextGroupTextDto> findTexts(Collection<Long> textGroupIds);
}
