# Classicist vs Mockist

- Mockist : Mock 객체를 사용하여 테스트
    - 각각의 객체를 단위테스트를 통해 정상 동작을 검증하였으니, 통합테스트 단계에서 Mock 객체를 사용해 테스트 하자는 관점.
    - 빠르고 가벼운 테스트를 위해 Mock 객체를 사용하는 관점.
- Classicist : 실제 객체를 사용하여 테스트
  - 실제 객체를 사용해 테스트 해야 서로 다른 객체간의 협업에서도 문제가 없는지 검증할 수 있다는 관점.
  - Mock 객체를 사용한 테스트를 배제하는 관점이 아니다. 꼭 필요한 경우 Mock 객체 사용.

## 해당 강의에선 어떻게 테스트를 진행했는가.

Classicist 관점을 따랐다.

- Repository 
  - @DataJpaTest 를 통해 실제 객체를 사용하여 테스트
- Service 
  - @SpringBootTest 를 통해 실제 객체를 사용하여 테스트
  - 외부 요인(시스템, 라이브러리, API)이 테스트에 참가하는 경우 Mock 객체를 사용하여 테스트 (E.g.) Mail 발송
- Controller
  - Business Layer 와 Persistence Layer 를 Mocking 하여 단위 테스트를 진행했다.
  - Controller 하나, 빈 하나만 테스트하기 때문에 가볍다. 외부에서 요청한 파라미터를 주로 검증한다.
  - @WebMvcTest 를 통해 @MockBean 객체를 사용하여 테스트
    - @MockBean : Spring Boot Container가 필요하고 Bean이 container 에 존재 해야 한다면 @MockBean 을 쓰면 되고 아니라면 @Mock 을 쓰면 된다
    - @Autowired를 통해 의존성 주입

참고 링크 : https://velog.io/@suran-kim/스프링-부트-6일-차-테스팅-단위-통합테스트