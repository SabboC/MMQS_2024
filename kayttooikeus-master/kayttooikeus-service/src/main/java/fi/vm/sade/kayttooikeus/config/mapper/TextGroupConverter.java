package fi.vm.sade.kayttooikeus.config.mapper;

import fi.vm.sade.kayttooikeus.dto.TextGroupMapDto;
import fi.vm.sade.kayttooikeus.model.TextGroup;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

@Component
public class TextGroupConverter extends CustomConverter<TextGroup, TextGroupMapDto> {

    @Override
    public TextGroupMapDto convert(TextGroup source, Type<? extends TextGroupMapDto> destinationType, MappingContext mappingContext) {
        TextGroupMapDto destination = new TextGroupMapDto(source.getId());
        source.getTexts().stream().forEach((t) -> destination.put(t.getLang(), t.getText()));
        return destination;
    }

}
