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

/**
 * 学習計画エンティティ
 * 集約ルート
 * 
 * staticメソッドを完全削除、StudyPlanValidatorに委譲
 */
@Getter
public final class StudyPlan implements AggregateRootMarker<StudyPlanId> {
    
    private final EntityBase<StudyPlanId> entityBase;
    private final UserId userId;
    private final String title;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final StudyPlanStatus status;
    private final int targetHoursPerDay;
    
    // Package-private：Factoryからのみアクセス可能
    // バリデーションはFactoryで実施済み
    StudyPlan(final StudyPlanId id, final UserId userId, final String title, 
             final String description, final LocalDate startDate, final LocalDate endDate, 
             final StudyPlanStatus status, final int targetHoursPerDay) {
        this.entityBase = new EntityBase<>(id);
        this.userId = Objects.requireNonNull(userId, "User ID must not be null");
        this.title = Objects.requireNonNull(title, "Title must not be null");
        this.description = description != null ? description : "";
        this.startDate = Objects.requireNonNull(startDate, "Start date must not be null");
        this.endDate = Objects.requireNonNull(endDate, "End date must not be null");
        this.status = Objects.requireNonNull(status, "Status must not be null");
        this.targetHoursPerDay = targetHoursPerDay;
    }
    
    /**
     * 学習計画更新（新しいインスタンスを返す - 不変性）
     * バリデーションは呼び出し側（Application Service）で実施
     */
    public StudyPlan updatePlan(final String newTitle, final String newDescription, 
                               final LocalDate newStartDate, final LocalDate newEndDate, 
                               final int newTargetHoursPerDay) {
        final StudyPlan updatedPlan = new StudyPlan(
            this.getId(), this.userId, newTitle, newDescription, 
            newStartDate, newEndDate, this.status, newTargetHoursPerDay
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
    
    public boolean isNearDeadline() {
        return getRemainingDays() <= 7 && getRemainingDays() >= 0;
    }
    
    public long getTotalTargetHours() {
        return getDurationDays() * targetHoursPerDay;
    }
    
    public boolean isShortTerm() {
        return getDurationDays() <= 30;
    }
    
    public boolean isLongTerm() {
        return getDurationDays() >= 90;
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
}
