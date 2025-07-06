package com.learningapp.base.domain.valueobject;

import java.util.Objects;
import java.util.Optional;

/**
 * 学習セッションID
 */
public final class StudySessionId implements ValueObject<String> {
    
    private final IdentityValue identityValue;
    
    private StudySessionId(final IdentityValue identityValue) {
        this.identityValue = Objects.requireNonNull(identityValue, "IdentityValue must not be null");
    }
    
    public static StudySessionId of(final String value) {
        return new StudySessionId(new IdentityValue(value));
    }
    
    public static StudySessionId generate() {
        return new StudySessionId(new IdentityValue());
    }
    
    public static Optional<StudySessionId> ofNullable(final String value) {
        return value != null ? Optional.of(of(value)) : Optional.empty();
    }
    
    public static StudySessionId ofOrGenerate(final String value) {
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
        final StudySessionId that = (StudySessionId) obj;
        return Objects.equals(identityValue, that.identityValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(identityValue);
    }
    
    @Override
    public String toString() {
        return "StudySessionId{value='" + getValue() + "'}";
    }
}
