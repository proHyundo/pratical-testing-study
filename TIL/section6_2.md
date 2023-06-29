# section 6_2 Test Double

## 객체 종류
- fake : Production 환경에서 사용할 수 없는 흉내낸 것. E.g. FakeRepository 의 Map을 DB처럼 사용
- spy : 특정 메소드가 호출되었는지 등 행위를 기록
- stub
- mock

## stub & mock

- Martin Fowler) https://martinfowler.com/articles/mocksArentStubs.html
- 공통점 : 둘다 가짜 객체, 요청에 대한 응답을 가짜로 만들어서 테스트를 돕는다.
- 차이점
  - Stub : 기능 요청 후 Stub 의 상태를 검증
  - Mock : 행위 자체를 중점으로 검증

## @Mock, @InjectMocks, @ExtendWith(MockitoExtension.class)

### Mock 객체 생성 및 의존성 주입
```
  @DisplayName("직접 필요한 의존 관계 주입")
  @Test
  void test() {
    AAAClient aaaClient = Mockito.mock(AAAClient.class);
    AAAHistoryRepository aaaHistoryRepository = Mockito.mock(AAAHistoryRepository.class);
    AAAService mailService = new AAAService(aaaClient, aaaHistoryRepository);
  }
```
- AAAService 의 메서드를 단위테스트 하기 위해, AAAServie 의 의존성을 주입해야 한다.
- 이때 위 방법은 필요한 의존성을 직접 생성하여 직접 주입하는 방법이다.

```
  @ExtendWith(MockitoExtension.class)
  class AAAServiceTest {
    @Mock
    AAAClient aaaClient;
    @Mock
    AAAHistoryRepository aaaHistoryRepository;
    
    @DisplayName("직접 필요한 의존 관계 주입")
    @Test
    void test() {
      AAAService mailService = new AAAService(aaaClient, aaaHistoryRepository);
    }
  }
```
- 위 방법은 @Mock 를 사용하여, 필요한 의존성을 자동으로 생성하는 방법이다. 단, 주입은 직접하고 있다.
- @Mock 어노테이션을 사용하기 위해선, Junit5 에선 @ExtendWith(MockitoExtension.class) 를 사용해야 한다.

```
  @Mock
  AAAClient aaaClient;
  @Mock
  AAAHistoryRepository aaaHistoryRepository;
  @InjectMocks
  AAAService aaaService;
```
- 의존성 주입까지 자동으로 하기 위해 @InjectMocks 를 사용한다.

### Mock 객체의 행위 정의

```
  @Test
  void test() {
    Mockito.when(aaaService.doSomething(anyString()))
           .thenReturn("result");
  }
```

### @Spy 객체의 행위 정의

```
  doReturn(true)
    .when(aaaClient)
    .sendAAA(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
```