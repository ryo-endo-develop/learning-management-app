package com.learningapp.plan.domain.repository;

import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.plan.domain.entity.StudyGoal;

import java.util.List;

/**
 * StudyGoal Command Repository
 * Effective Java Item 18: 継承よりもコンポジション
 */
public interface StudyGoalCommandRepository {
    
    /**
     * 学習目標を保存
     */
    void save(StudyGoal studyGoal);
    
    /**
     * 学習目標を削除
     */
    void delete(StudyGoalId id);
    
    /**
     * 学習計画に紐づく全ての目標を削除
     */
    void deleteByStudyPlanId(StudyPlanId studyPlanId);
    
    /**
     * 複数の学習目標を一括保存
     */
    void saveAll(List<StudyGoal> studyGoals);
    
    /**
     * 学習計画の目標を一括置換
     */
    void replaceGoalsForStudyPlan(StudyPlanId studyPlanId, List<StudyGoal> newGoals);
}
