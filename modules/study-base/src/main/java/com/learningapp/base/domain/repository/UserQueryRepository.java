package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.User;
import com.learningapp.base.domain.valueobject.UserId;
import java.util.Optional;

/**
 * User Query Repository
 */
public interface UserQueryRepository extends QueryRepository<User, UserId> {
    
    /**
     * メールアドレスでユーザーを検索
     */
    Optional<User> findByEmail(String email);
}
