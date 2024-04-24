package fi.vm.sade.kayttooikeus.service;

import java.util.List;

public interface HenkiloCacheService {
    boolean saveAll(long offset, long count, List<String> oidHenkiloList);
}
