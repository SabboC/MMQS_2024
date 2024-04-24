package fi.vm.sade.kayttooikeus.enumeration;

import com.querydsl.core.types.OrderSpecifier;
import fi.vm.sade.kayttooikeus.model.QAnomus;
import lombok.Getter;

@Getter
public enum OrderByAnomus {
    ANOTTU_PVM_ASC("ANOTTU_PVM_ASC", QAnomus.anomus.anottuPvm.asc()),
    ANOTTU_PVM_DESC("ANOTTU_PVM_DESC", QAnomus.anomus.anottuPvm.desc()),
    ;

    private final String entry;
    private final OrderSpecifier value;

    OrderByAnomus(String entry, OrderSpecifier value) {
        this.entry = entry;
        this.value = value;
    }
}
