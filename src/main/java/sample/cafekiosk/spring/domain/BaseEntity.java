package sample.cafekiosk.spring.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식하도록 함
@EntityListeners(AuditingEntityListener.class) // BaseTimeEntity 클래스에 Auditing 기능을 포함시킴
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime modifiedDateTime;


}
