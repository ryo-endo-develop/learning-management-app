package com.learningapp.plan.domain.entity;

import com.learningapp.base.domain.entity.EntityBase;
import com.learningapp.base.domain.entity.EntityMarker;
import com.learningapp.base.domain.valueobject.StudyCategoryId;
import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.plan.domain.strategy.DifficultyStrategy;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 学習目標エンティティ
 * 学習計画の集約内エンティティ
 * 
 * staticファクトリーメソッドを完全削除、StudyGoalFactoryでのみ生成
 * Strategy Patternで複雑な条件分岐を排除
 */
@Getter
public final class StudyGoal implements EntityMarker<StudyGoalId> {
    
    private static final int MAX_TARGET_HOURS = 10000;
    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 100;
    
    private final EntityBase<StudyGoalId> entityBase;
    private final StudyPlanId studyPlanId;
    private final StudyCategoryId categoryId;
    private final int targetScore;
    private final int targetHours;
    private final int currentBestScore;
    private final int totalStudiedHours;
    
    // Package-private：Factoryからのみアクセス可能
    StudyGoal(final StudyGoalId id, final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
             final int targetScore, final int targetHours, final int currentBestScore, final int totalStudiedHours) {
        this.entityBase = new EntityBase<>(id);
        this.studyPlanId = Objects.requireNonNull(studyPlanId, "StudyPlanId must not be null");
        this.categoryId = Objects.requireNonNull(categoryId, "StudyCategoryId must not be null");
        this.targetScore = validateScore(targetScore);
        this.targetHours = validateHours(targetHours);
        this.currentBestScore = Math.max(0, currentBestScore);
        this.totalStudiedHours = Math.max(0, totalStudiedHours);
    }
    
    /**
     * 目標更新（新しいインスタンスを返す - 不変性）
     */
    public StudyGoal updateGoal(final int newTargetScore, final int newTargetHours) {
        final StudyGoal updatedGoal = new StudyGoal(
            this.getId(), this.studyPlanId, this.categoryId,
            newTargetScore, newTargetHours, 
            this.currentBestScore, this.totalStudiedHours
        );
        updatedGoal.entityBase.updateTimestamp();
        return updatedGoal;
    }
    
    /**
     * 進捗更新（新しいインスタンスを返す - 不変性）
     * ガード節使用
     */
    public StudyGoal updateProgress(final Integer newScore, final Integer additionalHours) {
        final int updatedBestScore = calculateUpdatedBestScore(newScore);
        final int updatedTotalHours = calculateUpdatedTotalHours(additionalHours);
        
        final StudyGoal updatedGoal = new StudyGoal(
            this.getId(), this.studyPlanId, this.categoryId,
            this.targetScore, this.targetHours, 
            updatedBestScore, updatedTotalHours
        );
        updatedGoal.entityBase.updateTimestamp();
        return updatedGoal;
    }
    
    // EntityMarkerの実装
    @Override
    public StudyGoalId getId() {
        return entityBase.getId();
    }
    
    @Override
    public LocalDateTime getCreatedAt() {
        return entityBase.getCreatedAt();
    }
    
    @Override
    public LocalDateTime getUpdatedAt() {
        return entityBase.getUpdatedAt();
    }
    
    // 業務ロジック
    public double getScoreAchievementRate() {
        if (targetScore == 0) return 100.0;
        return Math.min(100.0, (currentBestScore * 100.0) / targetScore);
    }
    
    public double getHoursAchievementRate() {
        if (targetHours == 0) return 100.0;
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
    
    public int getRemainingHours() {
        return Math.max(0, targetHours - totalStudiedHours);
    }
    
    public int getScoreGap() {
        return Math.max(0, targetScore - currentBestScore);
    }
    
    public double getOverallAchievementRate() {
        return (getScoreAchievementRate() + getHoursAchievementRate()) / 2.0;
    }
    
    /**
     * 目標の難易度判定（Strategy Pattern使用）
     */
    public GoalDifficulty getDifficulty() {
        return DifficultyStrategy.determineDifficulty(targetScore, targetHours);
    }
    
    /**
     * 推奨事項取得（Strategy Pattern使用）
     */
    public String getRecommendation() {
        return DifficultyStrategy.getRecommendation(targetScore, targetHours)
            .orElse("目標に向けて頑張りましょう。");
    }
    
    /**
     * 進捗ステータス（ガード節使用）
     */
    public ProgressStatus getProgressStatus() {
        final double achievementRate = getOverallAchievementRate();
        
        if (achievementRate >= 100.0) return ProgressStatus.COMPLETED;
        if (achievementRate >= 80.0) return ProgressStatus.ALMOST_DONE;
        if (achievementRate >= 50.0) return ProgressStatus.ON_TRACK;
        if (achievementRate >= 25.0) return ProgressStatus.BEHIND;
        return ProgressStatus.FAR_BEHIND;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final StudyGoal studyGoal = (StudyGoal) obj;
        return Objects.equals(entityBase.getId(), studyGoal.entityBase.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(entityBase.getId());
    }
    
    @Override
    public String toString() {
        return "StudyGoal{" +
                "id=" + getId() +
                ", studyPlanId=" + studyPlanId +
                ", categoryId=" + categoryId +
                ", targetScore=" + targetScore +
                ", targetHours=" + targetHours +
                ", currentBestScore=" + currentBestScore +
                ", totalStudiedHours=" + totalStudiedHours +
                ", achievementRate=" + String.format("%.1f%%", getOverallAchievementRate()) +
                ", difficulty=" + getDifficulty() +
                ", progressStatus=" + getProgressStatus() +
                '}';
    }
    
    // Private helper methods（ガード節使用）
    private int calculateUpdatedBestScore(final Integer newScore) {
        if (newScore == null) return this.currentBestScore;
        if (newScore < MIN_SCORE || newScore > MAX_SCORE) return this.currentBestScore;
        return Math.max(this.currentBestScore, newScore);
    }
    
    private int calculateUpdatedTotalHours(final Integer additionalHours) {
        if (additionalHours == null) return this.totalStudiedHours;
        if (additionalHours <= 0) return this.totalStudiedHours;
        return this.totalStudiedHours + additionalHours;
    }
    
    private static int validateScore(final int score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException("スコアは" + MIN_SCORE + "-" + MAX_SCORE + "の範囲で設定してください");
        }
        return score;
    }
    
    private static int validateHours(final int hours) {
        if (hours < 0) {
            throw new IllegalArgumentException("目標学習時間は0時間以上で設定してください");
        }
        if (hours > MAX_TARGET_HOURS) {
            throw new IllegalArgumentException("目標学習時間は" + MAX_TARGET_HOURS + "時間以内で設定してください");
        }
        return hours;
    }
    
    /**
     * 目標の難易度
     */
    public enum GoalDifficulty {
        VERY_EASY("とても易しい"),
        EASY("易しい"),
        MEDIUM("普通"),
        HARD("難しい"),
        VERY_HARD("とても難しい");
        
        private final String displayName;
        
        GoalDifficulty(final String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * 進捗ステータス
     */
    public enum ProgressStatus {
        COMPLETED("完了"),
        ALMOST_DONE("もうすぐ完了"),
        ON_TRACK("順調"),
        BEHIND("遅れ気味"),
        FAR_BEHIND("大幅に遅れ");
        
        private final String displayName;
        
        ProgressStatus(final String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
