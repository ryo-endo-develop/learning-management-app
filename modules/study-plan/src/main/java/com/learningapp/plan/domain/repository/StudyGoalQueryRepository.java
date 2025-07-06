package com.learningapp.plan.domain.repository;

import com.learningapp.base.domain.repository.QueryRepository;
import com.learningapp.base.domain.valueobject.StudyCategoryId;
import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.plan.domain.entity.StudyGoal;

import java.util.List;
import java.util.Optional;

/**
 * StudyGoal Query Repository
 */
public interface StudyGoalQueryRepository extends QueryRepository<StudyGoal, StudyGoalId> {
    
    /**
     * 学習計画IDで目標を検索
     */
    List<StudyGoal> findByStudyPlanId(StudyPlanId studyPlanId);
    
    /**
     * 学習計画IDとカテゴリIDで目標を検索
     */
    Optional<StudyGoal> findByStudyPlanIdAndCategoryId(StudyPlanId studyPlanId, StudyCategoryId categoryId);
    
    /**
     * 目標達成済みの目標を検索
     */
    List<StudyGoal> findAchievedGoalsByStudyPlanId(StudyPlanId studyPlanId);
}
