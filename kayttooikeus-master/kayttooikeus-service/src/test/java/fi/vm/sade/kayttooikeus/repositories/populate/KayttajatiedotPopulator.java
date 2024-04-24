package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.dto.MfaProvider;
import fi.vm.sade.kayttooikeus.model.Henkilo;
import fi.vm.sade.kayttooikeus.model.Kayttajatiedot;

import javax.persistence.EntityManager;

public class KayttajatiedotPopulator implements Populator<Kayttajatiedot> {

    private final Populator<Henkilo> henkilo;
    private final String username;
    private final MfaProvider mfaProvider;

    public KayttajatiedotPopulator(Populator<Henkilo> henkilo, String username, MfaProvider mfaProvider) {
        this.henkilo = henkilo;
        this.username = username;
        this.mfaProvider = mfaProvider;
    }

    public static KayttajatiedotPopulator kayttajatiedot(Populator<Henkilo> henkilo, String username) {
        return new KayttajatiedotPopulator(henkilo, username, null);
    }

    public static KayttajatiedotPopulator kayttajatiedot(Populator<Henkilo> henkilo, String username, MfaProvider mfaProvider) {
        return new KayttajatiedotPopulator(henkilo, username, mfaProvider);
    }

    @Override
    public Kayttajatiedot apply(EntityManager t) {
        Henkilo henkilo = this.henkilo.apply(t);
        Kayttajatiedot kayttajatiedot = new Kayttajatiedot();
        kayttajatiedot.setHenkilo(henkilo);
        kayttajatiedot.setUsername(username);
        kayttajatiedot.setMfaProvider(mfaProvider);
        t.persist(kayttajatiedot);
        henkilo.setKayttajatiedot(kayttajatiedot);
        return kayttajatiedot;
    }

}
