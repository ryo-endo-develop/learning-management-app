package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.User;
import com.learningapp.base.domain.valueobject.UserId;

import java.util.List;
import java.util.Optional;

/**
 * User Query Repository
 * Effective Java Item 18: 継承よりもコンポジション
 * Effective Java Item 55: Optionalを適切に使用する
 */
public interface UserQueryRepository {
    
    /**
     * IDでユーザーを取得
     */
    Optional<User> findById(UserId id);
    
    /**
     * 全ユーザーを取得
     */
    List<User> findAll();
    
    /**
     * 存在チェック
     */
    boolean existsById(UserId id);
    
    /**
     * 件数取得
     */
    long count();
    
    /**
     * メールアドレスでユーザーを検索
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 名前でユーザーを部分検索
     */
    List<User> findByNameContaining(String name);
    
    /**
     * 企業ドメインでユーザーを検索
     */
    List<User> findByEmailDomain(String domain);
}
