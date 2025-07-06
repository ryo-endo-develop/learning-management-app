package com.learningapp.base.domain.factory;

import com.learningapp.base.domain.entity.User;
import com.learningapp.base.domain.valueobject.UserId;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Userエンティティのファクトリクラス
 * Effective Java Item 1: staticファクトリーメソッドの代替
 * GoF Factory Pattern
 */
@Component
public class UserFactory {
    
    /**
     * 新規ユーザー作成
     */
    public User createNewUser(final String name, final String email) {
        validateCreationInputs(name, email);
        
        return User.create(name, email);
    }
    
    /**
     * 既存ユーザー復元（永続化層から）
     */
    public User restoreUser(final UserId id, final String name, final String email) {
        validateRestorationInputs(id, name, email);
        
        return User.restore(id, name, email);
    }
    
    /**
     * テスト用ユーザー作成
     */
    public User createTestUser(final String name) {
        final String testEmail = name.toLowerCase().replace(" ", ".") + "@example.com";
        return createNewUser(name, testEmail);
    }
    
    /**
     * 管理者ユーザー作成
     */
    public User createAdminUser(final String name, final String email) {
        validateAdminCreationInputs(name, email);
        
        return User.create(name, email);
    }
    
    private void validateCreationInputs(final String name, final String email) {
        Objects.requireNonNull(name, "Name must not be null");
        Objects.requireNonNull(email, "Email must not be null");
        
        // 将来的にビジネスルールを追加可能
        // - 重複チェック
        // - 企業ドメイン制限
        // - 命名規則チェック等
    }
    
    private void validateRestorationInputs(final UserId id, final String name, final String email) {
        Objects.requireNonNull(id, "UserId must not be null");
        validateCreationInputs(name, email);
    }
    
    private void validateAdminCreationInputs(final String name, final String email) {
        validateCreationInputs(name, email);
        
        // 管理者固有のバリデーション
        if (!email.endsWith("@company.com")) {
            throw new IllegalArgumentException("管理者は企業ドメインのメールアドレスが必要です");
        }
    }
}
