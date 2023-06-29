# Presentation Layer (2)

## MockMvc

```
@Autowired
private MockMvc mockMvc;
```
- 

## @WebMvcTest

- Controller 만 분리해 테스트를 하기 위한 어노테이션

## @MockBean

- Mock 객체를 주입받기 위한 어노테이션
- 예를들어, Presentation Layer(ProductController) 에서 Business Layer(ProductService) 를 호출하는 경우, Business Layer 를 Mocking 하여 테스트를 진행한다.

## ObjectMapper

- JSON <-> Object 변환을 위한 라이브러리
- ObjectMapper 는 기본생성자를 이용해 변환을 한다고 한다. 그러나, Builder 만 존재했던 ProductCreateRequest 의 경우 ObjectMapper 를 이용해 정상 변환 되었다.
  - @Builder 는 기본생성자를 포함하고 있기 때문에 ObjectMapper 는 기본생성자를 이용해 변환을 한 것으로 추측된다.
  - 참고 : https://projectlombok.org/features/Builder 의 'In the builder: A package private no-args empty constructor.'
  - 참고 : 구글 바드 답변
    ```
    The @Builder annotation generates a builder class that contains a no-args constructor. This constructor is private because the @Builder annotation is designed to be used with the builder pattern, and the builder pattern typically requires that the builder class be created using the builder() method.
    The ProductResponse class does not have any other constructors, so the @Builder annotation will generate a private no-args constructor. This means that the only way to create a ProductResponse object is to use the builder() method.
    If you need to create a ProductResponse object without using the builder() method, you can use the @NoArgsConstructor annotation. This annotation will generate a public no-args constructor, but it will override the @Builder annotation and the no-args constructor will no longer be private.
    However, in this case, it is not necessary to use the @NoArgsConstructor annotation because the ProductResponse class does not have any required fields. Therefore, the @Builder annotation is sufficient to create ProductResponse objects.
    ```
- Java 오브젝트를 Json으로 파싱하는 것을 직렬화(serialize)라고 하며, 반대는 역직렬화(deserialize)라고 합니다.
- 참고 : https://velog.io/@conatuseus/RequestBody에-왜-기본-생정자는-필요하고-Setter는-필요-없을까-2-ejk5siejhh
- 참고 : https://da-nyee.github.io/posts/woowacourse-why-the-default-constructor-is-needed/

**보충 필요**
- @Builder 패턴이란 무엇이고, 무엇을 만들어 주는 것인가?
- Builder Class 란 ?
- ObjectMapper 가 기본생성자를 사용해 변환하는 코드를 이해해보자. (위 2개의 링크 참고)
- @Builder 와 @NoArgsConstructor 를 동시에 사용하는 경우 private 이 생성될까 public이 생성될까?

## 파라미터 검증의 책임

```
@NotBlank
@max(10)
private String name;
```
- 문자열 파라미터 값 검증을 예시로 들어볼 때, @NotBlank 는 기본적인 유효한 문자열을 검증하는 반면,
@max(10) 은 도메인 정책에 가까운 검증이다.
- 따라서 강의에서는 @NotBlank 는 Presentation Layer 에서 검증하고, @max(10) 은 Business Layer 에서 검증하는 것이 좋다고 한다.
- 서로 다른 성격의 검증은 구분하는 것을 권장한다고 한다. 반드시 하나의 레이어에서 모든 검증이 이루어질 필요 없다고 한다.

## DTO의 분리

- 현재 OrderController 는 OrderCreateDto 를 Service Layer 로 해당 Dto 를 그대로 전달하고 있다. 이 OrderCreateDto 는 Controller 하위에 있다. 
- 즉, 두 레이어간 Dto 를 통한 의존이 발생하고 있다. 추후, 서비스가 커져 레이어 별 모듈을 분리할 필요가 있을 경우, 이러한 의존은 장애물이 된다.
- 상위(Controller) 레이어는 어떤 하위(Service) 레이어를 호출해야 하는지 알고 있음이 당연하지만, 하위레이어는 상위레이어의 정채를 모르는 것이 올바르다.
- 따라서 Service 용 Dto를 별도로 생성하는 것을 권장한다고 한다.
- 이로써 Service Layer 는 Controller Layer 를 의존하지 않고, ServiceDto 에선 ControllerDto 가 수행하던 검증 Valid 책임을 가지고 있지 않아도 된다.
```
├─api
│  │  ApiControllerAdvice.java
│  │  ApiResponse.java
│  │
│  ├─controller
│  │  ├─order
│  │  │  │  OrderController.java
│  │  │  │
│  │  │  └─request
│  │  │          OrderCreateRequest.java
│  │  │
│  │  └─product
│  │      │  ProductController.java
│  │      │
│  │      └─dto
│  │          └─request
│  │                  ProductCreateRequest.java
│  │
│  └─service
│      ├─order
│      │      OrderResponse.java
│      │      OrderService.java
│      │
│      └─product
│          │  ProductService.java
│          │
│          └─response
│                  ProductResponse.java
│
├─config
│      JpaAuditingConfig.java
│
└─domain
    │  BaseEntity.java
    │
    ├─order
    │      Order.java
    │      OrderRepository.java
    │      OrderStatus.java
    │
    ├─orderproduct
    │      OrderProduct.java
    │      OrderProductRepository.java
    │
    ├─product
    │      Product.java
    │      ProductRepository.java
    │      ProductSellingStatus.java
    │      ProductType.java
    │
    └─stock
            Stock.java
            StockRepository.java
```