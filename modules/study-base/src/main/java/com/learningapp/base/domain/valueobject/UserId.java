package com.learningapp.base.domain.valueobject;

import java.util.Objects;
import java.util.Optional;

/**
 * ユーザーID
 * Effective Java Item 1: staticファクトリーメソッドを検討する
 * Effective Java Item 18: 継承よりもコンポジション
 * Effective Java Item 55: Optionalを適切に使用する
 */
public final class UserId implements ValueObject<String> {
    
    private final IdentityValue identityValue;
    
    private UserId(final IdentityValue identityValue) {
        this.identityValue = Objects.requireNonNull(identityValue, "IdentityValue must not be null");
    }
    
    /**
     * 既存の値から生成
     */
    public static UserId of(final String value) {
        return new UserId(new IdentityValue(value));
    }
    
    /**
     * 新規生成
     */
    public static UserId generate() {
        return new UserId(new IdentityValue());
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
    
    @Override
    public String getValue() {
        return identityValue.getValue();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final UserId userId = (UserId) obj;
        return Objects.equals(identityValue, userId.identityValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(identityValue);
    }
    
    @Override
    public String toString() {
        return "UserId{value='" + getValue() + "'}";
    }
}
