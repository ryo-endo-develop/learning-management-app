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
 */
@Service
@RequiredArgsConstructor
public class StudyPlanDomainService {
    
    private final StudyPlanQueryRepository studyPlanQueryRepository;
    
    /**
     * 学習計画作成時の重複チェック
     */
    public void validateStudyPlanCreation(UserId userId, LocalDate startDate, LocalDate endDate) {
        List<StudyPlan> overlappingPlans = studyPlanQueryRepository.findOverlappingPlans(userId, startDate, endDate);
        
        if (!overlappingPlans.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("指定期間（%s〜%s）に重複する学習計画が存在します", startDate, endDate)
            );
        }
    }
    
    /**
     * ユーザーのアクティブな学習計画数チェック
     */
    public void validateActiveStudyPlanLimit(UserId userId) {
        List<StudyPlan> activePlans = studyPlanQueryRepository.findActiveByUserId(userId);
        
        if (activePlans.size() >= 3) {
            throw new IllegalArgumentException("同時に実行できる学習計画は3つまでです");
        }
    }
    
    /**
     * 学習計画の削除可能性チェック
     */
    public boolean canDeleteStudyPlan(StudyPlanId studyPlanId) {
        // 学習セッションが記録されている場合は削除不可
        // TODO: StudySession側の実装後に追加
        return true;
    }
    
    /**
     * 学習計画の完了条件チェック
     */
    public boolean isStudyPlanCompletable(StudyPlan studyPlan) {
        // 終了日が過ぎている、または全目標が達成されている場合
        return LocalDate.now().isAfter(studyPlan.getEndDate()) || 
               hasAllGoalsAchieved(studyPlan.getId());
    }
    
    private boolean hasAllGoalsAchieved(StudyPlanId studyPlanId) {
        // TODO: StudyGoal の実装完了後に実装
        return false;
    }
}
