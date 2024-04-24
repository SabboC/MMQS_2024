package fi.vm.sade.kayttooikeus.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CasRedirectParametersResponse {
    private String authToken;
    private String service;
}
