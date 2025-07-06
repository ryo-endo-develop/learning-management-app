package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.User;
import com.learningapp.base.domain.valueobject.UserId;

/**
 * User Command Repository
 * Effective Java Item 18: 継承よりもコンポジション
 */
public interface UserCommandRepository {
    
    /**
     * ユーザーを保存
     */
    void save(User user);
    
    /**
     * ユーザーを削除
     */
    void delete(UserId id);
    
    /**
     * ユーザーの物理削除（管理者用）
     */
    void deletePhysically(UserId id);
}
