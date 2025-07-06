package com.learningapp.plan.domain.service;

import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.base.domain.valueobject.UserId;
import com.learningapp.plan.domain.entity.StudyPlan;
import com.learningapp.plan.domain.repository.StudyPlanQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 学習計画ドメインサービス
 * ドメインロジックの実装
 * Factory使用に対応
 */
@Service
@RequiredArgsConstructor
public class StudyPlanDomainService {
    
    private final StudyPlanQueryRepository studyPlanQueryRepository;
    
    /**
     * 学習計画作成時の重複チェック
     */
    public void validateStudyPlanCreation(final UserId userId, final LocalDate startDate, final LocalDate endDate) {
        final List<StudyPlan> overlappingPlans = studyPlanQueryRepository.findOverlappingPlans(userId, startDate, endDate);
        
        if (!overlappingPlans.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("指定期間（%s〜%s）に重複する学習計画が存在します", startDate, endDate)
            );
        }
    }
    
    /**
     * ユーザーのアクティブな学習計画数チェック
     */
    public void validateActiveStudyPlanLimit(final UserId userId) {
        final List<StudyPlan> activePlans = studyPlanQueryRepository.findActiveByUserId(userId);
        
        if (activePlans.size() >= 3) {
            throw new IllegalArgumentException("同時に実行できる学習計画は3つまでです");
        }
    }
    
    /**
     * 学習計画の削除可能性チェック
     */
    public boolean canDeleteStudyPlan(final StudyPlanId studyPlanId) {
        // 学習セッションが記録されている場合は削除不可
        // TODO: StudySession側の実装後に追加
        return true;
    }
    
    /**
     * 学習計画の完了条件チェック
     */
    public boolean isStudyPlanCompletable(final StudyPlan studyPlan) {
        // 終了日が過ぎている、または全目標が達成されている場合
        return LocalDate.now().isAfter(studyPlan.getEndDate()) || 
               hasAllGoalsAchieved(studyPlan.getId());
    }
    
    /**
     * 学習計画の効率性分析
     */
    public PlanEfficiencyAnalysis analyzeEfficiency(final StudyPlan studyPlan) {
        final long durationDays = studyPlan.getDurationDays();
        final long totalTargetHours = studyPlan.getTotalTargetHours();
        final double hoursPerDay = (double) totalTargetHours / durationDays;
        
        EfficiencyLevel level;
        String recommendation;
        
        if (hoursPerDay > 6) {
            level = EfficiencyLevel.OVERLOADED;
            recommendation = "1日の学習時間が多すぎます。計画を見直すことをお勧めします。";
        } else if (hoursPerDay > 4) {
            level = EfficiencyLevel.INTENSIVE;
            recommendation = "集中的な学習計画です。継続可能性を考慮してください。";
        } else if (hoursPerDay >= 2) {
            level = EfficiencyLevel.BALANCED;
            recommendation = "バランスの取れた学習計画です。";
        } else if (hoursPerDay >= 1) {
            level = EfficiencyLevel.LIGHT;
            recommendation = "軽めの学習計画です。目標達成に十分か確認してください。";
        } else {
            level = EfficiencyLevel.INSUFFICIENT;
            recommendation = "学習時間が不足している可能性があります。";
        }
        
        return new PlanEfficiencyAnalysis(level, hoursPerDay, recommendation);
    }
    
    /**
     * 学習計画のリスク評価
     */
    public PlanRiskAssessment assessRisk(final StudyPlan studyPlan) {
        RiskLevel riskLevel = RiskLevel.LOW;
        String riskFactors = "";
        
        // 期間が短すぎるリスク
        if (studyPlan.getDurationDays() < 30) {
            riskLevel = RiskLevel.HIGH;
            riskFactors += "学習期間が短すぎます。";
        }
        
        // 1日の学習時間が多すぎるリスク
        if (studyPlan.getTargetHoursPerDay() > 4) {
            riskLevel = riskLevel == RiskLevel.LOW ? RiskLevel.MEDIUM : RiskLevel.HIGH;
            riskFactors += "1日の学習時間が多すぎる可能性があります。";
        }
        
        // 期限が近すぎるリスク
        if (studyPlan.isNearDeadline()) {
            riskLevel = riskLevel == RiskLevel.LOW ? RiskLevel.MEDIUM : RiskLevel.HIGH;
            riskFactors += "期限が近づいています。";
        }
        
        if (riskFactors.isEmpty()) {
            riskFactors = "リスク要因は見つかりませんでした。";
        }
        
        return new PlanRiskAssessment(riskLevel, riskFactors);
    }
    
    private boolean hasAllGoalsAchieved(final StudyPlanId studyPlanId) {
        // TODO: StudyGoal の実装完了後に実装
        return false;
    }
    
    /**
     * 効率性分析結果
     */
    public record PlanEfficiencyAnalysis(
        EfficiencyLevel level,
        double averageHoursPerDay,
        String recommendation
    ) {}
    
    /**
     * リスク評価結果
     */
    public record PlanRiskAssessment(
        RiskLevel riskLevel,
        String riskFactors
    ) {}
    
    /**
     * 効率性レベル
     */
    public enum EfficiencyLevel {
        INSUFFICIENT("不足"),
        LIGHT("軽め"),
        BALANCED("バランス良好"),
        INTENSIVE("集中的"),
        OVERLOADED("過負荷");
        
        private final String displayName;
        
        EfficiencyLevel(final String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * リスクレベル
     */
    public enum RiskLevel {
        LOW("低"),
        MEDIUM("中"),
        HIGH("高");
        
        private final String displayName;
        
        RiskLevel(final String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
