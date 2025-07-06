package com.learningapp.plan.domain.repository;

import com.learningapp.base.domain.repository.CommandRepository;
import com.learningapp.base.domain.valueobject.StudyPlanId;
import com.learningapp.plan.domain.entity.StudyPlan;

/**
 * StudyPlan Command Repository
 */
public interface StudyPlanCommandRepository extends CommandRepository<StudyPlan, StudyPlanId> {
}
