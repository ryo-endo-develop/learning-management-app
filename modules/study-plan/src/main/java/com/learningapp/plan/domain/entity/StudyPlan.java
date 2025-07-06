package com.learningapp.plan.domain.entity;

import com.learningapp.base.domain.entity.AggregateRoot;
import com.learningapp.base.domain.enums.StudyPlanStatus;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.base.domain.valueobject.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 学習計画エンティティ
 * 集約ルート
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyPlan extends AggregateRoot<StudyPlanId> {
    
    private UserId userId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private StudyPlanStatus status;
    private Integer targetHoursPerDay;
    
    public StudyPlan(StudyPlanId id, UserId userId, String title, String description, 
                     LocalDate startDate, LocalDate endDate, Integer targetHoursPerDay) {
        super(id);
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = StudyPlanStatus.ACTIVE;
        this.targetHoursPerDay = targetHoursPerDay;
    }
    
    public static StudyPlan create(UserId userId, String title, String description,
                                   LocalDate startDate, LocalDate endDate, Integer targetHoursPerDay) {
        validateInputs(title, startDate, endDate, targetHoursPerDay);
        return new StudyPlan(StudyPlanId.generate(), userId, title, description, 
                           startDate, endDate, targetHoursPerDay);
    }
    
    public void updatePlan(String title, String description, LocalDate startDate, 
                          LocalDate endDate, Integer targetHoursPerDay) {
        validateInputs(title, startDate, endDate, targetHoursPerDay);
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetHoursPerDay = targetHoursPerDay;
        updateTimestamp();
    }
    
    public void complete() {
        if (this.status == StudyPlanStatus.COMPLETED) {
            throw new IllegalStateException("学習計画は既に完了しています");
        }
        this.status = StudyPlanStatus.COMPLETED;
        updateTimestamp();
    }
    
    public void pause() {
        if (this.status != StudyPlanStatus.ACTIVE) {
            throw new IllegalStateException("実施中の学習計画のみ一時停止できます");
        }
        this.status = StudyPlanStatus.PAUSED;
        updateTimestamp();
    }
    
    public void resume() {
        if (this.status != StudyPlanStatus.PAUSED) {
            throw new IllegalStateException("一時停止中の学習計画のみ再開できます");
        }
        this.status = StudyPlanStatus.ACTIVE;
        updateTimestamp();
    }
    
    public void cancel() {
        if (this.status == StudyPlanStatus.COMPLETED) {
            throw new IllegalStateException("完了した学習計画はキャンセルできません");
        }
        this.status = StudyPlanStatus.CANCELLED;
        updateTimestamp();
    }
    
    public boolean isActive() {
        return this.status == StudyPlanStatus.ACTIVE;
    }
    
    public long getDurationDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    private static void validateInputs(String title, LocalDate startDate, LocalDate endDate, Integer targetHoursPerDay) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("学習計画のタイトルは必須です");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("タイトルは200文字以内で入力してください");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("開始日は必須です");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("終了日は必須です");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("開始日は終了日より前である必要があります");
        }
        if (targetHoursPerDay != null && (targetHoursPerDay < 1 || targetHoursPerDay > 24)) {
            throw new IllegalArgumentException("1日の目標学習時間は1-24時間の範囲で設定してください");
        }
    }
}
