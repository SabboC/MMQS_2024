package fi.vm.sade.kayttooikeus.controller;

import fi.vm.sade.kayttooikeus.AbstractApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractApplicationTest {
    @Autowired
    protected MockMvc mvc;
}
