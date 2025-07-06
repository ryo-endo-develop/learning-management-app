package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.Identity;

/**
 * 集約ルートの基底クラス
 * DDD の Aggregate Root pattern
 */
public abstract class AggregateRoot<T extends Identity> extends Entity<T> {
    
    protected AggregateRoot() {
        super();
    }
    
    protected AggregateRoot(T id) {
        super(id);
    }
    
    /**
     * ドメインイベント発行のための拡張ポイント
     * 将来的にドメインイベントを実装する際に利用
     */
    protected void publishDomainEvent(Object event) {
        // TODO: ドメインイベント発行機能を実装
    }
}
