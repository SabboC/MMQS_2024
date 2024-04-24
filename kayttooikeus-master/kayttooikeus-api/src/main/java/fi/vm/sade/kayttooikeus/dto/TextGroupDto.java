package fi.vm.sade.kayttooikeus.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class TextGroupDto implements Localizable, Serializable {
    private Long id;
    private List<TextDto> texts = new ArrayList<>();

    public TextGroupDto() {
    }

    public TextGroupDto(Long id) {
        this.id = id;
    }

    @Override
    public TextGroupDto put(String lang, String value) {
        Optional<TextDto> text = find(lang);
        if (text.isPresent()) {
            text.get().setText(value);
        } else {
            texts.add(new TextDto(lang, value));
        }
        return this;
    }

    private Optional<TextDto> find(String lang) {
        return texts.stream().filter(t -> t.getLang().equalsIgnoreCase(lang)
            && t.getText() != null).findFirst();
    }

    @Override
    public String get(String lang) {
        return find(lang).map(TextDto::getText).orElse(null);
    }

    @Override
    public Optional<String> getOrAny(String lang) {
        Optional<String> opt = find(lang).map(TextDto::getText);
        if (opt.isPresent()) {
            return opt;
        }
        return texts.stream().filter(t -> t.getText() != null).map(TextDto::getText).findFirst();
    }
    
    public static TextGroupDto localizeLaterById(Long id) {
        return id == null ? null : new TextGroupDto(id);
    }
}