package fi.vm.sade.kayttooikeus.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StrongIdentificationInternalDto {
    String authToken;
    String henkiloOid;
}
