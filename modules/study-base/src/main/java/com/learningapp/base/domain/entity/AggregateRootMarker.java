package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.Identity;

/**
 * 集約ルートのマーカーインターフェース
 * DDD の Aggregate Root pattern
 */
public interface AggregateRootMarker<T extends Identity> extends EntityMarker<T> {
    
    /**
     * ドメインイベント発行のための拡張ポイント
     * 将来的にドメインイベントを実装する際に利用
     */
    default void publishDomainEvent(Object event) {
        // TODO: ドメインイベント発行機能を実装
    }
}
