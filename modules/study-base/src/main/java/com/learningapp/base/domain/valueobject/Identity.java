package com.learningapp.base.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

/**
 * 識別子の基底クラス
 */
public abstract class Identity {
    
    private final String value;
    
    protected Identity(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Identity value cannot be null or empty");
        }
        this.value = value;
    }
    
    protected Identity() {
        this.value = UUID.randomUUID().toString();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(value, identity.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + value + "}";
    }
}
