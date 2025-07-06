package com.learningapp.base.domain.valueobject;

/**
 * 学習セッションID
 */
public final class StudySessionId extends Identity {
    
    public StudySessionId(String value) {
        super(value);
    }
    
    public StudySessionId() {
        super();
    }
    
    public static StudySessionId of(String value) {
        return new StudySessionId(value);
    }
    
    public static StudySessionId generate() {
        return new StudySessionId();
    }
}
