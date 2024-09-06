package com.sparta3tm.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 100)
    private String createdBy;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column(length = 100)
    private String updatedBy;

    private LocalDateTime deletedAt;

    @Column(length = 100)
    private String deletedBy;

    private boolean isDelete = false;

    public void softDelete(String deletedBy){
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.isDelete = true;
    }

}
