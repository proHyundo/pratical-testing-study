package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @DisplayName("보유한 재고보다 요청된 수량이 더 많은지 확인한다.")
    @Test
    void isQuantityLessThan() {
        // given
        Stock stock1 = Stock.create("001", 1);
        int quantity = 2;
        // when
        boolean result = stock1.isQuantityLessThan(quantity);
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("보유한 재고보다 요청된 수량이 더 적은지 확인한다.")
    @Test
    void isQuantityLessThan2() {
        // given
        Stock stock1 = Stock.create("001", 2);
        int quantity = 1;
        // when
        boolean result = stock1.isQuantityLessThan(quantity);
        // then
        assertThat(result).isFalse();
    }
    
    @DisplayName("재고를 주어진 개수만큼 차감한다.")
    @Test
    void deductQuantity() {
        // given
        Stock stock1 = Stock.create("001", 1);
        int quantity = 1;
        // when
        stock1.deductQuantity(quantity);
        // then
        assertThat(stock1.getQuantity()).isEqualTo(0);
        assertThat(stock1.getQuantity()).isZero();
    }

    @DisplayName("재고보다 많은 개수만큼 차감 시 예외가 발생한다.")
    @Test
    void deductQuantity2() {
        // given
        Stock stock1 = Stock.create("001", 1);
        int quantity = 2;
        // when
        // then
        assertThatThrownBy(() -> stock1.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("차감할 재고 수량이 없습니다.");
    }
}