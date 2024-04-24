package fi.vm.sade.kayttooikeus.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrganisaatioAspect {

    private OrganisaatioHelper organisaatioHelper;

    @Autowired
    public OrganisaatioAspect(OrganisaatioHelper helper) {
        organisaatioHelper = helper;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.OrganisaatioService.updateOrganisaatioCache())" +
            "&& args()", argNames = "proceedingJoinPoint")
    private Object logUpdateOrganisaatioCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        organisaatioHelper.logUpdateOrganisationCache();
        return result;
    }

}
