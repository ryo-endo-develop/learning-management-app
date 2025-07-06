package com.learningapp.plan.domain.entity;

import com.learningapp.base.domain.entity.EntityBase;
import com.learningapp.base.domain.entity.EntityMarker;
import com.learningapp.base.domain.valueobject.StudyCategoryId;
import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.base.domain.valueobject.StudyPlanId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 学習目標エンティティ
 * 学習計画の集約内エンティティ
 * Effective Java Item 18: 継承よりもコンポジション
 * Effective Java Item 17: 可変性を最小限に抑える
 * Effective Java Item 55: Optionalを適切に使用する
 */
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
    
    private StudyGoal(final StudyGoalId id, final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                     final int targetScore, final int targetHours, final int currentBestScore, final int totalStudiedHours) {
        this.entityBase = new EntityBase<>(id);
        this.studyPlanId = Objects.requireNonNull(studyPlanId, "StudyPlanId must not be null");
        this.categoryId = Objects.requireNonNull(categoryId, "StudyCategoryId must not be null");
        this.targetScore = targetScore;
        this.targetHours = targetHours;
        this.currentBestScore = Math.max(0, currentBestScore);
        this.totalStudiedHours = Math.max(0, totalStudiedHours);
    }
    
    /**
     * 新規学習目標作成
     */
    public static StudyGoal create(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                   final Integer targetScore, final Integer targetHours) {
        final int validatedTargetScore = validateAndGetScore(targetScore);
        final int validatedTargetHours = validateAndGetHours(targetHours);
        
        return new StudyGoal(StudyGoalId.generate(), studyPlanId, categoryId, 
                           validatedTargetScore, validatedTargetHours, 0, 0);
    }
    
    /**
     * 既存学習目標復元（永続化層から）
     */
    public static StudyGoal restore(final StudyGoalId id, final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                   final Integer targetScore, final Integer targetHours, 
                                   final Integer currentBestScore, final Integer totalStudiedHours) {
        final int validatedTargetScore = validateAndGetScore(targetScore);
        final int validatedTargetHours = validateAndGetHours(targetHours);
        final int validatedCurrentBestScore = Optional.ofNullable(currentBestScore).orElse(0);
        final int validatedTotalStudiedHours = Optional.ofNullable(totalStudiedHours).orElse(0);
        
        return new StudyGoal(id, studyPlanId, categoryId, validatedTargetScore, validatedTargetHours,
                           validatedCurrentBestScore, validatedTotalStudiedHours);
    }
    
    /**
     * 目標更新（新しいインスタンスを返す - 不変性）
     */
    public StudyGoal updateGoal(final Integer newTargetScore, final Integer newTargetHours) {
        final int validatedTargetScore = validateAndGetScore(newTargetScore);
        final int validatedTargetHours = validateAndGetHours(newTargetHours);
        
        final StudyGoal updatedGoal = new StudyGoal(
            this.getId(), this.studyPlanId, this.categoryId,
            validatedTargetScore, validatedTargetHours, 
            this.currentBestScore, this.totalStudiedHours
        );
        updatedGoal.entityBase.updateTimestamp();
        return updatedGoal;
    }
    
    /**
     * 進捗更新（新しいインスタンスを返す - 不変性）
     */
    public StudyGoal updateProgress(final Integer newScore, final Integer additionalHours) {
        final int updatedBestScore = Optional.ofNullable(newScore)
            .filter(score -> score >= MIN_SCORE && score <= MAX_SCORE)
            .map(score -> Math.max(this.currentBestScore, score))
            .orElse(this.currentBestScore);
            
        final int updatedTotalHours = Optional.ofNullable(additionalHours)
            .filter(hours -> hours > 0)
            .map(hours -> this.totalStudiedHours + hours)
            .orElse(this.totalStudiedHours);
        
        final StudyGoal updatedGoal = new StudyGoal(
            this.getId(), this.studyPlanId, this.categoryId,
            this.targetScore, this.targetHours, 
            updatedBestScore, updatedTotalHours
        );
        updatedGoal.entityBase.updateTimestamp();
        return updatedGoal;
    }
    
    // Getters
    public StudyPlanId getStudyPlanId() {
        return studyPlanId;
    }
    
    public StudyCategoryId getCategoryId() {
        return categoryId;
    }
    
    public int getTargetScore() {
        return targetScore;
    }
    
    public int getTargetHours() {
        return targetHours;
    }
    
    public int getCurrentBestScore() {
        return currentBestScore;
    }
    
    public int getTotalStudiedHours() {
        return totalStudiedHours;
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
    
    /**
     * 残り必要学習時間
     */
    public int getRemainingHours() {
        return Math.max(0, targetHours - totalStudiedHours);
    }
    
    /**
     * 目標スコアまでの差
     */
    public int getScoreGap() {
        return Math.max(0, targetScore - currentBestScore);
    }
    
    /**
     * 全体の達成率（スコアと時間の平均）
     */
    public double getOverallAchievementRate() {
        return (getScoreAchievementRate() + getHoursAchievementRate()) / 2.0;
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
                '}';
    }
    
    private static int validateAndGetScore(final Integer score) {
        if (score == null) {
            throw new IllegalArgumentException("目標スコアは必須です");
        }
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException("スコアは" + MIN_SCORE + "-" + MAX_SCORE + "の範囲で設定してください");
        }
        return score;
    }
    
    private static int validateAndGetHours(final Integer hours) {
        if (hours == null) {
            throw new IllegalArgumentException("目標学習時間は必須です");
        }
        if (hours < 0) {
            throw new IllegalArgumentException("目標学習時間は0時間以上で設定してください");
        }
        if (hours > MAX_TARGET_HOURS) {
            throw new IllegalArgumentException("目標学習時間は" + MAX_TARGET_HOURS + "時間以内で設定してください");
        }
        return hours;
    }
}
