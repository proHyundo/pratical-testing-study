package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private StockRepository stockRepository;

    @AfterEach    // @Transactional 을 임시로 붙여 @AfterEach 비활성화 했음.
    // Test 단계에서는 @Transactional 을 붙이지 않는 것이 추후 프로덕션 코드의 트랜잭션이 미적용된 것을 방지할 수 있다.
    // 롤백기능이 편리하기에 적절히 사용하면 좋지만, 트랜잭션의 범위를 명확하게 인지하고 있어야 한다.
    void testTearDown() {
        // deleteAllInBatch : 모든 엔티티를 삭제한다.
        // deleteAll : 모든 엔티티를 삭제하고, 트랜잭션을 커밋한다.
        // 차이점 : deleteAllInBatch는 트랜잭션을 사용하지 않기 때문에, 삭제 쿼리를 바로 실행한다.
        // @DataJpaTest는 @Transactional이 기본으로 설정되어 있어, tearDown이 필요 없다.
        // @SpringBootTest는 @Transactional이 기본으로 설정되어 있지 않아, tearDown이 필요하다.
            // @Transactional 을 추가적으로 설정하면 해결이 되지만, ????
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = createProduct("001", HANDMADE, 4000);
        Product product2 = createProduct("002", HANDMADE, 4500);
        Product product3 = createProduct("003", HANDMADE, 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();
        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);
        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactlyInAnyOrder(registeredDateTime, 8500);
        assertThat(orderResponse.getProducts())
                .hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("002", 4500)
                );
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = createProduct("001", HANDMADE, 4000);
        Product product2 = createProduct("002", HANDMADE, 4500);
        Product product3 = createProduct("003", HANDMADE, 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();
        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactlyInAnyOrder(registeredDateTime, 8000);
        assertThat(orderResponse.getProducts())
                .hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000)
                );
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = createProduct("001", BOTTLE, 4_000);
        Product product2 = createProduct("002", BAKERY, 4_500);
        Product product3 = createProduct("003", HANDMADE, 7_000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();
        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);
        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactlyInAnyOrder(registeredDateTime, 19_500);
        assertThat(orderResponse.getProducts())
                .hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000),
                        tuple("002", 4500),
                        tuple("003", 7000)
                );
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks)
                .hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithStock2() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = createProduct("001", BOTTLE, 4_000);
        Product product2 = createProduct("002", BAKERY, 4_500);
        Product product3 = createProduct("003", HANDMADE, 7_000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "001", "001"))
                .build();
        // when // then
        assertThatThrownBy( () -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");


    }

    private Product createProduct(String productNumber, ProductType type, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(SELLING)
                .name("메뉴명")
                .price(price)
                .build();
    }

}