package com.learningapp.base.domain.valueobject;

import java.util.Objects;
import java.util.Optional;

/**
 * 学習計画ID
 */
public final class StudyPlanId implements ValueObject<String> {
    
    private final IdentityValue identityValue;
    
    private StudyPlanId(final IdentityValue identityValue) {
        this.identityValue = Objects.requireNonNull(identityValue, "IdentityValue must not be null");
    }
    
    public static StudyPlanId of(final String value) {
        return new StudyPlanId(new IdentityValue(value));
    }
    
    public static StudyPlanId generate() {
        return new StudyPlanId(new IdentityValue());
    }
    
    public static Optional<StudyPlanId> ofNullable(final String value) {
        return value != null ? Optional.of(of(value)) : Optional.empty();
    }
    
    public static StudyPlanId ofOrGenerate(final String value) {
        return value != null ? of(value) : generate();
    }
    
    @Override
    public String getValue() {
        return identityValue.getValue();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final StudyPlanId that = (StudyPlanId) obj;
        return Objects.equals(identityValue, that.identityValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(identityValue);
    }
    
    @Override
    public String toString() {
        return "StudyPlanId{value='" + getValue() + "'}";
    }
}
