package com.learningapp.plan.domain.factory;

import com.learningapp.base.domain.enums.StudyPlanStatus;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.base.domain.valueobject.UserId;
import com.learningapp.plan.domain.entity.StudyPlan;
import com.learningapp.plan.domain.validator.StudyPlanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

/**
 * StudyPlanエンティティのファクトリクラス
 * Validator Strategy Patternを使用
 */
@Component
@RequiredArgsConstructor
public class StudyPlanFactory {
    
    private final StudyPlanValidator validator;
    private static final int DEFAULT_TARGET_HOURS = 2;
    
    /**
     * 新規学習計画作成
     */
    public StudyPlan createNewStudyPlan(final UserId userId, final String title, final String description,
                                       final LocalDate startDate, final LocalDate endDate, 
                                       final Integer targetHoursPerDay) {
        Objects.requireNonNull(userId, "UserId must not be null");
        
        final String validatedTitle = validator.validateAndNormalizeTitle(title);
        validator.validateDateRange(startDate, endDate);
        
        final int validatedTargetHours = targetHoursPerDay != null ? 
            validator.validateTargetHours(targetHoursPerDay) : DEFAULT_TARGET_HOURS;
        
        // 実現可能性評価
        final var feasibility = validator.evaluateFeasibility(startDate, endDate, validatedTargetHours);
        if (!feasibility.isFeasible()) {
            throw new IllegalArgumentException(feasibility.message());
        }
        
        return new StudyPlan(StudyPlanId.generate(), userId, validatedTitle, description, 
                           startDate, endDate, StudyPlanStatus.ACTIVE, validatedTargetHours);
    }
    
    /**
     * 既存学習計画復元（永続化層から）
     */
    public StudyPlan restoreStudyPlan(final StudyPlanId id, final UserId userId, final String title, 
                                     final String description, final LocalDate startDate, final LocalDate endDate,
                                     final StudyPlanStatus status, final Integer targetHoursPerDay) {
        Objects.requireNonNull(id, "StudyPlanId must not be null");
        Objects.requireNonNull(userId, "UserId must not be null");
        Objects.requireNonNull(status, "StudyPlanStatus must not be null");
        
        final String validatedTitle = validator.validateAndNormalizeTitle(title);
        validator.validateDateRange(startDate, endDate);
        
        final int validatedTargetHours = targetHoursPerDay != null ? 
            validator.validateTargetHours(targetHoursPerDay) : DEFAULT_TARGET_HOURS;
        
        return new StudyPlan(id, userId, validatedTitle, description, startDate, endDate, status, validatedTargetHours);
    }
    
    /**
     * データベーススペシャリスト試験用学習計画作成
     */
    public StudyPlan createDatabaseSpecialistPlan(final UserId userId, final LocalDate examDate) {
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = examDate.minusDays(7); // 試験1週間前に完了
        
        if (examDate.isBefore(LocalDate.now().plusDays(14))) {
            throw new IllegalArgumentException("試験日は最低2週間後に設定してください");
        }
        
        return createNewStudyPlan(
            userId,
            "データベーススペシャリスト合格への道",
            "データベーススペシャリスト試験に合格するための学習計画",
            startDate,
            endDate,
            2
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
            4
        );
    }
    
    /**
     * 長期学習計画作成
     */
    public StudyPlan createLongTermPlan(final UserId userId, final String title, final String description,
                                       final LocalDate startDate, final LocalDate endDate) {
        final long durationDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        
        if (durationDays < 90) {
            throw new IllegalArgumentException("長期学習計画は90日以上で設定してください");
        }
        
        // 長期計画は無理のない時間設定
        return createNewStudyPlan(userId, title, description, startDate, endDate, 1);
    }
}
