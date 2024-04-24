package fi.vm.sade.kayttooikeus.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

public class TextGroupMapDto implements Serializable, Localizable, Comparable<TextGroupMapDto> {
    @Getter
    private final Long id;
    @Getter
    private final Map<String, String> texts;

    public TextGroupMapDto() {
        this((Long)null);
    }

    public TextGroupMapDto(Long id) {
        this.id = id;
        this.texts = new HashMap<>();
    }

    public TextGroupMapDto(Map<String, String> values) {
        this.id = null;
        this.texts = values;
    }

    @JsonCreator
    public TextGroupMapDto(@JsonProperty("id") Long id, @JsonProperty("values") Map<String, String> values) {
        this.id = id;
        this.texts = values == null ? null : values.entrySet().stream()
                .map(kv -> new SimpleEntry<>(kv.getKey().toLowerCase(), kv.getValue()))
                .collect(toMap(Entry::getKey, Entry::getValue));
    }

    @JsonValue
    public Map<String, String> asMap() {
        return texts;
    }
    
    @JsonIgnore
    public String get(String lang) {
        return texts.get(lang.toLowerCase());
    }

    @Override
    public Optional<String> getOrAny(String lang) {
        Optional<String> opt = Optional.ofNullable(get(lang));
        if (opt.isPresent()) {
            return opt;
        }
        return texts.values().stream().filter(Objects::nonNull).findFirst();
    }

    @Override
    public TextGroupMapDto put(String lang, String value) {
        this.texts.put(lang.toLowerCase(), value);
        return this;
    }

    public static TextGroupMapDto localizeAsMapLaterById(Long id) {
        return id == null ? null : new TextGroupMapDto(id, new HashMap<>());
    }
    
    @Override
    public int compareTo(@NotNull TextGroupMapDto o) {
        int result = Localizable.compareLangs(this, o, "fi");
        if (result != 0) {
            return result;
        }
        result = Localizable.compareLangs(this, o, "sv");
        if (result != 0) {
            return result;
        }
        result = Localizable.compareLangs(this, o, "en");
        if (result != 0) {
            return result;
        }
        return Localizable.comparePrimarlyByLang(this, o, "fi");
    }
}
