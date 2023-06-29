# Mockito로 Stubbing하기

## JPA Cascade 에러

```
org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : example.cafekiosk.spring.domain.orderproduct.OrderProduct.product -> example.cafekiosk.spring.domain.product.Product; nested exception is java.lang.IllegalStateException: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : example.cafekiosk.spring.domain.orderproduct.OrderProduct.product -> example.cafekiosk.spring.domain.product.Product
```
- 출처 : 커뮤니티-질문-테스트오류
- OrderProduct Entity 에 Cascade 설정 후 해결되었다고 함.