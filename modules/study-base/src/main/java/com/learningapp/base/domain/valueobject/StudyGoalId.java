package com.learningapp.base.domain.valueobject;

import java.util.Objects;
import java.util.Optional;

/**
 * 学習目標ID
 */
public final class StudyGoalId implements ValueObject<String> {
    
    private final IdentityValue identityValue;
    
    private StudyGoalId(final IdentityValue identityValue) {
        this.identityValue = Objects.requireNonNull(identityValue, "IdentityValue must not be null");
    }
    
    public static StudyGoalId of(final String value) {
        return new StudyGoalId(new IdentityValue(value));
    }
    
    public static StudyGoalId generate() {
        return new StudyGoalId(new IdentityValue());
    }
    
    public static Optional<StudyGoalId> ofNullable(final String value) {
        return value != null ? Optional.of(of(value)) : Optional.empty();
    }
    
    public static StudyGoalId ofOrGenerate(final String value) {
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
        final StudyGoalId that = (StudyGoalId) obj;
        return Objects.equals(identityValue, that.identityValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(identityValue);
    }
    
    @Override
    public String toString() {
        return "StudyGoalId{value='" + getValue() + "'}";
    }
}
