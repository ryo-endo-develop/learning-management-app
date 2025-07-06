package com.learningapp.base.domain.enums;

/**
 * 学習セッションの種別
 */
public enum StudySessionType {
    THEORY("THEORY", "理論学習"),
    PRACTICE("PRACTICE", "演習問題"),
    EXAM("EXAM", "模擬試験"),
    REVIEW("REVIEW", "復習");
    
    private final String code;
    private final String displayName;
    
    StudySessionType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static StudySessionType fromCode(String code) {
        for (StudySessionType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown StudySessionType code: " + code);
    }
}
