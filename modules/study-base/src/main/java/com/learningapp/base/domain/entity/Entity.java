package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.Identity;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * エンティティの基底クラス
 */
public abstract class Entity<T extends Identity> {
    
    protected T id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    
    protected Entity() {
        // ORMのため
    }
    
    protected Entity(T id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public T getId() {
        return id;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    protected void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
