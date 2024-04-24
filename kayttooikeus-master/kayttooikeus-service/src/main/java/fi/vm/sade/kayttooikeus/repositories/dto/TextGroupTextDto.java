package fi.vm.sade.kayttooikeus.repositories.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TextGroupTextDto implements Serializable {
    private Long textGroupId;
    private String lang;
    private String text;
}
