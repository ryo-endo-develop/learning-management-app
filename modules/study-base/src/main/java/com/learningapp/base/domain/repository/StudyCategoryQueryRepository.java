package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.StudyCategory;
import com.learningapp.base.domain.valueobject.StudyCategoryId;

import java.util.List;
import java.util.Optional;

/**
 * StudyCategory Query Repository
 * Effective Java Item 18: 継承よりもコンポジション
 * Effective Java Item 55: Optionalを適切に使用する
 */
public interface StudyCategoryQueryRepository {
    
    /**
     * IDでカテゴリを取得
     */
    Optional<StudyCategory> findById(StudyCategoryId id);
    
    /**
     * 全カテゴリを取得
     */
    List<StudyCategory> findAll();
    
    /**
     * 存在チェック
     */
    boolean existsById(StudyCategoryId id);
    
    /**
     * 件数取得
     */
    long count();
    
    /**
     * 表示順でソートして全カテゴリを取得
     */
    List<StudyCategory> findAllOrderByDisplayOrder();
    
    /**
     * カテゴリ名で検索
     */
    List<StudyCategory> findByNameContaining(String name);
    
    /**
     * 試験関連カテゴリを取得
     */
    List<StudyCategory> findExamCategories();
    
    /**
     * 実技系カテゴリを取得
     */
    List<StudyCategory> findPracticalCategories();
}
