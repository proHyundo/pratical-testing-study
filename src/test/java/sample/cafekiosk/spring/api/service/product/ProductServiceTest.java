package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProduct() {
        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        productRepository.saveAll(List.of(product1, product2));
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카페모카")
                .price(5000)
                .build();
        // when
        ProductResponse resultProductResponse = productService.createProduct(request.toServiceRequest());
        // then
        assertThat(resultProductResponse).extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactly("003", ProductType.HANDMADE, ProductSellingStatus.SELLING, "카페모카", 5000);
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(3).extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("002", "카페라떼", 4500),
                        tuple("003", "카페모카", 5000));
    }

    @DisplayName("신규 상품을 등록한다. 등록된 상품이 없는 경우 상품번호는 001 이다.")
    @Test
    void createProductWhenProductEmpty() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("카페모카")
                .price(5000)
                .build();
        // when
        ProductResponse resultProductResponse = productService.createProduct(request.toServiceRequest());
        // then
        assertThat(resultProductResponse).extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactly("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "카페모카", 5000);
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