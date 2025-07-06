package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.Entity;
import com.learningapp.base.domain.valueobject.Identity;
import java.util.List;
import java.util.Optional;

/**
 * Query側Repository基盤インターface
 * 読み込み操作（検索・取得）に特化
 */
public interface QueryRepository<E extends Entity<I>, I extends Identity> {
    
    /**
     * IDでエンティティを取得
     */
    Optional<E> findById(I id);
    
    /**
     * 全エンティティを取得
     */
    List<E> findAll();
    
    /**
     * 存在チェック
     */
    boolean existsById(I id);
    
    /**
     * 件数取得
     */
    long count();
}
