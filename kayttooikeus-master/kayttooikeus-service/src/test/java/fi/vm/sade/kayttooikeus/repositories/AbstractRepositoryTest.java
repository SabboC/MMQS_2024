package fi.vm.sade.kayttooikeus.repositories;

import fi.vm.sade.kayttooikeus.AbstractApplicationTest;
import fi.vm.sade.kayttooikeus.repositories.populate.Populator;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public abstract class AbstractRepositoryTest extends AbstractApplicationTest {
    
    @BeforeClass
    public static void beforeClass() {
        System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    }
    
    @PersistenceContext
    protected EntityManager em;
    
    protected<T> T populate(Populator<T> populator) {
        T entity = populator.apply(em);
        em.flush();
        return entity;
    }
}
