package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> productList = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        return productList.stream()
                .map(ProductResponse::of) // ProductResponse::of == product -> ProductResponse.of(product)
                .collect(toList());
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        String nextProductNumber = createNextProductNumber();
        Product savedProduct = productRepository.save(request.toEntity(nextProductNumber));
        return ProductResponse.of(savedProduct);
    }

    // DB 에서 마지막 저장된 Product 상품번호 + 1
    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        return latestProductNumber==null? "001" : String.format("%03d", Integer.parseInt(latestProductNumber) + 1);
    }
}
