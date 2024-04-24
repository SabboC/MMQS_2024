package fi.vm.sade.kayttooikeus.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"kayttoOikeusRyhmaId"})
public class OrganisaatioViiteDto {
    private Long id;
    private String organisaatioTyyppi;
    @JsonIgnore
    private Long kayttoOikeusRyhmaId;
}
