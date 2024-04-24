package fi.vm.sade.kayttooikeus.repositories.populate;

import fi.vm.sade.kayttooikeus.dto.OrganisaatioHenkiloTyyppi;
import fi.vm.sade.kayttooikeus.model.Henkilo;
import fi.vm.sade.kayttooikeus.model.OrganisaatioHenkilo;

import javax.persistence.EntityManager;

import java.time.LocalDate;

import static fi.vm.sade.kayttooikeus.repositories.populate.Populator.first;

public class OrganisaatioHenkiloPopulator implements Populator<OrganisaatioHenkilo> {
    private final Populator<Henkilo> henkilo;
    private final String organisaatioOid;
    private LocalDate voimassaAlku = LocalDate.now();
    private LocalDate voimassaAsti;
    private String tehtavanimike;
    private OrganisaatioHenkiloTyyppi tyyppi = OrganisaatioHenkiloTyyppi.VIRKAILIJA;
    private boolean passivoitu = false;

    public OrganisaatioHenkiloPopulator(String henkiloOid, String organisaatioOid) {
        this.henkilo = new HenkiloPopulator(henkiloOid);
        this.organisaatioOid = organisaatioOid;
    }

    public OrganisaatioHenkiloPopulator(Populator<Henkilo> henkilo, String organisaatioOid) {
        this.henkilo = henkilo;
        this.organisaatioOid = organisaatioOid;
    }

    public static OrganisaatioHenkiloPopulator organisaatioHenkilo(String henkiloOid, String organisaatioOid) {
        return new OrganisaatioHenkiloPopulator(henkiloOid, organisaatioOid);
    }
    
    public static OrganisaatioHenkiloPopulator organisaatioHenkilo(Populator<Henkilo> henkilo, String organisaatioOid) {
        return new OrganisaatioHenkiloPopulator(henkilo, organisaatioOid);
    }
    
    public OrganisaatioHenkiloPopulator voimassaAlkaen(LocalDate alkaen) {
        this.voimassaAlku = alkaen;
        return this;
    }

    public OrganisaatioHenkiloPopulator voimassaAsti(LocalDate asti) {
        this.voimassaAsti = asti;
        return this;
    }

    public OrganisaatioHenkiloPopulator tehtavanimike(String tehtavanimike) {
        this.tehtavanimike = tehtavanimike;
        return this;
    }
    
    public OrganisaatioHenkiloPopulator passivoitu() {
        this.passivoitu = true;
        return this;
    }

    public OrganisaatioHenkiloPopulator tyyppi(OrganisaatioHenkiloTyyppi tyyppi) {
        this.tyyppi = tyyppi;
        return this;
    }

    @Override
    public OrganisaatioHenkilo apply(EntityManager entityManager) {
        Henkilo henkilo = this.henkilo.apply(entityManager);
        OrganisaatioHenkilo found = first(entityManager.createQuery("select o from OrganisaatioHenkilo o " +
                    "where o.henkilo.oidHenkilo=:henkiloOid and o.organisaatioOid = :oOid")
                .setParameter("henkiloOid", henkilo.getOidHenkilo())
                .setParameter("oOid", organisaatioOid));
        if (found != null) {
            return found;
        }
        OrganisaatioHenkilo organisaatioHenkilo = new OrganisaatioHenkilo();
        organisaatioHenkilo.setHenkilo(henkilo);
        organisaatioHenkilo.setOrganisaatioOid(organisaatioOid);
        organisaatioHenkilo.setTehtavanimike(tehtavanimike);
        organisaatioHenkilo.setPassivoitu(passivoitu);
        organisaatioHenkilo.setVoimassaAlkuPvm(voimassaAlku);
        organisaatioHenkilo.setVoimassaLoppuPvm(voimassaAsti);
        organisaatioHenkilo.setOrganisaatioHenkiloTyyppi(tyyppi);
        entityManager.persist(organisaatioHenkilo);
        henkilo.getOrganisaatioHenkilos().add(organisaatioHenkilo);
        return organisaatioHenkilo;
    }
}
