package com.learningapp.base.domain.repository;

import java.util.List;
import java.util.Optional;

/**
 * Query側Repository基盤インターface
 * 読み込み操作（検索・取得）に特化
 * Effective Java Item 18: 継承よりもコンポジション（interface使用）
 * Effective Java Item 55: Optionalを適切に使用する
 */
public interface QueryRepository<E, I> {
    
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
    
    /**
     * 複数IDで一括取得
     */
    default List<E> findAllById(final List<I> ids) {
        return ids.stream()
            .map(this::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }
    
    /**
     * ページング対応検索（将来拡張用）
     */
    default List<E> findAll(final int offset, final int limit) {
        return findAll().stream()
            .skip(offset)
            .limit(limit)
            .toList();
    }
}
