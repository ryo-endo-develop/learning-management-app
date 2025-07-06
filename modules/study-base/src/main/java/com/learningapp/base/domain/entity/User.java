package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.UserId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ユーザーエンティティ
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Entity<UserId> {
    
    private String name;
    private String email;
    
    public User(UserId id, String name, String email) {
        super(id);
        this.name = name;
        this.email = email;
    }
    
    public static User create(String name, String email) {
        validateName(name);
        validateEmail(email);
        return new User(UserId.generate(), name, email);
    }
    
    public void updateProfile(String name, String email) {
        validateName(name);
        validateEmail(email);
        this.name = name;
        this.email = email;
        updateTimestamp();
    }
    
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ユーザー名は必須です");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("ユーザー名は100文字以内で入力してください");
        }
    }
    
    private static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("メールアドレスは必須です");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("有効なメールアドレスを入力してください");
        }
    }
}
