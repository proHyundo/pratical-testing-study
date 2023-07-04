package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {
    
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType() {
        // given
        ProductType type = ProductType.HANDMADE;
        // when
        boolean result = ProductType.containsStockType(type);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType2() {
        // given
        ProductType type = ProductType.BOTTLE;
        // when
        boolean result = ProductType.containsStockType(type);
        // then
        assertThat(result).isTrue();
    }

    @CsvSource({
            "HANDMADE, false",
            "BOTTLE, true",
            "BAKERY, true"
    })
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @ParameterizedTest
    void containsStockType3(ProductType productType, boolean expected) {
        // given
        // when
        boolean result = ProductType.containsStockType(productType);
        // then
        assertThat(result).isEqualTo(expected);
    }

}