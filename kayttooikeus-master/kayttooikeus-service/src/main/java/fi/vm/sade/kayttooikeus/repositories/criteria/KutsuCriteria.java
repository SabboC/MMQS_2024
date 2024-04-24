package fi.vm.sade.kayttooikeus.repositories.criteria;

import com.querydsl.core.BooleanBuilder;
import fi.vm.sade.kayttooikeus.dto.KutsunTila;
import fi.vm.sade.kayttooikeus.dto.enumeration.KutsuView;
import fi.vm.sade.kayttooikeus.enumeration.KayttooikeusRooli;
import fi.vm.sade.kayttooikeus.model.*;
import lombok.*;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KutsuCriteria extends BaseCriteria {
    private List<KutsunTila> tilas;
    private String kutsujaOid;
    private String sahkoposti;
    private Set<String> organisaatioOids;
    private Set<Long> kayttooikeusryhmaIds;
    private String kutsujaOrganisaatioOid;
    private Set<Long> kutsujaKayttooikeusryhmaIds;
    private String searchTerm;
    private Boolean subOrganisations;

    private KutsuView view;

    public BooleanBuilder onCondition(QKayttoOikeusRyhma kutsuKayttooikeusryhma, QKayttoOikeusRyhma kutsuttuKayttooikeusryhma) {
        QKutsu kutsu = QKutsu.kutsu;
        QKutsuOrganisaatio kutsuOrganisaatio = QKutsuOrganisaatio.kutsuOrganisaatio;
        QOrganisaatioHenkilo organisaatioHenkilo = QOrganisaatioHenkilo.organisaatioHenkilo;
        BooleanBuilder builder = new BooleanBuilder();
        if (used(tilas)) {
            builder.and(kutsu.tila.in(tilas));
        }
        if (used(kutsujaOid)) {
            builder.and(kutsu.kutsuja.eq(kutsujaOid));
        }
        if (used(sahkoposti)) {
            builder.and(kutsu.sahkoposti.eq(sahkoposti));
        }
        if (!CollectionUtils.isEmpty(this.organisaatioOids)) {
            builder.and(kutsuOrganisaatio.organisaatioOid.in(this.organisaatioOids));
        }
        if (StringUtils.hasLength(this.kutsujaOrganisaatioOid)) {
            builder.and(organisaatioHenkilo.organisaatioOid.eq(this.kutsujaOrganisaatioOid));
        }
        if (!CollectionUtils.isEmpty(this.kutsujaKayttooikeusryhmaIds)) {
            builder.and(kutsuttuKayttooikeusryhma.id.in(this.kutsujaKayttooikeusryhmaIds));
        }
        if (StringUtils.hasLength(this.searchTerm)) {
            Arrays.stream(this.searchTerm.split(" "))
                    .forEach(searchTerm -> builder.and(kutsu.etunimi.containsIgnoreCase(searchTerm)
                            .or(kutsu.sukunimi.containsIgnoreCase(searchTerm))));
        }
        if (KutsuView.ADMIN.equals(this.view)) {
            builder.and(kutsuKayttooikeusryhma.kayttoOikeus.any().rooli.eq(KayttooikeusRooli.VASTUUKAYTTAJAT.getName()));
        }
        if (!CollectionUtils.isEmpty(this.kayttooikeusryhmaIds)) {
            builder.and(kutsuKayttooikeusryhma.id.in(this.kayttooikeusryhmaIds));
        }

        return builder;
    }
}
