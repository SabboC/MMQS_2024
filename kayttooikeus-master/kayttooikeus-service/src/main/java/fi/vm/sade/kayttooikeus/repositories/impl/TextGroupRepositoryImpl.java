package fi.vm.sade.kayttooikeus.repositories.impl;

import com.querydsl.core.types.Projections;
import fi.vm.sade.kayttooikeus.model.TextGroup;
import fi.vm.sade.kayttooikeus.repositories.TextGroupRepository;
import fi.vm.sade.kayttooikeus.repositories.dto.TextGroupTextDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static fi.vm.sade.kayttooikeus.model.QText.text1;

@Repository
public class TextGroupRepositoryImpl extends AbstractRepository implements TextGroupRepository {
    @Override
    public List<TextGroupTextDto> findTexts(Collection<Long> textGroupIds) {
        if (textGroupIds.isEmpty()) {
            return new ArrayList<>();
        }
        return jpa().from(text1).<TextGroupTextDto>select(Projections.bean(TextGroupTextDto.class,
                    text1.textGroup.id.as("textGroupId"),
                    text1.lang.as("lang"),
                    text1.text.as("text")
                )).where(text1.textGroup.id.in(textGroupIds))
                    .orderBy(text1.textGroup.id.asc(), text1.lang.asc())
                .fetch();
    }

}
