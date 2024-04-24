package fi.vm.sade.kayttooikeus.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MfaTriggerDto {
    @NotEmpty
    private String principalId;
    @NotEmpty
    private String serviceId;
}
