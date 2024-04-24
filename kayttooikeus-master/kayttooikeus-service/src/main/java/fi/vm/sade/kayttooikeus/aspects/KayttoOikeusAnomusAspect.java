package fi.vm.sade.kayttooikeus.aspects;

import fi.vm.sade.kayttooikeus.dto.GrantKayttooikeusryhmaDto;
import fi.vm.sade.kayttooikeus.dto.KayttooikeusAnomusDto;
import fi.vm.sade.kayttooikeus.dto.UpdateHaettuKayttooikeusryhmaDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class KayttoOikeusAnomusAspect {

    private KayttoOikeusAnomusHelper kayttoOikeusAnomusHelper;

    @Autowired
    public KayttoOikeusAnomusAspect(KayttoOikeusAnomusHelper helper) {
        kayttoOikeusAnomusHelper = helper;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KayttooikeusAnomusService.updateHaettuKayttooikeusryhma(*))" +
            "&& args(updateHaettuKayttooikeusryhmaDto)", argNames = "proceedingJoinPoint, updateHaettuKayttooikeusryhmaDto")
    private Object logApproveOrRejectKayttooikeusAnomus(ProceedingJoinPoint proceedingJoinPoint, UpdateHaettuKayttooikeusryhmaDto updateHaettuKayttooikeusryhmaDto) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusAnomusHelper.logApproveOrRejectKayttooikeusAnomus(updateHaettuKayttooikeusryhmaDto, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KayttooikeusAnomusService.lahetaUusienAnomuksienIlmoitukset(*))" +
            "&& args(anottuPvm)", argNames = "proceedingJoinPoint, anottuPvm")
    private Object logSendKayttooikeusAnomusNotification(ProceedingJoinPoint proceedingJoinPoint, LocalDate anottuPvm) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusAnomusHelper.logSendKayttooikeusAnomusNotification(anottuPvm, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KayttooikeusAnomusService.cancelKayttooikeusAnomus(*))" +
            "&& args(kayttooikeusRyhmaId)", argNames = "proceedingJoinPoint, kayttooikeusRyhmaId")
    private Object logCancelKayttooikeusAnomus(ProceedingJoinPoint proceedingJoinPoint, Long kayttooikeusRyhmaId) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusAnomusHelper.logCancelKayttooikeusAnomus(kayttooikeusRyhmaId, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KayttooikeusAnomusService.createKayttooikeusAnomus(..))" +
            "&& args(anojaOid, kayttooikeusAnomusDto)", argNames = "proceedingJoinPoint, anojaOid, kayttooikeusAnomusDto")
    private Object logCreateKayttooikeusAnomus(ProceedingJoinPoint proceedingJoinPoint, String anojaOid, KayttooikeusAnomusDto kayttooikeusAnomusDto) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusAnomusHelper.logCreateKayttooikeusAnomus(anojaOid, kayttooikeusAnomusDto, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KayttooikeusAnomusService.grantKayttooikeusryhma(..))" +
            "&& args(anojaOid, organisaatioOid, updateHaettuKayttooikeusryhmaDtoList)", argNames = "proceedingJoinPoint, anojaOid, organisaatioOid, updateHaettuKayttooikeusryhmaDtoList")
    private Object logGrantKayttooikeusryhma(ProceedingJoinPoint proceedingJoinPoint, String anojaOid, String organisaatioOid, List<GrantKayttooikeusryhmaDto> updateHaettuKayttooikeusryhmaDtoList) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kayttoOikeusAnomusHelper.logGrantKayttooikeusryhma(anojaOid, organisaatioOid, updateHaettuKayttooikeusryhmaDtoList, result);
        return result;
    }
}
