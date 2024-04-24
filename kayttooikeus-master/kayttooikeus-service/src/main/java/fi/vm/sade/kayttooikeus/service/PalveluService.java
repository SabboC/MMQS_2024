package fi.vm.sade.kayttooikeus.service;

import fi.vm.sade.kayttooikeus.dto.PalveluDto;

import java.util.List;

public interface PalveluService {
    List<PalveluDto> listPalvelus();
}
