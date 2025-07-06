package com.learningapp.plan.domain.strategy;

import com.learningapp.plan.domain.entity.StudyGoal;

import java.util.List;
import java.util.Optional;

/**
 * 学習目標の難易度判定戦略
 * Strategy Pattern
 */
public interface DifficultyStrategy {
    
    boolean matches(int targetScore, int targetHours);
    
    StudyGoal.GoalDifficulty getDifficulty();
    
    String getRecommendation();
    
    /**
     * 複数の戦略から適切なものを選択
     */
    static StudyGoal.GoalDifficulty determineDifficulty(final int targetScore, final int targetHours) {
        return getAllStrategies().stream()
            .filter(strategy -> strategy.matches(targetScore, targetHours))
            .findFirst()
            .map(DifficultyStrategy::getDifficulty)
            .orElse(StudyGoal.GoalDifficulty.VERY_EASY);
    }
    
    /**
     * 推奨事項を取得
     */
    static Optional<String> getRecommendation(final int targetScore, final int targetHours) {
        return getAllStrategies().stream()
            .filter(strategy -> strategy.matches(targetScore, targetHours))
            .findFirst()
            .map(DifficultyStrategy::getRecommendation);
    }
    
    /**
     * 全ての戦略を取得（難易度順）
     */
    static List<DifficultyStrategy> getAllStrategies() {
        return List.of(
            new VeryHardStrategy(),
            new HardStrategy(),
            new MediumStrategy(),
            new EasyStrategy(),
            new VeryEasyStrategy()
        );
    }
}

/**
 * とても難しい目標の戦略
 */
final class VeryHardStrategy implements DifficultyStrategy {
    @Override
    public boolean matches(final int targetScore, final int targetHours) {
        return targetScore >= 90 && targetHours >= 50;
    }
    
    @Override
    public StudyGoal.GoalDifficulty getDifficulty() {
        return StudyGoal.GoalDifficulty.VERY_HARD;
    }
    
    @Override
    public String getRecommendation() {
        return "非常に高い目標です。計画的な学習と十分な休息を心がけてください。";
    }
}

/**
 * 難しい目標の戦略
 */
final class HardStrategy implements DifficultyStrategy {
    @Override
    public boolean matches(final int targetScore, final int targetHours) {
        return targetScore >= 80 && targetHours >= 30;
    }
    
    @Override
    public StudyGoal.GoalDifficulty getDifficulty() {
        return StudyGoal.GoalDifficulty.HARD;
    }
    
    @Override
    public String getRecommendation() {
        return "挑戦的な目標です。継続的な学習が重要になります。";
    }
}

/**
 * 普通の目標の戦略
 */
final class MediumStrategy implements DifficultyStrategy {
    @Override
    public boolean matches(final int targetScore, final int targetHours) {
        return targetScore >= 70 && targetHours >= 20;
    }
    
    @Override
    public StudyGoal.GoalDifficulty getDifficulty() {
        return StudyGoal.GoalDifficulty.MEDIUM;
    }
    
    @Override
    public String getRecommendation() {
        return "バランスの取れた目標です。着実に進めていきましょう。";
    }
}

/**
 * 易しい目標の戦略
 */
final class EasyStrategy implements DifficultyStrategy {
    @Override
    public boolean matches(final int targetScore, final int targetHours) {
        return targetScore >= 60 && targetHours >= 10;
    }
    
    @Override
    public StudyGoal.GoalDifficulty getDifficulty() {
        return StudyGoal.GoalDifficulty.EASY;
    }
    
    @Override
    public String getRecommendation() {
        return "取り組みやすい目標です。基礎をしっかり固めましょう。";
    }
}

/**
 * とても易しい目標の戦略
 */
final class VeryEasyStrategy implements DifficultyStrategy {
    @Override
    public boolean matches(final int targetScore, final int targetHours) {
        return true; // デフォルト
    }
    
    @Override
    public StudyGoal.GoalDifficulty getDifficulty() {
        return StudyGoal.GoalDifficulty.VERY_EASY;
    }
    
    @Override
    public String getRecommendation() {
        return "無理のない目標です。まずは学習習慣を身につけることから始めましょう。";
    }
}
