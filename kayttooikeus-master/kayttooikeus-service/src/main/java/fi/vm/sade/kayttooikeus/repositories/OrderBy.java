package fi.vm.sade.kayttooikeus.repositories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.querydsl.core.types.Order.DESC;

public class OrderBy<T extends Enum<T>> {
    @Getter
    protected T by;
    @Getter
    protected Order direction;
    @JsonIgnore
    protected List<OrderSpecifier<?>> specifiers = new ArrayList<>();

    public OrderBy(T by, Order direction) {
        this.by = by;
        this.direction = direction;
    }
    
    public static <T extends Enum<T>> OrderBy<T> orderer(T by, Order direction) {
        return new OrderBy<T>(by, direction);
    }

    public OrderBy<T> byDefault(@NotNull T by, @NotNull Order direction) {
        if (this.by == null) {
            this.by = by;
        }
        if (this.direction == null) {
            this.direction = direction;   
        }
        return this;
    }
    
    public<E extends ComparableExpressionBase<? extends Comparable>> OrderBy<T> order(T when, E then) {
        return order(when, (Supplier<? extends ComparableExpressionBase<? extends Comparable>>) () -> then);
    }
    
    public<E extends ComparableExpressionBase<? extends Comparable>> OrderBy<T> order(E inAllCases) {
        return order(direction == DESC ? inAllCases.desc() : inAllCases.asc());
    }

    public OrderBy<T> order(OrderSpecifier<?> spec) {
        specifiers.add(spec);
        return this;
    }
    
    public OrderBy<T> order(T when, Supplier<? extends ComparableExpressionBase<? extends Comparable>> then) {
        if (when == by) {
            specifiers.add(direction == DESC ? then.get().desc() : then.get().asc());
        }
        return this;
    }
    
    public OrderSpecifier<?>[] get() {
        return specifiers.toArray(new OrderSpecifier<?>[specifiers.size()]);
    }
}
