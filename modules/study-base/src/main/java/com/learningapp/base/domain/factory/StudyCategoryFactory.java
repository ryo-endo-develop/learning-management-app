package com.learningapp.base.domain.factory;

import com.learningapp.base.domain.entity.StudyCategory;
import com.learningapp.base.domain.validator.StudyCategoryValidator;
import com.learningapp.base.domain.valueobject.StudyCategoryId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * StudyCategoryエンティティのファクトリクラス
 * Validator Strategy Patternを使用
 */
@Component
@RequiredArgsConstructor
public class StudyCategoryFactory {
    
    private final StudyCategoryValidator validator;
    
    /**
     * 新規カテゴリ作成
     */
    public StudyCategory createNewCategory(final String name, final String description, final int displayOrder) {
        final String validatedName = validator.validateAndNormalizeName(name);
        final int validatedDisplayOrder = validator.validateDisplayOrder(displayOrder);
        
        return new StudyCategory(StudyCategoryId.generate(), validatedName, description, validatedDisplayOrder);
    }
    
    /**
     * 既存カテゴリ復元（永続化層から）
     */
    public StudyCategory restoreCategory(final StudyCategoryId id, final String name, 
                                        final String description, final int displayOrder) {
        Objects.requireNonNull(id, "StudyCategoryId must not be null");
        
        final String validatedName = validator.validateAndNormalizeName(name);
        final int validatedDisplayOrder = validator.validateDisplayOrder(displayOrder);
        
        return new StudyCategory(id, validatedName, description, validatedDisplayOrder);
    }
    
    /**
     * デフォルトカテゴリ一括作成
     */
    public List<StudyCategory> createDefaultCategories() {
        return List.of(
            createNewCategory("午前I", "基本情報技術者レベルの基礎知識", 1),
            createNewCategory("午前II", "データベース専門分野", 2),
            createNewCategory("午後I", "記述式問題（データベース設計・SQL）", 3),
            createNewCategory("午後II", "論述式問題（システム提案）", 4),
            createNewCategory("SQL実践", "SQL文法とクエリ最適化", 5),
            createNewCategory("データベース設計", "概念設計・論理設計・物理設計", 6),
            createNewCategory("パフォーマンス", "インデックス・実行計画・チューニング", 7),
            createNewCategory("運用管理", "バックアップ・リカバリ・セキュリティ", 8)
        );
    }
    
    /**
     * 試験分野のカテゴリ作成
     */
    public StudyCategory createExamCategory(final String examType, final String description, final int displayOrder) {
        if (!validator.isValidExamCategoryName(examType)) {
            throw new IllegalArgumentException("試験分野は午前、午後、実践、理論のいずれかを含む必要があります");
        }
        
        return createNewCategory(examType, description, displayOrder);
    }
    
    /**
     * カスタムカテゴリ作成（ユーザー定義）
     */
    public StudyCategory createCustomCategory(final String name, final String description) {
        // カスタムカテゴリは最後に配置
        final int customDisplayOrder = 999;
        return createNewCategory(name, description, customDisplayOrder);
    }
}
