package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.StudyCategoryId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 学習カテゴリエンティティ
 * 午前I、午前II、午後I、午後II等の学習分野
 * Effective Java Item 18: 継承よりもコンポジションを選ぶ
 */
public final class StudyCategory implements EntityMarker<StudyCategoryId> {
    
    private final EntityBase<StudyCategoryId> entityBase;
    private final String name;
    private final String description;
    private final int displayOrder;
    
    private StudyCategory(final StudyCategoryId id, final String name, final String description, final int displayOrder) {
        this.entityBase = new EntityBase<>(id);
        this.name = validateAndGetName(name);
        this.description = description != null ? description.trim() : "";
        this.displayOrder = Math.max(0, displayOrder);
    }
    
    /**
     * 新規カテゴリ作成
     */
    public static StudyCategory create(final String name, final String description, final int displayOrder) {
        return new StudyCategory(StudyCategoryId.generate(), name, description, displayOrder);
    }
    
    /**
     * 既存カテゴリ復元（永続化層から）
     */
    public static StudyCategory restore(final StudyCategoryId id, final String name, final String description, final int displayOrder) {
        return new StudyCategory(id, name, description, displayOrder);
    }
    
    /**
     * カテゴリ情報更新（新しいインスタンスを返す - 不変性）
     */
    public StudyCategory updateCategory(final String newName, final String newDescription, final int newDisplayOrder) {
        final StudyCategory updatedCategory = new StudyCategory(this.getId(), newName, newDescription, newDisplayOrder);
        updatedCategory.entityBase.updateTimestamp();
        return updatedCategory;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getDisplayOrder() {
        return displayOrder;
    }
    
    // EntityMarkerの実装
    @Override
    public StudyCategoryId getId() {
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
     * 業務ロジック: 試験関連カテゴリかどうか
     */
    public boolean isExamCategory() {
        return name.contains("午前") || name.contains("午後");
    }
    
    /**
     * 業務ロジック: 実技系カテゴリかどうか
     */
    public boolean isPracticalCategory() {
        return name.contains("実践") || name.contains("SQL") || name.contains("設計");
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final StudyCategory that = (StudyCategory) obj;
        return Objects.equals(entityBase.getId(), that.entityBase.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(entityBase.getId());
    }
    
    @Override
    public String toString() {
        return "StudyCategory{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", displayOrder=" + displayOrder +
                '}';
    }
    
    private static String validateAndGetName(final String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("カテゴリ名は必須です");
        }
        final String trimmedName = name.trim();
        if (trimmedName.length() > 100) {
            throw new IllegalArgumentException("カテゴリ名は100文字以内で入力してください");
        }
        return trimmedName;
    }
}
