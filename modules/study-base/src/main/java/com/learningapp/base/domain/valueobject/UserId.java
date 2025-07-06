package com.learningapp.base.domain.valueobject;

import java.util.Optional;

/**
 * ユーザーID
 * Effective Java Item 1: staticファクトリーメソッドを検討する
 * Effective Java Item 55: Optionalを適切に使用する
 */
public final class UserId extends Identity {
    
    private UserId(final String value) {
        super(value);
    }
    
    private UserId() {
        super();
    }
    
    /**
     * 既存の値から生成
     */
    public static UserId of(final String value) {
        return new UserId(value);
    }
    
    /**
     * 新規生成
     */
    public static UserId generate() {
        return new UserId();
    }
    
    /**
     * Optional を使ったnull安全なファクトリーメソッド
     */
    public static Optional<UserId> ofNullable(final String value) {
        return value != null ? Optional.of(of(value)) : Optional.empty();
    }
    
    /**
     * null安全なファクトリーメソッド（nullの場合は新規生成）
     */
    public static UserId ofOrGenerate(final String value) {
        return value != null ? of(value) : generate();
    }
}
