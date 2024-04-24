package fi.vm.sade.kayttooikeus.service.impl;

import fi.vm.sade.kayttooikeus.service.TimeService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class TimeServiceImpl implements TimeService {

    @Override
    public OffsetDateTime getOffsetDateTimeNow() {
        return OffsetDateTime.now();
    }

}
