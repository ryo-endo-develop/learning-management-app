package com.learningapp.plan.domain.repository;

import com.learningapp.base.domain.repository.CommandRepository;
import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.plan.domain.entity.StudyGoal;

/**
 * StudyGoal Command Repository
 */
public interface StudyGoalCommandRepository extends CommandRepository<StudyGoal, StudyGoalId> {
    
    /**
     * 学習計画に紐づく全ての目標を削除
     */
    void deleteByStudyPlanId(com.learningapp.base.domain.valueobject.StudyPlanId studyPlanId);
}
