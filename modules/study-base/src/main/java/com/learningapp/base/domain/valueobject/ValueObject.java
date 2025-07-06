package com.learningapp.base.domain.valueobject;

/**
 * Value Object のマーカーインターフェース
 * Effective Java Item 18: 継承よりもコンポジション
 */
public interface ValueObject<T> {
    
    /**
     * Value Objectの値を取得
     */
    T getValue();
    
    /**
     * 同じ型のValue Objectとの値比較
     */
    default boolean isSameValue(final ValueObject<T> other) {
        if (other == null) return false;
        return getValue() != null && getValue().equals(other.getValue());
    }
}
