package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.User;
import com.learningapp.base.domain.valueobject.UserId;

/**
 * User Command Repository
 */
public interface UserCommandRepository extends CommandRepository<User, UserId> {
}
