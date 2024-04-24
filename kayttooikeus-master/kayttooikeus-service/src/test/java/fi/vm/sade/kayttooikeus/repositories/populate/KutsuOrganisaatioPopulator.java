package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.model.KayttoOikeusRyhma;
import fi.vm.sade.kayttooikeus.model.KutsuOrganisaatio;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KutsuOrganisaatioPopulator implements Populator<KutsuOrganisaatio> {
    private final String organisaatioOid;
    private final List<Populator<KayttoOikeusRyhma>> kayttoOikeusRyhmat = new ArrayList<>();

    public KutsuOrganisaatioPopulator(String organisaatioOid) {
        this.organisaatioOid = organisaatioOid;
    }
    
    public static KutsuOrganisaatioPopulator kutsuOrganisaatio(String organisaatioOid) {
        return new KutsuOrganisaatioPopulator(organisaatioOid);
    }
    
    public KutsuOrganisaatioPopulator ryhma(Populator<KayttoOikeusRyhma> ryhmaPopulator) {
        this.kayttoOikeusRyhmat.add(ryhmaPopulator);
        return this;
    }

    @Override
    public KutsuOrganisaatio apply(EntityManager entityManager) {
        KutsuOrganisaatio kutsuOrganisaatio = new KutsuOrganisaatio();
        kutsuOrganisaatio.setOrganisaatioOid(organisaatioOid);
        kutsuOrganisaatio.getRyhmat().addAll(kayttoOikeusRyhmat.stream().map(r -> r.apply(entityManager))
                .collect(Collectors.toList()));
        return kutsuOrganisaatio;
    }
}
