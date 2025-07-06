package com.learningapp.base.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * 識別子の共通実装
 * Effective Java Item 17: 可変性を最小限に抑える
 */
public final class IdentityValue {
    
    private final String value;
    
    public IdentityValue(final String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Identity value must not be null or empty");
        }
        this.value = value;
    }
    
    public IdentityValue() {
        this.value = UUID.randomUUID().toString();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final IdentityValue that = (IdentityValue) obj;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "IdentityValue{value='" + value + "'}";
    }
}
