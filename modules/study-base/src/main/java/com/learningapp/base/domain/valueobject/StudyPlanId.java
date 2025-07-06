package com.learningapp.base.domain.valueobject;

/**
 * 学習計画ID
 */
public final class StudyPlanId extends Identity {
    
    public StudyPlanId(String value) {
        super(value);
    }
    
    public StudyPlanId() {
        super();
    }
    
    public static StudyPlanId of(String value) {
        return new StudyPlanId(value);
    }
    
    public static StudyPlanId generate() {
        return new StudyPlanId();
    }
}
