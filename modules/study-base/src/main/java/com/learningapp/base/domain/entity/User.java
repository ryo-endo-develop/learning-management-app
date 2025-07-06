package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.UserId;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * ユーザーエンティティ
 */
@Getter
public final class User implements EntityMarker<UserId> {
    
    private final EntityBase<UserId> entityBase;
    private final String name;
    private final String email;
    
    // Package-private：Factoryからのみアクセス可能
    // バリデーションはFactoryで実施済み
    User(final UserId id, final String name, final String email) {
        this.entityBase = new EntityBase<>(id);
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.email = Objects.requireNonNull(email, "Email must not be null");
    }
    
    /**
     * プロフィール更新（新しいインスタンスを返す - 不変性）
     * バリデーションは呼び出し側（Application Service）で実施
     */
    public User updateProfile(final String newName, final String newEmail) {
        final User updatedUser = new User(this.getId(), newName, newEmail);
        updatedUser.entityBase.updateTimestamp();
        return updatedUser;
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
     * シンプルな判定のみ（複雑な判定はEmailValidatorに委譲）
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
}
