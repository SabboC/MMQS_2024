package fi.vm.sade.kayttooikeus.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class KutsuOrganisaatioListDto implements Serializable {
    @JsonIgnore
    private Long kutsuId;
    private Long id;
    private String oid;
    private TextGroupMapDto nimi;
}
