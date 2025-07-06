package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.StudyCategory;
import com.learningapp.base.domain.valueobject.StudyCategoryId;

/**
 * StudyCategory Command Repository
 * Effective Java Item 18: 継承よりもコンポジション
 */
public interface StudyCategoryCommandRepository {
    
    /**
     * カテゴリを保存
     */
    void save(StudyCategory category);
    
    /**
     * カテゴリを削除
     */
    void delete(StudyCategoryId id);
    
    /**
     * 表示順を一括更新
     */
    void updateDisplayOrders(java.util.Map<StudyCategoryId, Integer> orderMap);
}
