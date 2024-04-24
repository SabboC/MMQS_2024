package fi.vm.sade.kayttooikeus.service.impl;

import fi.vm.sade.kayttooikeus.model.*;
import fi.vm.sade.kayttooikeus.repositories.KayttoOikeusRyhmaTapahtumaHistoriaDataRepository;
import fi.vm.sade.kayttooikeus.repositories.MyonnettyKayttoOikeusRyhmaTapahtumaRepository;
import fi.vm.sade.kayttooikeus.service.MyonnettyKayttoOikeusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyonnettyKayttoOikeusServiceImpl implements MyonnettyKayttoOikeusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyonnettyKayttoOikeusServiceImpl.class);

    private final MyonnettyKayttoOikeusRyhmaTapahtumaRepository myonnettyKayttoOikeusRyhmaTapahtumaRepository;
    private final KayttoOikeusRyhmaTapahtumaHistoriaDataRepository kayttoOikeusRyhmaTapahtumaHistoriaDataRepository;

    @Override
    public void poistaVanhentuneet(DeleteDetails details) {
        LOGGER.info("Vanhentuneiden käyttöoikeuksien poisto aloitetaan");
        List<MyonnettyKayttoOikeusRyhmaTapahtuma> kayttoOikeudet = myonnettyKayttoOikeusRyhmaTapahtumaRepository
                .findByVoimassaLoppuPvmBefore(LocalDate.now());

        for (MyonnettyKayttoOikeusRyhmaTapahtuma kayttoOikeus : kayttoOikeudet) {;
            OrganisaatioHenkilo organisaatioHenkilo = kayttoOikeus.getOrganisaatioHenkilo();
            Henkilo henkilo = organisaatioHenkilo.getHenkilo();
            henkilo.getHenkiloVarmennettavas().stream()
                    .filter(HenkiloVarmentaja::isTila)
                    .forEach(henkiloVarmentajaSuhde -> {
                        henkiloVarmentajaSuhde.setTila(false);
                        this.getFirstStillActiveKayttooikeusForSameOrganisation(kayttoOikeus, kayttoOikeudet)
                                .ifPresent(myonnettyKayttooikeus -> henkiloVarmentajaSuhde.setTila(true));
                    });

            poista(kayttoOikeus, details);
        }
        LOGGER.info("Vanhentuneiden käyttöoikeuksien poisto päättyy: poistettiin {} käyttöoikeutta", kayttoOikeudet.size());
    }

    // Tutkii löytyykö varmentavan henkilön käyttöoikeuksista yhä oikeuksia poistuvan oikeuden organisaatioon
    private Optional<MyonnettyKayttoOikeusRyhmaTapahtuma> getFirstStillActiveKayttooikeusForSameOrganisation(
            MyonnettyKayttoOikeusRyhmaTapahtuma poistuvaKayttooikeus,
            List<MyonnettyKayttoOikeusRyhmaTapahtuma> poistuvatKayttoOikeudet) {
        String varmentavaHenkiloOid = poistuvaKayttooikeus.getOrganisaatioHenkilo().getHenkilo().getOidHenkilo();
        String poistettavaKayttooikeusOrganisaatioOid = poistuvaKayttooikeus.getOrganisaatioHenkilo().getOrganisaatioOid();
        return this.myonnettyKayttoOikeusRyhmaTapahtumaRepository
                .findByOrganisaatioHenkiloHenkiloOidHenkilo(varmentavaHenkiloOid).stream()
                .filter(myonnettyKayttooikeus -> myonnettyKayttooikeus.getOrganisaatioHenkilo().isAktiivinen())
                .filter(myonnettyKayttooikeus -> poistettavaKayttooikeusOrganisaatioOid
                        .equals(myonnettyKayttooikeus.getOrganisaatioHenkilo().getOrganisaatioOid()))
                .filter(myonnettyKayttooikeus -> poistuvatKayttoOikeudet.stream()
                        .noneMatch(kayttoOikeus -> kayttoOikeus.getId().equals(myonnettyKayttooikeus.getId())))
                .findFirst();
    }

    @Override
    public void poista(MyonnettyKayttoOikeusRyhmaTapahtuma myonnettyKayttoOikeusRyhmaTapahtuma, DeleteDetails details) {
        OrganisaatioHenkilo organisaatioHenkilo = myonnettyKayttoOikeusRyhmaTapahtuma.getOrganisaatioHenkilo();
        organisaatioHenkilo.getKayttoOikeusRyhmaHistorias().add(poistaInternal(myonnettyKayttoOikeusRyhmaTapahtuma, details));
        organisaatioHenkilo.getMyonnettyKayttoOikeusRyhmas().remove(myonnettyKayttoOikeusRyhmaTapahtuma);
        if (organisaatioHenkilo.getMyonnettyKayttoOikeusRyhmas().isEmpty()) {
            organisaatioHenkilo.setPassivoitu(true);
        }
    }

    @Override
    public void passivoi(OrganisaatioHenkilo organisaatioHenkilo, DeleteDetails details) {
        Iterator<MyonnettyKayttoOikeusRyhmaTapahtuma> iterator = organisaatioHenkilo.getMyonnettyKayttoOikeusRyhmas().iterator();
        while (iterator.hasNext()) {
            organisaatioHenkilo.getKayttoOikeusRyhmaHistorias().add(poistaInternal(iterator.next(), details));
            iterator.remove();
        }
        organisaatioHenkilo.setPassivoitu(true);
    }

    private KayttoOikeusRyhmaTapahtumaHistoria poistaInternal(MyonnettyKayttoOikeusRyhmaTapahtuma myonnettyKayttoOikeusRyhmaTapahtuma, DeleteDetails details) {
        myonnettyKayttoOikeusRyhmaTapahtumaRepository.delete(myonnettyKayttoOikeusRyhmaTapahtuma);
        return kayttoOikeusRyhmaTapahtumaHistoriaDataRepository.save(myonnettyKayttoOikeusRyhmaTapahtuma.toHistoria(
                details.getKasittelija(), details.getTila(), LocalDateTime.now(), details.getSyy()));
    }

}
