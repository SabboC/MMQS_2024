package fi.vm.sade.kayttooikeus.service.impl;

import fi.vm.sade.kayttooikeus.config.OrikaBeanMapper;
import fi.vm.sade.kayttooikeus.config.properties.CommonProperties;
import fi.vm.sade.kayttooikeus.dto.*;
import fi.vm.sade.kayttooikeus.model.Henkilo;
import fi.vm.sade.kayttooikeus.model.Kayttajatiedot;
import fi.vm.sade.kayttooikeus.repositories.HenkiloDataRepository;
import fi.vm.sade.kayttooikeus.repositories.HenkiloHibernateRepository;
import fi.vm.sade.kayttooikeus.repositories.OrganisaatioHenkiloRepository;
import fi.vm.sade.kayttooikeus.repositories.criteria.OrganisaatioHenkiloCriteria;
import fi.vm.sade.kayttooikeus.service.*;
import fi.vm.sade.kayttooikeus.service.external.OppijanumerorekisteriClient;
import fi.vm.sade.oppijanumerorekisteri.dto.HenkiloCreateDto;
import fi.vm.sade.oppijanumerorekisteri.dto.HenkiloHakuCriteria;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class VirkailijaServiceImpl implements VirkailijaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VirkailijaServiceImpl.class);

    private final PermissionCheckerService permissionCheckerService;
    private final HenkiloHibernateRepository henkiloHibernateRepository;
    private final OrganisaatioHenkiloRepository organisaatioHenkiloRepository;
    private final CommonProperties commonProperties;
    private final KayttajatiedotService kayttajatiedotService;
    private final CryptoService cryptoService;
    private final OppijanumerorekisteriClient oppijanumerorekisteriClient;
    private final HenkiloDataRepository henkiloRepository;
    private final OrikaBeanMapper mapper;

    @Override
    public String create(VirkailijaCreateDto createDto) {
        String kayttajatunnus = createDto.getKayttajatunnus();
        String salasana = createDto.getSalasana();

        // validointi (suoritettava ennen kuin oid luodaan oppijanumerorekisteriin)
        kayttajatiedotService.throwIfUsernameIsNotValid(kayttajatunnus);
        kayttajatiedotService.throwIfUsernameExists(kayttajatunnus);
        cryptoService.throwIfNotStrongPassword(salasana);

        // luodaan oid oppijanumerorekisteriin
        HenkiloCreateDto henkiloCreateDto = mapper.map(createDto, HenkiloCreateDto.class);
        String oid = oppijanumerorekisteriClient.createHenkilo(henkiloCreateDto);

        // tallennetaan virkailijaksi käyttöoikeuspalveluun
        Henkilo entity = henkiloRepository.findByOidHenkilo(oid).orElseGet(() -> new Henkilo(oid));
        mapper.map(createDto, entity);
        entity.setKayttajaTyyppi(KayttajaTyyppi.VIRKAILIJA);
        Kayttajatiedot kayttajatiedot = new Kayttajatiedot();
        kayttajatiedot.setUsername(kayttajatunnus);
        String salt = cryptoService.generateSalt();
        String hash = cryptoService.getSaltedHash(salasana, salt);
        kayttajatiedot.setSalt(salt);
        kayttajatiedot.setPassword(hash);
        kayttajatiedot.setHenkilo(entity);
        entity.setKayttajatiedot(kayttajatiedot);
        henkiloRepository.save(entity);

        return oid;
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<KayttajaReadDto> list(VirkailijaCriteriaDto criteria) {
        LOGGER.info("Haetaan käyttäjät {}", criteria);

        if (criteria.getOrganisaatioOids() == null && criteria.getKayttooikeudet() == null && criteria.getKayttoOikeusRyhmaNimet() == null) {
            throw new IllegalArgumentException("Pakollinen hakuehto (organisaatioOids, kayttooikeudet tai kayttoOikeusRyhmaNimet) puuttuu");
        }

        Set<String> henkiloOids = getHenkiloOids(criteria);
        if (henkiloOids.isEmpty()) {
            return emptyList();
        }

        HenkiloHakuCriteria oppijanumerorekisteriCriteria = mapper.map(criteria, HenkiloHakuCriteria.class);
        oppijanumerorekisteriCriteria.setHenkiloOids(henkiloOids);
        return oppijanumerorekisteriClient.listYhteystiedot(oppijanumerorekisteriCriteria).stream()
                .map(henkilo -> mapper.map(henkilo, KayttajaReadDto.class))
                .collect(toList());
    }

    private Set<String> getHenkiloOids(VirkailijaCriteriaDto kayttajaCriteria) {
        OrganisaatioHenkiloCriteria organisaatioHenkiloCriteria = mapper.map(kayttajaCriteria, OrganisaatioHenkiloCriteria.class);

        String kayttajaOid = permissionCheckerService.getCurrentUserOid();
        List<String> organisaatioOids = organisaatioHenkiloRepository.findUsersOrganisaatioHenkilosByPalveluRoolis(
                kayttajaOid, PalveluRooliGroup.KAYTTAJAHAKU);
        if (!organisaatioOids.contains(commonProperties.getRootOrganizationOid())) {
            organisaatioHenkiloCriteria.setOrRetainOrganisaatioOids(organisaatioOids);
        }

        return henkiloHibernateRepository.findOidsBy(organisaatioHenkiloCriteria);
    }
}
