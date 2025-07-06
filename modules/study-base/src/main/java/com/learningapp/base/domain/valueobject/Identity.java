package com.learningapp.base.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * 識別子の基底クラス
 * Effective Java Item 17: 可変性を最小限に抑える
 * Effective Java Item 10,11: equals/hashCodeの適切な実装
 */
public abstract class Identity {
    
    private final String value;
    
    protected Identity(final String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Identity value must not be null or empty");
        }
        this.value = value;
    }
    
    protected Identity() {
        this.value = UUID.randomUUID().toString();
    }
    
    public final String getValue() {
        return value;
    }
    
    public final boolean isSameValue(final Identity other) {
        return Objects.equals(this.value, other.value);
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Identity identity = (Identity) obj;
        return Objects.equals(value, identity.value);
    }
    
    @Override
    public final int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public final String toString() {
        return getClass().getSimpleName() + "{value='" + value + "'}";
    }
}
