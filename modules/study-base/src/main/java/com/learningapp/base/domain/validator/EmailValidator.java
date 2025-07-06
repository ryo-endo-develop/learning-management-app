package com.learningapp.base.domain.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * メールアドレスバリデーター
 * Strategy Pattern + Dependency Injection
 */
@Component
public class EmailValidator {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    /**
     * メールアドレスを検証し、正規化して返す
     */
    public String validateAndNormalize(final String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("メールアドレスは必須です");
        }
        
        final String trimmedEmail = email.trim().toLowerCase();
        
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("有効なメールアドレスを入力してください");
        }
        
        // 将来的な拡張ポイント
        // - ドメインブラックリストチェック
        // - 使い捨てメールアドレス検出
        // - 企業ドメイン制限
        validateBusinessRules(trimmedEmail);
        
        return trimmedEmail;
    }
    
    /**
     * 企業メールアドレスかどうか判定
     */
    public boolean isCorporateEmail(final String email) {
        if (email == null) return false;
        
        final String domain = extractDomain(email);
        return !isConsumerEmailDomain(domain);
    }
    
    /**
     * ドメインを抽出
     */
    public String extractDomain(final String email) {
        if (email == null) return "";
        
        final int atIndex = email.indexOf('@');
        return atIndex != -1 ? email.substring(atIndex + 1) : "";
    }
    
    private void validateBusinessRules(final String email) {
        // 将来的なビジネスルール
        // - 特定ドメインの禁止
        // - 使い捨てメールサービスの検出
        // - 企業ポリシーに基づく制限
    }
    
    private boolean isConsumerEmailDomain(final String domain) {
        return domain.equals("gmail.com") || 
               domain.equals("yahoo.com") || 
               domain.equals("hotmail.com") ||
               domain.equals("outlook.com");
    }
}
