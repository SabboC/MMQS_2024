package fi.vm.sade.kayttooikeus.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TextDto implements Serializable {
    @JsonIgnore
    private Long id;
    private String text;
    private String lang;

    public TextDto() {
    }

    public TextDto(String lang, String text) {
        this.lang = lang;
        this.text = text;
    }
}
