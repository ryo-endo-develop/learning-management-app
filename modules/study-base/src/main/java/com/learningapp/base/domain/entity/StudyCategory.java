package com.learningapp.base.domain.entity;

import com.learningapp.base.domain.valueobject.StudyCategoryId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 学習カテゴリエンティティ
 * 午前I、午前II、午後I、午後II等の学習分野
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyCategory extends Entity<StudyCategoryId> {
    
    private String name;
    private String description;
    private int displayOrder;
    
    public StudyCategory(StudyCategoryId id, String name, String description, int displayOrder) {
        super(id);
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
    }
    
    public static StudyCategory create(String name, String description, int displayOrder) {
        validateName(name);
        return new StudyCategory(StudyCategoryId.generate(), name, description, displayOrder);
    }
    
    public void updateCategory(String name, String description, int displayOrder) {
        validateName(name);
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
        updateTimestamp();
    }
    
    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("カテゴリ名は必須です");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("カテゴリ名は100文字以内で入力してください");
        }
    }
}
