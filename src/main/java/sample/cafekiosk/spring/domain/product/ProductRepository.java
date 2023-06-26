package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 상품 판매 상태 list로 상품 list 조회
     * select * from product where selling_status in ('SELLING', 'HOLD');
     * */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    /**
     * 상품 번호 list로 상품 list 조회
     * select * from product where product_number in ('001', '002', '003');
     * */
    List<Product> findAllByProductNumberIn(List<String> productNumbers);
}
