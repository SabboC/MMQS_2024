package fi.vm.sade.kayttooikeus.service;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeudenTila;
import fi.vm.sade.kayttooikeus.model.Henkilo;
import fi.vm.sade.kayttooikeus.model.MyonnettyKayttoOikeusRyhmaTapahtuma;
import fi.vm.sade.kayttooikeus.model.OrganisaatioHenkilo;
import lombok.Data;

public interface MyonnettyKayttoOikeusService {

    /**
     * Poistaa vanhentuneet käyttöoikeudet henkilöiltä.
     *
     * @param details toiminnon lisätiedot
     */
    void poistaVanhentuneet(DeleteDetails details);

    /**
     * Poistaa myönnetyn käyttöoikeuden. Passivoi samalla organisaation henkilöltä jos oli viimeinen käyttöoikeus.
     * @param myonnettyKayttoOikeusRyhmaTapahtuma poistettava käyttöoikeus
     * @param details toiminnon lisätiedot
     */
    void poista(MyonnettyKayttoOikeusRyhmaTapahtuma myonnettyKayttoOikeusRyhmaTapahtuma, DeleteDetails details);

    /**
     * Poistaa henkilön käyttöoikeudet organisaatiosta ja passivoi organisaation henkilöltä.
     * @param organisaatioHenkilo passivoitava henkilön organisaatio
     * @param details toiminnon lisätiedot
     */
    void passivoi(OrganisaatioHenkilo organisaatioHenkilo, DeleteDetails details);

    @Data
    class DeleteDetails {
        private final Henkilo kasittelija;
        private final KayttoOikeudenTila tila;
        private final String syy;
    }

}
