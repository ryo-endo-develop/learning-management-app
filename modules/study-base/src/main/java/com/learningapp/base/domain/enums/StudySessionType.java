package com.learningapp.base.domain.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * 学習セッションの種別
 * Effective Java Item 34: intよりもenumを使う
 */
public enum StudySessionType {
    THEORY("THEORY", "理論学習"),
    PRACTICE("PRACTICE", "演習問題"),
    EXAM("EXAM", "模擬試験"),
    REVIEW("REVIEW", "復習");
    
    private final String code;
    private final String displayName;
    
    StudySessionType(final String code, final String displayName) {
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
    public static Optional<StudySessionType> fromCodeOptional(final String code) {
        if (code == null) return Optional.empty();
        
        return Arrays.stream(values())
            .filter(type -> type.code.equals(code))
            .findFirst();
    }
    
    /**
     * コードからEnumを取得（例外あり）
     */
    public static StudySessionType fromCode(final String code) {
        return fromCodeOptional(code)
            .orElseThrow(() -> new IllegalArgumentException("Unknown StudySessionType code: " + code));
    }
    
    /**
     * スコア記録が必要な種別かどうか
     */
    public boolean requiresScore() {
        return this == PRACTICE || this == EXAM;
    }
    
    /**
     * 理論学習系かどうか
     */
    public boolean isTheoryBased() {
        return this == THEORY || this == REVIEW;
    }
    
    /**
     * 実践系かどうか
     */
    public boolean isPracticeBased() {
        return this == PRACTICE || this == EXAM;
    }
}
