package fi.vm.sade.kayttooikeus.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class LocalizableOrganisaatio {
    protected TextGroupMapDto nimi;
    protected String organisaatioOid;
}
