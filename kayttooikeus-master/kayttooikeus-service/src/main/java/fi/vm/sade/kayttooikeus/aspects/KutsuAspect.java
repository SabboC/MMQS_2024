package fi.vm.sade.kayttooikeus.aspects;

import fi.vm.sade.kayttooikeus.dto.KutsuCreateDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class KutsuAspect {

    private KutsuHelper kutsuHelper;

    @Autowired
    public KutsuAspect(KutsuHelper helper) {
        kutsuHelper = helper;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KutsuService.createKutsu(*))" +
            "&& args(dto)", argNames = "proceedingJoinPoint, dto")
    private Object logCreateKutsu(ProceedingJoinPoint proceedingJoinPoint, KutsuCreateDto dto) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kutsuHelper.logCreateKutsu(dto, result);
        return result;
    }

    @Around(value = "execution(public * fi.vm.sade.kayttooikeus.service.KutsuService.deleteKutsu(*))" +
            "&& args(id)", argNames = "proceedingJoinPoint, id")
    private Object logDeleteKutsu(ProceedingJoinPoint proceedingJoinPoint, Long id) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        kutsuHelper.logDeleteKutsu(id, result);
        return result;
    }

}
