package fi.vm.sade.kayttooikeus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class CasGoogleAuthToken {
    @NotNull
    private List scratchCodes;
    @NotNull
    private Integer id;
    @NotNull
    private String secretKey;
    @NotNull
    private Long validationCode;
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String registrationDate;

    @JsonProperty("@class")
    public String getClassProperty() {
        return "org.apereo.cas.gauth.credential.GoogleAuthenticatorAccount";
    }
}
