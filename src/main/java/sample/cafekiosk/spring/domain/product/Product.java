package sample.cafekiosk.spring.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.*;

//@ToString
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED) // 기본 생성자의 접근 권한을 protected로 제한
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING) // JPA로 데이터베이스를 저장할 때 Enum 값을 어떤 형태로 저장할지 결정
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    private Product( String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        // id 는 자동 생성되기에 제외
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }
}
