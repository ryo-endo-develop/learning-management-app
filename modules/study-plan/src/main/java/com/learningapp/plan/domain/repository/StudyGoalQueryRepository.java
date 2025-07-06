package com.learningapp.plan.domain.repository;

import com.learningapp.base.domain.valueobject.StudyCategoryId;
import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.plan.domain.entity.StudyGoal;

import java.util.List;
import java.util.Optional;

/**
 * StudyGoal Query Repository
 * Effective Java Item 18: 継承よりもコンポジション
 * Effective Java Item 55: Optionalを適切に使用する
 */
public interface StudyGoalQueryRepository {
    
    /**
     * IDで学習目標を取得
     */
    Optional<StudyGoal> findById(StudyGoalId id);
    
    /**
     * 全学習目標を取得
     */
    List<StudyGoal> findAll();
    
    /**
     * 存在チェック
     */
    boolean existsById(StudyGoalId id);
    
    /**
     * 件数取得
     */
    long count();
    
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
    
    /**
     * 目標未達成の目標を検索
     */
    List<StudyGoal> findUnachievedGoalsByStudyPlanId(StudyPlanId studyPlanId);
    
    /**
     * 学習時間目標を達成した目標を検索
     */
    List<StudyGoal> findHoursAchievedGoalsByStudyPlanId(StudyPlanId studyPlanId);
    
    /**
     * スコア目標を達成した目標を検索
     */
    List<StudyGoal> findScoreAchievedGoalsByStudyPlanId(StudyPlanId studyPlanId);
    
    /**
     * カテゴリ別の目標達成率を取得
     */
    List<GoalAchievementSummary> getAchievementSummaryByStudyPlan(StudyPlanId studyPlanId);
    
    /**
     * 目標達成サマリーの内部クラス
     */
    record GoalAchievementSummary(
        StudyCategoryId categoryId,
        String categoryName,
        int targetScore,
        int currentBestScore,
        int targetHours,
        int totalStudiedHours,
        double scoreAchievementRate,
        double hoursAchievementRate,
        boolean isGoalAchieved
    ) {}
}
