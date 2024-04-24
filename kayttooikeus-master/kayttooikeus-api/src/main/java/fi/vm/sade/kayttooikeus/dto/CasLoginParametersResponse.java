package fi.vm.sade.kayttooikeus.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CasLoginParametersResponse {
    private String service;
}
