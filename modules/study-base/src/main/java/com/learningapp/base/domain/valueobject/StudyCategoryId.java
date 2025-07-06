package com.learningapp.base.domain.valueobject;

/**
 * 学習カテゴリID
 */
public final class StudyCategoryId extends Identity {
    
    public StudyCategoryId(String value) {
        super(value);
    }
    
    public StudyCategoryId() {
        super();
    }
    
    public static StudyCategoryId of(String value) {
        return new StudyCategoryId(value);
    }
    
    public static StudyCategoryId generate() {
        return new StudyCategoryId();
    }
}
