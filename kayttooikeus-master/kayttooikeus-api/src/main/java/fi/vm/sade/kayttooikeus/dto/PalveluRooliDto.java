package fi.vm.sade.kayttooikeus.dto;

import lombok.*;

import java.util.stream.Stream;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PalveluRooliDto implements LocalizableDto {
    private String palveluName;
    private TextGroupListDto palveluTexts;
    private String rooli;
    private TextGroupListDto rooliTexts;

    public void setPalveluTextsId(Long id) {
        this.palveluTexts = TextGroupListDto.localizeAsListLaterById(id);
    }

    public void setRooliTextsId(Long id) {
        this.rooliTexts = TextGroupListDto.localizeAsListLaterById(id);
    }
    
    @Override
    public Stream<Localizable> localizableTexts() {
        return LocalizableDto.of(palveluTexts, rooliTexts).localizableTexts();
    }
}
