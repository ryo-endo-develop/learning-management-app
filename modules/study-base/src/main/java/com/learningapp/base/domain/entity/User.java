package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.UserId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * ユーザーエンティティ
 * Effective Java Item 18: 継承よりもコンポジションを選ぶ
 * Effective Java Item 17: 可変性を最小限に抑える
 * Effective Java Item 55: Optionalを適切に使用する
 */
public final class User implements EntityMarker<UserId> {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    private final EntityBase<UserId> entityBase;
    private final String name;
    private final String email;
    
    private User(final UserId id, final String name, final String email) {
        this.entityBase = new EntityBase<>(id);
        this.name = validateAndGetName(name);
        this.email = validateAndGetEmail(email);
    }
    
    /**
     * 新規ユーザー作成
     */
    public static User create(final String name, final String email) {
        return new User(UserId.generate(), name, email);
    }
    
    /**
     * 既存ユーザー復元（永続化層から）
     */
    public static User restore(final UserId id, final String name, final String email) {
        return new User(id, name, email);
    }
    
    /**
     * プロフィール更新（新しいインスタンスを返す - 不変性）
     */
    public User updateProfile(final String newName, final String newEmail) {
        final User updatedUser = new User(this.getId(), newName, newEmail);
        updatedUser.entityBase.updateTimestamp();
        return updatedUser;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    // EntityMarkerの実装
    @Override
    public UserId getId() {
        return entityBase.getId();
    }
    
    @Override
    public LocalDateTime getCreatedAt() {
        return entityBase.getCreatedAt();
    }
    
    @Override
    public LocalDateTime getUpdatedAt() {
        return entityBase.getUpdatedAt();
    }
    
    /**
     * 業務ロジック: メールドメインの取得
     * Optional で安全に返す
     */
    public Optional<String> getEmailDomain() {
        final int atIndex = email.indexOf('@');
        return atIndex != -1 ? 
            Optional.of(email.substring(atIndex + 1)) : 
            Optional.empty();
    }
    
    /**
     * 業務ロジック: 表示名の取得
     */
    public String getDisplayName() {
        return name;
    }
    
    /**
     * 業務ロジック: 企業メールかどうか判定
     */
    public boolean isCorporateEmail() {
        return getEmailDomain()
            .map(domain -> !domain.equals("gmail.com") && 
                          !domain.equals("yahoo.com") && 
                          !domain.equals("hotmail.com"))
            .orElse(false);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final User user = (User) obj;
        return Objects.equals(entityBase.getId(), user.entityBase.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(entityBase.getId());
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
    
    private static String validateAndGetName(final String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ユーザー名は必須です");
        }
        final String trimmedName = name.trim();
        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException("ユーザー名は100文字以内で入力してください");
        }
        return trimmedName;
    }
    
    private static String validateAndGetEmail(final String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("メールアドレスは必須です");
        }
        final String trimmedEmail = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("有効なメールアドレスを入力してください");
        }
        return trimmedEmail;
    }
}
