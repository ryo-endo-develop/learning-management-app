package com.learningapp.base.domain.enums;

/**
 * 学習計画のステータス
 */
public enum StudyPlanStatus {
    ACTIVE("ACTIVE", "実施中"),
    COMPLETED("COMPLETED", "完了"),
    PAUSED("PAUSED", "一時停止"),
    CANCELLED("CANCELLED", "キャンセル");
    
    private final String code;
    private final String displayName;
    
    StudyPlanStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static StudyPlanStatus fromCode(String code) {
        for (StudyPlanStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown StudyPlanStatus code: " + code);
    }
}
