package com.learningapp.base.domain.validator;

import org.springframework.stereotype.Component;

/**
 * 名前バリデーター
 * Strategy Pattern + Dependency Injection
 */
@Component
public class NameValidator {
    
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 1;
    
    /**
     * 名前を検証し、正規化して返す
     */
    public String validateAndNormalize(final String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("名前は必須です");
        }
        
        final String trimmedName = name.trim();
        
        if (trimmedName.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("名前は" + MIN_NAME_LENGTH + "文字以上で入力してください");
        }
        
        if (trimmedName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("名前は" + MAX_NAME_LENGTH + "文字以内で入力してください");
        }
        
        // 将来的な拡張ポイント
        validateBusinessRules(trimmedName);
        
        return trimmedName;
    }
    
    /**
     * 表示用の名前を生成
     */
    public String generateDisplayName(final String name) {
        final String validatedName = validateAndNormalize(name);
        
        // 将来的な機能拡張
        // - 敬語付与
        // - 匿名化
        // - 略称生成
        
        return validatedName;
    }
    
    /**
     * 管理者名として適切かチェック
     */
    public boolean isValidAdminName(final String name) {
        try {
            final String validatedName = validateAndNormalize(name);
            // 管理者固有のルール
            return !validatedName.toLowerCase().contains("test") &&
                   !validatedName.toLowerCase().contains("admin");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private void validateBusinessRules(final String name) {
        // 将来的なビジネスルール
        // - 禁止文字チェック
        // - 不適切な単語フィルタ
        // - 文字種制限（ひらがな、カタカナ、漢字、英数字のみ）
        
        if (containsInappropriateWords(name)) {
            throw new IllegalArgumentException("不適切な文字が含まれています");
        }
    }
    
    private boolean containsInappropriateWords(final String name) {
        // 簡易的な実装（将来的にはより高度なフィルタリング）
        final String lowerName = name.toLowerCase();
        return lowerName.contains("admin") || lowerName.contains("root");
    }
}
