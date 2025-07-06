package com.learningapp.plan.domain.entity;

import com.learningapp.base.domain.entity.AggregateRootMarker;
import com.learningapp.base.domain.entity.EntityBase;
import com.learningapp.base.domain.enums.StudyPlanStatus;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.base.domain.valueobject.UserId;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 学習計画エンティティ
 * 集約ルート
 */
@Getter
public final class StudyPlan implements AggregateRootMarker<StudyPlanId> {
    
    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MIN_TARGET_HOURS = 1;
    private static final int MAX_TARGET_HOURS = 24;
    private static final int DEFAULT_TARGET_HOURS = 2;
    
    private final EntityBase<StudyPlanId> entityBase;
    private final UserId userId;
    private final String title;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final StudyPlanStatus status;
    private final int targetHoursPerDay;
    
    // Package-private：Factoryからのみアクセス可能
    StudyPlan(final StudyPlanId id, final UserId userId, final String title, 
             final String description, final LocalDate startDate, final LocalDate endDate, 
             final StudyPlanStatus status, final int targetHoursPerDay) {
        this.entityBase = new EntityBase<>(id);
        this.userId = Objects.requireNonNull(userId, "User ID must not be null");
        this.title = validateAndGetTitle(title);
        this.description = Optional.ofNullable(description).orElse("");
        this.startDate = Objects.requireNonNull(startDate, "Start date must not be null");
        this.endDate = Objects.requireNonNull(endDate, "End date must not be null");
        this.status = Objects.requireNonNull(status, "Status must not be null");
        this.targetHoursPerDay = targetHoursPerDay;
        
        validateDateRange(startDate, endDate);
    }
    
    /**
     * 学習計画更新（新しいインスタンスを返す - 不変性）
     */
    public StudyPlan updatePlan(final String newTitle, final String newDescription, 
                               final LocalDate newStartDate, final LocalDate newEndDate, 
                               final Integer newTargetHoursPerDay) {
        final int validatedTargetHours = Optional.ofNullable(newTargetHoursPerDay)
            .map(StudyPlan::validateAndGetTargetHours)
            .orElse(this.targetHoursPerDay);
            
        final StudyPlan updatedPlan = new StudyPlan(
            this.getId(), this.userId, newTitle, newDescription, 
            newStartDate, newEndDate, this.status, validatedTargetHours
        );
        updatedPlan.entityBase.updateTimestamp();
        return updatedPlan;
    }
    
    /**
     * ステータス変更
     */
    public StudyPlan complete() {
        if (this.status == StudyPlanStatus.COMPLETED) {
            throw new IllegalStateException("学習計画は既に完了しています");
        }
        return changeStatus(StudyPlanStatus.COMPLETED);
    }
    
    public StudyPlan pause() {
        if (this.status != StudyPlanStatus.ACTIVE) {
            throw new IllegalStateException("実施中の学習計画のみ一時停止できます");
        }
        return changeStatus(StudyPlanStatus.PAUSED);
    }
    
    public StudyPlan resume() {
        if (this.status != StudyPlanStatus.PAUSED) {
            throw new IllegalStateException("一時停止中の学習計画のみ再開できます");
        }
        return changeStatus(StudyPlanStatus.ACTIVE);
    }
    
    public StudyPlan cancel() {
        if (this.status == StudyPlanStatus.COMPLETED) {
            throw new IllegalStateException("完了した学習計画はキャンセルできません");
        }
        return changeStatus(StudyPlanStatus.CANCELLED);
    }
    
    // EntityMarkerの実装
    @Override
    public StudyPlanId getId() {
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
    public boolean isActive() {
        return this.status == StudyPlanStatus.ACTIVE;
    }
    
    public long getDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    public boolean isOverdue() {
        return LocalDate.now().isAfter(endDate) && status == StudyPlanStatus.ACTIVE;
    }
    
    public long getRemainingDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }
    
    /**
     * 完了期限まで1週間以内かどうか
     */
    public boolean isNearDeadline() {
        return getRemainingDays() <= 7 && getRemainingDays() >= 0;
    }
    
    /**
     * 総目標学習時間
     */
    public long getTotalTargetHours() {
        return getDurationDays() * targetHoursPerDay;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final StudyPlan studyPlan = (StudyPlan) obj;
        return Objects.equals(entityBase.getId(), studyPlan.entityBase.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(entityBase.getId());
    }
    
    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + getId() +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", targetHoursPerDay=" + targetHoursPerDay +
                '}';
    }
    
    private StudyPlan changeStatus(final StudyPlanStatus newStatus) {
        final StudyPlan updatedPlan = new StudyPlan(
            this.getId(), this.userId, this.title, this.description,
            this.startDate, this.endDate, newStatus, this.targetHoursPerDay
        );
        updatedPlan.entityBase.updateTimestamp();
        return updatedPlan;
    }
    
    private static String validateAndGetTitle(final String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("学習計画のタイトルは必須です");
        }
        final String trimmedTitle = title.trim();
        if (trimmedTitle.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("タイトルは" + MAX_TITLE_LENGTH + "文字以内で入力してください");
        }
        return trimmedTitle;
    }
    
    private static int validateAndGetTargetHours(final int targetHoursPerDay) {
        if (targetHoursPerDay < MIN_TARGET_HOURS || targetHoursPerDay > MAX_TARGET_HOURS) {
            throw new IllegalArgumentException(
                String.format("1日の目標学習時間は%d-%d時間の範囲で設定してください", MIN_TARGET_HOURS, MAX_TARGET_HOURS)
            );
        }
        return targetHoursPerDay;
    }
    
    private static void validateDateRange(final LocalDate startDate, final LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("開始日は終了日より前である必要があります");
        }
    }
}
