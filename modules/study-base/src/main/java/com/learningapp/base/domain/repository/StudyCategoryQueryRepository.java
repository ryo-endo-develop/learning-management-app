package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.StudyCategory;
import com.learningapp.base.domain.valueobject.StudyCategoryId;
import java.util.List;

/**
 * StudyCategory Query Repository
 */
public interface StudyCategoryQueryRepository extends QueryRepository<StudyCategory, StudyCategoryId> {
    
    /**
     * 表示順でソートして全カテゴリを取得
     */
    List<StudyCategory> findAllOrderByDisplayOrder();
    
    /**
     * カテゴリ名で検索
     */
    List<StudyCategory> findByNameContaining(String name);
}
