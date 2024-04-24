package fi.vm.sade.kayttooikeus.config.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class LocalDateConverter extends BidirectionalConverter<Date, LocalDate> {

    @Override
    public LocalDate convertTo(Date source, Type<LocalDate> destinationType, MappingContext mappingContext) {
        return (source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    @Override
    public Date convertFrom(LocalDate source, Type<Date> destinationType, MappingContext mappingContext) {
        return (Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

}
