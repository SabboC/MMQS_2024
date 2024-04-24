package fi.vm.sade.kayttooikeus.service.external;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrganisaatioHakutulos {
    private int numHits;
    private List<OrganisaatioPerustieto> organisaatiot = new ArrayList<>();
}
