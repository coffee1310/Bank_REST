package com.example.bank_rest.util.converter;

public interface EntityConverter<T1, T2> {
    T1 toEntity(T2 dto);
}
