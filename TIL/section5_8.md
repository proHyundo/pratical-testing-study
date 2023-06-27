# Presentation Layer (1)

- Controller 테스트 관련 챕터인데, 추가된 요구사항을 구현하고 테스트하다보니 다른 레이어에 대한 테스트 위주로 진행하게 되었다.

## Presentation Layer

- 외부 세계의 요청을 가장 먼저 받는 계층
- 파라미터에 대한 최소한의 검증 수행
- Business Layer, Persistence Layer 를 테스트 할 땐, 모킹 없이 스프링을 띄워 통합 테스트를 진행했다.
- Presentation Layer 를 테스트 할 땐, 하위 레이어는 모킹을 사용하여 단위 테스트를 진행한다.

## Mock

## Etc - Business Layer Test (00:23:00 ~)

- 얕은 Business Layer Test 는 Repository Layer Test 와 크게 다른게 없어 보일 순 있지만, 로직이 추가될 경우를 대비해 동일 테스트라고 보이더라도 테스트를 하는 것을 권장한다.

## Etc - g/w/t 중 when

- when은 행위를 서술하는 영역이다. 따라서 코드 한 줄로 끝나는 경우가 대부분이다.
- 그 외 모든 준비과정은 given 영역에서 작성한다.

## JPA

```
@Query(value = "SELECT p.product_number FROM Product p ORDER BY p.id desc LIMIT 1", nativeQuery = true)
    String findLatestProductNumber();
```
- 왜 product_number 는 동작하고, productNumber는 동장하지 않을까? 왜 변수명 productNumber는 컬럼명 product_number로 변경되었을까
- JPA의 네이밍컨벤션 때문. (출처 : 구글 바드)
When you use JPA, the column names in the database are mapped to properties in the entity class using a naming strategy. The default naming strategy used by JPA is the `SpringNamingStrategy`. This strategy converts camel case names to snake case names, and adds an underscore before any uppercase letters. For example, the property `productNumber` will be mapped to the column `product_number` in the database.
You can change the naming strategy used by JPA by setting the `spring.jpa.hibernate.naming-strategy` property in your application.properties file. For example, to use the `EJB3NamingStrategy`, you would set the property to `org.hibernate.cfg.EJB3NamingStrategy`.

Here is a table of the different naming strategies that are available in JPA:

| Naming Strategy | Description |
|---|---|
| SpringNamingStrategy | Converts camel case names to snake case names, and adds an underscore before any uppercase letters. |
| UpperCaseNamingStrategy | Converts all property names to uppercase letters. |
| LowerCaseNamingStrategy | Converts all property names to lowercase letters. |
| ImplicitNamingStrategyLegacyJpaImpl | Converts camel case names to snake case names, but does not add an underscore before any uppercase letters. |
| EJB3NamingStrategy | Converts all property names to lowercase letters, and adds an underscore before any uppercase letters. |

I hope this helps! Let me know if you have any other questions.

## 동시성 이슈 (00:38:00 ~)

- 개수가 증가 또는 감소하는 경우, 혼자만 접근가능한 경우를 제외하곤 동시성 이슈를 항상 고려해야 한다.
- DB에서 ProductNumber 컬럼에 Unique Index 를 걸어서 해결할 수도 있다.
- 동시에 접근하는 사용자가 적은 경우, 충돌발생시 재시도를 통해 해결할 수 있고, 동시에 접근하는 사용자가 많은 경우, ProductNumber 생성 정책을 UUID 로 변경하여 해결할 수도 있다.

## Transactional (00:45:32 ~)

```aidl
@Transactional(readOnly = true)
```
- readOnly = true 를 사용하면, 읽기전용 트랜잭션이 활성화 된다.
- 읽기 전용이란, CRUD 중 CUD 작업이 수행될 수 없고 조회만 가능하다.
- JPA 를 같이 사용하는 경우, CUD 작업이 동작하지 않아 [스냅샷 저장, 변경 감지]를 하지 않아 성능 향상 이점이 있다.
- CQRS Pattern : Command(CUD를 통칭하는 용어) 와 Query(읽기) 를 분리하는 패턴
  - 대게 Read 작업이 CUD 에 비해 압도적으로 많다. 따라서 이 둘의 책임을 분리를 권장하는 패턴.
  - Master - Slave 구조로 분리하는 경우가 많다.
    - Master : CUD 작업만 수행, Slave : Read 작업만 수행
    - @Transactional(readOnly = true) 를 사용한 경우 Slave DB 의 End-Point 로 요청이 가도록 설정한다.
    - 이를 통해 장애 격리에도 이점이 있다.
- 따라서 CUD 작업에는 @Transactional(readOnly = false) 를 사용하고, Read 작업에는 @Transactional(readOnly = true) 를 사용하는 것을 권장한다.
- 이때, 메서드 마다 어노테이션을 붙이는 것보다, 서비스 클래스 상단에 @Transactional(readOnly = true) 붙이고, CUD 는 메서드마다 @Transactional 를 붙이는 것을 권장한다.
