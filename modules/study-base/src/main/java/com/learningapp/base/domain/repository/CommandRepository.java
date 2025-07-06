package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.Entity;
import com.learningapp.base.domain.valueobject.Identity;

/**
 * Command側Repository基盤インターface
 * 書き込み操作（作成・更新・削除）に特化
 */
public interface CommandRepository<E extends Entity<I>, I extends Identity> {
    
    /**
     * エンティティを保存
     */
    void save(E entity);
    
    /**
     * エンティティを削除
     */
    void delete(I id);
}
