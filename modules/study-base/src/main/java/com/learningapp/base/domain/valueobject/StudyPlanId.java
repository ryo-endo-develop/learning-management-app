package com.learningapp.base.domain.valueobject;

/**
 * 学習計画ID
 */
public final class StudyPlanId extends Identity {
    
    private StudyPlanId(final String value) {
        super(value);
    }
    
    private StudyPlanId() {
        super();
    }
    
    public static StudyPlanId of(final String value) {
        return new StudyPlanId(value);
    }
    
    public static StudyPlanId generate() {
        return new StudyPlanId();
    }
    
    public static StudyPlanId ofNullable(final String value) {
        return value != null ? of(value) : generate();
    }
}
