package com.learningapp.plan.domain.repository;

import com.learningapp.base.domain.enums.StudyPlanStatus;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.base.domain.valueobject.UserId;
import com.learningapp.plan.domain.entity.StudyPlan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * StudyPlan Query Repository
 * Effective Java Item 18: 継承よりもコンポジション
 * Effective Java Item 55: Optionalを適切に使用する
 */
public interface StudyPlanQueryRepository {
    
    /**
     * IDで学習計画を取得
     */
    Optional<StudyPlan> findById(StudyPlanId id);
    
    /**
     * 全学習計画を取得
     */
    List<StudyPlan> findAll();
    
    /**
     * 存在チェック
     */
    boolean existsById(StudyPlanId id);
    
    /**
     * 件数取得
     */
    long count();
    
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
    
    /**
     * 期限切れの学習計画を検索
     */
    List<StudyPlan> findOverduePlans();
    
    /**
     * 期限が近い学習計画を検索（1週間以内）
     */
    List<StudyPlan> findNearDeadlinePlans();
    
    /**
     * ユーザーの学習計画統計を取得
     */
    StudyPlanStatistics getStatisticsByUserId(UserId userId);
    
    /**
     * 学習計画統計の内部クラス
     */
    record StudyPlanStatistics(
        long totalPlans,
        long activePlans,
        long completedPlans,
        double averageDurationDays,
        double completionRate
    ) {}
}
