package com.learningapp.base.domain.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * 学習計画のステータス
 * Effective Java Item 34: intよりもenumを使う
 */
public enum StudyPlanStatus {
    ACTIVE("ACTIVE", "実施中"),
    COMPLETED("COMPLETED", "完了"),
    PAUSED("PAUSED", "一時停止"),
    CANCELLED("CANCELLED", "キャンセル");
    
    private final String code;
    private final String displayName;
    
    StudyPlanStatus(final String code, final String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * コードから安全にEnumを取得
     * Effective Java Item 55: Optionalを適切に使用
     */
    public static Optional<StudyPlanStatus> fromCodeOptional(final String code) {
        if (code == null) return Optional.empty();
        
        return Arrays.stream(values())
            .filter(status -> status.code.equals(code))
            .findFirst();
    }
    
    /**
     * コードからEnumを取得（例外あり）
     */
    public static StudyPlanStatus fromCode(final String code) {
        return fromCodeOptional(code)
            .orElseThrow(() -> new IllegalArgumentException("Unknown StudyPlanStatus code: " + code));
    }
    
    /**
     * アクティブなステータスかどうか
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 完了ステータスかどうか
     */
    public boolean isCompleted() {
        return this == COMPLETED;
    }
    
    /**
     * 変更可能なステータスかどうか
     */
    public boolean isModifiable() {
        return this == ACTIVE || this == PAUSED;
    }
}
