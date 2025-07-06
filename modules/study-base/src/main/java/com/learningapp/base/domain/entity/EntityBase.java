package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.Identity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * エンティティの基本実装
 * Effective Java Item 17: 可変性を最小限に抑える
 * Lombok アノテーション除去：手動実装でより明示的に
 */
public final class EntityBase<T extends Identity> {
    
    private final T id;
    private final LocalDateTime createdAt;
    private volatile LocalDateTime updatedAt;  // 更新可能なフィールドのみ volatile
    
    public EntityBase(final T id) {
        this.id = Objects.requireNonNull(id, "ID must not be null");
        final LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
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
    
    /**
     * 更新時刻を現在時刻に更新
     * スレッドセーフ
     */
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 同一性判定
     */
    public boolean isSameEntity(final EntityBase<T> other) {
        if (other == null) return false;
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final EntityBase<?> that = (EntityBase<?>) obj;
        return Objects.equals(id, that.id);
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
