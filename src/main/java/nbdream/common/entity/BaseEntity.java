package nbdream.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @PrePersist
    public void init() {
        this.status = Status.NORMAL;
    }

    public void expired() {
        this.status = Status.EXPIRED;
    }
    public void recover() {
        this.status = Status.NORMAL;
    }
}