package com.learningapp.plan.domain.factory;

import com.learningapp.base.domain.valueobject.StudyCategoryId;
import com.learningapp.base.domain.valueobject.StudyGoalId;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.plan.domain.entity.StudyGoal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * StudyGoalエンティティのファクトリクラス
 * Package-privateコンストラクタを使用してエンティティを生成
 */
@Component
public class StudyGoalFactory {
    
    /**
     * 新規学習目標作成
     */
    public StudyGoal createNewGoal(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                   final int targetScore, final int targetHours) {
        validateCreationInputs(studyPlanId, categoryId, targetScore, targetHours);
        
        return new StudyGoal(StudyGoalId.generate(), studyPlanId, categoryId, targetScore, targetHours, 0, 0);
    }
    
    /**
     * 既存学習目標復元（永続化層から）
     */
    public StudyGoal restoreGoal(final StudyGoalId id, final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                final int targetScore, final int targetHours, 
                                final int currentBestScore, final int totalStudiedHours) {
        validateRestorationInputs(id, studyPlanId, categoryId, targetScore, targetHours);
        
        return new StudyGoal(id, studyPlanId, categoryId, targetScore, targetHours, 
                           currentBestScore, totalStudiedHours);
    }
    
    /**
     * データベーススペシャリスト試験用デフォルト目標作成
     */
    public List<StudyGoal> createDefaultDatabaseSpecialistGoals(final StudyPlanId studyPlanId, 
                                                               final List<StudyCategoryId> categoryIds) {
        validateDefaultGoalsInputs(studyPlanId, categoryIds);
        
        return List.of(
            createGoalForCategory(studyPlanId, categoryIds.get(0), 70, 30), // 午前I
            createGoalForCategory(studyPlanId, categoryIds.get(1), 80, 40), // 午前II
            createGoalForCategory(studyPlanId, categoryIds.get(2), 60, 50), // 午後I
            createGoalForCategory(studyPlanId, categoryIds.get(3), 60, 30), // 午後II
            createGoalForCategory(studyPlanId, categoryIds.get(4), 85, 25), // SQL実践
            createGoalForCategory(studyPlanId, categoryIds.get(5), 75, 35), // データベース設計
            createGoalForCategory(studyPlanId, categoryIds.get(6), 70, 20), // パフォーマンス
            createGoalForCategory(studyPlanId, categoryIds.get(7), 65, 15)  // 運用管理
        );
    }
    
    /**
     * 高得点目標の学習目標作成（上級者向け）
     */
    public StudyGoal createHighScoreGoal(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                        final int baseTargetHours) {
        validateCreationInputs(studyPlanId, categoryId, 90, baseTargetHours);
        
        // 高得点目標は通常より多くの学習時間を設定
        final int enhancedHours = Math.min(baseTargetHours * 2, 100);
        return createNewGoal(studyPlanId, categoryId, 90, enhancedHours);
    }
    
    /**
     * 基礎固め目標の学習目標作成（初心者向け）
     */
    public StudyGoal createBasicGoal(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                    final int targetHours) {
        validateCreationInputs(studyPlanId, categoryId, 60, targetHours);
        
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
        
        // 短期集中のため学習時間を多めに設定
        final int intensiveHours = targetScore > 80 ? 15 : 10;
        return createNewGoal(studyPlanId, categoryId, targetScore, intensiveHours);
    }
    
    private StudyGoal createGoalForCategory(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                           final int targetScore, final int targetHours) {
        return createNewGoal(studyPlanId, categoryId, targetScore, targetHours);
    }
    
    private void validateCreationInputs(final StudyPlanId studyPlanId, final StudyCategoryId categoryId,
                                       final int targetScore, final int targetHours) {
        Objects.requireNonNull(studyPlanId, "StudyPlanId must not be null");
        Objects.requireNonNull(categoryId, "StudyCategoryId must not be null");
        
        if (targetScore < 0 || targetScore > 100) {
            throw new IllegalArgumentException("目標スコアは0-100の範囲で設定してください");
        }
        
        if (targetHours < 0) {
            throw new IllegalArgumentException("目標学習時間は0時間以上で設定してください");
        }
        
        if (targetHours > 1000) {
            throw new IllegalArgumentException("目標学習時間は1000時間以内で設定してください");
        }
    }
    
    private void validateRestorationInputs(final StudyGoalId id, final StudyPlanId studyPlanId, 
                                          final StudyCategoryId categoryId, final int targetScore, 
                                          final int targetHours) {
        Objects.requireNonNull(id, "StudyGoalId must not be null");
        validateCreationInputs(studyPlanId, categoryId, targetScore, targetHours);
    }
    
    private void validateDefaultGoalsInputs(final StudyPlanId studyPlanId, final List<StudyCategoryId> categoryIds) {
        Objects.requireNonNull(studyPlanId, "StudyPlanId must not be null");
        Objects.requireNonNull(categoryIds, "CategoryIds must not be null");
        
        if (categoryIds.size() < 8) {
            throw new IllegalArgumentException("デフォルト目標作成には8つのカテゴリが必要です");
        }
        
        final long distinctCount = categoryIds.stream().distinct().count();
        if (distinctCount != categoryIds.size()) {
            throw new IllegalArgumentException("カテゴリIDに重複があります");
        }
    }
}
