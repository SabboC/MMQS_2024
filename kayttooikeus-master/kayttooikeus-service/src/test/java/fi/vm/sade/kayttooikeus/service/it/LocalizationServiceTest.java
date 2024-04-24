package fi.vm.sade.kayttooikeus.service.it;


import fi.vm.sade.kayttooikeus.config.OrikaBeanMapper;
import fi.vm.sade.kayttooikeus.dto.Localizable;
import fi.vm.sade.kayttooikeus.dto.LocalizableDto;
import fi.vm.sade.kayttooikeus.dto.TextGroupDto;
import fi.vm.sade.kayttooikeus.model.TextGroup;
import fi.vm.sade.kayttooikeus.service.LocalizationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Stream;

import static fi.vm.sade.kayttooikeus.repositories.populate.TextGroupPopulator.text;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class LocalizationServiceTest extends AbstractServiceIntegrationTest {
    @Autowired
    private LocalizationService localizationService;

    @Test
    public void localizeSingle() {
        TextGroup g1 = populate(text("FI", "Testi1-fi")
                    .put("EN", "Test1-en")
                    .put("SV", "Test1-sv"));
        Localizable localizable = new TextGroupDto(g1.getId());
        LocalizableDto dto = () -> Stream.of(localizable);
        localizationService.localize(dto);
        assertEquals("Testi1-fi", localizable.get("fi"));
        assertEquals("Test1-sv", localizable.get("SV"));
    }

    @Test
    public void localizeMultiple() {
        TextGroup g1 = populate(text("FI", "Testi1-fi")
                .put("EN", "Test1-en")
                .put("SV", "Test1-sv")),
            g2 = populate(text("EN", "Test"));
        Localizable localizable1 = new TextGroupDto(g1.getId()),
                    localizable2 = new TextGroupDto(g2.getId());
        LocalizableDto dto = () -> Stream.of(localizable1, localizable2);
        localizationService.localize(dto);
        assertEquals("Testi1-fi", localizable1.get("fi"));
        assertEquals("Test", localizable2.get("en"));
    }

}
