package fi.vm.sade.kayttooikeus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AnomusKasiteltyRecipientDto {
    private String nimi;
    private KayttoOikeudenTila tila;
    private String hylkaysperuste;

    public AnomusKasiteltyRecipientDto(String nimi, KayttoOikeudenTila tila) {
        this.nimi = nimi;
        this.tila = tila;
        hylkaysperuste = null;
    }
}
