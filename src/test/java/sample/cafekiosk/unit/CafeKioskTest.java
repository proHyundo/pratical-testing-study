package sample.cafekiosk.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    @DisplayName("음료 추가시 주문목록에 추가.") // 명사 나열 보다 문장으로 표현. 테스트 행위 결과도 기술.
    @Test
    void add() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();

        // when
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());

        // then
        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void addSeveralBeverage() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        // when
        cafeKiosk.add(americano, 2); // 경계값 테스트

        // then
        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(cafeKiosk.getBeverages().get(1).getName()).isEqualTo("아메리카노");
    }

    // 해피케이스 ↔ 예외케이스
    // 예외케이스는 경계값 테스트를 권장.
    @Test
    void addZeroBeverageException(){
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        // when
        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("음료는 1잔 이상이어야 합니다.");
    }

    @Test
    void remove(){
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        // when
        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void clear(){
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());
        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        // when
        cafeKiosk.clear();
        // then
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @DisplayName("주문목록의 상품 가격 합계 계산.")
    @Test
    void calculateTotalPrice(){
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 2);
        // when
        int totalPrice = cafeKiosk.calculateTotalPrice();
        // then
        assertThat(totalPrice).isEqualTo(8000);
    }


    @Test
    void createOrderWithCurrentTime(){
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);
        // when
        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 6, 21, 11, 0, 0));
        // then
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    // 테스트 하기 어려운 부분(영역)은 분리하기.
    // 외부로 분리할 수록 테스트 가능한 코드(계층)가 늘어난다.
    @DisplayName("영업시간 외 주문 불가능.")
    @Test
    void createOrderWithOutSideOpenTimeException(){
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);
        // when
        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 6, 21, 22, 0, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 가능 시간이 아닙니다.");
    }

}