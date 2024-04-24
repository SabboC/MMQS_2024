package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeusHistoriaDto;
import fi.vm.sade.kayttooikeus.dto.PalveluKayttoOikeusDto;
import fi.vm.sade.kayttooikeus.dto.PalveluRooliDto;
import fi.vm.sade.kayttooikeus.model.KayttoOikeus;
import fi.vm.sade.kayttooikeus.repositories.dto.ExpiringKayttoOikeusDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public interface KayttoOikeusRepository extends BaseRepository<KayttoOikeus> {
    boolean isHenkiloMyonnettyKayttoOikeusToPalveluInRole(String henkiloOid, String palvelu, String role);

    List<PalveluKayttoOikeusDto> listKayttoOikeusByPalvelu(String palveluName);

    List<KayttoOikeusHistoriaDto> listMyonnettyKayttoOikeusHistoriaForHenkilo(String henkiloOid);

    List<ExpiringKayttoOikeusDto> findSoonToBeExpiredTapahtumas(LocalDate now, Period...expireThresholds);

    List<PalveluRooliDto> findPalveluRoolitByKayttoOikeusRyhmaId(Long id);

    KayttoOikeus findByRooliAndPalvelu(String rooli, String palvelu);

    List<String> findHenkilosByRyhma(long id);
}
