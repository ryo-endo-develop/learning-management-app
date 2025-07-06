package com.learningapp.plan.domain.repository;

import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.plan.domain.entity.StudyPlan;

import java.util.List;

/**
 * StudyPlan Command Repository
 * Effective Java Item 18: 継承よりもコンポジション
 */
public interface StudyPlanCommandRepository {
    
    /**
     * 学習計画を保存
     */
    void save(StudyPlan studyPlan);
    
    /**
     * 学習計画を削除
     */
    void delete(StudyPlanId id);
    
    /**
     * 複数の学習計画を一括保存
     */
    void saveAll(List<StudyPlan> studyPlans);
    
    /**
     * ユーザーの学習計画を一括削除
     */
    void deleteByUserId(com.learningapp.base.domain.valueobject.UserId userId);
}
