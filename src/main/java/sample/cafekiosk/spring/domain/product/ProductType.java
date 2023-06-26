package sample.cafekiosk.spring.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    HANDMADE("제조 음료"),
    BOTTLE("병음료"),
    BAKERY("베이커리"),
    ;

    private final String text;

    public static boolean containsStockType(ProductType type) {
        return List.of(BAKERY, BOTTLE).contains(type);
    }
}
