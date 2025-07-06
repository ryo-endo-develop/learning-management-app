package com.learningapp.base.domain.factory;

import com.learningapp.base.domain.entity.StudyCategory;
import com.learningapp.base.domain.valueobject.StudyCategoryId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * StudyCategoryエンティティのファクトリクラス
 * GoF Factory Pattern
 */
@Component
public class StudyCategoryFactory {
    
    /**
     * 新規カテゴリ作成
     */
    public StudyCategory createNewCategory(final String name, final String description, final int displayOrder) {
        validateCreationInputs(name, description, displayOrder);
        
        return StudyCategory.create(name, description, displayOrder);
    }
    
    /**
     * 既存カテゴリ復元（永続化層から）
     */
    public StudyCategory restoreCategory(final StudyCategoryId id, final String name, 
                                        final String description, final int displayOrder) {
        validateRestorationInputs(id, name, description, displayOrder);
        
        return StudyCategory.restore(id, name, description, displayOrder);
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
        if (!examType.contains("午前") && !examType.contains("午後")) {
            throw new IllegalArgumentException("試験分野は午前または午後を含む必要があります");
        }
        
        return createNewCategory(examType, description, displayOrder);
    }
    
    private void validateCreationInputs(final String name, final String description, final int displayOrder) {
        Objects.requireNonNull(name, "Category name must not be null");
        
        if (displayOrder < 0) {
            throw new IllegalArgumentException("Display order must be non-negative");
        }
        
        // 将来的なビジネスルール拡張ポイント
        // - カテゴリ名の重複チェック
        // - 表示順の重複チェック
        // - カテゴリ階層構造の検証等
    }
    
    private void validateRestorationInputs(final StudyCategoryId id, final String name, 
                                          final String description, final int displayOrder) {
        Objects.requireNonNull(id, "StudyCategoryId must not be null");
        validateCreationInputs(name, description, displayOrder);
    }
}
