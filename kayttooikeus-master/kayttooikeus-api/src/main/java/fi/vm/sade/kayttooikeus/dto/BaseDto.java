package fi.vm.sade.kayttooikeus.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import static java.util.Collections.unmodifiableMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.Objects.requireNonNull;

public class BaseDto {

    private final Map<String, Object> others = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getOthers() {
        return unmodifiableMap(others);
    }

    @JsonAnySetter
    public void setOther(String name, Object value) {
        others.put(requireNonNull(name), value);
    }

    public void removeOther(String name) {
        others.remove(requireNonNull(name));
    }

}
