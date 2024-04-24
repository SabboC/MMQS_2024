package fi.vm.sade.kayttooikeus.aspects;

import fi.vm.sade.kayttooikeus.repositories.dto.ExpiringKayttoOikeusDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class KayttoOikeusAspect {

    private KayttoOikeusHelper kayttoOikeusHelper;

    @Autowired
    public KayttoOikeusAspect(KayttoOikeusHelper helper) {
        kayttoOikeusHelper = helper;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.EmailService.sendExpirationReminder(..))" +
            "&& args(henkiloOid, tapahtumas)", argNames = "proceedingJoinPoint, henkiloOid, tapahtumas")
    private Object logSendKayttooikeusReminder(ProceedingJoinPoint proceedingJoinPoint, String henkiloOid, List<ExpiringKayttoOikeusDto> tapahtumas) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusHelper.logSendKayttooikeusReminder(henkiloOid, tapahtumas, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.MyonnettyKayttoOikeusService.poistaVanhentuneet(*))" +
            "&& args(kasittelijaOid)", argNames = "proceedingJoinPoint, kasittelijaOid")
    private Object logSendKayttooikeusReminder(ProceedingJoinPoint proceedingJoinPoint, String kasittelijaOid) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusHelper.logRemoveExpiredKayttooikeudet(kasittelijaOid, result);
        return result;
    }

}
