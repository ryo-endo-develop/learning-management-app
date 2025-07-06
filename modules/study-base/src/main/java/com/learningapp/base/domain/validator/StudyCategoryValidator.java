package com.learningapp.base.domain.validator;

import org.springframework.stereotype.Component;

/**
 * 学習カテゴリバリデーター
 * Strategy Pattern + Dependency Injection
 */
@Component
public class StudyCategoryValidator {
    
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 1;
    
    /**
     * カテゴリ名を検証し、正規化して返す
     */
    public String validateAndNormalizeName(final String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("カテゴリ名は必須です");
        }
        
        final String trimmedName = name.trim();
        
        if (trimmedName.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("カテゴリ名は" + MIN_NAME_LENGTH + "文字以上で入力してください");
        }
        
        if (trimmedName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("カテゴリ名は" + MAX_NAME_LENGTH + "文字以内で入力してください");
        }
        
        validateBusinessRules(trimmedName);
        
        return trimmedName;
    }
    
    /**
     * 表示順を検証
     */
    public int validateDisplayOrder(final int displayOrder) {
        if (displayOrder < 0) {
            throw new IllegalArgumentException("表示順は0以上で設定してください");
        }
        
        if (displayOrder > 999) {
            throw new IllegalArgumentException("表示順は999以下で設定してください");
        }
        
        return displayOrder;
    }
    
    /**
     * 試験カテゴリ名として適切かチェック
     */
    public boolean isValidExamCategoryName(final String name) {
        if (name == null) return false;
        
        return name.contains("午前") || name.contains("午後") || 
               name.contains("実践") || name.contains("理論");
    }
    
    private void validateBusinessRules(final String name) {
        // カテゴリ固有のビジネスルール
        if (containsInvalidCharacters(name)) {
            throw new IllegalArgumentException("カテゴリ名に無効な文字が含まれています");
        }
        
        if (isDuplicateDefaultCategory(name)) {
            throw new IllegalArgumentException("既定カテゴリと重複する名前は使用できません");
        }
    }
    
    private boolean containsInvalidCharacters(final String name) {
        // 特殊文字や絵文字の制限
        return name.matches(".*[<>\"'&].*");
    }
    
    private boolean isDuplicateDefaultCategory(final String name) {
        // 既定カテゴリとの重複チェック（将来的にはRepositoryから取得）
        return false; // 簡易実装
    }
}
