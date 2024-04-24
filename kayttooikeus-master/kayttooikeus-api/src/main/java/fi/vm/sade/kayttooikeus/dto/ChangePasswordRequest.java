package fi.vm.sade.kayttooikeus.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotEmpty
    public String loginToken;
    @NotEmpty
    public String currentPassword;
    @NotEmpty
    public String newPassword;
}
