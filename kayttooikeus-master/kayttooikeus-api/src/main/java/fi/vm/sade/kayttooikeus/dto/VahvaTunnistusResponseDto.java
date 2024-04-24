package fi.vm.sade.kayttooikeus.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VahvaTunnistusResponseDto {

    private String authToken;
    private String service;

    public Map<String, Object> asMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        Optional.ofNullable(authToken).ifPresent(value -> map.put("authToken", value));
        Optional.ofNullable(service).ifPresent(value -> map.put("service", value));
        return map;
    }

}
