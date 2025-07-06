package com.learningapp.plan.domain.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 学習計画バリデーター
 * Strategy Pattern + Dependency Injection
 */
@Component
public class StudyPlanValidator {
    
    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MIN_TARGET_HOURS = 1;
    private static final int MAX_TARGET_HOURS = 24;
    private static final int MAX_PLAN_DURATION_DAYS = 365;
    
    /**
     * タイトルを検証し、正規化して返す
     */
    public String validateAndNormalizeTitle(final String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("学習計画のタイトルは必須です");
        }
        
        final String trimmedTitle = title.trim();
        
        if (trimmedTitle.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("タイトルは" + MAX_TITLE_LENGTH + "文字以内で入力してください");
        }
        
        validateTitleBusinessRules(trimmedTitle);
        
        return trimmedTitle;
    }
    
    /**
     * 目標学習時間を検証
     */
    public int validateTargetHours(final int targetHoursPerDay) {
        if (targetHoursPerDay < MIN_TARGET_HOURS || targetHoursPerDay > MAX_TARGET_HOURS) {
            throw new IllegalArgumentException(
                String.format("1日の目標学習時間は%d-%d時間の範囲で設定してください", MIN_TARGET_HOURS, MAX_TARGET_HOURS)
            );
        }
        
        return targetHoursPerDay;
    }
    
    /**
     * 日付範囲を検証
     */
    public void validateDateRange(final LocalDate startDate, final LocalDate endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("開始日は必須です");
        }
        
        if (endDate == null) {
            throw new IllegalArgumentException("終了日は必須です");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("開始日は終了日より前である必要があります");
        }
        
        final long durationDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        if (durationDays > MAX_PLAN_DURATION_DAYS) {
            throw new IllegalArgumentException("学習計画の期間は" + MAX_PLAN_DURATION_DAYS + "日以内で設定してください");
        }
        
        validateDateBusinessRules(startDate, endDate);
    }
    
    /**
     * 学習計画の実現可能性を評価
     */
    public PlanFeasibility evaluateFeasibility(final LocalDate startDate, final LocalDate endDate, final int targetHoursPerDay) {
        final long durationDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        final long totalHours = durationDays * targetHoursPerDay;
        
        if (totalHours < 50) {
            return new PlanFeasibility(false, "総学習時間が少なすぎます。より長期間またはより多くの時間を設定してください。");
        }
        
        if (targetHoursPerDay > 8) {
            return new PlanFeasibility(false, "1日8時間を超える学習は継続が困難です。");
        }
        
        if (durationDays < 30) {
            return new PlanFeasibility(true, "短期集中プランです。集中的な学習が必要になります。");
        }
        
        return new PlanFeasibility(true, "実現可能な学習計画です。");
    }
    
    private void validateTitleBusinessRules(final String title) {
        if (containsInappropriateWords(title)) {
            throw new IllegalArgumentException("タイトルに不適切な単語が含まれています");
        }
    }
    
    private void validateDateBusinessRules(final LocalDate startDate, final LocalDate endDate) {
        final LocalDate today = LocalDate.now();
        
        if (startDate.isBefore(today.minusDays(7))) {
            throw new IllegalArgumentException("開始日は過去1週間より前に設定できません");
        }
        
        // 休日や祝日を考慮した検証（将来的な拡張ポイント）
    }
    
    private boolean containsInappropriateWords(final String title) {
        final String lowerTitle = title.toLowerCase();
        return lowerTitle.contains("test") || lowerTitle.contains("dummy");
    }
    
    /**
     * 計画の実現可能性評価結果
     */
    public record PlanFeasibility(
        boolean isFeasible,
        String message
    ) {}
}
