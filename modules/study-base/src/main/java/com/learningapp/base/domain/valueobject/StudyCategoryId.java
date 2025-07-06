package com.learningapp.base.domain.valueobject;

import java.util.Objects;
import java.util.Optional;

/**
 * 学習カテゴリID
 */
public final class StudyCategoryId implements ValueObject<String> {
    
    private final IdentityValue identityValue;
    
    private StudyCategoryId(final IdentityValue identityValue) {
        this.identityValue = Objects.requireNonNull(identityValue, "IdentityValue must not be null");
    }
    
    public static StudyCategoryId of(final String value) {
        return new StudyCategoryId(new IdentityValue(value));
    }
    
    public static StudyCategoryId generate() {
        return new StudyCategoryId(new IdentityValue());
    }
    
    public static Optional<StudyCategoryId> ofNullable(final String value) {
        return value != null ? Optional.of(of(value)) : Optional.empty();
    }
    
    public static StudyCategoryId ofOrGenerate(final String value) {
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
        final StudyCategoryId that = (StudyCategoryId) obj;
        return Objects.equals(identityValue, that.identityValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(identityValue);
    }
    
    @Override
    public String toString() {
        return "StudyCategoryId{value='" + getValue() + "'}";
    }
}
