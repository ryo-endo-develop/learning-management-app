package com.learningapp.base.domain.valueobject;

/**
 * ユーザーID
 */
public final class UserId extends Identity {
    
    public UserId(String value) {
        super(value);
    }
    
    public UserId() {
        super();
    }
    
    public static UserId of(String value) {
        return new UserId(value);
    }
    
    public static UserId generate() {
        return new UserId();
    }
}
