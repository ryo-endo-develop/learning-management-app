package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.Identity;
import java.time.LocalDateTime;

/**
 * エンティティのマーカーインターフェース
 * Effective Java Item 18: 継承よりもコンポジションを選ぶ
 */
public interface EntityMarker<T extends Identity> {
    
    T getId();
    
    LocalDateTime getCreatedAt();
    
    LocalDateTime getUpdatedAt();
    
    /**
     * エンティティの同一性判定
     */
    default boolean isSameEntity(EntityMarker<T> other) {
        if (other == null) return false;
        return getId() != null && getId().equals(other.getId());
    }
}
