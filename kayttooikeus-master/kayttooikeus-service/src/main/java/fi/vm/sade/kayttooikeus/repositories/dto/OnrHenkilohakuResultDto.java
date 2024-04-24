package fi.vm.sade.kayttooikeus.repositories.dto;

import fi.vm.sade.oppijanumerorekisteri.dto.HenkiloHakuDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OnrHenkilohakuResultDto {
    private int number;
    private int size;
    private int numberOfElements;
    private boolean last;
    private List<HenkiloHakuDto> results;
}
