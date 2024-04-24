package fi.vm.sade.kayttooikeus.config.mapper;

import fi.vm.sade.kayttooikeus.dto.KayttajaReadDto;
import fi.vm.sade.kayttooikeus.service.dto.HenkiloYhteystiedotDto;
import fi.vm.sade.kayttooikeus.util.YhteystietoUtil;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VirkailijaReadMappers {

    @Bean
    public CustomMapper<KayttajaReadDto, HenkiloYhteystiedotDto> virkailijaReadDtoToHenkiloYhteystiedotDtoMapper() {
        return new CustomMapper<KayttajaReadDto, HenkiloYhteystiedotDto>() {
            @Override
            public void mapAtoB(KayttajaReadDto a, HenkiloYhteystiedotDto b, MappingContext context) {
                throw new UnsupportedOperationException("Not implemented yet.");
            }

            @Override
            public void mapBtoA(HenkiloYhteystiedotDto b, KayttajaReadDto a, MappingContext context) {
                a.setOid(b.getOidHenkilo());
                a.setSahkoposti(YhteystietoUtil.getWorkEmail(b.getYhteystiedotRyhma()).orElse(null));
            }
        };
    }

}
