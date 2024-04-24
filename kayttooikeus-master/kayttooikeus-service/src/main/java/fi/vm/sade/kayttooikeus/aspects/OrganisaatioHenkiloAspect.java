package fi.vm.sade.kayttooikeus.aspects;

import fi.vm.sade.kayttooikeus.dto.OrganisaatioHenkiloCreateDto;
import fi.vm.sade.kayttooikeus.dto.OrganisaatioHenkiloUpdateDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class OrganisaatioHenkiloAspect {

    private OrganisaatioHenkiloHelper organisaatioHenkiloHelper;

    @Autowired
    public OrganisaatioHenkiloAspect(OrganisaatioHenkiloHelper helper) {
        organisaatioHenkiloHelper = helper;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.OrganisaatioHenkiloService.passivoiHenkiloOrganisation(..))" +
            "&& args(oidHenkilo, henkiloOrganisationOid)", argNames = "proceedingJoinPoint, oidHenkilo, henkiloOrganisationOid")
    private Object logPassivoiHenkiloOrganisaatio(ProceedingJoinPoint proceedingJoinPoint, String oidHenkilo, String henkiloOrganisationOid) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        organisaatioHenkiloHelper.logPassivoiOrganisaatioHenkilo(oidHenkilo, henkiloOrganisationOid, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.OrganisaatioHenkiloService.createOrUpdateOrganisaatioHenkilos(..))" +
            "&& args(henkiloOid, organisaatioHenkiloDtoList)", argNames = "proceedingJoinPoint, henkiloOid, organisaatioHenkiloDtoList")
    private Object logCreateOrUpdateHenkilos(ProceedingJoinPoint proceedingJoinPoint, String henkiloOid,
                                        List<OrganisaatioHenkiloUpdateDto> organisaatioHenkiloDtoList) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        organisaatioHenkiloHelper.logCreateOrUpdateOrganisaatioHenkilo(henkiloOid, organisaatioHenkiloDtoList, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.OrganisaatioHenkiloService.addOrganisaatioHenkilot(..))" +
            "&& args(henkiloOid, organisaatioHenkilot)", argNames = "proceedingJoinPoint, henkiloOid, organisaatioHenkilot")
    private Object logFindOrCreateOrganisaatioHenkilo(ProceedingJoinPoint proceedingJoinPoint, String henkiloOid, List<OrganisaatioHenkiloCreateDto> organisaatioHenkilot) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        organisaatioHenkiloHelper.logFindOrCreateOrganisaatioHenkilot(henkiloOid, organisaatioHenkilot, result);
        return result;
    }

}
