package com.learningapp.plan.domain.factory;

import com.learningapp.base.domain.enums.StudyPlanStatus;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.base.domain.valueobject.UserId;
import com.learningapp.plan.domain.entity.StudyPlan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

/**
 * StudyPlanエンティティのファクトリクラス
 * Package-privateコンストラクタを直接呼び出し
 */
@Component
public class StudyPlanFactory {
    
    private static final int DEFAULT_TARGET_HOURS = 2;
    
    /**
     * 新規学習計画作成
     */
    public StudyPlan createNewStudyPlan(final UserId userId, final String title, final String description,
                                       final LocalDate startDate, final LocalDate endDate, 
                                       final Integer targetHoursPerDay) {
        validateCreationInputs(userId, title, startDate, endDate, targetHoursPerDay);
        
        final int validatedTargetHours = targetHoursPerDay != null ? targetHoursPerDay : DEFAULT_TARGET_HOURS;
        
        return new StudyPlan(StudyPlanId.generate(), userId, title, description, 
                           startDate, endDate, StudyPlanStatus.ACTIVE, validatedTargetHours);  // 直接new
    }
    
    /**
     * 既存学習計画復元（永続化層から）
     */
    public StudyPlan restoreStudyPlan(final StudyPlanId id, final UserId userId, final String title, 
                                     final String description, final LocalDate startDate, final LocalDate endDate,
                                     final StudyPlanStatus status, final Integer targetHoursPerDay) {
        validateRestorationInputs(id, userId, title, startDate, endDate, status, targetHoursPerDay);
        
        final int validatedTargetHours = targetHoursPerDay != null ? targetHoursPerDay : DEFAULT_TARGET_HOURS;
        
        return new StudyPlan(id, userId, title, description, startDate, endDate, status, validatedTargetHours);  // 直接new
    }
    
    /**
     * データベーススペシャリスト試験用学習計画作成
     */
    public StudyPlan createDatabaseSpecialistPlan(final UserId userId, final LocalDate examDate) {
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = examDate.minusDays(7); // 試験1週間前に完了
        
        validateExamDate(examDate);
        
        return createNewStudyPlan(
            userId,
            "データベーススペシャリスト合格への道",
            "データベーススペシャリスト試験に合格するための学習計画",
            startDate,
            endDate,
            2 // 1日2時間の学習
        );
    }
    
    /**
     * 短期集中学習計画作成
     */
    public StudyPlan createIntensivePlan(final UserId userId, final String title, final int durationDays) {
        if (durationDays < 7 || durationDays > 30) {
            throw new IllegalArgumentException("短期集中学習計画は7-30日の範囲で設定してください");
        }
        
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = startDate.plusDays(durationDays);
        
        return createNewStudyPlan(
            userId,
            title + "（短期集中）",
            "短期集中学習計画（" + durationDays + "日間）",
            startDate,
            endDate,
            4 // 1日4時間の集中学習
        );
    }
    
    private void validateCreationInputs(final UserId userId, final String title, 
                                       final LocalDate startDate, final LocalDate endDate, 
                                       final Integer targetHoursPerDay) {
        Objects.requireNonNull(userId, "UserId must not be null");
        Objects.requireNonNull(title, "Title must not be null");
        Objects.requireNonNull(startDate, "Start date must not be null");
        Objects.requireNonNull(endDate, "End date must not be null");
        
        if (startDate.isBefore(LocalDate.now().minusDays(1))) {
            throw new IllegalArgumentException("開始日は昨日以降で設定してください");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("開始日は終了日より前である必要があります");
        }
        
        final long durationDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        if (durationDays > 365) {
            throw new IllegalArgumentException("学習計画の期間は1年以内で設定してください");
        }
    }
    
    private void validateRestorationInputs(final StudyPlanId id, final UserId userId, final String title,
                                          final LocalDate startDate, final LocalDate endDate,
                                          final StudyPlanStatus status, final Integer targetHoursPerDay) {
        Objects.requireNonNull(id, "StudyPlanId must not be null");
        Objects.requireNonNull(status, "StudyPlanStatus must not be null");
        
        // 復元時は過去日も許可（履歴データのため）
        validateCreationInputsForRestore(userId, title, startDate, endDate, targetHoursPerDay);
    }
    
    private void validateCreationInputsForRestore(final UserId userId, final String title, 
                                                 final LocalDate startDate, final LocalDate endDate, 
                                                 final Integer targetHoursPerDay) {
        Objects.requireNonNull(userId, "UserId must not be null");
        Objects.requireNonNull(title, "Title must not be null");
        Objects.requireNonNull(startDate, "Start date must not be null");
        Objects.requireNonNull(endDate, "End date must not be null");
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("開始日は終了日より前である必要があります");
        }
    }
    
    private void validateExamDate(final LocalDate examDate) {
        if (examDate.isBefore(LocalDate.now().plusDays(14))) {
            throw new IllegalArgumentException("試験日は最低2週間後に設定してください");
        }
    }
}
