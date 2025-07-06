package com.learningapp.plan.domain.repository;

import com.learningapp.base.domain.repository.QueryRepository;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.base.domain.valueobject.UserId;
import com.learningapp.base.domain.enums.StudyPlanStatus;
import com.learningapp.plan.domain.entity.StudyPlan;

import java.time.LocalDate;
import java.util.List;

/**
 * StudyPlan Query Repository
 */
public interface StudyPlanQueryRepository extends QueryRepository<StudyPlan, StudyPlanId> {
    
    /**
     * ユーザーIDで学習計画を検索
     */
    List<StudyPlan> findByUserId(UserId userId);
    
    /**
     * ユーザーIDとステータスで学習計画を検索
     */
    List<StudyPlan> findByUserIdAndStatus(UserId userId, StudyPlanStatus status);
    
    /**
     * 期間が重複する学習計画を検索
     */
    List<StudyPlan> findOverlappingPlans(UserId userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * アクティブな学習計画を検索
     */
    List<StudyPlan> findActiveByUserId(UserId userId);
}
