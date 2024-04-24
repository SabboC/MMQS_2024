package fi.vm.sade.kayttooikeus.aspects;

import fi.vm.sade.kayttooikeus.dto.KayttoOikeusCreateDto;
import fi.vm.sade.kayttooikeus.dto.KayttoOikeusRyhmaModifyDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class KayttoOikeusRyhmaAspect {

    private KayttoOikeusRyhmaHelper kayttoOikeusRyhmaHelper;

    @Autowired
    public KayttoOikeusRyhmaAspect(KayttoOikeusRyhmaHelper helper) {
        kayttoOikeusRyhmaHelper = helper;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KayttoOikeusService.createKayttoOikeusRyhma(*))" +
            "&& args(uusiRyhma)", argNames = "proceedingJoinPoint, uusiRyhma")
    private Object logCreateKayttooikeusryhmä(ProceedingJoinPoint proceedingJoinPoint, KayttoOikeusRyhmaModifyDto uusiRyhma) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusRyhmaHelper.logCreateKayttooikeusryhma(uusiRyhma, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KayttoOikeusService.createKayttoOikeus(*))" +
            "&& args(kayttoOikeus)", argNames = "proceedingJoinPoint, kayttoOikeus")
    private Object logCreateKayttooikeus(ProceedingJoinPoint proceedingJoinPoint, KayttoOikeusCreateDto kayttoOikeus) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusRyhmaHelper.logCreateKayttooikeus(kayttoOikeus, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KayttoOikeusService.updateKayttoOikeusForKayttoOikeusRyhma(..))" +
            "&& args(id, ryhmaData)", argNames = "proceedingJoinPoint, id, ryhmaData")
    private Object logUpdateKayttoOikeusForKayttoOikeusRyhma(ProceedingJoinPoint proceedingJoinPoint, long id, KayttoOikeusRyhmaModifyDto ryhmaData) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusRyhmaHelper.logUpdateKayttoOikeusForKayttoOikeusRyhma(id, ryhmaData, result);
        return result;
    }

}
