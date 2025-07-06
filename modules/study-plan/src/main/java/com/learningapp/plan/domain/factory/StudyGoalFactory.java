package com.learningapp.plan.domain.factory;

import com.learningapp.base.domain.valueobject.StudyCategoryId;
import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.plan.domain.entity.StudyGoal;
import com.learningapp.plan.domain.validator.StudyGoalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * StudyGoalエンティティのファクトリクラス
 * Validator Strategy Patternを使用
 */
@Component
@RequiredArgsConstructor
public class StudyGoalFactory {
    
    private final StudyGoalValidator validator;
    
    /**
     * 新規学習目標作成
     */
    public StudyGoal createNewGoal(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                   final int targetScore, final int targetHours) {
        Objects.requireNonNull(studyPlanId, "StudyPlanId must not be null");
        Objects.requireNonNull(categoryId, "StudyCategoryId must not be null");
        
        final int validatedScore = validator.validateScore(targetScore);
        final int validatedHours = validator.validateHours(targetHours);
        
        // 目標の妥当性評価
        final var assessment = validator.assessGoalValidity(validatedScore, validatedHours);
        if (!assessment.isValid()) {
            throw new IllegalArgumentException(assessment.message());
        }
        
        return new StudyGoal(StudyGoalId.generate(), studyPlanId, categoryId, validatedScore, validatedHours, 0, 0);
    }
    
    /**
     * 既存学習目標復元（永続化層から）
     */
    public StudyGoal restoreGoal(final StudyGoalId id, final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                final int targetScore, final int targetHours, 
                                final int currentBestScore, final int totalStudiedHours) {
        Objects.requireNonNull(id, "StudyGoalId must not be null");
        Objects.requireNonNull(studyPlanId, "StudyPlanId must not be null");
        Objects.requireNonNull(categoryId, "StudyCategoryId must not be null");
        
        final int validatedScore = validator.validateScore(targetScore);
        final int validatedHours = validator.validateHours(targetHours);
        
        return new StudyGoal(id, studyPlanId, categoryId, validatedScore, validatedHours, 
                           Math.max(0, currentBestScore), Math.max(0, totalStudiedHours));
    }
    
    /**
     * データベーススペシャリスト試験用デフォルト目標作成
     */
    public List<StudyGoal> createDefaultDatabaseSpecialistGoals(final StudyPlanId studyPlanId, 
                                                               final List<StudyCategoryId> categoryIds) {
        Objects.requireNonNull(studyPlanId, "StudyPlanId must not be null");
        Objects.requireNonNull(categoryIds, "CategoryIds must not be null");
        
        if (categoryIds.size() < 8) {
            throw new IllegalArgumentException("デフォルト目標作成には8つのカテゴリが必要です");
        }
        
        return List.of(
            createNewGoal(studyPlanId, categoryIds.get(0), 70, 30), // 午前I
            createNewGoal(studyPlanId, categoryIds.get(1), 80, 40), // 午前II
            createNewGoal(studyPlanId, categoryIds.get(2), 60, 50), // 午後I
            createNewGoal(studyPlanId, categoryIds.get(3), 60, 30), // 午後II
            createNewGoal(studyPlanId, categoryIds.get(4), 85, 25), // SQL実践
            createNewGoal(studyPlanId, categoryIds.get(5), 75, 35), // データベース設計
            createNewGoal(studyPlanId, categoryIds.get(6), 70, 20), // パフォーマンス
            createNewGoal(studyPlanId, categoryIds.get(7), 65, 15)  // 運用管理
        );
    }
    
    /**
     * 高得点目標の学習目標作成（上級者向け）
     */
    public StudyGoal createHighScoreGoal(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                        final int baseTargetHours) {
        final int enhancedHours = Math.min(baseTargetHours * 2, 100);
        return createNewGoal(studyPlanId, categoryId, 90, enhancedHours);
    }
    
    /**
     * 基礎固め目標の学習目標作成（初心者向け）
     */
    public StudyGoal createBasicGoal(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                    final int targetHours) {
        return createNewGoal(studyPlanId, categoryId, 60, targetHours);
    }
    
    /**
     * 短期集中目標の学習目標作成
     */
    public StudyGoal createIntensiveGoal(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                        final int targetScore) {
        if (targetScore < 70) {
            throw new IllegalArgumentException("短期集中目標のスコアは70以上で設定してください");
        }
        
        final int intensiveHours = targetScore > 80 ? 15 : 10;
        return createNewGoal(studyPlanId, categoryId, targetScore, intensiveHours);
    }
    
    /**
     * 合格ライン目標作成（確実な合格を目指す）
     */
    public StudyGoal createPassingGoal(final StudyPlanId studyPlanId, final StudyCategoryId categoryId) {
        // 合格ライン（60点）より少し余裕を持った設定
        return createNewGoal(studyPlanId, categoryId, 65, 20);
    }
    
    /**
     * 進捗更新用ヘルパーメソッド
     */
    public StudyGoal updateGoalProgress(final StudyGoal goal, final Integer newScore, final Integer additionalHours) {
        final var validation = validator.validateProgressUpdate(newScore, additionalHours);
        if (!validation.isValid()) {
            throw new IllegalArgumentException(validation.message());
        }
        
        return goal.updateProgress(newScore, additionalHours);
    }
}
