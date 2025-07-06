package com.learningapp.base.domain.repository;

import com.learningapp.base.domain.entity.StudyCategory;
import com.learningapp.base.domain.valueobject.StudyCategoryId;

/**
 * StudyCategory Command Repository
 */
public interface StudyCategoryCommandRepository extends CommandRepository<StudyCategory, StudyCategoryId> {
}
