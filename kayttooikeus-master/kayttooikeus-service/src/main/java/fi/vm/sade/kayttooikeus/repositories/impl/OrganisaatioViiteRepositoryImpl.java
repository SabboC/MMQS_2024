package fi.vm.sade.kayttooikeus.repositories.impl;

import com.querydsl.core.types.Projections;
import fi.vm.sade.kayttooikeus.dto.OrganisaatioViiteDto;
import fi.vm.sade.kayttooikeus.model.OrganisaatioViite;
import fi.vm.sade.kayttooikeus.repositories.OrganisaatioViiteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static fi.vm.sade.kayttooikeus.model.QOrganisaatioViite.organisaatioViite;

@Repository
public class OrganisaatioViiteRepositoryImpl extends BaseRepositoryImpl<OrganisaatioViite>
        implements OrganisaatioViiteRepository {

    @Override
    public List<OrganisaatioViiteDto> findByKayttoOikeusRyhmaIds(Set<Long> ids) {
        return jpa().from(organisaatioViite)
                .where(organisaatioViite.kayttoOikeusRyhma.id.in(ids))
                .select(Projections.constructor(OrganisaatioViiteDto.class,
                        organisaatioViite.id.as("id"),
                        organisaatioViite.organisaatioTyyppi.as("organisaatioTyyppi"),
                        organisaatioViite.kayttoOikeusRyhma.id.as("kayttoOikeusRyhmaId")))
                .fetch();
    }

}
