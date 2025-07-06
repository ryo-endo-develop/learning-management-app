package com.learningapp.base.domain.valueobject;

/**
 * 学習目標ID
 */
public final class StudyGoalId extends Identity {
    
    public StudyGoalId(String value) {
        super(value);
    }
    
    public StudyGoalId() {
        super();
    }
    
    public static StudyGoalId of(String value) {
        return new StudyGoalId(value);
    }
    
    public static StudyGoalId generate() {
        return new StudyGoalId();
    }
}
