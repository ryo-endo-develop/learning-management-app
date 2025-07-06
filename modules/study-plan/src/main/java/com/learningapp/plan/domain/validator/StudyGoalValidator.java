package com.learningapp.plan.domain.validator;

import org.springframework.stereotype.Component;

/**
 * 学習目標バリデーター
 * Strategy Pattern + Dependency Injection
 */
@Component
public class StudyGoalValidator {
    
    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 100;
    private static final int MAX_TARGET_HOURS = 10000;
    
    /**
     * スコアを検証
     */
    public int validateScore(final int score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException("スコアは" + MIN_SCORE + "-" + MAX_SCORE + "の範囲で設定してください");
        }
        
        return score;
    }
    
    /**
     * 学習時間を検証
     */
    public int validateHours(final int hours) {
        if (hours < 0) {
            throw new IllegalArgumentException("目標学習時間は0時間以上で設定してください");
        }
        
        if (hours > MAX_TARGET_HOURS) {
            throw new IllegalArgumentException("目標学習時間は" + MAX_TARGET_HOURS + "時間以内で設定してください");
        }
        
        return hours;
    }
    
    /**
     * 目標の妥当性を評価
     */
    public GoalValidityAssessment assessGoalValidity(final int targetScore, final int targetHours) {
        validateScore(targetScore);
        validateHours(targetHours);
        
        // 目標の妥当性評価
        if (targetScore >= 90 && targetHours < 20) {
            return new GoalValidityAssessment(
                false, 
                "高いスコア目標には十分な学習時間が必要です。最低20時間以上を推奨します。"
            );
        }
        
        if (targetScore <= 50 && targetHours > 100) {
            return new GoalValidityAssessment(
                false,
                "低いスコア目標に対して学習時間が過剰です。より高い目標設定を検討してください。"
            );
        }
        
        if (targetHours > 500) {
            return new GoalValidityAssessment(
                false,
                "学習時間が非常に多く設定されています。現実的な目標に調整することを推奨します。"
            );
        }
        
        return new GoalValidityAssessment(true, "適切な目標設定です。");
    }
    
    /**
     * 進捗更新値を検証
     */
    public ProgressUpdateValidation validateProgressUpdate(final Integer newScore, final Integer additionalHours) {
        if (newScore != null) {
            if (newScore < MIN_SCORE || newScore > MAX_SCORE) {
                return new ProgressUpdateValidation(
                    false, 
                    "スコアは" + MIN_SCORE + "-" + MAX_SCORE + "の範囲で入力してください"
                );
            }
        }
        
        if (additionalHours != null) {
            if (additionalHours < 0) {
                return new ProgressUpdateValidation(
                    false,
                    "追加学習時間は0時間以上で入力してください"
                );
            }
            
            if (additionalHours > 24) {
                return new ProgressUpdateValidation(
                    false,
                    "1日に24時間を超える学習時間は設定できません"
                );
            }
        }
        
        return new ProgressUpdateValidation(true, "有効な進捗更新です");
    }
    
    /**
     * 目標の妥当性評価結果
     */
    public record GoalValidityAssessment(
        boolean isValid,
        String message
    ) {}
    
    /**
     * 進捗更新の検証結果
     */
    public record ProgressUpdateValidation(
        boolean isValid,
        String message
    ) {}
}
