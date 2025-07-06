package com.learningapp.base.domain.repository;

import java.util.List;
import java.util.Optional;

/**
 * Command側Repository基盤インターface
 * 書き込み操作（作成・更新・削除）に特化
 * Effective Java Item 18: 継承よりもコンポジション（interface使用）
 */
public interface CommandRepository<E, I> {
    
    /**
     * エンティティを保存
     */
    void save(E entity);
    
    /**
     * エンティティを削除
     */
    void delete(I id);
    
    /**
     * 複数エンティティを一括保存
     */
    default void saveAll(final List<E> entities) {
        entities.forEach(this::save);
    }
    
    /**
     * 複数エンティティを一括削除
     */
    default void deleteAll(final List<I> ids) {
        ids.forEach(this::delete);
    }
}
