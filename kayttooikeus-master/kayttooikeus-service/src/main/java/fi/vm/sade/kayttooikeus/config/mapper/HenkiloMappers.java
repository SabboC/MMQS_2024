package fi.vm.sade.kayttooikeus.config.mapper;

import fi.vm.sade.kayttooikeus.dto.HenkiloNimiDto;
import fi.vm.sade.kayttooikeus.model.Henkilo;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HenkiloMappers {

    @Bean
    public CustomMapper<Henkilo, HenkiloNimiDto> henkiloNimiDtoMapper() {
        return new CustomMapper<Henkilo, HenkiloNimiDto>() {
            @Override
            public void mapAtoB(Henkilo a, HenkiloNimiDto b, MappingContext context) {
                b.setOid(a.getOidHenkilo());
                b.setEtunimet(a.getEtunimetCached());
                b.setSukunimi(a.getSukunimiCached());
            }
        };
    }

}
