package fi.vm.sade.kayttooikeus.config.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class ZonedDateTimeConverter extends BidirectionalConverter<Date, ZonedDateTime> {

    @Override
    public ZonedDateTime convertTo(Date source, Type<ZonedDateTime> destinationType, MappingContext mappingContext) {
        return (ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public Date convertFrom(ZonedDateTime source, Type<Date> destinationType, MappingContext mappingContext) {
        return (Date.from(source.toInstant()));
    }

}
