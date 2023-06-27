package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
@ActiveProfiles("test") // application.yml 설정 파일을 사용한다.
@DataJpaTest // JPA 관련 설정만 로드
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));
        // then
        // extracting : 검증하고자 하는 필드만 추출 가능.
        // containsExactlyInAnyOrder : 순서는 상관없이 값만 같으면 된다.
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("상품 번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "메뉴명", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "메뉴명", 4500);
        Product product3 = createProduct("003", HANDMADE, SELLING, "메뉴명", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));
        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "메뉴명", 4000),
                        tuple("002", "메뉴명", 4500)
                );
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 조회한다.")
    @Test
    void findLatestProductNumber() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "메뉴명", 4000);
        Product product2 = createProduct("002", HANDMADE, SELLING, "메뉴명", 4500);
        Product product3 = createProduct("003", HANDMADE, SELLING, "메뉴명", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String result = productRepository.findLatestProductNumber();
        // then
        assertThat(result).isEqualTo("003");
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 조회할 때, 저장된 상품이 없으면 null 을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {
        // given
        // when
        String result = productRepository.findLatestProductNumber();
        // then
        assertThat(result).isNull();
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus status, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(status)
                .name(name)
                .price(price)
                .build();
    }

}