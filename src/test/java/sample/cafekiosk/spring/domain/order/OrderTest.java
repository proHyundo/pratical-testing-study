package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

class OrderTest {

    @DisplayName("주문 생성 시, 주문 상태는 INIT 이다.")
    @Test
    void orderStateInit() {
        // given
        List<Product> products = List.of(
                createProduct("001", 4000),
                createProduct("002", 4500)
        );
        // when
        Order order = Order.create(products, LocalDateTime.now());
        // then
        assertThat(order.getStatus()).isEqualByComparingTo(OrderStatus.INIT);
        // isEqualByComparingTo : enum 타입의 값 비교
    }

    @DisplayName("주문 생성 시, 상품 리스트에서 주문 총 금액을 계산")
    @Test
    void calculateTotalPrice() {
        // given
        List<Product> products = List.of(
                createProduct("001", 4000),
                createProduct("002", 4500)
        );
        // when
        Order order = Order.create(products, LocalDateTime.now());
        // then
        assertThat(order.getTotalPrice()).isEqualTo(8500);
    }

    @DisplayName("주문 생성 시, 등록 시간을 기록한다.")
    @Test
    void orderRegisteredTime() {
        // given
        List<Product> products = List.of(
                createProduct("001", 4000),
                createProduct("002", 4500)
        );
        LocalDateTime registeredTime = LocalDateTime.now(); // 검증할 대상이 시간이므로, 변수에 저장한다.
        // when
        Order order = Order.create(products, registeredTime);
        // then : 등록 시간이 정상적으로 기록되었는지 검증
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredTime);
        // isEqualByComparingTo : enum 타입의 값 비교
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(ProductType.HANDMADE)
                .sellingStatus(SELLING)
                .name("메뉴명")
                .price(price)
                .build();
    }

}