package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("특정 일자 로부터 1일치 결제완료 상태의 주문들을 가져온다.")
    @Test
    void findOrdersByDateAndOrderStatus() {
        // given
        LocalDateTime orderDate1 = LocalDateTime.of(2023, 6, 27, 13, 0, 0);
        LocalDateTime orderDate2 = LocalDateTime.of(2023, 6, 28, 12, 0, 1);

        Product product1 = Product.builder().productNumber("001").price(4000).build();
        Product product2 = Product.builder().productNumber("002").price(4500).build();

        productRepository.saveAll(List.of(product1, product2));

        Order order1 = Order.create(List.of(product1, product2), orderDate1);
        Order order2 = Order.create(List.of(product1, product2), orderDate2);

        Order orderResult1 = orderRepository.save(order1);
        Order orderResult2 = orderRepository.save(order2);

        orderRepository.save(orderResult1.paymentCompleted());
        orderRepository.save(orderResult2.paymentCompleted());
        // when
        List<Order> ordersBy = orderRepository.findOrdersBy(orderDate1, orderDate1.plusDays(1), OrderStatus.PAYMENT_COMPLETED);
        System.out.println("ordersBy = " + ordersBy.get(0));
        System.out.println("ordersBy.get(0).getRegisteredDateTime() = " + ordersBy.get(0).getRegisteredDateTime());
        System.out.println("ordersBy.get(0).getOrderProducts() = " + ordersBy.get(0).getOrderProducts().toString());

        // then
        assertThat(ordersBy).hasSize(2)
                .extracting("registeredDateTime", "status")
                .containsExactlyInAnyOrder(
                        tuple(orderDate1, OrderStatus.PAYMENT_COMPLETED),
                        tuple(orderDate2, OrderStatus.PAYMENT_COMPLETED)
                );
    }
}