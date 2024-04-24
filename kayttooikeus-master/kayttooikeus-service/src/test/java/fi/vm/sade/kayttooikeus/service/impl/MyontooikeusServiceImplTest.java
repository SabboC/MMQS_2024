package fi.vm.sade.kayttooikeus.service.impl;

import fi.vm.sade.kayttooikeus.repositories.KayttoOikeusRyhmaMyontoViiteRepository;
import fi.vm.sade.kayttooikeus.repositories.criteria.MyontooikeusCriteria;
import fi.vm.sade.kayttooikeus.service.external.OrganisaatioClient;
import fi.vm.sade.kayttooikeus.util.OrganisaatioMyontoPredicate;
import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyontooikeusServiceImplTest {

    private MyontooikeusServiceImpl myontooikeusServiceImpl;

    private KayttoOikeusRyhmaMyontoViiteRepository kayttoOikeusRyhmaMyontoViiteRepositoryMock;
    private OrganisaatioClient organisaatioClientMock;

    @Before
    public void setup() {
        kayttoOikeusRyhmaMyontoViiteRepositoryMock = mock(KayttoOikeusRyhmaMyontoViiteRepository.class);
        organisaatioClientMock = mock(OrganisaatioClient.class);

        myontooikeusServiceImpl = new MyontooikeusServiceImpl(kayttoOikeusRyhmaMyontoViiteRepositoryMock, organisaatioClientMock);
    }

    @Test
    public void getMyontooikeudet() {
        Set<Long> org1oikeudet = Stream.of(1L, 2L, 3L).collect(toSet());
        Set<Long> org11oikeudet = Stream.of(1L, 3L, 5L).collect(toSet());
        Set<Long> kaikkiOikeudet = Stream.concat(org1oikeudet.stream(), org11oikeudet.stream()).collect(toSet());
        when(kayttoOikeusRyhmaMyontoViiteRepositoryMock.getSlaveIdsByMasterHenkiloOid(any(), any()))
                .thenReturn(Stream.of(entry("org1", org1oikeudet), entry("org11", org11oikeudet))
                        .collect(toMap(MapEntry::getKey, MapEntry::getValue)));
        when(organisaatioClientMock.listWithChildOids(eq("org1"), any()))
                .thenReturn(Stream.of("org1", "org11", "org111", "org12").collect(toSet()));
        when(organisaatioClientMock.listWithChildOids(eq("org11"), any()))
                .thenReturn(Stream.of("org11", "org111").collect(toSet()));

        Map<String, Set<Long>> myontooikeudet = myontooikeusServiceImpl.getMyontooikeudet("myontaja1",
                MyontooikeusCriteria.oletus(), new OrganisaatioMyontoPredicate(false));

        assertThat(myontooikeudet).hasSize(4).contains(
                entry("org1", org1oikeudet), entry("org11", kaikkiOikeudet), entry("org111", kaikkiOikeudet),
                entry("org12", org1oikeudet));
    }

}
