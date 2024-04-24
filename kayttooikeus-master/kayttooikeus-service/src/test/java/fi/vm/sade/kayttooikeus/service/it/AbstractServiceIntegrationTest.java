package fi.vm.sade.kayttooikeus.service.it;

import fi.vm.sade.kayttooikeus.repositories.populate.Populator;
import fi.vm.sade.kayttooikeus.service.AbstractServiceTest;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public abstract class AbstractServiceIntegrationTest extends AbstractServiceTest {
    @PersistenceContext
    protected EntityManager em;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }

    protected<T> T populate(Populator<T> populator) {
        T entity = populator.apply(em);
        em.flush();
        return entity;
    }
}
