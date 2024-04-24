package fi.vm.sade.kayttooikeus.service.impl;

import fi.vm.sade.kayttooikeus.config.OrikaBeanMapper;
import fi.vm.sade.kayttooikeus.dto.PalveluDto;
import fi.vm.sade.kayttooikeus.repositories.PalveluRepository;
import fi.vm.sade.kayttooikeus.service.LocalizationService;
import fi.vm.sade.kayttooikeus.service.PalveluService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class PalveluServiceImpl implements PalveluService {
    private PalveluRepository palveluRepository;
    private LocalizationService localizationService;

    @Autowired
    public PalveluServiceImpl(PalveluRepository palveluRepository, LocalizationService localizationService) {
        this.palveluRepository = palveluRepository;
        this.localizationService = localizationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PalveluDto> listPalvelus() {
        List<PalveluDto> palvelus = localizationService.localize(palveluRepository.findAll());
        Map<Long,PalveluDto> palvelusById = palvelus.stream().collect(toMap(PalveluDto::getId, dto -> dto));
        palvelus.stream().filter(dto -> dto.getKokoelma() != null)
                .forEach(dto -> dto.setKokoelma(palvelusById.get(dto.getKokoelma().getId())));
        return palvelus;
    }
}
