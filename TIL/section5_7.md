## 정적 팩토리 메서드 (해당 파트 아니였음)

```
@NoArgsConstructor
@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> products;

    @Builder
    public OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .products(order.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList()))
                .build();
    }

}
```

## Stream

- 

## @Transactional

- @Transactional 어노테이션의 편리함 때문에 Test Code 에서 많이 사용하지만, 항상 트랜잭션의 범위에 유의해야 한다. 


## 동시성 제어

- 재고 감소는 항상 동시성을 고민해 봐야 한다.
- 학습 키워드 : optimistic lock, pessimistic lock 등이 있다.

## IntelliJ IDE 단축키 (Windows)

- Ctrl + Shift + T : Create Test Class
- Ctrl + Alt + M : Extract Method
- Ctrl + Alt + V : Extract Variable
- Ctrl + Shift + Arrow Up/Down : Move Line Up/Down