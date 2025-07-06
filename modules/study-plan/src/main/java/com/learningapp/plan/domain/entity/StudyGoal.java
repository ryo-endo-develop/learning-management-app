package com.learningapp.plan.domain.entity;

import com.learningapp.base.domain.entity.Entity;
import com.learningapp.base.domain.valueobject.StudyCategoryId;
import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 学習目標エンティティ
 * 学習計画の集約内エンティティ
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGoal extends Entity<StudyGoalId> {
    
    private StudyPlanId studyPlanId;
    private StudyCategoryId categoryId;
    private Integer targetScore;
    private Integer targetHours;
    private Integer currentBestScore;
    private Integer totalStudiedHours;
    
    public StudyGoal(StudyGoalId id, StudyPlanId studyPlanId, StudyCategoryId categoryId,
                     Integer targetScore, Integer targetHours) {
        super(id);
        this.studyPlanId = studyPlanId;
        this.categoryId = categoryId;
        this.targetScore = targetScore;
        this.targetHours = targetHours;
        this.currentBestScore = 0;
        this.totalStudiedHours = 0;
    }
    
    public static StudyGoal create(StudyPlanId studyPlanId, StudyCategoryId categoryId,
                                   Integer targetScore, Integer targetHours) {
        validateInputs(targetScore, targetHours);
        return new StudyGoal(StudyGoalId.generate(), studyPlanId, categoryId, targetScore, targetHours);
    }
    
    public void updateGoal(Integer targetScore, Integer targetHours) {
        validateInputs(targetScore, targetHours);
        this.targetScore = targetScore;
        this.targetHours = targetHours;
        updateTimestamp();
    }
    
    public void updateProgress(Integer newScore, Integer additionalHours) {
        if (newScore != null) {
            validateScore(newScore);
            if (newScore > this.currentBestScore) {
                this.currentBestScore = newScore;
            }
        }
        
        if (additionalHours != null && additionalHours > 0) {
            this.totalStudiedHours += additionalHours;
        }
        
        updateTimestamp();
    }
    
    public double getScoreAchievementRate() {
        if (targetScore == 0) return 0.0;
        return Math.min(100.0, (currentBestScore * 100.0) / targetScore);
    }
    
    public double getHoursAchievementRate() {
        if (targetHours == 0) return 0.0;
        return Math.min(100.0, (totalStudiedHours * 100.0) / targetHours);
    }
    
    public boolean isScoreTargetAchieved() {
        return currentBestScore >= targetScore;
    }
    
    public boolean isHoursTargetAchieved() {
        return totalStudiedHours >= targetHours;
    }
    
    public boolean isGoalAchieved() {
        return isScoreTargetAchieved() && isHoursTargetAchieved();
    }
    
    private static void validateInputs(Integer targetScore, Integer targetHours) {
        validateScore(targetScore);
        if (targetHours == null || targetHours < 0) {
            throw new IllegalArgumentException("目標学習時間は0時間以上で設定してください");
        }
        if (targetHours > 10000) {
            throw new IllegalArgumentException("目標学習時間は10000時間以内で設定してください");
        }
    }
    
    private static void validateScore(Integer score) {
        if (score == null || score < 0 || score > 100) {
            throw new IllegalArgumentException("スコアは0-100の範囲で設定してください");
        }
    }
}
