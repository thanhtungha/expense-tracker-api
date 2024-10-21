package com.example.expense_tracker_api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Persistable<UUID> {
    @Id
    protected UUID id;

    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.InstantConverter.class)
    protected Instant createdAt;

    @CreatedBy
    protected String createdBy;

    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.InstantConverter.class)
    protected Instant lastUpdatedAt;

    @LastModifiedBy
    protected String lastUpdatedBy;

    @Transient
    private boolean persisted;

    @Override
    public boolean isNew() {
        return !persisted;
    }

    @PostPersist
    @PostLoad
    public void setPersisted() {
        persisted = true;

        onPostLoad();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (Hibernate.getClass(this) != Hibernate.getClass(obj))) {
            return false;
        }

        return obj instanceof BaseEntity && (getId() != null) && Objects.equals(getId(), ((BaseEntity) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // Template method
    protected void onPostLoad() {
    }
}
