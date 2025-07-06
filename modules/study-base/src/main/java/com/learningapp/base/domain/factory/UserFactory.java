package com.learningapp.base.domain.factory;

import com.learningapp.base.domain.entity.User;
import com.learningapp.base.domain.validator.EmailValidator;
import com.learningapp.base.domain.validator.NameValidator;
import com.learningapp.base.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Userエンティティのファクトリクラス
 * Validator Strategy Patternを使用
 */
@Component
@RequiredArgsConstructor
public class UserFactory {
    
    private final NameValidator nameValidator;
    private final EmailValidator emailValidator;
    
    /**
     * 新規ユーザー作成
     */
    public User createNewUser(final String name, final String email) {
        final String validatedName = nameValidator.validateAndNormalize(name);
        final String validatedEmail = emailValidator.validateAndNormalize(email);
        
        return new User(UserId.generate(), validatedName, validatedEmail);
    }
    
    /**
     * 既存ユーザー復元（永続化層から）
     */
    public User restoreUser(final UserId id, final String name, final String email) {
        Objects.requireNonNull(id, "UserId must not be null");
        
        final String validatedName = nameValidator.validateAndNormalize(name);
        final String validatedEmail = emailValidator.validateAndNormalize(email);
        
        return new User(id, validatedName, validatedEmail);
    }
    
    /**
     * テスト用ユーザー作成
     */
    public User createTestUser(final String name) {
        final String testEmail = generateTestEmail(name);
        return createNewUser(name, testEmail);
    }
    
    /**
     * 管理者ユーザー作成
     */
    public User createAdminUser(final String name, final String email) {
        // 管理者固有のバリデーション
        if (!nameValidator.isValidAdminName(name)) {
            throw new IllegalArgumentException("管理者名として不適切です");
        }
        
        final String validatedEmail = emailValidator.validateAndNormalize(email);
        
        if (!emailValidator.isCorporateEmail(validatedEmail)) {
            throw new IllegalArgumentException("管理者は企業ドメインのメールアドレスが必要です");
        }
        
        final String validatedName = nameValidator.validateAndNormalize(name);
        return new User(UserId.generate(), validatedName, validatedEmail);
    }
    
    /**
     * 企業ユーザー作成
     */
    public User createCorporateUser(final String name, final String email) {
        final String validatedEmail = emailValidator.validateAndNormalize(email);
        
        if (!emailValidator.isCorporateEmail(validatedEmail)) {
            throw new IllegalArgumentException("企業ドメインのメールアドレスが必要です");
        }
        
        return createNewUser(name, email);
    }
    
    private String generateTestEmail(final String name) {
        final String safeName = name.toLowerCase()
            .replace(" ", ".")
            .replaceAll("[^a-z0-9.]", "");
        return safeName + "@example.com";
    }
}
