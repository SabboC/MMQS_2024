package fi.vm.sade.kayttooikeus.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PalveluRooliModifyDto {

    @NotNull
    private String palveluName;
    @NotNull
    private String rooli;

}
